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

      private val laps_adapter: data_model.LapsAdapter = data_model.LapsAdapter(this, model.laps)

     //Function is called when the activity is created. It will set the content on the screen
     // and bind the buttons to the correct actions.
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        bind_button()
        list_frag_listview.adapter= laps_adapter
    }


    //Binds the buttons so that they can actually be used by the user.
    private fun bind_button(){
        start_but.setOnClickListener {
            model.start_time=SystemClock.uptimeMillis()
            model.mHandler.postDelayed(update_timer, 0)
        }

        //Displays that the stop button was pressed and it will then change the stop to say resume
        //and when pressed it will continue from where it was paused.
        stop_but.setOnClickListener {
            Toast.makeText(this, "The Stopwatch was stopped.", Toast.LENGTH_SHORT).show()
            //TODO add the ability to be able to save the time so that you can resume the stopwatch.
            model.mHandler.removeCallbacks(update_timer)
        }

        //Displays the Toast that the reset button was pressed and resets the time back to zero.
        reset_but.setOnClickListener {
            Toast.makeText(this, "The Stopwatch was reset.", Toast.LENGTH_SHORT).show()
            model.mHandler.removeCallbacks(update_timer)
            model.reset_time()
            Timer.text =model.show_time()
            laps_adapter.notifyDataSetChanged() //Tells us adapter that the data set changed.
        }

        //When the lap button is pressed records the string representation of the time and adds
        //it to the laps array-list and tells the adapter that the data changed.
        lap_but.setOnClickListener{
            //Adds the current time as a string to the array-list of laps.
            model.laps.add(model.show_time())
            //Tells the adapter the the data set as changed.
            laps_adapter.notifyDataSetChanged()
        }
    }

    //The function is the one that updates the time with a runnable class.
    private val update_timer = object : Runnable {
        override fun run() {
            model.running_time()
            Timer.text = model.show_time()
            //Continues to do this without delay until stop or reset is pressed.
            model.mHandler.postDelayed(this, 0)
        }
    }


}

