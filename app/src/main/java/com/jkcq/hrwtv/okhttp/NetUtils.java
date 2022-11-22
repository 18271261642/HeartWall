package com.jkcq.hrwtv.okhttp;

import android.util.Pair;

import com.google.gson.JsonObject;
import com.jkcq.hrwtv.okhttp.request.OkHttpDownloadRequest;
import com.jkcq.hrwtv.okhttp.request.OkHttpRequest;
import com.jkcq.hrwtv.okhttp.request.OkHttpUploadRequest;
import com.jkcq.hrwtv.util.JsonUtils;
import com.squareup.okhttp.Request;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/*
 *
 * @author 苗恒聚
 * V 1.0.0
 * Create at 2016/10/28 13:55
 */
public class NetUtils {

    public static <T extends BaseBean> void doPost(String url, HashMap<String, Object> sq, final Class<T> clazz, final OnHttpRequestCallBack<T> callback) {
        final OkHttpRequest.Builder builder = new OkHttpRequest.Builder()
                .url(url)
                .jsonContent(JsonUtils.getInstance().toJSON(sq))
                .headers(appendHeaders())
                .tag(url);

        builder.post(new CommonTokenResultCallback<T>(clazz) {

            @Override
            protected OkHttpRequest.Builder getRequestBuilder() {
                return builder;
            }

            @Override
            public void onResponse(T bean) {
                if (null == bean) {
                    if (callback != null) {
                        callback.onGetErrorCode(bean);
                    }
                    return;
                }

                if (bean.getCode() == 2000 || bean.getCode() == 2001 || bean.getCode() == 2002 || bean.getCode() == 200) {//TODO 后台规定200,2000,2001,2002即为成功
                    if (callback != null)
                        callback.onSuccess(bean);
                } else {
                    //访问错误
                    if (callback != null) {
                        callback.onGetErrorCode(bean);
                    }
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                if (callback != null) {
                    callback.onFailure(0, e.getMessage());
                }
            }
        });
    }

    public static <T extends BaseBean> void doGet(String url, HashMap<String, String> sq, final Class<T> clazz, final OnHttpRequestCallBack<T> callback) {
        final OkHttpRequest.Builder builder = new OkHttpRequest.Builder()
                .url(url)
                .params(sq)
                .headers(appendHeaders())
                .tag(url);

        builder.get(new CommonTokenResultCallback<T>(clazz) {

            @Override
            protected OkHttpRequest.Builder getRequestBuilder() {
                return builder;
            }

            @Override
            public void onResponse(T bean) {
                if (null == bean) {
                    if (callback != null) {
                        callback.onGetErrorCode(bean);
                    }
                    return;
                }

                if (bean.getCode() == 200 || bean.getCode() == 2000) {
                    if (callback != null)
                        callback.onSuccess(bean);
                } else {
                    //访问错误
                    if (callback != null) {
                        callback.onGetErrorCode(bean);
                    }
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                if (callback != null) {
                    callback.onFailure(0, e.getMessage());
                }
            }
        });
    }



    public static void doDownload(String url, HashMap<String, Object> sq, String destFileDir, String destFileName, final OnHttpRequestCallBack<String> callback) {
        final OkHttpDownloadRequest.Builder builder = new OkHttpRequest.Builder()
                .url(url)
                .jsonContent(JsonUtils.getInstance().toJSON(sq))
                .headers(appendHeaders())
                .tag(url)
                .destFileDir(destFileDir)
                .destFileName(destFileName);

        builder.download(new CommonTokenResultCallback() {
            @Override
            protected OkHttpRequest.Builder getRequestBuilder() {
                return builder;
            }

            @Override
            public void onResponse(Object response) {
                if (callback != null) {
                    callback.onSuccess(response.toString());
                }
            }

            @Override
            public void inProgress(float progress) {
                if (callback != null) {
                    callback.inProgress(progress);
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                if (callback != null) {
                    callback.onFailure(0, e.getMessage());
                }
            }
        });
    }

    public static void doUpload(String url, HashMap<String, Object> sq, Pair<String, File>[] files, final OnHttpRequestCallBack<List<String>> callback) {
        final OkHttpUploadRequest.Builder builder = new OkHttpUploadRequest.Builder()
                .url(url)
                .jsonContent(JsonUtils.getInstance().toJSON(sq))
                .headers(appendHeaders())
                .tag(url)
                .files(files);

        builder.upload(new CommonTokenResultCallback() {
            @Override
            protected OkHttpRequest.Builder getRequestBuilder() {
                return builder;
            }

            @Override
            public void onResponse(Object response) {
                JsonObject jsonObject = (JsonObject) response;
                if (!jsonObject.has("url")) {
                    if (callback != null) {
                        callback.onGetErrorCode(null);
                    }
                    return;
                }
                List<String> files = JsonUtils.getInstance().parseArray(jsonObject.getAsJsonArray("url").toString(), String.class);

                if (callback != null) {
                    callback.onSuccess(files);
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                if (callback != null) {
                    callback.onFailure(0, e.getMessage());
                }
            }
        });
    }

    public static <T extends BaseBean> void doUpload(String url, HashMap<String, Object> sq, Pair<String, File>[] files, final Class<T> clazz, final OnHttpRequestCallBack<T> callback) {
        final OkHttpUploadRequest.Builder builder = new OkHttpUploadRequest.Builder()
                .url(url)
                .jsonContent(JsonUtils.getInstance().toJSON(sq))
                .headers(appendHeaders())
                .tag(url)
                .files(files);

        builder.upload(new CommonTokenResultCallback<T>(clazz) {
            @Override
            protected OkHttpRequest.Builder getRequestBuilder() {
                return builder;
            }

            @Override
            public void onResponse(T bean) {
                if (null == bean) {
                    if (callback != null) {
                        callback.onGetErrorCode(bean);
                    }
                    return;
                }

                if (bean.getCode() == 2000 || bean.getCode() == 2001 || bean.getCode() == 2002) {//TODO 2000即为成功
                    if (callback != null)
                        callback.onSuccess(bean);
                } else {
                    //访问错误
                    if (callback != null) {
                        callback.onGetErrorCode(bean);
                    }
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                if (callback != null) {
                    callback.onFailure(0, e.getMessage());
                }
            }
        });
    }

    private static HashMap<String, String> appendHeaders() {
        HashMap<String, String> headers = new HashMap<>();

//        String token = TokenUtil.getInstance().getToken(BaseApp.getApp());
//        String userId = TokenUtil.getInstance().getPeopleId(BaseApp.getApp());
        headers.put("Content-Type", "application/json;charset=UTF-8");
//        headers.put("deviceId", SystemUtils.getDeviceId(BaseApp.getApp()));

//        if (!TextUtils.isEmpty(token)) {
//            headers.put("token", TokenUtil.getInstance().getToken(BaseApp.getApp()));
//        }
//        if (!TextUtils.isEmpty(userId)) {
//            headers.put("userId", TokenUtil.getInstance().getPeopleId(BaseApp.getApp()));
//        }
        return headers;
    }

    public static Pair<String, File>[] getPairFiles(File... files) {
        if (null == files || files.length <= 0) {
            return null;
        }

        Pair<String, File>[] pairs = new Pair[files.length];

        int index = 0;
        for (File file : files) {
            if (null == file) {
                continue;
            }
            pairs[index] = new Pair<>("file", file);
            index++;
        }
        return pairs;
    }
}