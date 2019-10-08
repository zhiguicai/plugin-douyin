package com.hb.douyinplugin.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.hb.douyinplugin.DataEntity
import com.hb.douyinplugin.R

/**
 * createTime: 2019/10/7.16:19
 * updateTime: 2019/10/7.16:19
 * author: singleMan.
 * desc:
 */
class VideoListAdapter constructor(val dataList: List<DataEntity.AwemeListBean>) : RecyclerView.Adapter<VideoListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_video, null)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return if (dataList == null) 0 else dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(holder.imageView.context).load(dataList.get(position).video.origin_cover.url_list[0]).into(holder.imageView)
        holder.tv_title.setText(dataList.get(position).desc)
        holder.tv_vid.setText( dataList.get(position).aweme_id)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView = itemView.findViewById<ImageView>(R.id.imageview)
        val tv_title = itemView.findViewById<TextView>(R.id.tv_title)
        val tv_vid = itemView.findViewById<TextView>(R.id.tv_vid)

    }
}