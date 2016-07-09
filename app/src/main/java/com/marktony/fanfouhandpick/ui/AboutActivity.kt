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
import android.view.MenuItem
import com.marktony.fanfouhandpick.R
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        initViews()

        layout_rate.setOnClickListener {

            try {
                val uri = Uri.parse("market://details?id=" + packageName)
                val intent = Intent(Intent.ACTION_VIEW,uri)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } catch (ex: android.content.ActivityNotFoundException){
                Snackbar.make(layout_rate,R.string.no_app_market,Snackbar.LENGTH_SHORT).show()
            }

        }

        layout_feedback.setOnClickListener {
            try {
                val uri = Uri.parse(getString(R.string.send_to))
                val intent = Intent(Intent.ACTION_SENDTO,uri)
                intent.putExtra(Intent.EXTRA_SUBJECT,R.string.mail_subject)
                intent.putExtra(Intent.EXTRA_TEXT,getString(R.string.sdk_version) + Build.VERSION.RELEASE + "\n")
                startActivity(intent)
            }catch (ex: android.content.ActivityNotFoundException){
                Snackbar.make(layout_feedback,R.string.no_mail_app,Snackbar.LENGTH_SHORT).show()
            }
        }

        layout_donate.setOnClickListener {
            val dialog = AlertDialog.Builder(this@AboutActivity).create()
            dialog.setTitle(R.string.donate)
            dialog.setMessage(getString(R.string.donate_message))
            dialog.setButton(AlertDialog.BUTTON_NEGATIVE,getString(R.string.negative),
                    { dialogInterface, i ->

                    })
            dialog.setButton(AlertDialog.BUTTON_POSITIVE,getString(R.string.positive),
                    {dialogInterface,i ->
                        val manager: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val data: ClipData = ClipData.newPlainText("text",getString(R.string.ali_pay_account))
                        manager.primaryClip = data
                    })
            dialog.show()
        }

        tv_gitHub.setOnClickListener {
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
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}
