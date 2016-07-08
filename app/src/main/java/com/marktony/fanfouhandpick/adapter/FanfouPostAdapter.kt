package com.marktony.fanfouhandpick.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.marktony.fanfouhandpick.model.FanfouPost

import com.marktony.fanfouhandpick.R
import com.marktony.fanfouhandpick.interfaze.OnRecyclerViewOnClickListener
import com.marktony.fanfouhandpick.view.CircleImageView

/**
 * Created by lizhaotailang on 2016/7/6.
 */

class FanfouPostAdapter(val context: Context , val list: List<FanfouPost>) : RecyclerView.Adapter<FanfouPostAdapter.FanfouPostsViewHolder>() {

    private val inflater: LayoutInflater

    private var mListener: OnRecyclerViewOnClickListener? = null

    init {
        this.inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FanfouPostsViewHolder? {
        val view: View = inflater.inflate(R.layout.fanfou_item_layout,parent,false)
        return FanfouPostsViewHolder(view, mListener!!)
    }

    override fun onBindViewHolder(holder: FanfouPostsViewHolder, position: Int) {

        val item = list[position]

        Glide.with(context).load(item.avatarUrl).asBitmap().into(holder.civAvatar)

        if (!item.imgUrl.isNullOrEmpty()){
            holder.ivImage.visibility = View.VISIBLE
        } else {
            holder.ivImage.visibility = View.INVISIBLE
        }

        holder.tvAuthor.text = item.author
        holder.tvContent.text = android.text.Html.fromHtml(item.content).toString()
        holder.tvTime.text = item.time
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setItemClickListener(listener: OnRecyclerViewOnClickListener){
        this.mListener = listener
    }

    inner class FanfouPostsViewHolder(itemView: View,listener: OnRecyclerViewOnClickListener) : RecyclerView.ViewHolder(itemView),View.OnClickListener{

        internal var civAvatar: CircleImageView
        internal var tvAuthor: TextView
        internal var ivImage: ImageView
        internal var tvContent: TextView
        internal var tvTime: TextView
        internal var listener: OnRecyclerViewOnClickListener

        init {
            civAvatar = itemView.findViewById(R.id.avatar) as CircleImageView
            tvAuthor = itemView.findViewById(R.id.tv_author) as TextView
            ivImage = itemView.findViewById(R.id.iv_main) as ImageView
            tvContent = itemView.findViewById(R.id.tv_content) as TextView
            tvTime = itemView.findViewById(R.id.tv_time) as TextView
            this.listener = listener
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.OnItemClick(p0!!,layoutPosition)
        }

    }
}
