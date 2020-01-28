package com.example.xmlystudio.base;

public interface IBasePresenter<T> {


    /**
     *  注册回调借口
     * @param t
     */
    void registerViewCallback(T t);

    /**
     * 取消注册回调借口
     * @param
     */
    void unRegisterViewCallback(T t);


}
