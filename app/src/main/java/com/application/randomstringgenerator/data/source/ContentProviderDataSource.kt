package com.application.randomstringgenerator.data.source

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import com.application.randomstringgenerator.data.model.RandomString
import org.json.JSONObject

class ContentProviderDataSource(private val contentResolver: ContentResolver) {

    private val contentURI = Uri.parse("content://com.iav.contestdataprovider/text")

    fun getRandomString(maxLength: Int): RandomString? {

        val cursor: Cursor? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Use queryArgs for API level 26+
            val queryArgs = Bundle().apply {
                putString("length", maxLength.toString()) // Custom parameter for length
            }
            contentResolver.query(contentURI, null, queryArgs, null)
        } else {
            // Use URI query parameters for API level <26
            val uriWithParams = contentURI.buildUpon()
                .appendQueryParameter("length", maxLength.toString()) // Custom parameter for length
                .build()
            contentResolver.query(uriWithParams, null, null, null, null)
        }

        cursor?.use {
            if (it.moveToFirst()) {
                val jsonString = it.getString(it.getColumnIndexOrThrow("data"))
                val jsonObject = JSONObject(jsonString)
                val randomText = jsonObject.getJSONObject("randomText")
                return RandomString(
                    value = randomText.getString("value").substring(0,maxLength), // truncating the string manually as content provider length parameter is not working properly
                    length = maxLength,
                    created = randomText.getString("created")
                )
            }
        }
        return null
    }
}
