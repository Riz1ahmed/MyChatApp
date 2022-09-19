package com.learner.codereducer.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File

object MediaUtils {
    fun playVideo(context: Context, videoPath: String) {
        val outFile = File(videoPath)
        if (outFile.exists()) {
            val uri =
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) Uri.parse(outFile.absolutePath)
                else FileProvider.getUriForFile(context, "${context.packageName}.riz1", outFile)

            val intent = Intent(Intent.ACTION_VIEW, uri)
                .setDataAndType(uri, "video/*") //or specify with "video/mp4"
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.startActivity(intent)

        }
    }

    fun getVideoPickerIntent(): Intent {
        return Intent(Intent.ACTION_OPEN_DOCUMENT).also {
            it.addCategory(Intent.CATEGORY_OPENABLE)
            it.type = "video/*"
            it.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        }
    }

    fun getThumbnailAt(videoUri: String, secInMicro: Long): Bitmap? {
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(videoUri)
        return mmr.getFrameAtTime(secInMicro)
    }

    /**The result will find on [onActivityResult] with [requestCode] request code*/
    fun openAudioPicker(fragment: Fragment, requestCode: Int) {
        val gallery = Intent()
        gallery.type = "audio/*"
        gallery.action = Intent.ACTION_GET_CONTENT
        fragment.startActivityForResult(gallery, requestCode)
    }

    /**Not implemented*/
    fun notifyGallery(){

    }
}