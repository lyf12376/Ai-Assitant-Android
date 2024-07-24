package com.example.myapplication.utils.fileSelectUtils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class SelectDocument : ActivityResultContract<Unit?, DocumentResult>() {
    private var context: Context? = null

    override fun createIntent(context: Context, input: Unit?): Intent {
        this.context = context
        return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(
                "application/pdf",
                "text/plain",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "application/vnd.ms-powerpoint",
                "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                "application/zip",
                "application/x-rar-compressed",
                "application/x-tar",
                "application/x-7z-compressed",
                "text/csv",
                "text/xml",
                "text/html",
                "application/json",
                "application/octet-stream"
            ))
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): DocumentResult {
        return DocumentResult(intent?.data, resultCode == Activity.RESULT_OK)
    }
}

data class DocumentResult(val uri: Uri?, val isSuccess: Boolean)

