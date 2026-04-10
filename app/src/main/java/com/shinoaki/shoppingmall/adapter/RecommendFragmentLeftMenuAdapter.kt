package com.shinoaki.shoppingmall.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shinoaki.shoppingmall.R
import com.shinoaki.shoppingmall.data.RecommendFragmentLeftMenuItem


class RecommendFragmentLeftMenuAdapter(
    val items: List<RecommendFragmentLeftMenuItem>
) : RecyclerView.Adapter<RecommendFragmentLeftMenuAdapter.ViewHolder>() {

    // 点击监听器
    private var onItemClickListener: ((RecommendFragmentLeftMenuItem, Int) -> Unit)? = null

    // 当前选中的位置
    private var selectedPosition = 0

    // 创建 ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recommend_fragment_left_menu, parent, false)
        return ViewHolder(view)
    }


    // 绑定数据
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, position == selectedPosition)
        // 设置点击事件
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(item, position)
            setSelectedPosition(position)

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

    public fun setOnItemClickListener(listener: (RecommendFragmentLeftMenuItem, Int) -> Unit) {
        onItemClickListener = listener
    }

    // 返回数据数量
    override fun getItemCount(): Int = items.size

    // 设置选中的位置
    fun setSelectedPosition(position: Int) {
        val oldPosition = selectedPosition
        selectedPosition = position
        // 更新新旧位置的样式
        notifyItemChanged(oldPosition)
        notifyItemChanged(selectedPosition)
    }

    // ViewHolder 内部类
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val viewSelected: View = itemView.findViewById(R.id.viewSelected)
        fun bind(item: RecommendFragmentLeftMenuItem, isSelected: Boolean) {
            tvTitle.text = item.title
            Glide.with(itemView.context)
                .load(item.iconUrl)           // 网络图片URL
                .placeholder(R.drawable.acc)  // 加载中占位图
                .error(R.drawable.gwc)         // 加载失败占位图
//                .circleCrop()                       // 圆形裁剪（可选）
                .into(ivIcon)
            // 设置选中样式
            if (isSelected) {
                tvTitle.setTextColor(itemView.context.getColor(R.color.black))
                viewSelected.visibility = View.VISIBLE
                itemView.setBackgroundColor(itemView.context.getColor(android.R.color.transparent))
            } else {
                tvTitle.setTextColor(itemView.context.getColor(R.color.black))
                viewSelected.visibility = View.GONE
                itemView.setBackgroundColor(itemView.context.getColor(android.R.color.transparent))
            }
        }
    }
}