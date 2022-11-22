package com.jkcq.hrwtv.okhttp.request;

import android.text.TextUtils;

import com.jkcq.hrwtv.okhttp.OkHttpClientManager;
import com.jkcq.hrwtv.okhttp.callback.ResultCallback;
import com.jkcq.hrwtv.util.Logger;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.util.Map;


public class OkHttpPostRequest extends OkHttpRequest {
    protected String content;
    protected String jsonContent;
    protected byte[] bytes;
    protected File file;

    protected int type = TYPE_PARAMS;
    protected static final int TYPE_PARAMS = 1;
    protected static final int TYPE_STRING = 2;
    protected static final int TYPE_BYTES = 3;
    protected static final int TYPE_FILE = 4;
    protected static final int TYPE_JSON = 5;

    protected final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream;charset=utf-8");
    protected final MediaType MEDIA_TYPE_STRING = MediaType.parse("text/plain;charset=utf-8");
    protected final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=UTF-8");


    protected OkHttpPostRequest(String url, String tag, Map<String, String> params, Map<String, String> headers, String content, String jsonContent, byte[] bytes, File file) {
        super(url, tag, params, headers);
        this.content = content;
        this.jsonContent = jsonContent;
        this.bytes = bytes;
        this.file = file;
        validParams();
    }

    private void validParams() {
        int count = 0;
        if (params != null /*&& !params.isEmpty()*/) {
            type = TYPE_PARAMS;
            count++;
        }
        if (content != null) {
            type = TYPE_STRING;
            count++;
        }
        if (jsonContent != null) {
            type = TYPE_JSON;
            count++;
        }
        if (bytes != null) {
            type = TYPE_BYTES;
            count++;
        }
        if (file != null) {
            type = TYPE_FILE;
            count++;
        }

        if (count <= 0 || count > 1) {
            //throw new IllegalArgumentException("the params , content , file , bytes must has one and only one .");
        }
    }

    @Override
    protected Request buildRequest() {
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url can not be empty!");
        }

        Request.Builder builder = new Request.Builder();
        appendHeaders(builder, headers);
        builder.url(url).tag(tag).post(requestBody);
        return builder.build();
    }

    @Override
    protected RequestBody buildRequestBody() {
        RequestBody requestBody = null;
        switch (type) {
            case TYPE_PARAMS:
                FormEncodingBuilder builder = new FormEncodingBuilder();
                addParams(builder, params);
                requestBody = builder.build();
                Logger.e("访问url" + String.valueOf(url + params.toString().trim().replace(", ", "&").replace("{", "?").replace("}", "")));
                break;
            case TYPE_BYTES:
                requestBody = RequestBody.create(MEDIA_TYPE_STREAM, bytes);
                break;
            case TYPE_FILE:
                requestBody = RequestBody.create(MEDIA_TYPE_STREAM, file);
                break;
            case TYPE_STRING:
                requestBody = RequestBody.create(MEDIA_TYPE_STRING, content);
                Logger.e("访问url" + String.valueOf(url + "&&" + content));
                break;
            case TYPE_JSON:
                requestBody = RequestBody.create(MEDIA_TYPE_JSON, jsonContent);
                Logger.e("访问url" + String.valueOf(url + "&&" + jsonContent));
                break;
        }
        return requestBody;
    }

    @Override
    protected RequestBody wrapRequestBody(RequestBody requestBody, final ResultCallback callback) {
        CountingRequestBody countingRequestBody = new CountingRequestBody(requestBody, new CountingRequestBody.OnRequestProgressListener() {
            @Override
            public void onRequestProgress(final long bytesWritten, final long contentLength) {
                final float progress = bytesWritten * 1.0f / contentLength;
                Logger.e("onRequestProgress progress = " + progress);
                OkHttpClientManager.getInstance().getDelivery().post(new Runnable() {
                    @Override
                    public void run() {
                        callback.inProgress(progress);
                    }
                });
            }
        });
        return countingRequestBody;
    }

    private void addParams(FormEncodingBuilder builder, Map<String, String> params) {
        if (builder == null) {
            throw new IllegalArgumentException("builder can not be null .");
        }

        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                if (TextUtils.isEmpty(params.get(key))) {
                    builder.add(key, "");
                } else {
                    builder.add(key, params.get(key));
                }
            }
        }
    }
}