package com.zlz.app.tecentavdemo.room;

/**
 * 定义一个房间模块与 Activity 通讯的接口：
 */
public interface IRoomView {

    // 进入房间成功
    void onEnterRoom();

    // 进房间失败
    void onEnterRoomFailed(String module, int errCode, String errMsg);

    // 退出房间成功
    void onQuitRoomSuccess();

    // 退出房间失败
    void onQuitRoomFailed(String module, int errCode, String errMsg);

    // 房间断开
    void onRoomDisconnect(String module, int errCode, String errMsg);

}
