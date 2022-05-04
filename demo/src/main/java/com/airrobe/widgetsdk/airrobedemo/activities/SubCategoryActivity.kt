package com.airrobe.widgetsdk.airrobedemo.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airrobe.widgetsdk.airrobedemo.R
import com.airrobe.widgetsdk.airrobedemo.adapters.CategoriesRVAdapter
import com.airrobe.widgetsdk.airrobedemo.configs.Consts
import com.airrobe.widgetsdk.airrobedemo.ui.VerticalSpaceItemDecoration
import com.airrobe.widgetsdk.airrobedemo.utils.StatusBarTranslucent

class SubCategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        StatusBarTranslucent.setStatusBarTranslucent(this, false)
        StatusBarTranslucent.setStatusBarColor(this)

        val brandId = intent.getIntExtra("brandId", 0)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        tvTitle.text = Consts.brands[brandId].brand
        
        val rvCategory = findViewById<RecyclerView>(R.id.rv_categories)
        rvCategory.layoutManager = LinearLayoutManager(this)
        rvCategory.addItemDecoration(VerticalSpaceItemDecoration(20))

        val rvCategoryAdapter = CategoriesRVAdapter(this, Consts.categories)
        rvCategory.adapter = rvCategoryAdapter
    }
}