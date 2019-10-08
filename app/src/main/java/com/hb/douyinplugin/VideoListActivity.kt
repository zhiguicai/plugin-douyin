package com.hb.douyinplugin

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.webkit.ValueCallback
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.alibaba.fastjson.JSON
import com.hb.douyinplugin.adapter.VideoListAdapter
import com.hb.douyinplugin.utils.Utils
import kotlinx.android.synthetic.main.activity_video_list.*
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * createTime: 2019/10/7.15:04
 * updateTime: 2019/10/7.15:04
 * author: singleMan.
 * desc:
 */
class VideoListActivity : AppCompatActivity() {

    lateinit var adapter: VideoListAdapter

    val dataList = arrayListOf<DataEntity.AwemeListBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_list)
        initView()
        initData()
        initEvent()
    }

    private fun initView() {

        recyclerview.layoutManager = LinearLayoutManager(this)

        val webSettings = webview.getSettings()

        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true)
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true)

        webview.loadUrl("file:///android_asset/douyin_sign.html")


        webview.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                val string = et_user_link.text.toString().trim()
                if (TextUtils.isEmpty(string)) return
                analysisInput(string)
            }
        })


        btn_load.setOnClickListener({
            val input = et_user_link.text.toString().trim()
            if (TextUtils.isEmpty(input)) {
                Toast.makeText(this@VideoListActivity, "输入有误", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            webview.reload()

        })


    }

    //解析输入内容
    private fun analysisInput(input: String) {

        val userId = input.substring(1 + input.lastIndexOf("/"))
        webview.evaluateJavascript("javascript:getSign('$userId')", ValueCallback<String> { sign ->
            Toast.makeText(this@VideoListActivity, "sign:$sign", Toast.LENGTH_SHORT).show()
            val signString = sign.substring(1, sign.length - 1)

            //拼装获取列表的url
            val task = AnalyzerTask()
            task.execute(input, userId, signString)

        })
    }

    private fun initData() {
        adapter = VideoListAdapter(dataList)
        recyclerview.adapter = adapter

    }

    private fun initEvent() {


    }


    inner class AnalyzerTask : AsyncTask<String, String, String>() {

        override fun doInBackground(vararg params: String?): String {
            val userLink = params[0] as String
            val uid = params[1] as String
            val sign = params[2] as String

            val url = Utils.stripUrl(userLink)
            val html = OkHttpClient().newCall(Request.Builder().url(url).build()).execute().body()!!.string()

            val dytk = Utils.parseDytk(html)

            val format = "https://www.iesdouyin.com/web/api/v2/aweme/post/?user_id=%s&sec_uid=&count=21&max_cursor=0&aid=1128&_signature=%s&dytk=%s"

            return String.format(format, uid, sign, dytk)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Log.e("hb_", "拼接完成的请求地址：\n" + result!!)

            loadVideoList(result)

        }

    }


    //利用okhttp 请求数据
    fun loadVideoList(url: String) {
        Log.d("hb_", "开始http请求获取数据>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .build()
        val request = Request.Builder()
                .url(url)
                .build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                Log.d("hb_", "请求失败了：" + e.message)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                Log.d("hb_", "已经返回数据>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
                val string = response.body()!!.string()
                Log.d("hb_", string)
                try {
                    val dEntity = JSON.parseObject<DataEntity>(string, DataEntity::class.java)
                    val aweme_list = dEntity.getAweme_list()
                    if (aweme_list.isEmpty()) {
                        //如果是空的，则说明加载失败，需要重试
                        loadVideoList(url)
                        return
                    }
                    //TODO 加载成功，打印结果
                    Log.d("hb_", "视频列表：" + JSON.toJSONString(aweme_list))

                    runOnUiThread({
                        dataList.clear()
                        dataList.addAll(aweme_list)
                        adapter.notifyDataSetChanged()
                    })

                } catch (e: Exception) {
                    Log.d("hb_", "http请求获取视频列表失败了:" + Log.getStackTraceString(e))
                    e.printStackTrace()
                    loadVideoList(url)
                }

            }

        })
    }
}