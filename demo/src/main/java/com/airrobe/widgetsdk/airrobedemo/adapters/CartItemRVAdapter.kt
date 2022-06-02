package com.airrobe.widgetsdk.airrobedemo.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airrobe.widgetsdk.airrobedemo.R
import com.airrobe.widgetsdk.airrobedemo.activities.CartActivity
import com.airrobe.widgetsdk.airrobedemo.models.ItemModel
import com.airrobe.widgetsdk.airrobedemo.utils.SharedPreferenceManager
import com.airrobe.widgetsdk.airrobedemo.utils.Utils

class CartItemRVAdapter(
    private val activity: Activity,
    private val cartItems: ArrayList<ItemModel>
) : RecyclerView.Adapter<CartItemRVAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(activity)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.recyclerview_cart_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.tvTitle.text = cartItem.title
        holder.tvSubTitle.text = cartItem.subTitle
        holder.tvPrice.text = cartItem.price
        holder.ivProduct.setImageResource(cartItem.image)
        holder.llRemove.setOnTouchListener { view, motionEvent ->
            if (Utils.touchAnimator(activity, view, motionEvent)) {
                cartItems.remove(cartItems[position])
                SharedPreferenceManager.setCartItems(activity, cartItems)

                val cartActivity = activity as CartActivity
                val categories = arrayListOf<String>()
                for (item in cartItems) {
                    categories.add(item.category)
                }
                cartActivity.multiOptInWidget.initialize(categories)

                notifyDataSetChanged()
            }
            true
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProduct: ImageView = itemView.findViewById(R.id.iv_product)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvSubTitle: TextView = itemView.findViewById(R.id.tv_sub_title)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_price)
        val llRemove: LinearLayout = itemView.findViewById(R.id.ll_remove)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }
}