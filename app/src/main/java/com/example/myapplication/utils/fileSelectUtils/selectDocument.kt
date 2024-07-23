package com.example.myapplication.utils.fileSelectUtils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class SelectDocument :ActivityResultContract<Unit?,DocumentResult>(){
    private var context:Context? = null
    override fun createIntent(context: Context, input: Unit?): Intent {
        this.context = context
//        Log.d("TAG", "createIntent: ")
        return Intent(Intent.ACTION_OPEN_DOCUMENT).addCategory(Intent.CATEGORY_OPENABLE).setType("*/*")
    }

    override fun parseResult(resultCode: Int, intent: Intent?): DocumentResult {
        return DocumentResult(intent?.data,resultCode == Activity.RESULT_OK)
    }

}

data class DocumentResult(val uri: Uri?,val isSuccess:Boolean)