package com.airrobe.widgetsdk.airrobedemo.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airrobe.widgetsdk.airrobedemo.R
import com.airrobe.widgetsdk.airrobedemo.adapters.SubCategoriesRVAdapter
import com.airrobe.widgetsdk.airrobedemo.configs.Consts
import com.airrobe.widgetsdk.airrobedemo.ui.VerticalSpaceItemDecoration
import com.airrobe.widgetsdk.airrobedemo.utils.StatusBarTranslucent
import com.airrobe.widgetsdk.airrobedemo.utils.Utils

class SubCategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category)
        StatusBarTranslucent.setStatusBarTranslucent(this, false)
        StatusBarTranslucent.setStatusBarColor(this)

        val categoryId = intent.getIntExtra("categoryId", 0)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        tvTitle.text = Consts.categories[categoryId].category
        val tvCategory = findViewById<TextView>(R.id.tv_category)
        tvCategory.text = Consts.categories[categoryId].category
        val ivCategory = findViewById<ImageView>(R.id.iv_category)
        ivCategory.setImageResource(Consts.categories[categoryId].image)
        
        val rvSubCategory = findViewById<RecyclerView>(R.id.rv_sub_categories)
        rvSubCategory.layoutManager = LinearLayoutManager(this)
        rvSubCategory.addItemDecoration(VerticalSpaceItemDecoration(20))

        val rvSubCategoryAdapter = SubCategoriesRVAdapter(this, Consts.subCategories)
        rvSubCategory.adapter = rvSubCategoryAdapter
        val ivBack = findViewById<ImageView>(R.id.iv_back)
        ivBack.setOnTouchListener { view, motionEvent ->
            if (Utils.touchAnimator(this, view, motionEvent)) {
                finish()
            }
            true
        }
    }
}