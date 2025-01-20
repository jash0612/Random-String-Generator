package com.application.randomstringgenerator.data.model

data class RandomString(
    val value: String,
    val length: Int,
    val created: String // ISO 8601 format
)
