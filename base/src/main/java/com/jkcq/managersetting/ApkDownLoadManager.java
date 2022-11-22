package com.jkcq.managersetting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import android.util.Log;

import com.jkcq.hrwtv.base.R;
import com.jkcq.managersetting.util.Arith;
import com.jkcq.managersetting.view.UpdateDialog;
import com.liulishuo.okdownload.DownloadListener;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * created by wq on 2019/6/25
 */
public class ApkDownLoadManager {
    DownloadTask task;
    static long fileTotalLength = 0;
    static long increaseLength = 0;
    private String mFileName = "HeartRateWall.apk";

    UpdateDialog updateDialog;
    private Activity mActivity;

    public ApkDownLoadManager(Activity activity) {
        this.mActivity = activity;
    }

    public void startDownLoad(String apkDownloadUrl) {
        updateDialog = new UpdateDialog(mActivity, 1, mActivity.getResources().getString(R.string.downloading));
        updateDialog.setOnDialogClickListener(updateDialogListener);
        updateDialog.show();
        downloadAPK(apkDownloadUrl, mFileName);
    }

    private OnDialogClickListener updateDialogListener = new OnDialogClickListener() {
        @Override
        public void dialogClickType(int type) {
            updateDialog.dismiss();
            downLoadApkCancel();
        }
    };

    public void downLoadApkCancel() {
        if (task != null) {
            task.cancel();
        }
    }

    //ArrayList<DownloadTask> addTask = new ArrayList<>();
   // DownloadTask[] tasks;

   /* public void downLoadSenceCancle() {
        tasks = new DownloadTask[addTask.size()];
        for (int i = 0; i < addTask.size(); i++) {
            tasks[i] = addTask.get(i);
        }
        DownloadTask.cancel(tasks);
    }*/

    /**
     * 下载apk
     *
     * @param url
     * @param fileName
     */
    public void downloadAPK(String url, final String fileName) {

        task = new DownloadTask.Builder(url, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
                .setFilename(fileName)
                // the minimal interval millisecond for callback progress
                .setMinIntervalMillisCallbackProcess(30)
                .setConnectionCount(1)
                // do re-download even if the task has already been completed in the past.
                .setPassIfAlreadyCompleted(false)
                .build();
        task.enqueue(listener);

    }


    DownloadListener listener = new DownloadListener() {
        @Override
        public void taskStart(@NonNull DownloadTask task) {
            Log.e("shao", "-------taskStart----");
            // increaseLength = 0;
        }

        @Override
        public void connectTrialStart(@NonNull DownloadTask task, @NonNull Map<String, List<String>> requestHeaderFields) {
            Log.e("shao", "-------connectTrialStart----");
        }

        @Override
        public void connectTrialEnd(@NonNull DownloadTask task, int responseCode, @NonNull Map<String, List<String>> responseHeaderFields) {
            Log.e("shao", "-------connectTrialEnd----");
        }

        @Override
        public void downloadFromBeginning(@NonNull DownloadTask task, @NonNull BreakpointInfo info, @NonNull ResumeFailedCause cause) {
            Log.e("shao", "-------downloadFromBeginning----" + info.getTotalLength());
            fileTotalLength = info.getTotalLength();
            updateDialog.setTvPackgeSize(Arith.div(fileTotalLength, 1024 * 1024, 2));
        }

        @Override
        public void downloadFromBreakpoint(@NonNull DownloadTask task, @NonNull BreakpointInfo info) {
            Log.e("shao", "-------downloadFromBreakpoint----");
        }

        @Override
        public void connectStart(@NonNull DownloadTask task, int blockIndex, @NonNull Map<String, List<String>> requestHeaderFields) {
            Log.e("shao", "-------connectStart----");
        }

        @Override
        public void connectEnd(@NonNull DownloadTask task, int blockIndex, int responseCode, @NonNull Map<String, List<String>> responseHeaderFields) {
            Log.e("shao", "-------connectEnd----");
        }

        @Override
        public void fetchStart(@NonNull DownloadTask task, int blockIndex, long contentLength) {
            //    increaseLength = 0;
            Log.e("shao", "-------fetchStart----");
        }

        @Override
        public void fetchProgress(@NonNull DownloadTask task, int blockIndex, long increaseBytes) {
            Log.e("shao", "-------fetchProgress----" + increaseBytes + "fileTotalLength-=" + fileTotalLength);
            increaseLength = increaseLength + increaseBytes;
            updateDialog.updateProgress((float) (increaseLength / (double) fileTotalLength));
//                updateDialog.updateProgress((float) Arith.div(increaseLength,fileTotalLength,0));
        }

        @Override
        public void fetchEnd(@NonNull DownloadTask task, int blockIndex, long contentLength) {
            //下载成功了才会走这个方法
            Log.e("shao", "-------fetchEnd----");

           // if (increaseLength == fileTotalLength) {
                clear();
                updateDialog.dismiss();
                // 下载完成后，开启系统安装apk功能！
                File file = new File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        , mFileName);
                Log.e("ApkDownLoadManager", file.getAbsolutePath());
                install(mActivity, file);
//                    Intent intent = new Intent();
//                    intent.setAction("android.intent.action.VIEW");
//                    intent.addCategory("android.intent.category.DEFAULT");
//                    intent.setDataAndType(
//                            Uri.parse("file:" + new File(MyFileUtil.getVideoDir() + "/" + fileName).getAbsolutePath()),
//                            "application/vnd.android.package-archive");
//                    startActivityForResult(intent, 1);
            /*} else {
                clear();
            }*/

        }

        @Override
        public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause) {
            Log.e("shao", "-------taskEnd----");
            //下载完成 取消也会走这里

        }
    };

    public void clear() {
        this.increaseLength = 0;
    }

    /**
     * 通过隐式意图调用系统安装程序安装APK
     */
    public static void install(Context context, File file) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
}
