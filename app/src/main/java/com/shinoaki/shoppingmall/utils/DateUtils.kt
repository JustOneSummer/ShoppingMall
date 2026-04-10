package com.shinoaki.shoppingmall.utils

import android.os.Build

class DateUtils {
    companion object {
        /**
         * 获取当前日期和时间 返回格式 yyyy-MM-dd HH:mm:ss
         */
        fun getCurrentDate(): String {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Android 8.0+ 使用 Java 8 API
                java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            } else {
                // 低版本使用 SimpleDateFormat
                val format = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
                format.format(java.util.Date())
            }
        }
    }
}