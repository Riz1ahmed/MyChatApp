package com.learner.codereducer.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.learner.codereducer.utils.extentions.customToast
import java.io.File

/**
 * @author Riz1Ahmed
 * Before use this please get permission of sharing any media file.
 * For that:
 * 1. Add bellow code inside Manifest <application/> block
 *  ```
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.riz1"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
    android:name="android.support.FILE_PROVIDER_PATHS"
    android:resource="@xml/provider_paths" />
</provider> <!-- For Shareable Uri -->
 *  ```
 *  Also you have to remember, if you want to share in a specific app,
 *  for example in facebook, then also add bellow code inside Manifest <application/> block
 *  ```
    Code haven't remember
 *  ```
 *
 *  2. Create a file with any name inside res/xml folder. And add bellow code
 *  ```
 <?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <cache-path name="cache-path " path="."/>
    <files-path name="files-path" path="."/>
    <external-cache-path name="external-cache-path" path="."/>
    <external-files-path name="external-files-path" path="."/>
</paths>
 *  ```
 *  3. Now you can use this [ShareUtils]
 * */
object ShareUtils {
    fun shareMedia(context: Context, mediaPath: String, packageId: String?) {
        shareMedia(context, providerUri(context, mediaPath), packageId)
    }

    /**
     * @param context Running Application context
     * @param uri sharing Media file uri.
     * @param packageId if share in a specific app give that app packageId.
     * For default share set this value as null.<\br>
     * .
     * .
     * Some common app PackageId:
     * YourApp = [BuildConfig.APPLICATION_ID]
     * youtube = [com.google.android.youtube]
     * facebook = [com.facebook.katana]
     * whatsApp = [com.whatsapp]
     * instagram = [com.instagram.android]
     * twitter = [com.twitter.android]
     * snapchat = [com.snapchat.android]
     * dropbox = [com.dropbox.android]
     * linkedIn = [com.linkedin.android]
     * linkedInLite = [com.linkedin.android.lite]
     */
    fun shareMedia(context: Context, uri: Uri, packageId: String?) {
        if (packageId != null && context.packageManager.getLaunchIntentForPackage(packageId) == null) {
            context.customToast("Please first install the app")
            openUrl(context, "https://play.google.com/store/apps/details?id=$packageId")
        } else {
            try {
                val intent = Intent(Intent.ACTION_SEND)
                intent.setPackage(packageId)
                intent.type = "video/*"
                intent.putExtra(Intent.EXTRA_STREAM, uri)
                context.startActivity(Intent.createChooser(intent, "Share Video to"))
            } catch (ex: Exception) {
            }
        }
    }

    fun getSupportedUri(context: Context, filePath: String): Uri {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) Uri.parse(filePath)
        else providerUri(context, filePath)
    }

    private fun providerUri(context: Context, filePath: String): Uri {
        return FileProvider.getUriForFile(
            context,
            context.packageName + ".riz1",//Here ".riz1" should be same as Manifest provider
            File(filePath)
        )
    }

    fun sharePlanText(context: Context, title: String, message: String) {
        context.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, message)
        }, "Choose one"))
    }

    fun openEmail(context: Context, emails: Array<String>, subject: String, message: String) {
        context.startActivity(Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // only email apps should handle this
            putExtra(Intent.EXTRA_EMAIL, emails)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
        })
    }

    private fun openUrl(context: Context, url: String) =
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}
