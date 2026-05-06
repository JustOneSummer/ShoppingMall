package com.shinoaki.shoppingmall.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.shinoaki.shoppingmall.R
import com.shinoaki.shoppingmall.data.HotFragmentRightMenuItem
import com.shinoaki.shoppingmall.data.PageResponse
import com.shinoaki.shoppingmall.data.RecommendFragmentRightMenuItem

class HotFragmentRightMenuAdapter(var items: List<HotFragmentRightMenuItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    // 点击监听器
    private var onItemClickListener: ((HotFragmentRightMenuItem, Int) -> Unit)? = null


    // 创建 ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recommend_fragment_right_menu, parent, false)
        return ViewHolder(view)
    }

    // 绑定数据
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val item = items[position]
                holder.bind(item)
                holder.itemView.setOnClickListener {
                    onItemClickListener?.invoke(item, position)
                }
            }

            is FooterViewHolder -> {
                holder.bind()
            }
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

    fun updateData(newItems: PageResponse<HotFragmentRightMenuItem>) {
        if (!newItems.data.isEmpty()) {
            val diffCallback = ProductDiffCallback(items, newItems.data)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            items = newItems.data
            diffResult.dispatchUpdatesTo(this)
        }
    }

    public fun setOnItemClickListener(listener: (HotFragmentRightMenuItem, Int) -> Unit) {
        onItemClickListener = listener
    }

    // 返回数据数量
    override fun getItemCount(): Int = items.size

    // ViewHolder 内部类
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivIcon: ShapeableImageView = itemView.findViewById(R.id.ivIcon)
        private val tvTitle: MaterialTextView = itemView.findViewById(R.id.tvTitle)
        private val monthlySales: MaterialTextView = itemView.findViewById(R.id.monthlySales)

        //部分情况下隐藏或者是其他文字描述
        private val salesLabel: MaterialTextView = itemView.findViewById(R.id.salesLabel)
        private val productDetails: MaterialTextView = itemView.findViewById(R.id.productDetails)
        private val price: MaterialTextView = itemView.findViewById(R.id.price)

        fun bind(item: HotFragmentRightMenuItem) {
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
        private val oldList: List<HotFragmentRightMenuItem>,
        private val newList: List<HotFragmentRightMenuItem>
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


    class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

        fun bind() {
            progressBar.isIndeterminate = true  // 启动动画
        }
    }
}