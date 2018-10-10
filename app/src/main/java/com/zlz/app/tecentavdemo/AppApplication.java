package com.zlz.app.tecentavdemo;

import android.app.Application;

import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveRoomConfig;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.zlz.app.tecentavdemo.room.MessageObservable;


/**
 * 初始化 iLiveSDK
 */
public class AppApplication extends Application {

    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        // 判断仅在主线程进行初始化
        if (MsfSdkUtils.isMainProcess(this)) {
            /**
             *
             * 初始化iLiveSDK
             * 此处请参考文档替换自己应用的SDKAPPID与ACCOUNTTYPE后进行测试
             *
             */
            ILiveSDK.getInstance().initSdk(this, 1400147203  , 36862 );
            // 初始化iLiveSDK房间管理模块
            ILiveRoomManager.getInstance().init(new ILiveRoomConfig()
                    .setRoomMsgListener(MessageObservable.getInstance()));// *初始化iLiveSDK房间管理模块并设置消息监听
        }
    }

    public static Application getApplication() {
        return application;
    }
}
