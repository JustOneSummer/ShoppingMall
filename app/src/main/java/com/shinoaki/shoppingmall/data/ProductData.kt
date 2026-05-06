package com.shinoaki.shoppingmall.data

import com.shinoaki.shoppingmall.entity.CarEntity
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class ProductData(
    val car: CarEntity,
    val shopping: RecommendFragmentRightMenuItem,
    var checkStatus: Boolean = false
)
