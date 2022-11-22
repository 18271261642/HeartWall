package com.jkcq.hrwtv.service.daemon.service;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;

import com.jkcq.hrwtv.util.Logger;


/**
 * Android 5.0+ 使用的 JobScheduler. 运行在 :watch 子进程中.
 */

@SuppressLint("NewApi")
public class JobSchedulerService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {


        Logger.w("JobSchedulerService", "定时开启： StepService,OutdoorRunningService,IndoorRunningService");

//        jobFinished(params, false);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
