package com.shinoaki.shoppingmall.data

data class PageResponse<T>(
    val data: List<T>,      // 当前页数据
    val page: Int,          // 当前页码
    val pageSize: Int,      // 每页大小
    val totalCount: Int,    // 总数据量
    val hasMore: Boolean    // 是否还有更多数据
)
