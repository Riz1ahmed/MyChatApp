package com.learner.codereducer.utils

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns.DISPLAY_NAME
import android.util.Log

object CProviderUtils {
    fun getVideoUriByName(context: Context, title: String?): Uri? {

        Log.d("xyz", "search Video: $title")
        /**
         * Content resolver actually working like SQL query.
         * @param uri (SELECT) Root folder. in which folder you want to search/query
         * @param projection (FROM) which columns want to return. Remember it, without
         * adding a column here you can't do anything with the column. For
         * example: if wou do something with "_ID", you must add it here. Otherwise
         * will get error.
         * @param selection (WHERE) If want to filter cursor, then write condition here with
         * SQL language.
         * @param selectionArgs if have any arguments on "selection" (3rd parameter)
         * then take them here with array.
         * @param sortOrder if want to sort the cursor, then code hare.
         */
        try {
            query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,//Root folder
                arrayOf(MediaStore.Video.Media._ID, DISPLAY_NAME),//which columns want to return
                "$DISPLAY_NAME LIKE ?", //if want filter write with SQL
                arrayOf(title), //Argument of selection
                null,
                context
            )?.use { cursor ->
                @SuppressLint("Range")
                if (cursor.moveToFirst()) {
                    return Uri.withAppendedPath(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                    )
                }
            }
        } catch (e: Exception) {
            Log.d("xyz", "Video Uri Exception: $e")
        }
        return null
    }

    private fun query(
        select: Uri,
        from: Array<String>?,
        where: String?,
        whereArgs: Array<String?>?,
        sortBy: String?,
        context: Context
    ): Cursor? = context.contentResolver.query(select, from, where, whereArgs, sortBy)
}