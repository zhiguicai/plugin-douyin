package com.hb.douyinplugin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.RadioGroup
import android.widget.Toast
import com.hb.douyinplugin.eventbus.DefaultEventMessage
import com.hb.douyinplugin.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initData()
        initEvent()
    }

    override fun onResume() {
        super.onResume()
        if (Utils.isLiving(this, "IPCService")) {
            tv_state.text = "已连接"
        } else {
            tv_state.text = "未连接"
        }
    }

    private fun initView() {
        //初始化
        radiogroup.check(R.id.dofeed)
        et_json.setText("{\n" +
                "    \"videoId\":\"6693124310501969166\"\n" +
                "}")
    }

    private fun initEvent() {
        btn_videolist.setOnClickListener({
            startActivity(Intent(this@MainActivity, VideoListActivity::class.java))
        })


        radiogroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                when (checkedId) {
                    R.id.dofeed -> {
                        et_json.setText("{\n" +
                                "    \"videoId\":\"6693124310501969166\"\n" +
                                "}")
                    }
                    R.id.docomment -> {
                        et_json.setText("{\n" +
                                "    \"videoId\":\"6693124310501969166\",\n" +
                                "    \"commentText\":\"这个视频真赞呦!\"\n" +
                                "}")
                    }
                    R.id.rb_img -> {
                        et_json.setText("{\n" +
                                "    \"toUID\":\"3768748035023923\",\n" +
                                "    \"imagePath\":\"/storage/emulated/0/FreeVideo/FreeVideo_share_image.jpg\"\n" +
                                "}")
                    }
                    R.id.rb_video -> {
                        et_json.setText("{\n" +
                                "    \"toUID\":\"3768748035023923\",\n" +
                                "    \"formUID\":\"1147517703031011\",\n" +
                                "    \"videoID\":\"6693124310501969166\",\n" +
                                "    \"title\":\"分享视频\",\n" +
                                "    \"coverUri\":\"17b4b00084db599b06eaa\",\n" +
                                "    \"coverUrl\":\"https://p3-dy.byteimg.com/aweme/300x400/17b4b00084db599b06eaa.jpeg\"\n" +
                                "}")
                    }
                    R.id.rb_good -> {
                        et_json.setText("{\n" +
                                "    \"toUID\":\"3768748035023923\",\n" +
                                "    \"formUID\":\"2343801518304232\",\n" +
                                "    \"avatarUri\":\"temai/Fsn1xyep1a-udZWf2jSIh5btec2swww800-800\",\n" +
                                "    \"avatarUrl\":\"http://p1.pstatp.com/aweme/720x720/temai/Fsn1xyep1a-udZWf2jSIh5btec2swww800-800.jpeg\",\n" +
                                "    \"title\":\"旅行洗漱杯多功能刷牙杯子牙具盒漱口杯套装牙刷收纳盒便携式\",\n" +
                                "    \"promotionId\":\"3356738809466489345\",\n" +
                                "    \"productId\":\"3356713428592002459\",\n" +
                                "    \"userCount\":554352\n" +
                                "}")
                    }
                    R.id.rb_card -> {
                        et_json.setText("{\n" +
                                "    \"toUID\":\"3768748035023923\",\n" +
                                "    \"formUID\":\"1147517703031011\",\n" +
                                "    \"avatarUri\":\"93dd00183553d67c79ca\",\n" +
                                "    \"avatarUrl\":\"https://p3-dy.byteimg.com/aweme/720x720/93dd00183553d67c79ca.jpeg\",\n" +
                                "    \"name\":\"杭州之声\",\n" +
                                "    \"desc\":\"fm898989\"\n" +
                                "}")
                    }
                    R.id.rb_mobile -> {
                        Toast.makeText(this@MainActivity, "请填入正确的手机号后再试", Toast.LENGTH_SHORT).show()
                        et_json.setText("{\n" +
                                "    \"mobiles\":[\n" +
                                "        {\n" +
                                "            \"name\":\"测试1\",\n" +
                                "            \"phoneNumber\":[\n" +
                                "                \"17600110011\"\n" +
                                "            ]\n" +
                                "        },\n" +
                                "        {\n" +
                                "            \"name\":\"测试2\",\n" +
                                "            \"phoneNumber\":[\n" +
                                "                \"17600110011\"\n" +
                                "            ]\n" +
                                "        }\n" +
                                "    ]\n" +
                                "}")
                    }
                }
            }

        })

        btn_submit.setOnClickListener({
            val checkedRadioButtonId = radiogroup.checkedRadioButtonId
            var type = ""
            when (checkedRadioButtonId) {
                R.id.dofeed -> {
                    type = "doFeed"
                }
                R.id.docomment -> {
                    type = "doComment"
                }
                R.id.rb_img -> {
                    type = "img"
                }
                R.id.rb_video -> {
                    type = "video"
                }
                R.id.rb_good -> {
                    type = "good"
                }
                R.id.rb_card -> {
                    type = "card"
                }
                R.id.rb_mobile -> {
                    type = "transform-mobile"
                }
            }

            if (TextUtils.isEmpty(type)) {
                Toast.makeText(this@MainActivity, "请选择消息类型", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val inputJson = et_json.text.toString().trim()
            try {
                val input = JSONObject(inputJson)
                input.put("action", type)
                val eventMessage = DefaultEventMessage(input.toString())
                EventBus.getDefault().post(eventMessage)
                Toast.makeText(this@MainActivity, "提交完成", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "输入格式不正确", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun initData() {
    }

}
