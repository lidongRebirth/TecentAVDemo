package com.zlz.app.tecentavdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.core.ILiveLog;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.data.ILiveMessage;
import com.tencent.ilivesdk.data.msg.ILiveTextMessage;
import com.tencent.ilivesdk.listener.ILiveMessageListener;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.ilivesdk.view.AVVideoView;
import com.zlz.app.tecentavdemo.room.IRoomView;
import com.zlz.app.tecentavdemo.room.MessageObservable;
import com.zlz.app.tecentavdemo.room.RoomHelper;

import java.util.ArrayList;
import java.util.List;

import static com.tencent.ilivesdk.view.AVRootView.LAYOUT_GRAVITY_RIGHT;

public class RoomActivity extends AppCompatActivity implements IRoomView, View.OnClickListener, ILiveMessageListener {

    private RoomHelper roomHelper;
    private static final String TAG = "RoomActivity";
    private int roomID;
    private String username;            //貌似没有，深入研究下哪里可能会用
    private Button btn_swap;    //切换摄像头
    private Button btn_hangup;  //挂断
    private Button btn_swap_sound;//转为语音通话
    private boolean bFrontCamera = true;        //用于切换摄像头




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        btn_swap = findViewById(R.id.btn_swap);
        btn_hangup = findViewById(R.id.btn_hangup);
        btn_swap_sound = findViewById(R.id.btn_swap_sound);

        btn_swap.setOnClickListener(this);
        btn_hangup.setOnClickListener(this);
        btn_swap_sound.setOnClickListener(this);


        checkPermission();
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        roomID = intent.getIntExtra("roomid", 1234);


        roomHelper = new RoomHelper(this);
        // 获取渲染控件
        final  AVRootView avRootView = findViewById(R.id.av_root_view);//*
        // 设置没有渲染时的背景色为蓝色(注意不支持在布局中直接设置)
//        avRootView.getVideoGroup().setBackgroundColor(Color.BLUE);

        // 设置渲染控件
        roomHelper.setRootView(avRootView);
        avRootView.getVideoGroup().setBackground(R.drawable.room_background);   //*一定要在setRootView之后
        avRootView.setGravity(LAYOUT_GRAVITY_RIGHT);            //*窗口位于右上角成功

        avRootView.getVideoGroup().getGLRootView().setActivated(false);     //?

        avRootView.setSubCreatedListener(new AVRootView.onSubViewCreatedListener() {
            @Override
            public void onSubViewCreated() {
                for (int i = 1; i < ILiveConstants.MAX_AV_VIDEO_NUM; i++) {
                    final int index = i;
                    final AVVideoView videoView = avRootView.getViewByIndex(i);
                    videoView.setDragable(true);        // 可拖动
                    videoView.setGestureListener(new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapConfirmed(MotionEvent e) {        // 小屏点击交换
                            avRootView.swapVideoView(0, index);
                            return super.onSingleTapConfirmed(e);
                        }
                    });
                }
            }
        });


        roomHelper.createRoom(roomID, username);//*

        //*然后只需要在关心消息的Acitivty的onCreate中设置消息监听：
        MessageObservable.getInstance().addObserver(this);//*-------

    }

    @Override
    public void onEnterRoom() {
        Log.i(TAG, "onEnterRoom: 进入房间成功");
    }

    @Override
    public void onEnterRoomFailed(String module, int errCode, String errMsg) {
        Log.i(TAG, "onEnterRoom: 进入房间失败" + errCode + "错误信息：" + errMsg);
        if(errCode == 10021){
            roomHelper.joinEventRoom(roomID, username);
        }
    }

    @Override
    public void onQuitRoomSuccess() {
        Log.i(TAG, "onEnterRoom: 退出房间成功");

    }

    @Override
    public void onQuitRoomFailed(String module, int errCode, String errMsg) {
        Log.i(TAG, "onEnterRoom: 退出房间失败" + errCode + "错误信息：" + errMsg);

    }

    @Override
    public void onRoomDisconnect(String module, int errCode, String errMsg) {

    }

    protected boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(this,
                        (String[]) permissions.toArray(new String[0]),
                        100);
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        roomHelper.quitRoom();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_hangup:       //挂断，先退出房间，然后关掉界面
                roomHelper.quitRoom();            //*成功挂断
                finish();
                //*发一个信息让其他人知道要挂断，消息发送成不成功都得挂断
                ILiveTextMessage hangmessage = new ILiveTextMessage("hangup");
                ILiveRoomManager.getInstance().sendGroupMessage(hangmessage, new ILiveCallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        Log.i(TAG, "onSuccess: 消息发送成功");

                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {
                        Log.i(TAG, "发送消息失败: " + module + "|" + errCode + "|" + errMsg);
                    }
                });
                break;
            case R.id.btn_swap:
                bFrontCamera = !bFrontCamera;                                               //切换摄像头
                ILiveRoomManager.getInstance().switchCamera(bFrontCamera ? ILiveConstants.FRONT_CAMERA : ILiveConstants.BACK_CAMERA);
                break;
            case R.id.btn_swap_sound:
                //*-----抛信令让对方关闭
//                int i = ILiveRoomManager.getInstance().getCurCameraId();//只是自己的摄像头id
//                ILiveRoomManager.getInstance().enableCamera(i, false);//只关掉了自己的摄像头


//                int id = ILiveRoomManager.getInstance().getActiveCameraId();              这种方法也只能关掉自己的摄像头
//                ILiveRoomManager.getInstance().enableCamera(id, false);//只关掉了自己的摄像头
                //*未成功
                btn_swap_sound.setVisibility(View.GONE);
                btn_swap.setVisibility(View.GONE);
//                //*发一个信息让其他人知道要关闭摄像头
                ILiveTextMessage textMessage = new ILiveTextMessage("turnoffcamera");
                ILiveRoomManager.getInstance().sendGroupMessage(textMessage, new ILiveCallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        Log.i(TAG, "onSuccess: 消息发送成功");
                        //*---关闭自己的摄像头
                        int i = ILiveRoomManager.getInstance().getCurCameraId();//只是自己的摄像头id
                        ILiveRoomManager.getInstance().enableCamera(i, false);//只关掉了自己的摄像头
                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {
                        Toast.makeText(getApplicationContext(), "发送消息失败: " + module + "|" + errCode + "|" + errMsg,Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "发送消息失败: " + module + "|" + errCode + "|" + errMsg);
                    }
                });

                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MessageObservable.getInstance().deleteObserver(this);
    }

    //*这样在收到消息时，就可以在Acitivty中收到 onNewMessage 事件。
    @Override
    public void onNewMessage(ILiveMessage message) {
        Log.i(TAG, "onNewMessage: 收到消息了"+message);
        switch (message.getMsgType()) {
            case ILiveMessage.ILIVE_MSG_TYPE_TEXT:
                // 文本消息
                String s = ((ILiveTextMessage)message).getText();
                if(s.equals("turnoffcamera")){      //切换为语音聊天

                    btn_swap_sound.setVisibility(View.GONE);
                    btn_swap.setVisibility(View.GONE);

                    int i = ILiveRoomManager.getInstance().getCurCameraId();//只是自己的摄像头id
                    ILiveRoomManager.getInstance().enableCamera(i, false);//只关掉了自己的摄像头
                }else if(s.equals("hangup")){//挂断
                    roomHelper.quitRoom();
                    finish();
                }

                break;
        }

    }
}
