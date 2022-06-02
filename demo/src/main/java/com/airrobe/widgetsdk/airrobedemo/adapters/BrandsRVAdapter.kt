package com.airrobe.widgetsdk.airrobedemo.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airrobe.widgetsdk.airrobedemo.R
import com.airrobe.widgetsdk.airrobedemo.activities.BrandActivity
import com.airrobe.widgetsdk.airrobedemo.activities.CategoryActivity
import com.airrobe.widgetsdk.airrobedemo.models.BrandModel
import com.airrobe.widgetsdk.airrobedemo.utils.Utils

class BrandsRVAdapter(
    private val activity: Activity,
    private val brandModels: ArrayList<BrandModel>
) : RecyclerView.Adapter<BrandsRVAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(activity)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.recyclerview_brand_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val brandModel: BrandModel = brandModels[position]
        holder.tvBrand.text = brandModel.brand
        holder.ivBrand.setImageResource(brandModel.image)
        holder.llBrand.setOnTouchListener { view, motionEvent ->
            if (Utils.touchAnimator(activity, view, motionEvent)) {
                val intent = Intent(activity, CategoryActivity::class.java)
                intent.putExtra("brandId", position)
                activity.startActivity(intent)
            }
            true
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val llBrand: LinearLayout = itemView.findViewById(R.id.ll_brand)
        val tvBrand: TextView = itemView.findViewById(R.id.tv_brand)
        val ivBrand: ImageView = itemView.findViewById(R.id.iv_brand)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return brandModels.size
    }
}