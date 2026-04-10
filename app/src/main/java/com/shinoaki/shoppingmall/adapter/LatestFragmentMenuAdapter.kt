package com.shinoaki.shoppingmall.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.shinoaki.shoppingmall.R
import com.shinoaki.shoppingmall.data.LatestFragmentMenuItem

class LatestFragmentMenuAdapter(

) : RecyclerView.Adapter<LatestFragmentMenuAdapter.ViewHolder>() {
    private var items: List<LatestFragmentMenuItem> = emptyList()

    // 点击监听器
    private var onItemClickListener: ((LatestFragmentMenuItem, Int) -> Unit)? = null


    // 创建 ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recommend_fragment_right_menu, parent, false)
        return ViewHolder(view)
    }


    // 绑定数据
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        // 设置点击事件 点击时弹窗
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(item, position)
        }
        // 动态设置 margin：除了第一个，其他都添加 marginTop
        val layoutParams = holder.itemView.layoutParams as? ViewGroup.MarginLayoutParams
        if (position == 0) {
            layoutParams?.topMargin = 0
        } else {
            layoutParams?.topMargin = 6.dpToPx()
        }
    }

    // dp 转 px 扩展函数
    private fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    fun updateData(newItems: List<LatestFragmentMenuItem>) {
        val diffCallback = ProductDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    fun addData(newItems: List<LatestFragmentMenuItem>) {
        val diffCallback = ProductDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items += newItems
        diffResult.dispatchUpdatesTo(this)
    }

    // 返回数据数量
    override fun getItemCount(): Int = items.size


    // ViewHolder 内部类
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivIcon: ShapeableImageView = itemView.findViewById(R.id.ivIcon)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val monthlySales: TextView = itemView.findViewById(R.id.monthlySales)

        //部分情况下隐藏或者是其他文字描述
        private val salesLabel: TextView = itemView.findViewById(R.id.salesLabel)
        private val productDetails: TextView = itemView.findViewById(R.id.productDetails)
        private val price: TextView = itemView.findViewById(R.id.price)

        fun bind(item: LatestFragmentMenuItem) {
            tvTitle.text = item.title
            //先加载占位图
//            ivIcon.setImageResource(R.drawable.acc)
            Glide.with(itemView.context)
                .load(item.iconUrl)           // 网络图片URL
                .placeholder(R.drawable.acc)  // 加载中占位图
                .error(R.drawable.gwc)         // 加载失败占位图
//                .circleCrop()                       // 圆形裁剪（可选）
                .into(ivIcon)
            monthlySales.text = item.monthlySales
            salesLabel.text = item.label
            productDetails.text = item.detail
            price.text = item.price
            price.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.rmb16
                ), null, null, null
            )
        }
    }

    class ProductDiffCallback(
        private val oldList: List<LatestFragmentMenuItem>,
        private val newList: List<LatestFragmentMenuItem>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}