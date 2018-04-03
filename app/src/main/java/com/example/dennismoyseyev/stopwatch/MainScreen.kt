package com.example.dennismoyseyev.stopwatch

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main_screen.*

//Creates the data model. Used to store the data for the view.
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
        val list_view = findViewById<ListView>(R.id.list_frag_listview)
        list_view.adapter= custom_adapter(this)
        bind_button()

    }

    private class custom_adapter(context : Context) : BaseAdapter()
    {
        var mContext=context

        override fun getItem(p0: Int): Any {
           return "Something random."
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return 5
        }

        override fun getView(p0: Int, p1: View?, parent: ViewGroup?): View {
            val inflacter= LayoutInflater.from(mContext)
            val mRow= inflacter.inflate(R.layout.row, parent , false)
            return mRow

        }
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
            model.v.removeAllElements()
        }

        lap_but.setOnClickListener{
            model.v.add(model.time)
            //Toast.makeText(this, String.format("The lap was at %2d:%2d:%3d", model.v[0].minutes, model.v[0].seconds, model.v[0].milliseconds), Toast.LENGTH_SHORT).show()
        }
    }

}

