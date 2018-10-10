package com.zlz.app.tecentavdemo;

import com.tencent.ilivesdk.core.ILiveLoginManager;

import java.util.LinkedList;

/**
 * 监听帐户状态
 用户在登录后，也是有可能强制下线的( 帐号重复登录被踢，userSig 过期 )，所以应用需要监听这个状态。
 为了全局监听，可以创建一个观察者：
 并在登录成功后调用接口设置监听：
 */
public class StatusObservable implements ILiveLoginManager.TILVBStatusListener{
    // 消息监听链表
    private LinkedList<ILiveLoginManager.TILVBStatusListener> listObservers = new LinkedList<>();
    // 句柄
    private static StatusObservable instance;


    public static StatusObservable getInstance(){
        if (null == instance){
            synchronized (StatusObservable.class){
                if (null == instance){
                    instance = new StatusObservable();
                }
            }
        }
        return instance;
    }


    // 添加观察者
    public void addObserver(ILiveLoginManager.TILVBStatusListener listener){
        if (!listObservers.contains(listener)){
            listObservers.add(listener);
        }
    }

    // 移除观察者
    public void deleteObserver(ILiveLoginManager.TILVBStatusListener listener){
        listObservers.remove(listener);
    }

    // 获取观察者数量
    public int getObserverCount(){
        return listObservers.size();
    }

    @Override
    public void onForceOffline(int error, String message) {
        // 拷贝链表
        LinkedList<ILiveLoginManager.TILVBStatusListener> tmpList = new LinkedList<>(listObservers);
        for (ILiveLoginManager.TILVBStatusListener listener : tmpList){
            listener.onForceOffline(error, message);
        }
    }
}
