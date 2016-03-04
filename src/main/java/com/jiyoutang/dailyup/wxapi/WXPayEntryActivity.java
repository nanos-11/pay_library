package com.jiyoutang.dailyup.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jiyoutang.paylibrary.PayHelper;
import com.jiyoutang.paylibrary.PayStatusListener;
import com.jiyoutang.paylibrary.R;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


/**
 * Created by Administrator on 2015/5/29.
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";
    private static final int Sucess = 0;
    private static final int Fail = -1;
    private static final int Cancle = -2;
    private IWXAPI api;
    private static PayStatusListener mPayStatusListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result_layout);
        mPayStatusListener=PayHelper.getPayStatusListener();
        api = WXAPIFactory.createWXAPI(this, PayHelper.getAppId());

        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int code = resp.errCode;
            switch (code) {
                case Sucess:
                    if (mPayStatusListener != null) {
                        mPayStatusListener.paySuccess();
                    }
                    this.finish();
                    break;
                case Fail:
                    if (mPayStatusListener != null) {
                        mPayStatusListener.payFailed();
                    }
                    this.finish();
                    break;
                case Cancle:
                    if (mPayStatusListener != null) {
                        mPayStatusListener.payCancel();
                    }
                    this.finish();
                    break;
                default:
                    if (mPayStatusListener != null) {
                        mPayStatusListener.payFailed();
                    }
                    this.finish();
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPayStatusListener != null) {
            mPayStatusListener = null;
        }
    }
}