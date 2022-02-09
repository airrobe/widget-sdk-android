package com.airrobe.widgetsdk.airrobedemo.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.airrobe.widgetsdk.airrobedemo.R

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        findViewById<Button>(R.id.btn_opt_in).setOnClickListener {
            val intent = Intent(this, OptInActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.btn_multi_opt_in).setOnClickListener {
            val intent = Intent(this, MultiOptInActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.btn_confirmation).setOnClickListener {
            val intent = Intent(this, ConfirmationActivity::class.java)
            startActivity(intent)
        }
    }
}