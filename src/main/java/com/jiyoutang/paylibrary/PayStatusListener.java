package com.jiyoutang.paylibrary;

/**
 * 支付状态接口
 * Created by admin on 2015/9/28.
 */
public interface PayStatusListener {
    public void paySuccess();
    public void payFailed();
    public void payCancel();
}
