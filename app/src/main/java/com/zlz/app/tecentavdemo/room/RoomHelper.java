package com.zlz.app.tecentavdemo.room;

import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.core.ILiveRoomOption;
import com.tencent.ilivesdk.view.AVRootView;

/**
 * 创建房间模块
 */
public class RoomHelper implements ILiveRoomOption.onExceptionListener, ILiveRoomOption.onRoomDisconnectListener {

    private IRoomView roomView;

    public RoomHelper(IRoomView view){
        roomView = view;
    }
    // 设置渲染控件
    public void setRootView(AVRootView avRootView){
        ILiveRoomManager.getInstance().initAvRootView(avRootView);
    }
    // 创建房间
    public int createRoom(int roomId,String username){  //*
        ILiveRoomOption option = new ILiveRoomOption()
//                .privateMapKey(privateMapKey)   // 进房票据       //*貌似在控制台设置后就必须要有
                .imsupport(true)       // 不需要IM功能               //*为了能在聊天室聊天    修改房间模块中的创建和加入房间，配置 imsupport 为true 。
                .groupType("AVChatRoom")    // 使用互动直播聊天室(默认),与创建一致      //*-----
                .exceptionListener(this)  // 监听异常事件处理
                .roomDisconnectListener(this)   // 监听房间中断事件
                .controlRole(username)    // 使用user角色   //*
                .autoCamera(true)       // 进房间后自动打开摄像头并上行
                .autoMic(true);         // 进房间后自动要开Mic并上行

        return ILiveRoomManager.getInstance().createRoom(roomId, option, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                roomView.onEnterRoom();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                roomView.onEnterRoomFailed(module, errCode, errMsg);

            }
        });
    }
    //*----
    // 对应IMRoomActivity中的加入房间,即具备首发消息的功能
    public int joinEventRoom(int roomId,String username) {
        ILiveRoomOption option = new ILiveRoomOption()
                .imsupport(true)       // 开启IM功能
                .groupType("AVChatRoom")    // 使用互动直播聊天室(默认),与创建一致
                .exceptionListener(this)
                .roomDisconnectListener(this)
                .controlRole(username)//使用Guest角色
                .autoCamera(true) //进入房间不打开摄像头
                .autoMic(true); //进入房间不连麦
        return ILiveRoomManager.getInstance().joinRoom(roomId, option, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                roomView.onEnterRoom();

            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                roomView.onEnterRoomFailed(module, errCode, errMsg);
            }
        });

    }
    //*----




    // 退出房间
    public int quitRoom(){
        return ILiveRoomManager.getInstance().quitRoom(new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                roomView.onQuitRoomSuccess();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                roomView.onQuitRoomFailed(module, errCode, errMsg);
            }
        });
    }

    // 处理Activity事件
    public void onPause(){
        ILiveRoomManager.getInstance().onPause();
    }
    public void onResume(){
        ILiveRoomManager.getInstance().onResume();
    }

    @Override
    public void onException(int exceptionId, int errCode, String errMsg) {
        //处理异常事件
    }

    @Override
    public void onRoomDisconnect(int errCode, String errMsg) {
        // 处理房间中断(一般为断网或长时间无长行后台回收房间)
    }

}
