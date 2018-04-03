package com.example.dennismoyseyev.stopwatch

import android.content.Context
import android.os.Handler
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlin.collections.ArrayList


class data_model()
{
   private var time : time_class= time_class()
   var mHandler = Handler()
   var laps: ArrayList<String> = ArrayList()
   var start_time : Long = 0
   private var new_total_milliseconds_plus_additional_time: Long=0
   private var total_milliseconds: Long =0
   var is_Stopped: Boolean=false
   var save_time: Long = 0


    internal fun time_stopped()
    {
        save_time=start_time
    }

    //Using the example code from:http://javasampleapproach.com/android/kotlin-listview-example-android
    internal class LapsAdapter (context: Context, private var lapsList: ArrayList<String>) : BaseAdapter() {

        var mContext= context
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
                val layoutInflater = LayoutInflater.from(mContext)
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

    internal fun running_time()
    {
        time.milliseconds = SystemClock.elapsedRealtime() - start_time
        new_total_milliseconds_plus_additional_time = total_milliseconds +  time.milliseconds
        time.seconds =(new_total_milliseconds_plus_additional_time / 1000)
        time.minutes = time.seconds / 60
        time.seconds = time.seconds % 60
        time.milliseconds = (new_total_milliseconds_plus_additional_time % 1000)
    }

    //Resets the time. Makes everything go back to zero this is including the laps.
    internal fun reset_time()
    {
        time.milliseconds=0
        time.seconds=0
        time.minutes=0
        new_total_milliseconds_plus_additional_time=0
        total_milliseconds=0
        start_time=0
        laps.clear() //Removes all the records of the laps.
        save_time=0
    }

    //returns the time in a string. To be displayed as the text for the timer.
    internal fun show_time(): String
    {
        val minutes_str: String = time.minutes.toString()
        val seconds_str: String = time.seconds.toString()
        val milliseconds_str: String = time.milliseconds.toString()
        var mTimeString = "" //String that shows the time.

        mTimeString += if(minutes_str.length<2)
        {
            ("0$minutes_str:")
        }
        else {
            "$minutes_str:"
        }

        //Makes sure that the zero is there if the length of the seconds is not 2 digits.
        mTimeString += if(seconds_str.length<2)
        {
            ("0$seconds_str:")
        }
        else
            "$seconds_str:"

        //Makes sure to have the zeros in the right place for the millisecond part of the time.
        mTimeString += when {
            milliseconds_str.length==1 -> "00$milliseconds_str"
            milliseconds_str.length==2 -> "0$milliseconds_str"
            else -> milliseconds_str
        }

        //Returns the time with the nice format.
        return mTimeString
    }

}