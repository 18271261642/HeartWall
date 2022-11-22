package com.jkcq.hrwtv.test

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.jkcq.hrwtv.R
import com.jkcq.hrwtv.http.RetrofitHelper
import com.jkcq.hrwtv.http.bean.BaseResponse
import com.jkcq.hrwtv.http.bean.CourseInfo
import com.jkcq.hrwtv.http.widget.BaseObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import kotlin.Comparator

class TestOne : AppCompatActivity() {

    private  val tgs = "TestOne"

    private var testBtn1 : Button ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_one_layout)


        initViews();
        initData()
    }


    private fun initData(){
        val list = mutableListOf<Bean>()

        list.add(Bean(12.5f, 18f, 90f))
        list.add(Bean(12.5f, 61.734f, 168f))
        list.add(Bean(0.217f, 12.321892f, 74f))
        list.add(Bean(0f, 14.283699f, 84f))


        Timber.e("--11----list="+Gson().toJson(list))


        Collections.sort(list,object : Comparator<Bean>{
            override fun compare(p0: Bean?, p1: Bean?): Int {
                if (p1 != null) {
                    if (p0 != null) {
                        return p1.point.toString().compareTo(p0.point.toString())
                    }
                }
                return 0
            }

        })


        Timber.e("--22---list="+Gson().toJson(list))
    }


    data class Bean(var point: Float, val cal: Float, var hr: Float){


    }

    private fun initViews(){

        testBtn1 = findViewById(R.id.testBtn1)


        testBtn1?.setOnClickListener {
            getHeartRateClass()
        }
    }


    fun getHeartRateClass() {
        RetrofitHelper.service.getCourseList(
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseObserver<BaseResponse<List<CourseInfo>>>() {
                override fun onSuccess(baseResponse: BaseResponse<List<CourseInfo>>) {
                    baseResponse.data?.let {

                        Log.e(tgs, "----="+Gson().toJson(baseResponse))
                    }
                }
            })


    }

}