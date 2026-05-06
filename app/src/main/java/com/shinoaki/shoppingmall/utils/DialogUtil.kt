package com.shinoaki.shoppingmall.utils

import android.app.AlertDialog
import android.content.Context

object DialogUtil {

    fun showConfirmDialog(
        context: Context,
        title: String = "提示",
        message: String,
        onConfirm: () -> Unit
    ) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("确定") { _, _ -> onConfirm() }
            .setNegativeButton("取消", null)
            .show()
    }

    fun showDeleteDialog(
        context: Context,
        itemName: String,
        onDelete: () -> Unit
    ) {
        AlertDialog.Builder(context)
            .setTitle("删除")
            .setMessage("确定要删除「$itemName」吗？")
            .setIcon(android.R.drawable.ic_delete)
            .setPositiveButton("删除") { _, _ -> onDelete() }
            .setNegativeButton("取消", null)
            .show()
    }
}
