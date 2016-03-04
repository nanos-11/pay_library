package com.jiyoutang.paylibrary.model;

/**
 * 支付宝订单
 * Created by Administrator on 2015/5/26.
 */
public class AlipayOrderEntity {
    // 合作者身份ID
    private String id;
    // 卖家支付宝账号
    private String seller_id;
    // 商户网站唯一订单号
    private String shopOrderNumber;
    // 商品名称
    private String goodName;
    // 商品详情
    private String body;
    // 商品金额
    private double totalfee;
    // 服务器异步通知页面路径
    private String notify_url;
    // 接口名称， 固定值
    private String service;
    // 支付类型， 固定值
    private int paymentType;
    // 参数编码， 固定值
    private String charset;
    // 设置未付款交易的超时时间
    private String endTime;
    // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
    private String backUrl;
    // 调用银行卡支付，需配置此参数，参与签名， 固定值
    private String payMethod;

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getShopOrderNumber() {
        return shopOrderNumber;
    }

    public void setShopOrderNumber(String shopOrderNumber) {
        this.shopOrderNumber = shopOrderNumber;
    }

    public double getTotalfee() {
        return totalfee;
    }

    public void setTotalfee(double totalfee) {
        this.totalfee = totalfee;
    }
}
