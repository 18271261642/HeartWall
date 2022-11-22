package com.jkcq.hrwtv.okhttp.request;

import android.util.Pair;

import com.jkcq.hrwtv.util.Logger;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;

public class OkHttpUploadRequest extends OkHttpPostRequest {

    private Pair<String, File>[] files;

    protected OkHttpUploadRequest(String url, String tag, Map<String, String> params, Map<String, String> headers, Pair<String, File>[] files) {
        super(url, tag, params, headers, null, null, null, null);
        this.files = files;
    }

    private void addParams(MultipartBuilder builder, Map<String, String> params) {
        if (builder == null) {
            throw new IllegalArgumentException("builder can not be null .");
        }

        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, params.get(key)));
            }
        }
    }

    @Override
    public RequestBody buildRequestBody() {
        MultipartBuilder builder = new MultipartBuilder()
                .type(MultipartBuilder.FORM);
        if (files != null) {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++) {
                Pair<String, File> filePair = files[i];
                String fileKeyName = filePair.first;
                File file = filePair.second;
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + fileKeyName + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }

        RequestBody requestBody;
        switch (type) {
            case TYPE_PARAMS:
                addParams(builder, params);
                if (params != null) {
                    Logger.e("访问url" + String.valueOf(url + params.toString().trim().replace(", ", "&").replace("{", "?").replace("}", "")));
                }
                break;
            case TYPE_STRING:
                requestBody = RequestBody.create(MEDIA_TYPE_STRING, content);
                builder.addPart(requestBody);
                Logger.e("访问url" + String.valueOf(url + "&&" + content));
                break;
            case TYPE_JSON:
                requestBody = RequestBody.create(MEDIA_TYPE_JSON, jsonContent);
                builder.addPart(requestBody);
                Logger.e("访问url" + String.valueOf(url + "&&" + jsonContent));
                break;
        }

        return builder.build();
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}