package com.marktony.fanfouhandpick.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

import com.marktony.fanfouhandpick.R
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    private var content: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        initViews()

        // get intent
        val intent = intent

        Glide.with(this).load(intent.getStringExtra("avatarUrl")).asBitmap().into(avatar)
        tv_author.text = intent.getStringExtra("author")

        content = android.text.Html.fromHtml(intent.getStringExtra("content")).toString()

        tv_content.text = content
        tv_time.text = intent.getStringExtra("time")

        var imgUrl = intent.getStringExtra("imgUrl")

        if (!imgUrl.isNullOrEmpty()){
            imgUrl = imgUrl.replace("ff/m0/0c","ff/n0/0c")
            progress.visibility = View.VISIBLE

            Glide.with(this).load(imgUrl).listener(object : RequestListener<String,GlideDrawable>{
                override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                    progress.visibility = View.GONE

                    val snackbar = Snackbar.make(avatar,R.string.load_failed,Snackbar.LENGTH_SHORT)
                    snackbar.view.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                    val textView: TextView = snackbar.view.findViewById(android.support.design.R.id.snackbar_text) as TextView
                    textView.setTextColor(resources.getColor(R.color.colorAccent))
                    snackbar.show()

                    return false
                }

                override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                    progress.visibility = View.GONE
                    return false
                }

            }).into(fanfou_iv_main)

        } else {
            progress.visibility = View.GONE
        }

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

            val snackbar = Snackbar.make(avatar,R.string.copy_to_clipboard_ok,Snackbar.LENGTH_SHORT)
            snackbar.view.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            val textView: TextView = snackbar.view.findViewById(android.support.design.R.id.snackbar_text) as TextView
            textView.setTextColor(resources.getColor(R.color.colorAccent))
            snackbar.show()

        }
        return true
    }

    fun initViews(){
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

}
