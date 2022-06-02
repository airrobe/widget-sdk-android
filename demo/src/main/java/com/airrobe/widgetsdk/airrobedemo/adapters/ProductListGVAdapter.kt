package com.airrobe.widgetsdk.airrobedemo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.airrobe.widgetsdk.airrobedemo.R
import com.airrobe.widgetsdk.airrobedemo.models.ItemModel

class ProductListGVAdapter(
    private val context: Context,
    private val products: ArrayList<ItemModel>,
) : BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var ivProduct: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvSubTitle: TextView
    private lateinit var tvPrice: TextView

    override fun getCount(): Int {
        return products.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var view = convertView
        if (layoutInflater == null) {
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (view == null) {
            view = layoutInflater!!.inflate(R.layout.gridview_product_list_item, null)
        }
        ivProduct = view!!.findViewById(R.id.iv_product)
        tvTitle = view.findViewById(R.id.tv_title)
        tvSubTitle = view.findViewById(R.id.tv_sub_title)
        tvPrice = view.findViewById(R.id.tv_price)
        ivProduct.setImageResource(products[position].image)
        tvTitle.text = products[position].title
        tvSubTitle.text = products[position].subTitle
        tvPrice.text = products[position].price
        return view
    }
}
