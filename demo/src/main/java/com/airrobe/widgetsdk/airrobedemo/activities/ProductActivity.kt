package com.airrobe.widgetsdk.airrobedemo.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.airrobe.widgetsdk.airrobedemo.R
import com.airrobe.widgetsdk.airrobedemo.configs.Consts
import com.airrobe.widgetsdk.airrobedemo.utils.SharedPreferenceManager
import com.airrobe.widgetsdk.airrobedemo.utils.StatusBarTranslucent
import com.airrobe.widgetsdk.airrobedemo.utils.Utils
import com.airrobe.widgetsdk.airrobewidget.widgets.AirRobeOptIn
import ru.nikartm.support.ImageBadgeView

class ProductActivity : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        StatusBarTranslucent.setStatusBarTranslucent(this, false)
        StatusBarTranslucent.setStatusBarColor(this)

        val itemId = intent.getIntExtra("itemId", 0)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        tvTitle.text = Consts.products[itemId].title

        val ivProduct = findViewById<ImageView>(R.id.iv_product)
        ivProduct.setImageResource(Consts.products[itemId].image)
        val tvProductCategory = findViewById<TextView>(R.id.tv_product_category)
        tvProductCategory.text = Consts.products[itemId].category
        val tvProductTitle = findViewById<TextView>(R.id.tv_product_title)
        tvProductTitle.text = Consts.products[itemId].title
        val tvProductSubTitle = findViewById<TextView>(R.id.tv_product_sub_title)
        tvProductSubTitle.text = Consts.products[itemId].subTitle
        val tvProductPrice = findViewById<TextView>(R.id.tv_product_price)
        tvProductPrice.text = Consts.products[itemId].price

        val optInWidget = findViewById<AirRobeOptIn>(R.id.opt_in_widget)
        optInWidget.initialize(
            brand = "Gucci",
            material = "Leather",
            category = Consts.products[itemId].category,
            department = "Womenswear",
            priceCents = Consts.products[itemId].price.replace("$", "").toFloat()
        )

        val ivCart = findViewById<ImageBadgeView>(R.id.iv_cart)
        val btnAddCart = findViewById<Button>(R.id.btn_add_cart)
        btnAddCart.setOnClickListener {
            val items = SharedPreferenceManager.getCartItems(this)
            items.add(Consts.products[itemId])
            SharedPreferenceManager.setCartItems(this, items)
            ivCart.badgeValue = items.count()
        }
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