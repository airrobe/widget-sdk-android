package com.airrobe.widgetsdk.airrobedemo.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airrobe.widgetsdk.airrobedemo.R
import com.airrobe.widgetsdk.airrobedemo.adapters.CartItemRVAdapter
import com.airrobe.widgetsdk.airrobedemo.ui.VerticalSpaceItemDecoration
import com.airrobe.widgetsdk.airrobedemo.utils.SharedPreferenceManager
import com.airrobe.widgetsdk.airrobedemo.utils.Utils
import com.airrobe.widgetsdk.airrobewidget.widgets.AirRobeMultiOptIn

class CartActivity : AppCompatActivity() {
    lateinit var multiOptInWidget: AirRobeMultiOptIn

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val categories = arrayListOf<String>()
        for (item in SharedPreferenceManager.getCartItems(this)) {
            categories.add(item.category)
        }

        multiOptInWidget = findViewById(R.id.multi_opt_in_widget)
        multiOptInWidget.initialize(categories)

        val rvCartItems = findViewById<RecyclerView>(R.id.rv_cart_items)
        rvCartItems.layoutManager = LinearLayoutManager(this)
        rvCartItems.addItemDecoration(VerticalSpaceItemDecoration(20))

        val rvCartItemAdapter = CartItemRVAdapter(this, SharedPreferenceManager.getCartItems(this))
        rvCartItems.adapter = rvCartItemAdapter

        val ivBack = findViewById<ImageView>(R.id.iv_back)
        ivBack.setOnTouchListener { view, motionEvent ->
            if (Utils.touchAnimator(this, view, motionEvent)) {
                finish()
            }
            true
        }
    }
}