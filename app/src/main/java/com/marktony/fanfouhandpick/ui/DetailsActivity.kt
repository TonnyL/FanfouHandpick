package com.marktony.fanfouhandpick.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

import com.marktony.fanfouhandpick.R
import com.marktony.fanfouhandpick.view.CircleImageView

class DetailsActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var avatar: CircleImageView? = null
    private var tvAuthor: TextView? = null
    private var tvContent: TextView? = null
    private var tvTime: TextView? = null
    private var ivMain: ImageView? = null
    private var pbMain: ProgressBar? = null

    private var content: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        initViews()

        // get intent
        val intent = intent

        Glide.with(this).load(intent.getStringExtra("avatarUrl")).asBitmap().into(avatar)
        tvAuthor!!.text = intent.getStringExtra("author")

        content = android.text.Html.fromHtml(intent.getStringExtra("content")).toString()

        tvContent!!.text = content
        tvTime!!.text = intent.getStringExtra("time")

        var imgUrl = intent.getStringExtra("imgUrl")

        if (!imgUrl.isNullOrEmpty()){
            imgUrl = imgUrl.replace("ff/m0/0c","ff/n0/0c")
            pbMain!!.visibility = View.VISIBLE
        } else {
            pbMain!!.visibility = View.GONE
        }

        Glide.with(this).load(imgUrl).listener(object : RequestListener<String,GlideDrawable>{
            override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                pbMain!!.visibility = View.GONE
                return false
            }

            override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                pbMain!!.visibility = View.GONE
                return false
            }

        }).into(ivMain)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean{
        this.menuInflater.inflate(R.menu.menu_read,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{

        val id: Int = item.itemId
        if (id == android.R.id.home){
            onBackPressed()
        }
        if (id == R.id.action_share){
            val intent = Intent().setAction(Intent.ACTION_SEND).setType("text/plain")
            intent.putExtra(Intent.EXTRA_TEXT,content)
            startActivity(Intent.createChooser(intent,getString(R.string.share_to)))
        }
        if (id == R.id.action_copy_to_clipboard){
            val manager: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val data: ClipData = ClipData.newPlainText("text",content)
            manager.primaryClip = data
        }
        return true
    }

    fun initViews(){
        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        avatar = findViewById(R.id.avatar) as CircleImageView
        tvAuthor = findViewById(R.id.tv_author) as TextView
        tvContent = findViewById(R.id.tv_content) as TextView
        tvTime = findViewById(R.id.tv_time) as TextView
        ivMain = findViewById(R.id.fanfou_iv_main) as ImageView
        pbMain = findViewById(R.id.progress) as ProgressBar
    }

}
