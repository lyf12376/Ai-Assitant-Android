package com.example.myapplication.utils

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context

object ClipBoardUtils {
    fun pasteTextFromClipboard(context: Context): String? {
        // 获取ClipboardManager实例
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        // 检查剪贴板内容
        if (clipboard.hasPrimaryClip() && clipboard.primaryClipDescription?.hasMimeType(
                ClipDescription.MIMETYPE_TEXT_PLAIN) == true) {
            // 获取剪贴板内容
            val item = clipboard.primaryClip?.getItemAt(0)

            // 返回文本内容
            return item?.text?.toString()
        }

        return null
    }

    fun copyTextToClipboard(context: Context, text: String) {
        // 获取ClipboardManager实例
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        // 创建ClipData
        val clip = ClipData.newPlainText("label", text)

        // 设置剪贴板内容
        clipboard.setPrimaryClip(clip)

    }
}