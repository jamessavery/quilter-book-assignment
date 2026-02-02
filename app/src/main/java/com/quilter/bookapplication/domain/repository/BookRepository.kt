package com.quilter.bookapplication.domain.repository

import androidx.paging.PagingData
import com.quilter.bookapplication.domain.model.Book
import io.reactivex.rxjava3.core.Flowable

interface BookRepository {

    fun getSubjectBooksStream(subject: String): Flowable<PagingData<Book>>

}