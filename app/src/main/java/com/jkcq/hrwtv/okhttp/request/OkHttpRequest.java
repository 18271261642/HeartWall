package com.jkcq.hrwtv.okhttp.request;

import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;
import android.widget.ImageView;

import com.jkcq.hrwtv.okhttp.OkHttpClientManager;
import com.jkcq.hrwtv.okhttp.callback.ResultCallback;
import com.jkcq.hrwtv.util.Logger;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by zhy on 15/11/6.
 */
public abstract class OkHttpRequest {
    protected OkHttpClient mOkHttpClient;

    protected RequestBody requestBody;
    protected Request request;

    protected String url;
    protected String tag;
    protected Map<String, String> params;
    protected Map<String, String> headers;

    protected OkHttpRequest(String url, String tag,
                            Map<String, String> params, Map<String, String> headers) {
        this.url = url;
        this.tag = tag;
        this.params = params;
        this.headers = headers;
    }

    protected abstract Request buildRequest();

    protected abstract RequestBody buildRequestBody();

    protected void prepareInvoked(ResultCallback callback) {
        requestBody = buildRequestBody();
        requestBody = wrapRequestBody(requestBody, callback);
        request = buildRequest();
    }

    public void invokeAsyn(ResultCallback callback) {
        prepareInvoked(callback);
        OkHttpClientManager.getInstance().execute(request, callback);
    }

    /**
     * 文件上传下载进度
     *
     * @param requestBody
     * @param callback
     * @return
     */
    protected RequestBody wrapRequestBody(RequestBody requestBody, final ResultCallback callback) {
        return requestBody;
    }

    public <T> T invoke(Class<T> clazz) throws IOException {
        requestBody = buildRequestBody();
        Request request = buildRequest();
        return OkHttpClientManager.getInstance().execute(request, clazz);
    }

    /**
     * 添加Header
     *
     * @param builder
     * @param headers
     */
    protected void appendHeaders(Request.Builder builder, Map<String, String> headers) {
        if (builder == null) {
            throw new IllegalArgumentException("builder can not be empty!");
        }

        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) return;

        for (String key : headers.keySet()) {
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
        Logger.e("headers: " + headerBuilder.build().toString());
    }

    /**
     * 取消指定的tag请求队列
     */
    public void cancel() {
        if (!TextUtils.isEmpty(tag))
            OkHttpClientManager.getInstance().cancelTag(tag);
    }

    public static class Builder {
        private String url;
        private String tag;
        private String method;
        private Map<String, String> headers;
        private Map<String, String> params;
        private Pair<String, File>[] files;

        private String destFileDir;
        private String destFileName;

        private ImageView imageView;
        private int errorResId = -1;
        private Context context;
        //for post
        private String content;
        private String jsonContent;
        private byte[] bytes;
        private File file;

        public Builder() {
        }

        public Builder url(Context context, String url) {
            this.url = null == url ? "" : url;
            this.context = context;
            return this;
        }

        public Builder url(String url) {
            this.url = null == url ? "" : url;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder params(Map<String, String> params) {
            this.params = null == params ? new HashMap<String, String>() : params;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = null == headers ? new HashMap<String, String>() : headers;
            return this;
        }

        public Builder file(File file) {
            this.file = file;
            return this;
        }

        public Builder files(Pair<String, File>... files) {
            if (null == files || files.length <= 0) {
                return this;
            }
            this.files = files;
            return this;
        }

        public Builder destFileName(String destFileName) {
            this.destFileName = destFileName;
            return this;
        }

        public Builder destFileDir(String destFileDir) {
            this.destFileDir = destFileDir;
            return this;
        }

        public Builder imageView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public Builder errResId(int errorResId) {
            this.errorResId = errorResId;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder jsonContent(String jsonContent) {
            this.jsonContent = null == jsonContent ? "" : jsonContent;
            return this;
        }

        public String getUrl() {
            return url;
        }

        public String getTag() {
            return tag;
        }

        public String getMethod() {
            return method;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public Map<String, String> getParams() {
            return params;
        }

        public Pair<String, File>[] getFiles() {
            return files;
        }

        public String getDestFileDir() {
            return destFileDir;
        }

        public String getDestFileName() {
            return destFileName;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public int getErrorResId() {
            return errorResId;
        }

        public Context getContext() {
            return context;
        }

        public String getContent() {
            return content;
        }

        public String getJsonContent() {
            return jsonContent;
        }

        public byte[] getBytes() {
            return bytes;
        }

        public File getFile() {
            return file;
        }

        public <T> T get(Class<T> clazz) throws IOException {
            method = Method.GET;
            OkHttpRequest request = new OkHttpGetRequest(url, tag, params, headers);
            return request.invoke(clazz);
        }

        public OkHttpRequest get(ResultCallback callback) {
            method = Method.GET;
            OkHttpRequest request = new OkHttpGetRequest(url, tag, params, headers);
            request.invokeAsyn(callback);
            return request;
        }

        public <T> T post(Class<T> clazz) throws IOException {
            method = Method.POST;
            OkHttpRequest request = new OkHttpPostRequest(url, tag, params, headers, content, jsonContent, bytes, file);
            return request.invoke(clazz);
        }

        public OkHttpRequest post(ResultCallback callback) {
            method = Method.POST;
            OkHttpRequest request = new OkHttpPostRequest(url, tag, params, headers, content, jsonContent, bytes, file);
            request.invokeAsyn(callback);
            return request;
        }

        public OkHttpRequest upload(ResultCallback callback) {
            method = Method.UPLOAD;
            OkHttpRequest request = new OkHttpUploadRequest(url, tag, params, headers, files);
            request.invokeAsyn(callback);
            return request;
        }

        public <T> T upload(Class<T> clazz) throws IOException {
            method = Method.UPLOAD;
            OkHttpRequest request = new OkHttpUploadRequest(url, tag, params, headers, files);
            return request.invoke(clazz);
        }

        public OkHttpRequest download(ResultCallback callback) {
            if (params != null) {
                Logger.e("访问url：" + String.valueOf(url + params.toString().trim().replace(", ", "&").replace("{", "?").replace("}", "")));
            } else {
                Logger.e("访问url：" + String.valueOf(url));
            }

            method = Method.DOWNLOAD;
            OkHttpRequest request = new OkHttpDownloadRequest(url, tag, params, headers, destFileName, destFileDir);
            request.invokeAsyn(callback);
            return request;
        }

        public String download() throws IOException {
            if (params != null) {
                Logger.e("访问url：" + String.valueOf(url + params.toString().trim().replace(", ", "&").replace("{", "?").replace("}", "")));
            } else {
                Logger.e("访问url：" + String.valueOf(url));
            }

            method = Method.DOWNLOAD;
            OkHttpRequest request = new OkHttpDownloadRequest(url, tag, params, headers, destFileName, destFileDir);
            return request.invoke(String.class);
        }

        public void displayImage(ResultCallback callback) {
            method = Method.DISPLAYIMAGE;
            OkHttpRequest request = new OkHttpDisplayImgRequest(url, tag, params, headers, imageView, errorResId);
            request.invokeAsyn(callback);
        }

        public void builder(ResultCallback callback) {
            if (TextUtils.isEmpty(method)) {
                return;
            }

            switch (method) {
                case Method.POST:
                    post(callback);
                    break;
                case Method.GET:
                    get(callback);
                    break;
                case Method.UPLOAD:
                    upload(callback);
                    break;
                case Method.DOWNLOAD:
                    download(callback);
                    break;
                case Method.DISPLAYIMAGE:
                    displayImage(callback);
                    break;
            }
        }

        public <T> T builder(Class<T> clazz) throws IOException {
            if (TextUtils.isEmpty(method)) {
                throw new NullPointerException("method is null.");
            }

            switch (method) {
                case Method.POST:
                    return post(clazz);
                case Method.GET:
                    return get(clazz);
                case Method.UPLOAD:
                    return upload(clazz);
            }
            return null;
        }
    }

    public interface Method {
        String POST = "post";
        String GET = "get";
        String UPLOAD = "upload";
        String DOWNLOAD = "download";
        String DISPLAYIMAGE = "displayImage";
    }
}