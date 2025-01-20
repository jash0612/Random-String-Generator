package com.application.randomstringgenerator.data.repository

import com.application.randomstringgenerator.data.model.RandomString
import com.application.randomstringgenerator.data.source.ContentProviderDataSource

class RandomStringRepository(private val dataSource: ContentProviderDataSource) {

    fun fetchRandomString(maxLength: Int): RandomString? {
        return dataSource.getRandomString(maxLength)
    }
}