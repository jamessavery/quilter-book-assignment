package com.quilter.bookapplication.presentation.ui.screen.bookscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.quilter.bookapplication.domain.model.Book
import com.quilter.bookapplication.domain.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.reactive.asFlow
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    bookRepository: BookRepository
) : ViewModel() {

    private val _selectedSubject = MutableStateFlow(Subject.PROGRAMMING)
    val selectedSubject = _selectedSubject.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val booksPagingData: Flow<PagingData<Book>> = _selectedSubject
        .flatMapLatest { chosenSubject ->
            bookRepository.getSubjectBooksStream(chosenSubject.apiValue)
                .asFlow()
        }.cachedIn(viewModelScope)

    fun onUserChooseSubject(subject: Subject) {
        _selectedSubject.value = subject
    }

    // N.b. Api is case sensitive
    enum class Subject(
        val apiValue: String,
        val displayName: String
    ) {
        PROGRAMMING("programming", "Programming"),
        MANAGEMENT("management", "Management"),
        DESIGN("design", "Design")
    }

}