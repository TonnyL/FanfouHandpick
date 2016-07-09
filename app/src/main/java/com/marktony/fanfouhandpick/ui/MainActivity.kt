package com.marktony.fanfouhandpick.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.marktony.fanfouhandpick.R
import com.marktony.fanfouhandpick.adapter.FanfouPostAdapter
import com.marktony.fanfouhandpick.interfaze.OnRecyclerViewOnClickListener
import com.marktony.fanfouhandpick.model.FanfouPost
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    // views
    private var fab: FloatingActionButton? = null
    private var refresh: SwipeRefreshLayout? = null
    private var rvMain: RecyclerView? = null
    private var adapter: FanfouPostAdapter? = null
    private var postsList = ArrayList<FanfouPost>()

    private var TAG: String = "MainActivity"

    private var y: Int = 2015
    private var m: Int = 9
    private var d: Int = 5

    // RequestQueue
    private var queue : RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        queue = Volley.newRequestQueue(this.applicationContext)

        val c: Calendar = Calendar.getInstance()
        y = c.get(Calendar.YEAR)
        m = c.get(Calendar.MONTH)
        if (c.get(Calendar.HOUR_OF_DAY) < 8){
            d = c.get(Calendar.DAY_OF_MONTH) - 1
        } else {
            d = c.get(Calendar.DAY_OF_MONTH)
        }
        loadData(parseDate(y,m,d).toString())

        fab!!.setOnClickListener {

            val c = Calendar.getInstance()

            // month ranges from 0 to 11, not 1 to 12
            val dialog: DatePickerDialog = DatePickerDialog(this@MainActivity, DatePickerDialog.OnDateSetListener {
                datePicker,
                year, month, day ->

                if (!postsList.isEmpty()){
                    postsList.clear()
                    adapter!!.notifyDataSetChanged()
                    loadData(parseDate(year,month,day).toString())
                }

                y = year
                m = month
                d = day

            },y,m,d)

            dialog.datePicker.maxDate = c.timeInMillis

            // similarly, date ranges from 2015-10-5 to now
            // but do not set calendar (2015,10,5)
            c.set(2015,9,5)
            dialog.datePicker.minDate = c.timeInMillis
            dialog.show()
        }

        refresh!!.setOnRefreshListener {

            val c: Calendar = Calendar.getInstance()
            y = c.get(Calendar.YEAR)
            m = c.get(Calendar.MONTH)
            if (c.get(Calendar.HOUR_OF_DAY) < 8){
                d = c.get(Calendar.DAY_OF_MONTH) - 1
            } else {
                d = c.get(Calendar.DAY_OF_MONTH)
            }

            if (postsList.isNotEmpty()){
                postsList.clear()
            }
            loadData(parseDate(y,m,d).toString())
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menuInflater.inflate(R.menu.main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{

        val id: Int = item.itemId
        if (id == R.id.action_about){
            val intent = Intent(this@MainActivity,AboutActivity::class.java)
            startActivity(intent)
        }

        return true
    }

    fun initViews(){
        fab = findViewById(R.id.fab) as FloatingActionButton
        refresh = findViewById(R.id.refresh) as SwipeRefreshLayout
        rvMain = findViewById(R.id.rv_main) as RecyclerView
        rvMain!!.layoutManager = LinearLayoutManager(this)
        // set refresh button's color
        refresh!!.setColorSchemeColors(resources.getColor(R.color.colorAccent))
        // set refresh button's size
        refresh!!.setSize(SwipeRefreshLayout.DEFAULT)
    }

    fun loadData(date: String) {

        // build the request
        val request = JsonObjectRequest(Request.Method.GET,"http://blog.fanfou.com/digest/json/" + date + ".daily.json", Response.Listener<JSONObject> {

            jsonObject ->

            refresh?.post {
                refresh!!.isRefreshing = true
            }

            // get the json array
            val array = jsonObject.getJSONArray("msgs")

            // get the json object through for loop
            for (i in 0..(array.length() - 1)){
                val obj = array.getJSONObject(i)

                // init Fanfou Object
                val item = FanfouPost(obj.getString("avatar"),
                        obj.getString("realname"),
                        obj.getString("msg"),
                        obj.getString("time"),
                        obj.getJSONObject("img").getString("preview"))

                // add the object to list
                postsList.add(item)

            }

            // init the adapter
            adapter = FanfouPostAdapter(this,postsList)
            // attach adapter to recycler view
            rvMain!!.adapter = adapter
            adapter!!.setItemClickListener(object : OnRecyclerViewOnClickListener{
                override fun OnItemClick(v: View, position: Int) {
                    val intent = Intent(this@MainActivity,DetailsActivity::class.java)
                    val item = postsList[position]

                    intent.putExtra("avatarUrl",item.avatarUrl)
                    intent.putExtra("author",item.author)
                    intent.putExtra("content",item.content)
                    intent.putExtra("imgUrl",item.imgUrl)
                    intent.putExtra("time",item.time)

                    startActivity(intent)
                }
            })

            // stop refresh layout
            refresh?.post {
                refresh!!.isRefreshing = false
            }

        }, Response.ErrorListener {

            // show error through snack bar
            val snackbar = Snackbar.make(fab!!,R.string.load_failed,Snackbar.LENGTH_SHORT)
            snackbar.view.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            val textView: TextView = snackbar.view.findViewById(android.support.design.R.id.snackbar_text) as TextView
            textView.setTextColor(resources.getColor(R.color.colorAccent))
            snackbar.show()

            // stop refresh layout
            refresh?.post {
                refresh!!.isRefreshing = false
            }

        })

        // set request's tag
        request.tag = TAG
        // add request to request queue
        queue!!.add(request)

    }

    /**
     * @year year
     * @month month
     * @day date
     * @s string after parsed
     */
    fun parseDate(year: Int,month: Int,day: Int): String?{
        val format: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date: Date = Date(year - 1900,month,day)
        val s = format.format(date)
        return s
    }

    override fun onStop() {
        super.onStop()

        queue!!.cancelAll(TAG)
    }

}
