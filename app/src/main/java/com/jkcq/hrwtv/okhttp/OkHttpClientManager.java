package com.jkcq.hrwtv.okhttp;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.jkcq.hrwtv.AllocationApi;
import com.jkcq.hrwtv.app.BaseApp;
import com.jkcq.hrwtv.okhttp.callback.ResultCallback;
import com.jkcq.hrwtv.okhttp.tool.ExecutorDelivery;
import com.jkcq.hrwtv.okhttp.tool.ResponseDelivery;
import com.jkcq.hrwtv.util.JsonUtils;
import com.jkcq.hrwtv.util.Logger;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * OkHttpClientManager 管理类
 */
public class OkHttpClientManager {

    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private ResponseDelivery mDelivery;

    private OkHttpClientManager() {
        init();
    }

    private void init() {
        if (null == mOkHttpClient) {
            mOkHttpClient = new OkHttpClient();
            //cookie enabled
            mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
            mOkHttpClient.setConnectTimeout(45, TimeUnit.SECONDS);//连接超时
            mOkHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
            mOkHttpClient.setRetryOnConnectionFailure(true);

            mDelivery = new ExecutorDelivery(new Handler(Looper.getMainLooper()));
        }
    }

    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    public OkHttpClient getOkHttpClient() {
        init();
        return mOkHttpClient;
    }

    public ResponseDelivery getDelivery() {
        return mDelivery;
    }

    public void execute(Request request, ResultCallback callback) {
        if (callback == null) callback = ResultCallback.DEFAULT_RESULT_CALLBACK;
        final ResultCallback resCallBack = callback;
        resCallBack.onStart(request);

        if (!hasNetwork(BaseApp.getApp())) {
            AllocationApi.isNetwork = false;
            resCallBack.onError(request, new NetworkErrorException("网络未连接，请检查网络设置"));
            resCallBack.onEnd();
            return;
        }
        AllocationApi.isNetwork = true;

        getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailResultCallback(request, e, resCallBack);
            }

            @Override
            public void onResponse(final Response response) {
                try {
                    Headers headers = response.headers();
                    sendSuccessHeadersCallback(headers, resCallBack);

                    final String string = response.body().string();

                    if (null == response.networkResponse() || !response.networkResponse().isSuccessful()) {
                        sendFailResultCallback(response.request(), new NullPointerException(null == response.networkResponse() ? "未知异常." : response.networkResponse().message()), resCallBack);
                        Logger.e("返回结果：" + (null == response.networkResponse() ? "-1000 未知异常." : response.networkResponse().toString()));
                        return;
                    }

                    Logger.e("返回结果：" + string);

                    if (resCallBack.getClazz() != null && (resCallBack.getClazz() != BaseBean.class || resCallBack.getClazz().getSuperclass() != BaseBean.class)) {
                        Object o = JsonUtils.getInstance().parseObject(string, resCallBack.getClazz());
                        sendSuccessResultCallback(o, resCallBack);
                        return;
                    }
                    sendSuccessResultCallback(string, resCallBack);
                } catch (IOException e) {
                    sendFailResultCallback(response.request(), e, resCallBack);
                } catch (OutOfMemoryError e) {//内存溢出
                    sendFailResultCallback(response.request(), new Exception(e.getMessage()), resCallBack);
                } catch (Exception e)//Json解析的错误
                {
                    sendFailResultCallback(response.request(), e, resCallBack);
                }
            }
        });
    }

    public <T> T execute(Request request, Class<T> clazz) throws IOException {
        Call call = getOkHttpClient().newCall(request);
        Response execute = call.execute();
        String respStr = execute.body().string();
        return JsonUtils.getInstance().parseObject(respStr, clazz);
    }

    public void sendFailResultCallback(final Request request, final Exception e, final ResultCallback callback) {

        String result = collectExceptionInfo(e);

        Logger.e("请求失败：url = " + (null == request ? "" : request.urlString()) + " ,error = " + (TextUtils.isEmpty(result) ? "" : result));

        if (callback == null) return;

        mDelivery.postResponse(null, null, new Runnable() {
            @Override
            public void run() {
                callback.onError(request, e);
                callback.onEnd();
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        if (callback == null) return;

        mDelivery.postResponse(null, object, new Runnable() {
            @Override
            public void run() {
                //开启拦截器
                if (callback.onResponseIntercept(object)) {
                    return;
                }
                callback.onResponse(object);
                callback.onEnd();
            }
        });
    }

    public void sendSuccessHeadersCallback(final Headers headers, final ResultCallback callback) {
        if (callback == null) return;

        mDelivery.postResponse(null, headers, new Runnable() {
            @Override
            public void run() {
                callback.onHeaders(headers);
            }
        });
    }

    public void cancelTag(Object tag) {
        try {
            mOkHttpClient.cancel(tag);
        } catch (Exception e) {
            Logger.e("cancelTag error. tag = " + tag);
        }
    }

    private String collectExceptionInfo(Exception ex) {
        try {
            Writer writer = new StringWriter();

            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);

            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.close();

            return writer.toString();
        } catch (Exception e) {
            Log.e(OkHttpClientManager.class.getSimpleName(), "an error occured when collect Exception info", e);
        }
        return "";
    }

    public void setCertificates(InputStream... certificates) {
        setCertificates(certificates, null, null);
    }

    private TrustManager[] prepareTrustManager(InputStream... certificates) {
        if (certificates == null || certificates.length <= 0) return null;
        try {

            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                }
            }
            TrustManagerFactory trustManagerFactory = null;

            trustManagerFactory = TrustManagerFactory.
                    getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

            return trustManagers;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private KeyManager[] prepareKeyManager(InputStream bksFile, String password) {
        try {
            if (bksFile == null || password == null) return null;

            KeyStore clientKeyStore = KeyStore.getInstance("BKS");
            clientKeyStore.load(bksFile, password.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, password.toCharArray());
            return keyManagerFactory.getKeyManagers();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setCertificates(InputStream[] certificates, InputStream bksFile, String password) {
        try {
            TrustManager[] trustManagers = prepareTrustManager(certificates);
            KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
            SSLContext sslContext = SSLContext.getInstance("TLS");

            sslContext.init(keyManagers, new TrustManager[]{new MyTrustManager(chooseTrustManager(trustManagers))}, new SecureRandom());
            mOkHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    private X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }

    private class MyTrustManager implements X509TrustManager {
        private X509TrustManager defaultTrustManager;
        private X509TrustManager localTrustManager;

        public MyTrustManager(X509TrustManager localTrustManager) throws NoSuchAlgorithmException, KeyStoreException {
            TrustManagerFactory var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            var4.init((KeyStore) null);
            defaultTrustManager = chooseTrustManager(var4.getTrustManagers());
            this.localTrustManager = localTrustManager;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                defaultTrustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException ce) {
                localTrustManager.checkServerTrusted(chain, authType);
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    /**
     * 获得网络连接是否可用
     *
     * @param context
     * @return
     */
    private boolean hasNetwork(Context context) {
        ConnectivityManager con =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo workInfo = con.getActiveNetworkInfo();
        if (workInfo == null || !workInfo.isAvailable()) {
            return false;
        }
        return true;
    }
}