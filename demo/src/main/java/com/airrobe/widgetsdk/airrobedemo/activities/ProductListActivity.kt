package com.airrobe.widgetsdk.airrobedemo.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import com.airrobe.widgetsdk.airrobedemo.R
import com.airrobe.widgetsdk.airrobedemo.adapters.ProductListGVAdapter
import com.airrobe.widgetsdk.airrobedemo.configs.Consts
import com.airrobe.widgetsdk.airrobedemo.utils.SharedPreferenceManager
import com.airrobe.widgetsdk.airrobedemo.utils.StatusBarTranslucent
import com.airrobe.widgetsdk.airrobedemo.utils.Utils
import ru.nikartm.support.ImageBadgeView

class ProductListActivity : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        StatusBarTranslucent.setStatusBarTranslucent(this, false)
        StatusBarTranslucent.setStatusBarColor(this)

        val subCategoryId = intent.getIntExtra("subCategoryId", 0)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        tvTitle.text = Consts.subCategories[subCategoryId]

        val gvProduct = findViewById<GridView>(R.id.gv_product)
        val productListGVAdapter = ProductListGVAdapter(this, Consts.products)
        gvProduct.adapter = productListGVAdapter
        gvProduct.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, ProductActivity::class.java)
            intent.putExtra("itemId", position)
            startActivity(intent)
        }

        val ivCart = findViewById<ImageBadgeView>(R.id.iv_cart)
        ivCart.setOnTouchListener { view, motionEvent ->
            if (Utils.touchAnimator(this, view, motionEvent)) {
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
            }
            true
        }

        val ivBack = findViewById<ImageView>(R.id.iv_back)
        ivBack.setOnTouchListener { view, motionEvent ->
            if (Utils.touchAnimator(this, view, motionEvent)) {
                finish()
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