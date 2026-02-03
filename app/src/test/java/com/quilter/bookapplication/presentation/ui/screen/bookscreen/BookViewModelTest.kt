package com.quilter.bookapplication.presentation.ui.screen.bookscreen

import androidx.paging.PagingData
import com.quilter.bookapplication.MainDispatcherRule
import com.quilter.bookapplication.domain.model.Book
import com.quilter.bookapplication.domain.repository.BookRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: BookRepository = mockk()

    private val dummyPagingData = PagingData.empty<Book>()

    @Test
    fun `Default subject is programming, changing subject correctly swaps & ensures to use apiValue when invoking api`() = runTest {
        // given
        every { repository.getSubjectBooksStream(any()) } returns Flowable.just(dummyPagingData)

        // when
        val viewModel = BookViewModel(repository)
        viewModel.booksPagingData.first()

        // then
        assertEquals(BookViewModel.Subject.PROGRAMMING, viewModel.selectedSubject.value)
        verify { repository.getSubjectBooksStream(BookViewModel.Subject.PROGRAMMING.apiValue) }

        // and when
        viewModel.onUserChooseSubject(BookViewModel.Subject.MANAGEMENT)
        advanceUntilIdle()

        // then
        verify { repository.getSubjectBooksStream(BookViewModel.Subject.MANAGEMENT.apiValue) }
    }
}
