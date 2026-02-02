package com.quilter.bookapplication.data.repository

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.quilter.bookapplication.data.service.OpenLibraryApi
import com.quilter.bookapplication.data.service.model.BookDto
import com.quilter.bookapplication.data.service.model.toDomain
import com.quilter.bookapplication.domain.model.Book
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class BookPagingSource(
    private val api: OpenLibraryApi,
    private val subject: String
) : RxPagingSource<Int, Book>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Book>> {
        val page = params.key ?: 0
        val pageSize = params.loadSize

        println("JIMMY page -> $page")
        println("JIMMY loadSingle -> ${params.loadSize}")

        return api.getSubjectBooks(
            subject = subject,
            limit = pageSize,
            offset = page * pageSize
        )
            .subscribeOn(Schedulers.io())
            .map { response -> onPagingSuccess(response, page) }
            .onErrorReturn { error -> LoadResult.Error(error) }
    }

    private fun onPagingSuccess(response: BookDto, page: Int): LoadResult<Int, Book> {
        return LoadResult.Page(
            data = response.works.toDomain(),
            prevKey = if (page == 0) null else page - 1,
            nextKey = if (response.works.isEmpty()) null else page + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Book>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)

            // Calculate next page based on current page data
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}