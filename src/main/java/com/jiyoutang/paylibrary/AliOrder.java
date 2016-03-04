package com.jiyoutang.paylibrary;

/**
 * Created by tianlong on 2015/10/9.
 */
public class AliOrder {
    //唯一订单号
    private String adCode;
    //商品价格
    private double price;
    //支付宝回调地址
    private String notifyUrl;
    //商品详情："充值：5象芽币"
    private String body;
    //商品名称："充值象芽币"
    private String goodsName;

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
}
