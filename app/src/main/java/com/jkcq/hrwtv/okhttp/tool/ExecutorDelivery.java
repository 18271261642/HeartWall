/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jkcq.hrwtv.okhttp.tool;

import android.os.Handler;


import com.jkcq.hrwtv.okhttp.request.OkHttpRequest;

import java.util.concurrent.Executor;

/**
 * Delivers responses and errors.
 */
public class ExecutorDelivery implements ResponseDelivery {
    /**
     * Used for posting responses, typically to the main thread.
     */
    private final Executor mResponsePoster;

    /**
     * Creates a new response delivery interface.
     *
     * @param handler {@link Handler} to post responses on
     */
    public ExecutorDelivery(final Handler handler) {
        // Make an Executor that just wraps the handler.
        mResponsePoster = new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
    }

    /**
     * Creates a new response delivery interface, mockable version
     * for testing.
     *
     * @param executor For running delivery tasks
     */
    public ExecutorDelivery(Executor executor) {
        mResponsePoster = executor;
    }

    @Override
    public <T> void postResponse(OkHttpRequest request, T response) {
        mResponsePoster.execute(new ResponseDeliveryRunnable(request, response, null));
    }

    @Override
    public <T> void postResponse(OkHttpRequest request, T response, Runnable runnable) {
        mResponsePoster.execute(new ResponseDeliveryRunnable(request, response, runnable));
    }

    @Override
    public void postError(OkHttpRequest request, OkHttpError error) {
        mResponsePoster.execute(new ResponseDeliveryRunnable(request, null, null));
    }

    @Override
    public void post(Runnable runnable) {
        mResponsePoster.execute(new ResponseDeliveryRunnable(null, null, runnable));
    }

    /**
     * A Runnable used for delivering network responses to a progressListener on the
     * main thread.
     */
    @SuppressWarnings("rawtypes")
    private class ResponseDeliveryRunnable<T> implements Runnable {
        private final OkHttpRequest mRequest;
        private final T mResponse;
        private final Runnable mRunnable;

        public ResponseDeliveryRunnable(OkHttpRequest request, T response, Runnable runnable) {
            mRequest = request;
            mResponse = response;
            mRunnable = runnable;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            // If this request has canceled, finish it and don't deliver.
//            if (mRequest.isCanceled()) {
//                mRequest.finish("canceled-at-delivery");
//                return;
//            }

            // Deliver a normal response or error, depending.
//            if (null == mResponse) {
//                mRequest.deliverError(new OkHttpError(-1000, "mResponse is null!"));
//            } else {
//                mRequest.deliverResponse(mResponse);
//            }

            // If we have been provided a post-delivery runnable, run it.
            if (mRunnable != null) {
                mRunnable.run();
            }
        }
    }
}
