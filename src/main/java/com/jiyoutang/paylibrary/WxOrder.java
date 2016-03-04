package com.jiyoutang.paylibrary;

/**
 * Created by tianlong on 2015/10/9.
 */
public class WxOrder {
    //微信支付唯一id
    private String prepayId;
    //随机字符串
    private String nonceString;

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getNonceString() {
        return nonceString;
    }

    public void setNonceString(String nonceString) {
        this.nonceString = nonceString;
    }
}
