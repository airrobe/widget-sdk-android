package com.airrobe.widgetsdk.airrobedemo.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airrobe.widgetsdk.airrobedemo.R
import com.airrobe.widgetsdk.airrobedemo.adapters.BrandsRVAdapter
import com.airrobe.widgetsdk.airrobedemo.configs.Consts
import com.airrobe.widgetsdk.airrobedemo.ui.VerticalSpaceItemDecoration
import com.airrobe.widgetsdk.airrobedemo.utils.StatusBarTranslucent

class BrandActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brand)
        StatusBarTranslucent.setStatusBarTranslucent(this, false)
        StatusBarTranslucent.setStatusBarColor(this)

        val rvBrand = findViewById<RecyclerView>(R.id.rv_brands)
        rvBrand.layoutManager = LinearLayoutManager(this)
        rvBrand.addItemDecoration(VerticalSpaceItemDecoration(20))

        val rvBrandAdapter = BrandsRVAdapter(this, Consts.brands)
        rvBrand.adapter = rvBrandAdapter

        val btnLogo = findViewById<Button>(R.id.btn_logo_difference)
        btnLogo.setOnClickListener {
            val intent = Intent(this, LogosActivity::class.java)
            startActivity(intent)
        }
    }
}