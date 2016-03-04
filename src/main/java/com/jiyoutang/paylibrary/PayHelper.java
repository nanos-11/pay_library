package com.jiyoutang.paylibrary;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.jiyoutang.paylibrary.model.AlipayOrderEntity;
import com.jiyoutang.paylibrary.util.LogUtils;
import com.jiyoutang.paylibrary.util.PayUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

/**
 * 初始化支付参数
 * Created by admin on 2015/9/28.
 */
public class PayHelper {
    private static AlipayOrderEntity mAlipayOrderEntity;//支付宝支付对象
    private static PayReq mRequest;
    private static String mDefaultPartner;
    private static String mDefaultSeller;
    private static String mPrivateString;
    private static IWXAPI msgApi;
    private static String mAppId;
    private static String mAppKey;
    private static final int PAY_STAGE = 3000;
    private static PayStatusListener mPayStatusListener;

    /**
     * 对外透漏获取支付监听接口
     */
    public static PayStatusListener getPayStatusListener() {
        return mPayStatusListener;
    }

    /**
     * 对外抛出mAppId
     */
    public static String getAppId() {
        return mAppId;
    }

    /**
     * 对外透漏设置调试模式（是否带log）接口
     */
    public static void setDebug(boolean debug) {
        LogUtils.setmPrintLog(debug);
    }

    /**
     * 初始化支付宝支付参数
     *
     * @param defaultPartner 支付宝合作者Id
     * @param defaultSeller  支付宝收款账号
     * @param privateString  商户私钥
     */
    public static void initAliParam(String defaultPartner, String defaultSeller, String privateString) {
        mDefaultPartner = defaultPartner;
        mDefaultSeller = defaultSeller;
        mPrivateString = privateString;
        if (mAlipayOrderEntity == null) {
            mAlipayOrderEntity = new AlipayOrderEntity();
        }
        mAlipayOrderEntity.setService("mobile.securitypay.pay");
        mAlipayOrderEntity.setPaymentType(1);
        mAlipayOrderEntity.setCharset("utf-8");
        mAlipayOrderEntity.setEndTime("2h");
        mAlipayOrderEntity.setBackUrl("m.alipay.com");
    }

    /**
     * 初始化微信支付参数
     *
     * @param appId     微信支付的appId
     * @param appKey    微信支付的appKey
     * @param partnerId 微信支付的partnerId
     */
    public static void initWeixinParam(String appKey, String appId, String partnerId) {
        mAppId = appId;
        mAppKey = appKey;
        if (mRequest == null) {
            mRequest = new PayReq();
        }
        mRequest.appId = appId;
        mRequest.partnerId = partnerId;
        mRequest.packageValue = "Sign=WXPay";
        mRequest.timeStamp = String.valueOf(PayUtils.genTimeStamp());
    }

    /**
     * 支付阿里的订单
     *
     * @param context           上下文
     * @param order             阿里的订单
     * @param payStatusListener 支付监听
     */
    public static void payAliOrder(final Context context, AliOrder order, PayStatusListener payStatusListener) {
        if (mAlipayOrderEntity == null) {
            if (payStatusListener != null) {
                payStatusListener.payFailed();
            }
            return;
        }
        mPayStatusListener = payStatusListener;

        if (mAlipayOrderEntity != null) {
            mAlipayOrderEntity.setShopOrderNumber(order.getAdCode());
            mAlipayOrderEntity.setBody(order.getBody());
            mAlipayOrderEntity.setGoodName(order.getGoodsName());
            mAlipayOrderEntity.setTotalfee(order.getPrice());
            mAlipayOrderEntity.setNotify_url(order.getNotifyUrl());
            String orderInfoz = PayUtils.creatOrder(mAlipayOrderEntity, mDefaultPartner, mDefaultSeller);
            String sign = PayUtils.sign(orderInfoz, mPrivateString);
            try {
                // 仅需对sign 做URL编码
                sign = URLEncoder.encode(sign, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                final String payInfo = orderInfoz + "&sign=\"" + sign + "\"&"
                        + "sign_type=\"RSA\"";
                Runnable pay = new Runnable() {
                    @Override
                    public void run() {
                        synchronized (this) {
                            PayTask alipayTask = new PayTask((Activity) context);
                            String payResult = alipayTask.pay(payInfo);
                            //String payStatusListener
                            Message msg = new Message();
                            msg.what = PAY_STAGE;
                            msg.obj = payResult;
                            if (mHandler != null) {
                                if (mHandler.hasMessages(PAY_STAGE)) {
                                    mHandler.removeMessages(PAY_STAGE);
                                }
                                mHandler.sendMessageDelayed(msg, 300);
                            }

                        }
                    }
                };
                Thread payThread = new Thread(pay);
                payThread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 支付阿里的订单
     *
     * @param context           上下文
     * @param order             阿里的订单
     * @param payStatusListener 支付监听
     */
    public static void payWxOrder(final Context context, WxOrder order, PayStatusListener payStatusListener) {
        if (!PayHelper.isWXAppInstalledAndSupported(context, mAppId)) {
            if (payStatusListener != null) {
                payStatusListener.payFailed();
            }
            return;
        }
        mPayStatusListener = payStatusListener;
        PayReq payReq = PayHelper.mRequest;
        payReq.prepayId = order.getPrepayId();
        payReq.nonceStr = order.getNonceString();
        if (payReq != null) {
            List<NameValuePair> signParams = new LinkedList<NameValuePair>();
            signParams.add(new BasicNameValuePair("appid", payReq.appId));
            signParams.add(new BasicNameValuePair("noncestr", payReq.nonceStr));
            signParams.add(new BasicNameValuePair("package", payReq.packageValue));
            signParams.add(new BasicNameValuePair("partnerid", payReq.partnerId));
            signParams.add(new BasicNameValuePair("prepayid", payReq.prepayId));
            signParams.add(new BasicNameValuePair("timestamp", payReq.timeStamp));
            payReq.sign = PayUtils.genAppSign(signParams, mAppKey);
            if (PayHelper.msgApi != null) {
                PayHelper.msgApi.sendReq(payReq);
            }
        }

    }


    /**
     * 销毁支付对象
     */
    public static void destory() {
        if (mAlipayOrderEntity != null) {
            mAlipayOrderEntity = null;
        }
        if (mRequest != null) {
            mRequest = null;
        }
        if (mDefaultPartner != null) {
            mDefaultPartner = null;
        }
        if (mDefaultSeller != null) {
            mDefaultSeller = null;
        }
        if (msgApi != null) {
            msgApi = null;
        }
        if (mAppId != null) {
            mAppId = null;
        }
        if (mPayStatusListener != null) {
            mPayStatusListener = null;
        }
        System.gc();
    }

    /**
     * 判断微信是否可用
     *
     * @param context 上下文
     * @param appId   微信支付的appId
     */
    public static boolean isWXAppInstalledAndSupported(Context context, String appId) {
        mAppId = appId;
        msgApi = WXAPIFactory.createWXAPI(context, null);
        msgApi.registerApp(appId);
        return msgApi.isWXAppInstalled() && msgApi.isWXAppSupportAPI();
    }

    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PAY_STAGE:
                    String resultStatus = msg.obj.toString();
                    String tradeStatus = "resultStatus={";
                    int imemoStart = resultStatus.indexOf("resultStatus=");
                    imemoStart += tradeStatus.length();
                    int imemoEnd = resultStatus.indexOf("};memo=");
                    tradeStatus = resultStatus.substring(imemoStart, imemoEnd);
                    int status = Integer.valueOf(tradeStatus);
                    if (mPayStatusListener != null) {
                        switch (status) {
                            case 9000:
                                mPayStatusListener.paySuccess();
                                break;
                            case 6001:
                                mPayStatusListener.payCancel();
                                break;
                            case 8000:
                                mPayStatusListener.payFailed();
                                break;
                        }
                        mPayStatusListener = null;
                    }
                    break;
            }
        }
    };
}
