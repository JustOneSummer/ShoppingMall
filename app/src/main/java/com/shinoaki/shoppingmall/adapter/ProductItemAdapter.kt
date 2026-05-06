package com.shinoaki.shoppingmall.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shinoaki.shoppingmall.R
import com.shinoaki.shoppingmall.data.ProductData
import com.shinoaki.shoppingmall.databinding.ItemProductBinding

class ProductItemAdapter(
    //购物车加减事件
    private val onQuantityChange: (ProductData, Int) -> Unit,
    //购物车复选按钮事件
    private val onCheckBoxChange: (ProductData, Boolean) -> Unit
) : RecyclerView.Adapter<ProductItemAdapter.ViewHolder>() {
    private lateinit var binding: ItemProductBinding

    private var items: List<ProductData> = emptyList()


    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): ViewHolder {
        binding = ItemProductBinding.inflate(LayoutInflater.from(p0.context), p0, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        binding.checkBox.setOnCheckedChangeListener(null)
        holder.bind(items[position])
        //监听复选框
        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            val checkData = items[position]
            if (checkData.checkStatus != isChecked) {
                checkData.checkStatus = isChecked
                onCheckBoxChange(checkData, isChecked)
            }
        }
        binding.jia.setOnClickListener {
            val item = items[position]
            onQuantityChange(item, item.car.quantity + 1)
        }
        binding.jian.setOnClickListener {
            val item = items[position]
            onQuantityChange(item, item.car.quantity - 1)
        }

        // 动态设置 margin：除了第一个，其他都添加 marginTop
        val layoutParams = holder.itemView.layoutParams as? ViewGroup.MarginLayoutParams
        if (position == 0) {
            layoutParams?.topMargin = 0
        } else {
            layoutParams?.topMargin = 6.dpToPx()
        }
    }

    fun selectAll(isSelected: Boolean) {
        items.forEach { it.checkStatus = isSelected }
        //不建议使用DiffUtil  在全选后使用单选会有对象引用问题
        notifyItemRangeChanged(0, items.size)
    }

    fun getProductData(): List<ProductData> {
        return items
    }

    override fun getItemCount(): Int {
        return items.size
    }

    // dp 转 px 扩展函数
    private fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    fun quantityButtonStatus(status: Boolean) {
        binding.jia.isClickable = status
        binding.jian.isClickable = status
        binding.tvQuantity.isClickable = status
    }

    fun updateData(newItems: List<ProductData>) {
        val diffCallback = ProductDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(
        binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val bind = binding
        fun bind(item: ProductData) {
            bind.apply {
                tvTitle.text = item.shopping.title
                Glide.with(itemView.context)
                    .load(item.shopping.iconUrl)           // 网络图片URL
                    .placeholder(R.drawable.acc)  // 加载中占位图
                    .error(R.drawable.gwc)         // 加载失败占位图
//                .circleCrop()                       // 圆形裁剪（可选）
                    .into(ivIcon)
                salesLabel.text = item.shopping.label
                productDetails.text = item.shopping.detail
                price.text = item.shopping.price
                tvQuantity.text = item.car.quantity.toString()
                checkBox.isChecked = item.checkStatus
            }
        }
    }

    class ProductDiffCallback(
        private val oldList: List<ProductData>,
        private val newList: List<ProductData>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].car.id == newList[newItemPosition].car.id
                    && oldList[oldItemPosition].checkStatus == newList[newItemPosition].checkStatus
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}