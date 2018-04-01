package com.example.dennismoyseyev.stopwatch

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main_screen.*

var model: data_model= data_model()

class MainScreen : FragmentActivity() {

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        start_but.setOnClickListener{
            Toast.makeText(this, "Hello world! The time is "+model.time.seconds, Toast.LENGTH_SHORT).show()
        }
    }
}
