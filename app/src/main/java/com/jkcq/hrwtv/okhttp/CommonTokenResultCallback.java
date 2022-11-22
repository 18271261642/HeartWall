package com.jkcq.hrwtv.okhttp;

import com.jkcq.hrwtv.okhttp.callback.ResultCallback;
import com.jkcq.hrwtv.okhttp.request.OkHttpRequest;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;


/**
 * @author mhj
 *         Create at 2017/10/26 18:23
 */
public abstract class CommonTokenResultCallback<T> extends ResultCallback<T> {

    public CommonTokenResultCallback() {
    }

    public CommonTokenResultCallback(Class<T> clazz) {
        super(clazz);
    }

    @Override
    public void onStart(Request request) {
        super.onStart(request);
    }

    protected abstract OkHttpRequest.Builder getRequestBuilder();

    @Override
    public void onHeaders(Headers headers) {
        super.onHeaders(headers);
        if (null == headers) {
            return;
        }

//        Logger.e("onHeaders = " + headers.toString());
//        if (!TextUtils.isEmpty(headers.get("token"))) {
//            TokenUtil.getInstance().updateToken(BaseApp.getApp(), headers.get("token"));
//        }
    }

    @Override
    public boolean onResponseIntercept(T response) {
        return super.onResponseIntercept(response);
//        if (response instanceof BaseBean) {
//            BaseBean baseBean = (BaseBean) response;
//            if (baseBean.getCode() == 1000) {
//                refreshToken();
//                return true;
//            }
//            return super.onResponseIntercept(response);
//        } else if (response instanceof String) {
//            String result = (String) response;
//
//            //refreshToken();
//            return super.onResponseIntercept(response);
//        } else {
//            return super.onResponseIntercept(response);
//        }
    }

    /**
     * 刷新Token请求
     */
    private void refreshToken() {
        String url = "";
        HashMap<String, String> params = new HashMap<>();

        OkHttpRequest.Builder builder = new OkHttpRequest.Builder()
                .url(url)
                .params(params)
                .tag(url);

        builder.get(new ResultCallback<String>() {

            @Override
            public void onResponse(String bean) {
                requestAgain();
            }

            @Override
            public void onError(Request request, Exception e) {
                CommonTokenResultCallback.this.onError(request, e);
            }
        });
    }

    /**
     * Token刷新后重新请求一次
     */
    private void requestAgain() {
        OkHttpRequest.Builder builder = getRequestBuilder();
        if (null == builder) {
            onError(null, new NullPointerException("builder is null."));
            return;
        }

        //TODO 更新Token
        Map<String, String> params = builder.getParams();
        params.put("token", "");

        builder.builder(this);
    }
}