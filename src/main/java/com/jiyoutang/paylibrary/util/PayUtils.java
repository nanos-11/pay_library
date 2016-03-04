package com.jiyoutang.paylibrary.util;

import com.jiyoutang.paylibrary.alipay.AlipayBase64;
import com.jiyoutang.paylibrary.model.AlipayOrderEntity;
import com.jiyoutang.paylibrary.wxpay.MD5;

import org.apache.http.NameValuePair;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.List;

/**
 * Created by tianlong on 2015/10/9.
 */
public class PayUtils {

    private static final String ALGORITHM = "RSA";
    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";
    private static final String DEFAULT_CHARSET = "UTF-8";

    public static String sign(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                    AlipayBase64.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update(content.getBytes(DEFAULT_CHARSET));

            byte[] signed = signature.sign();

            return AlipayBase64.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * sign the order info. 生成订单内容
     *
     * @param orderEntity 订单信息
     */
    public static String creatOrder(AlipayOrderEntity orderEntity, String defaultPartner, String defaultSeller) {
        //签约合作者身份ID
        String orderInfo = "partner=" + "\"" + defaultPartner + "\"";

        // 卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + defaultSeller + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + orderEntity.getShopOrderNumber() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + orderEntity.getGoodName() + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + orderEntity.getBody() + "\"";

        // 商品金额 orderEntity.getTotalfee()
        orderInfo += "&total_fee=" + "\"" + orderEntity.getTotalfee() + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + orderEntity.getNotify_url() + "\"";

        // 接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"" + orderEntity.getPaymentType() + "\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"" + orderEntity.getEndTime() + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";
        return orderInfo;
    }

    public static String genAppSign(List<NameValuePair> params, String appKey) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(appKey);
        LogUtils.d("request=" + sb.toString());
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        LogUtils.d("appSign=" + appSign);
        return appSign;
    }

    public static long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }
}
