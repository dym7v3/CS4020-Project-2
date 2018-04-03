package com.example.dennismoyseyev.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.support.v4.app.FragmentActivity
import android.widget.TextView
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main_screen.*
import kotlinx.android.synthetic.main.list_frag.*

//Creates the data model. Used to store the data for the view.
var model: data_model= data_model()

class MainScreen : FragmentActivity() {

    private var mHandler = Handler()
    val laps_adapter= LapsAdapter(model.laps)

    //The function is the one that updates the time with a runnable class.
    private val update_timer = object : Runnable {
        override fun run() {
            model.running_time()
            Timer.text = model.show_time()
            //Continues to do this without delay until stop or reset is pressed.
            mHandler.postDelayed(this, 0)
        }
    }

     //Function is called when the activity is created. It will set the content on the screen
     // and bind the buttons to the correct actions.
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        bind_button()
        list_frag_listview.adapter= laps_adapter
    }

    //Using the example code from:http://javasampleapproach.com/android/kotlin-listview-example-android
    inner class LapsAdapter (private var lapsList: ArrayList<String>) : BaseAdapter() {

        //The size of the laps list.
        override fun getCount(): Int {
            return lapsList.size
        }

        //Gets the id of at the position.
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        //Adds the content form the arraylist to the view.
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

            val mRow: View?
            val vh: ViewHolder

            //Checks if the view is null. If it is then it will save the current row as the
            // convert-view.
            //I used the view holder to be able to get better scrolling.
            if (convertView == null) {
                mRow = layoutInflater.inflate(R.layout.row, parent, false)
                vh = ViewHolder(mRow)
                mRow.tag = vh
            } else {
                mRow= convertView
                vh = mRow.tag as ViewHolder
            }
            vh.lap_num.text = position.toString()
            vh.display_time.text =  lapsList[position]
            return mRow
        }
        //Gets the item at the current position that was give the integer.
        override fun getItem(position: Int): Any {
            return lapsList[position]
        }
    }
    //Makes the View Holder. Which I read makes better for scrolling.
    private class ViewHolder(view: View?) {
        val lap_num: TextView = view?.findViewById(R.id.lap_num) as TextView
        val display_time: TextView = view?.findViewById(R.id.display_time) as TextView
    }

    //Binds the buttons so that they can actually be used by the user.
    private fun bind_button(){
        start_but.setOnClickListener {
            model.start_time=SystemClock.uptimeMillis()
            mHandler.postDelayed(update_timer, 0)
        }

        //Displays that the stop button was pressed and it will then change the stop to say resume
        //and when pressed it will continue from where it was paused.
        stop_but.setOnClickListener {
            Toast.makeText(this, "The Stopwatch was stopped.", Toast.LENGTH_SHORT).show()
            //TODO add the ability to be able to save the time so that you can resume the stopwatch.
            mHandler.removeCallbacks(update_timer)
        }

        //Displays the Toast that the reset button was pressed and resets the time back to zero.
        reset_but.setOnClickListener {
            Toast.makeText(this, "The Stopwatch was reset.", Toast.LENGTH_SHORT).show()
            mHandler.removeCallbacks(update_timer)
            model.reset_time()
            Timer.text =model.show_time()
            model.laps.clear()
            laps_adapter.notifyDataSetChanged()
        }

        lap_but.setOnClickListener{
            //Adds the current time as a string to the arraylist of laps.
            model.laps.add(model.show_time())
            //Tells the adapter the the data set as changed.
            laps_adapter.notifyDataSetChanged()
        }
    }

}

