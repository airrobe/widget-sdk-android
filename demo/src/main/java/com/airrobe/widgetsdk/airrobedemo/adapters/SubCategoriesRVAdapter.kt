package com.airrobe.widgetsdk.airrobedemo.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airrobe.widgetsdk.airrobedemo.R
import com.airrobe.widgetsdk.airrobedemo.activities.SubCategoryActivity
import com.airrobe.widgetsdk.airrobedemo.utils.Utils

class SubCategoriesRVAdapter(
    private val activity: Activity,
    private val subCategoryModels: ArrayList<String>
) : RecyclerView.Adapter<SubCategoriesRVAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(activity)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.recyclerview_sub_category_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvSubCategory.text = subCategoryModels[position]
        holder.llSubCategory.setOnTouchListener { view, motionEvent ->
            if (Utils.touchAnimator(activity, view, motionEvent)) {
            }
            true
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val llSubCategory: LinearLayout = itemView.findViewById(R.id.ll_sub_category)
        var tvSubCategory: TextView = itemView.findViewById(R.id.tv_sub_category)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return subCategoryModels.size
    }
}