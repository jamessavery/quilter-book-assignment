package com.quilter.bookapplication.data.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.quilter.bookapplication.data.service.OpenLibraryApi
import com.quilter.bookapplication.data.service.model.Author
import com.quilter.bookapplication.data.service.model.BookDto
import com.quilter.bookapplication.data.service.model.Work
import com.quilter.bookapplication.data.service.model.toDomain
import com.quilter.bookapplication.domain.model.Book
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class BookPagingSourceTest {

    private val api: OpenLibraryApi = mockk()
    private val subject = "programming"
    private val pagingSource = BookPagingSource(api, subject)

    private val book = Book("1", "Jimmys' Clean Code", "James")
    private val mockWork = Work(
        key = "123",
        title = "Jimmys' Clean Code",
        editionCount = 1,
        authors = listOf(Author("123", "James")),
        coverId = 123
    )
    private val mockResponse = BookDto(
        key = "456",
        name = "Programming",
        subjectType = "subject",
        workCount = 100,
        works = listOf(mockWork)
    )

    @Test
    fun `loadSingle() success - returns page w correct data & keys`() {
        // given
        val expectedData = mockResponse.works.toDomain()
        every {
            api.getSubjectBooks(any(), any(), any(), any())
        } returns Single.just(mockResponse)

        // when
        val params = PagingSource.LoadParams.Refresh(
            key = 0,
            loadSize = Constant.INITIAL_LOAD_SIZE,
            placeholdersEnabled = false
        )
        val result = pagingSource.loadSingle(params).blockingGet()

        // then
        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(expectedData, page.data)
        assertEquals(null, page.prevKey)
        assertEquals(1, page.nextKey)

        verify { api.getSubjectBooks(any(), any(), any(), any()) }
    }

    @Test
    fun `loadSingle() success - non-zero page returns correct prevKey`() {
        // given
        every { api.getSubjectBooks(any(), any(), any(), any()) } returns Single.just(mockResponse)

        // when
        var params = PagingSource.LoadParams.Refresh(
            key = 1,
            loadSize = Constant.INITIAL_LOAD_SIZE,
            placeholdersEnabled = false
        )
        var result = pagingSource.loadSingle(params).blockingGet()

        // then
        assertTrue(result is PagingSource.LoadResult.Page)
        var page = result as PagingSource.LoadResult.Page
        assertEquals(0, page.prevKey)
        assertEquals(2, page.nextKey)

        // and when
        params = PagingSource.LoadParams.Refresh(
            key = 2,
            loadSize = Constant.INITIAL_LOAD_SIZE,
            placeholdersEnabled = false
        )
        result = pagingSource.loadSingle(params).blockingGet()

        // and then
        assertTrue(result is PagingSource.LoadResult.Page)
        page = result as PagingSource.LoadResult.Page
        assertEquals(1, page.prevKey)
        assertEquals(3, page.nextKey)
    }


    @Test
    fun `loadSingle() error - returns LoadResult Error`() {
        // given
        val error = RuntimeException("Generic network error")
        every {
            api.getSubjectBooks(any(), any(), any(), any())
        } returns Single.error(error)

        // when
        val params = PagingSource.LoadParams.Refresh(
            key = 0,
            loadSize = Constant.INITIAL_LOAD_SIZE,
            placeholdersEnabled = false
        )
        val result = pagingSource.loadSingle(params).blockingGet()

        // then
        assertTrue(result is PagingSource.LoadResult.Error)
        val errorResult = result as PagingSource.LoadResult.Error
        assertEquals(error, errorResult.throwable)
    }

    @Test
    fun `loadSingle() success - empty list returns null nextKey`() {
        // given
        val emptyResponse = BookDto(
            key = "456",
            name = "Programming",
            subjectType = "subject",
            workCount = 0,
            works = emptyList()
        )

        every {
            api.getSubjectBooks(any(), any(), any(), any())
        } returns Single.just(emptyResponse)

        // when
        val params = PagingSource.LoadParams.Refresh(
            key = 0,
            loadSize = Constant.INITIAL_LOAD_SIZE,
            placeholdersEnabled = false
        )
        val result = pagingSource.loadSingle(params).blockingGet()

        // then
        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(null, page.nextKey)
        assertTrue(page.data.isEmpty())
    }

    @Test
    fun `getRefreshKey() - returns correct key user is on based on anchor position`() {
        // given
        val page1 = PagingSource.LoadResult.Page(
            data = listOf(book),
            prevKey = 0,
            nextKey = 2
        )
        var state = PagingState(
            pages = listOf(page1),
            anchorPosition = 0,
            config = PagingConfig(pageSize = Constant.PAGE_SIZE),
            leadingPlaceholderCount = 0
        )

        // when & then
        assertEquals(1, pagingSource.getRefreshKey(state))

        // and given
        val page2 = PagingSource.LoadResult.Page(
            data = listOf(book),
            prevKey = 1,
            nextKey = 3
        )
        state = PagingState(
            pages = listOf(page1, page2),
            anchorPosition = 13,
            config = PagingConfig(pageSize = Constant.PAGE_SIZE),
            leadingPlaceholderCount = 0
        )

        // and when/then
        assertEquals(2, pagingSource.getRefreshKey(state))
    }

    @Test
    fun `getRefreshKey() - returns null when anchor position is null`() {
        // given
        val state = PagingState<Int, Book>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize = Constant.PAGE_SIZE),
            leadingPlaceholderCount = 0
        )

        // when
        val refreshKey = pagingSource.getRefreshKey(state)

        // then
        assertEquals(null, refreshKey)
    }

}