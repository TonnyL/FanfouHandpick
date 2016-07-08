package com.marktony.fanfouhandpick.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import com.marktony.fanfouhandpick.R

class AboutActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var layoutRate: LinearLayout? = null
    private var layoutFeedback: LinearLayout? = null
    private var layoutDonate: LinearLayout? = null
    private var tvGitHub: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        initViews()

        layoutRate!!.setOnClickListener {

            try {
                val uri = Uri.parse("market://details?id=" + packageName)
                val intent = Intent(Intent.ACTION_VIEW,uri)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } catch (ex: android.content.ActivityNotFoundException){
                Snackbar.make(layoutRate!!,R.string.no_app_market,Snackbar.LENGTH_SHORT).show()
            }

        }

        layoutFeedback!!.setOnClickListener {
            try {
                val uri = Uri.parse(getString(R.string.send_to))
                val intent = Intent(Intent.ACTION_SENDTO,uri)
                intent.putExtra(Intent.EXTRA_SUBJECT,R.string.mail_subject)
                intent.putExtra(Intent.EXTRA_TEXT,getString(R.string.sdk_version) + Build.VERSION.RELEASE + "\n")
                startActivity(intent)
            }catch (ex: android.content.ActivityNotFoundException){
                Snackbar.make(layoutRate!!,R.string.no_mail_app,Snackbar.LENGTH_SHORT).show()
            }
        }

        layoutDonate!!.setOnClickListener {
            val dialog = AlertDialog.Builder(this@AboutActivity).create()
            dialog.setTitle(R.string.donate)
            dialog.setMessage(getString(R.string.donate_message))
            dialog.setButton(AlertDialog.BUTTON_NEGATIVE,"CANCEL",
                    { dialogInterface, i ->

                    })
            dialog.setButton(AlertDialog.BUTTON_POSITIVE,"OK",
                    {dialogInterface,i ->
                        val manager: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val data: ClipData = ClipData.newPlainText("text",getString(R.string.ali_pay_account))
                        manager.primaryClip = data
                    })
            dialog.show()
        }

        tvGitHub!!.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.github_url))))
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        val id = item.itemId
        if (id == android.R.id.home){
            onBackPressed()
        }
        return true
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        layoutRate = findViewById(R.id.LL_rate) as LinearLayout
        layoutFeedback = findViewById(R.id.LL_feedback) as LinearLayout
        layoutDonate = findViewById(R.id.LL_donate) as LinearLayout
        tvGitHub = findViewById(R.id.tv_GitHub) as TextView
    }
}
