package com.airrobe.widgetsdk.airrobedemo.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airrobe.widgetsdk.airrobedemo.R
import com.airrobe.widgetsdk.airrobedemo.adapters.BrandsRVAdapter
import com.airrobe.widgetsdk.airrobedemo.configs.Consts
import com.airrobe.widgetsdk.airrobedemo.ui.VerticalSpaceItemDecoration
import com.airrobe.widgetsdk.airrobedemo.utils.SharedPreferenceManager
import com.airrobe.widgetsdk.airrobedemo.utils.StatusBarTranslucent
import com.airrobe.widgetsdk.airrobedemo.utils.Utils
import ru.nikartm.support.ImageBadgeView

class BrandActivity : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
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

        val ivCart = findViewById<ImageBadgeView>(R.id.iv_cart)
        ivCart.setOnTouchListener { view, motionEvent ->
            if (Utils.touchAnimator(this, view, motionEvent)) {
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        val items = SharedPreferenceManager.getCartItems(this)
        val ivCart = findViewById<ImageBadgeView>(R.id.iv_cart)
        ivCart.badgeValue = items.count()
    }
}