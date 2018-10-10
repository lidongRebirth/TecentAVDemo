package com.zlz.app.tecentavdemo;

import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveLoginManager;

/**
 * 创建一个 LoginHelper.java 用于登录操作
 */
public class LoginHelper implements ILiveLoginManager.TILVBStatusListener  {

    private ILoginView loginView;

    public LoginHelper(ILoginView view){
        this.loginView = view;
    }

    public void loginSDK(String userId, String userSig){
        ILiveLoginManager.getInstance().iLiveLogin(userId, userSig, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                loginView.onLoginSuccess();
                ILiveLoginManager.getInstance().setUserStatusListener(StatusObservable.getInstance());
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                loginView.onLoginFailed(module, errCode, errMsg);
            }
        });
    }

    /**
     * 离线时间处理
     *这样用户在收到 onForceOffline 事件就知道自己被踢下线了。
     * @param error
     * @param message
     */
    @Override
    public void onForceOffline(int error, String message) {

    }
}
