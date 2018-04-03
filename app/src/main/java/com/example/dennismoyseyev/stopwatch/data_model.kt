package com.example.dennismoyseyev.stopwatch

import android.os.SystemClock
import java.util.*
import kotlin.collections.ArrayList


class data_model()
{
   var time : time_class= time_class()
   var laps: ArrayList<String> = ArrayList()
   var start_time : Long = 0
   private var new_total_milliseconds_plus_additional_time: Long=0
   var total_milliseconds: Long =0


    internal fun running_time()
    {
        time.milliseconds = SystemClock.uptimeMillis() - start_time
        new_total_milliseconds_plus_additional_time = total_milliseconds +  time.milliseconds
        time.seconds =(new_total_milliseconds_plus_additional_time / 1000)
        time.minutes = time.seconds / 60
        time.seconds = time.seconds % 60
        time.milliseconds = (new_total_milliseconds_plus_additional_time % 1000)
    }

    internal fun reset_time()
    {
        time.milliseconds=0
        time.seconds=0
        time.minutes=0
        new_total_milliseconds_plus_additional_time=0
        total_milliseconds=0
        start_time=0

    }

    //returns the time in a string. To be displayed as the text for the timer.
    internal fun show_time(): String
    {
        return String.format("%2d:%2d:%3d", model.time.minutes, model.time.seconds, model.time.milliseconds)
    }

}