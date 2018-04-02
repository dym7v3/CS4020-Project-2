package com.example.dennismoyseyev.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.support.v4.app.FragmentActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main_screen.*

var model: data_model= data_model()

class MainScreen : FragmentActivity() {

    private var mHandler = Handler()

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
            //TODO add the ability to be able to say the time so that you can resume the stopwatch.
            mHandler.removeCallbacks(update_timer)
        }

        //Displays the Toast that the reset button was pressed and resets the time back to zero.
        reset_but.setOnClickListener {
            Toast.makeText(this, "The Stopwatch was reset.", Toast.LENGTH_SHORT).show()
            mHandler.removeCallbacks(update_timer)
            model.reset_time()
            Timer.text =model.show_time()

            //TODO need to remove the things from the vector.

        }
    }

}

