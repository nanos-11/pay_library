# pay_library
安卓支付模块集成说明


集成说明	对外API
1.	支付类PayHelper
PayHelper.initAliParam(String defaultPartner, String defaultSeller, String privateString);
方法说明：
初始化支付宝相关参数
参数说明：
defaultPartner 支付宝合作者Id  // 2088111560672915
defaultSeller 支付宝收款账号  // admin@xx.com
privateString 商品私钥
使用说明：
调用该方法生成静态的支付对象，但是需要在最后退出支付界面或者是支付成功之后调用
PayHelper.destory()销毁静态支付对象

PayHelper.initWeixinParam(String appKey, String appId, String partnerId);
方法说明：
初始化支付宝相关参数
参数说明：
appId 微信支付的appId  // wx2d140b79c9b16d9f
appKey 微信支付的appkey  // wx2d140b79c9b16d9f
     partnerId微信支付的partnerId  // 1246680191
使用说明：
调用该方法生成静态的支付对象，但是需要在最后退出支付界面或者是支付成功之后调用
PayHelper.destory()销毁静态支付对象

PayHelper.payAliOrder(Context context, AliOrder order, PayStatusListener payStatusListener);
方法说明：
支付支付宝订单
参数说明：
context上下文  // 
order支付宝订单对象  // 
payStatusListener 支付回调接口
使用说明：
支付支付宝的订单的时候需要调用

PayHelper.payWxOrder(Context context, WxOrder order, PayStatusListener payStatusListener);
方法说明：
支付微信订单
参数说明：
context上下文  // 
order微信订单对象  // 
payStatusListener支付回调接口
使用说明：
支付支付宝的订单的时候需要调用

PayHelper. isWXAppInstalledAndSupported(Context context,String appKey)
方法说明：
判断客户端是否安装微信
参数说明：
     context 上下文 // getApplicationcontext()或者是Activity.this
      appKey  微信支付的appKey  // 33D63A4D948D48B94471C5618FFD516D
使用说明：
     调用此方法主要用来判断客户端是否安装微信，支付前调用

PayHelper.destory();
方法说明：
销毁已经创建的支付宝或者是微信支付对象
参数说明：
无
使用说明：
调用该方法可以销毁已经创建的支付宝或者是微信支付对象

PayHelper.setDebug(boolean debug);
方法说明：
设置是否需要打印log
参数说明：
debug是否允许打印log // 
使用说明：
设置是否需要打印log

2.	支付宝订单AliOrder
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
3.	微信订单WxOrder
//随机字符串
private String nonceString;
//微信支付唯一id
private String prepayId;


4.	支付回调PayStatusListener
抽象接口说明：
public interface PayStatusListener {
    public void paySuccess();
    public void payFailed();
    public void payCancel();
}
使用说明：
配合支付订单接口使用
	混淆
   支付宝请添加:
       -keep class com.alipay.android.app.IAliPay{*;}
       -keep class com.alipay.android.app.IAlixPay{*;}
       -keep class com.alipay.android.app.IRemoteServiceCallback{*;}
       -keep class com.alipay.android.app.lib.ResourceMap{*;}
微信请添加:
    -keep class com.tencent.mm.sdk.modelmsg.** implements
     com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
    -keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
	AndroidManifest.xml
a)	需要把lib中的wxapi的包名改成<PackageName>.wxapi；
b)	在androidManifest中注册activity& receiver：
<activity
	android:name="com.alipay.sdk.app.H5PayActivity"
	android:configChanges="orientation|keyboardHidden|navigation"
	android:exported="false"
	android:screenOrientation="behind"
	android:windowSoftInputMode="adjustResize|stateHidden"/>
<activity
    android:name="com.jiyoutang.scanissue.wxapi.WXPayEntryActivity"
    android:exported="true"
    android:launchMode="singleTop"
    android:screenOrientation="portrait"/>
内部设计
     1.相关支付（微信和支付宝）的参数在PayHelper类中初始化（需要对相关的参数
       进行一下配置，建议在主线程中调用）,其中包含调用微信客户端的判断（判断微信
       是否安装）、生成相关支付对象的销毁（主要针对静态变量），PayHelper类中包含
       支付调用的方法，需要传入的参数。
     2.支付签名、生成订单主要在PayUtils类中，主要有支付宝支付生成订单、签名，微信
       订单签名的方法（函数），为支付辅助工具类（主项目无需调用）。
     3.支付实体类AliOrder和WxOrder主要供外部调用支付方法（函数）时生成支付订单
       的对象（封装的数据主要来自服务器）
     4.获取支付的状态，主要传入PayStatusListener对象实现对支付宝或者微信支付的状态
       的获取，有PaySucess（）、PayFailed（），PayCancel（），其中支付宝是通异步过
       线程实现对支付宝sdk的调用，在PayHelper通过handler发送消息通过
        PayStatusListener的回调方法将支付状态回调到主项目中（主线程），接下来处理
       你自己相关的业务即可。
     5.有关支付宝和微信支付需要的base64加密和相关的一些工具类在这里就赘述，详细
       可查看sdk。
      
     
       
      
   

