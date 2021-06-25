package com.udacity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var status: String
    private lateinit var fileName: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        intent.extras?.let {
            status = it.getString("status").toString()
            fileName = it.getString("filename").toString()
        }

        filename_text.text = fileName
        status_text.text = status

        if(status.equals("success", true))
            status_text.setTextColor(Color.GREEN) else status_text.setTextColor(Color.RED)
    }

}
