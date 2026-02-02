package com.quilter.bookapplication.domain.model

data class Book(
    val key: String,
    val title: String,
    val authors: String,
    val coverUrl: String? = null,
)