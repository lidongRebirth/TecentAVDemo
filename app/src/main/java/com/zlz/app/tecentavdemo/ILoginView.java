package com.zlz.app.tecentavdemo;

/**
 * 创建一个登录模块与 Acitivity 通讯的接口
 */
public interface ILoginView {
    // 登录成功
    void onLoginSuccess();
    // 登录失败
    void onLoginFailed(String module, int errCode, String errMsg);
}
