package com.example.dennismoyseyev.stopwatch
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.FragmentActivity
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
         if(savedInstanceState!=null)
         {
             model.start_time= savedInstanceState.getLong("MYTIME")
             model.time_started= savedInstanceState.getBoolean("TIMESTARTED")
             model.is_Stopped = savedInstanceState.getBoolean("STOPPED")
             if(model.time_started && model.is_Stopped !=true)
             {
                 model.mHandler.postDelayed(update_timer, 0)
             }
         }
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main_screen)
         bind_button()
         list_frag_listview.adapter = laps_adapter
     }

    //If it was resumed before the device was rotated then it will keep the resume button
    //and the current time.
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        if(model.is_Stopped)
        {
            Timer.text = model.show_time()
            start_but.text = getString(R.string.resume)
            model.is_Stopped=false
        }


    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.run {
           putLong("MYTIME", model.start_time)
           putBoolean("TIMESTARTED", model.time_started)
           putBoolean("STOPPED", model.is_Stopped)

        }
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState)
    }

    //Binds the buttons so that they can actually be used by the user.
    private fun bind_button(){
        start_but.setOnClickListener {
            model.start_time = SystemClock.uptimeMillis()-model.save_time
            stop_but.isClickable =true
            start_but.isClickable = false
            model.mHandler.postDelayed(update_timer, 0)
            start_but.text = getString(R.string.start)
            model.time_started=true
        }

        //Displays that the stop button was pressed and it will then change the stop to say resume
        //and when pressed it will continue from where it was paused.
        stop_but.setOnClickListener {
                model.is_Stopped=true
                start_but.text = getString(R.string.resume)
                stop_but.isClickable=false
                start_but.isClickable=true
                model.save_time= SystemClock.uptimeMillis()- model.start_time
                model.mHandler.removeCallbacks(update_timer)
                model.time_started=false
        }

        //Displays the Toast that the reset button was pressed and resets the time back to zero.
        reset_but.setOnClickListener {
            Toast.makeText(this, "The Stopwatch was reset.", Toast.LENGTH_SHORT).show()
            model.mHandler.removeCallbacks(update_timer)
            model.reset_time()
            Timer.text =model.show_time()
            laps_adapter.notifyDataSetChanged() //Tells us adapter that the data set changed.
            start_but.isClickable=true
            stop_but.text = getText(R.string.stop)
            start_but.text = getString(R.string.start)
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

