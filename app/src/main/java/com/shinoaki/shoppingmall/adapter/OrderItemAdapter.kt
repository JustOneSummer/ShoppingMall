package com.shinoaki.shoppingmall.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shinoaki.shoppingmall.R
import com.shinoaki.shoppingmall.data.OrderData
import com.shinoaki.shoppingmall.data.RecommendFragmentRightMenuItem
import com.shinoaki.shoppingmall.databinding.ItemOrderBinding
import com.shinoaki.shoppingmall.entity.DeliveryAddressEntity
import com.shinoaki.shoppingmall.entity.OrderEntity
import kotlinx.serialization.json.Json

class OrderItemAdapter() : RecyclerView.Adapter<OrderItemAdapter.ViewHolder>() {

    private lateinit var binding: ItemOrderBinding

    private var items: List<OrderData> = emptyList()
    override fun onCreateViewHolder(holder: ViewGroup, p: Int): ViewHolder {
        binding = ItemOrderBinding.inflate(LayoutInflater.from(holder.context), holder, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = items[position]
        holder.bind(item)
        // 动态设置 margin：除了第一个，其他都添加 marginTop
        val layoutParams = holder.itemView.layoutParams as? ViewGroup.MarginLayoutParams
        if (position == 0) {
            layoutParams?.topMargin = 0
        } else {
            layoutParams?.topMargin = 6.dpToPx()
        }
    }

    fun updateData(newItems: List<OrderEntity>) {
        val list = mutableListOf<OrderData>()
        for (order in newItems) {
            list.add(OrderData(order, Json.decodeFromString(order.productJson)))
        }
        val diffCallback = ProductDiffCallback(items, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = list
        diffResult.dispatchUpdatesTo(this)
    }

    // dp 转 px 扩展函数
    private fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        private val bind = binding
        fun bind(item: OrderData) {
            val dataList = item.shopping as List<RecommendFragmentRightMenuItem>

            if (!dataList.isEmpty()) {
                val data = dataList[0]
                //拼接商品名称信息 xxx,xxx,xxx
                val productName = dataList.joinToString(",") { it.title }
                bind.tvTitle.text = productName
                Glide.with(itemView.context)
                    .load(data.iconUrl)           // 网络图片URL
                    .placeholder(R.drawable.acc)  // 加载中占位图
                    .error(R.drawable.gwc)         // 加载失败占位图
//                .circleCrop()                       // 圆形裁剪（可选）
                    .into(bind.ivIcon)
                val deliveryAddress =
                    Json.decodeFromString(item.order.deliveryAddressJson) as DeliveryAddressEntity
                bind.name.text = "姓名: " + deliveryAddress.name
                bind.phone.text = "手机: " + deliveryAddress.phone
                bind.address.text = "地址: " + deliveryAddress.address
                bind.priceStatus.text = when (item.order.orderStatus) {
                    0 -> "订单待支付"
                    1 -> "订单已取消"
                    2 -> "交易成功"
                    3 -> "售后服务中"
                    else -> "未知"
                }
                bind.number.text = dataList.size.toString()
                bind.price.text = item.order.orderPrice
            }
        }
    }

    class ProductDiffCallback(
        private val oldList: List<OrderData>,
        private val newList: List<OrderData>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].order.id == newList[newItemPosition].order.id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}