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
import com.airrobe.widgetsdk.airrobedemo.activities.SubCategoryActivity
import com.airrobe.widgetsdk.airrobedemo.models.CategoryModel
import com.airrobe.widgetsdk.airrobedemo.utils.Utils

class CategoriesRVAdapter(
    private val activity: Activity,
    private val categoryModels: ArrayList<CategoryModel>
) : RecyclerView.Adapter<CategoriesRVAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(activity)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.recyclerview_category_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoryModel: CategoryModel = categoryModels[position]
        holder.tvCategory.text = categoryModel.category
        holder.ivCategory.setImageResource(categoryModel.image)
        holder.llCategory.setOnTouchListener { view, motionEvent ->
            if (Utils.touchAnimator(activity, view, motionEvent)) {
                val intent = Intent(activity, SubCategoryActivity::class.java)
                intent.putExtra("categoryId", position)
                activity.startActivity(intent)
            }
            true
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val llCategory: LinearLayout = itemView.findViewById(R.id.ll_category)
        var tvCategory: TextView = itemView.findViewById(R.id.tv_category)
        var ivCategory: ImageView = itemView.findViewById(R.id.iv_category)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return categoryModels.size
    }
}