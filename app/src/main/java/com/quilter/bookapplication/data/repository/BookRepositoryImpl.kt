package com.quilter.bookapplication.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.quilter.bookapplication.data.repository.Constant.INITIAL_LOAD_SIZE
import com.quilter.bookapplication.data.repository.Constant.PAGE_SIZE
import com.quilter.bookapplication.data.service.OpenLibraryApi
import com.quilter.bookapplication.domain.model.Book
import com.quilter.bookapplication.domain.repository.BookRepository
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    val booksApi: OpenLibraryApi
) : BookRepository {
    override fun getSubjectBooksStream(subject: String): Flowable<PagingData<Book>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = 10,
                initialLoadSize = INITIAL_LOAD_SIZE,
            ),
            pagingSourceFactory = { BookPagingSource(booksApi, subject) }
        ).flowable
    }
}

object Constant {
    const val PAGE_SIZE = 12
    const val INITIAL_LOAD_SIZE = 24
}