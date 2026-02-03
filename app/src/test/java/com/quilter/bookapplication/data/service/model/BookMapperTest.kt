package com.quilter.bookapplication.data.service.model

import com.quilter.bookapplication.domain.model.Book
import org.junit.Assert.*
import org.junit.Test

class BookMapperTest {

    @Test
    fun `toDomain() maps Work list to Book list correctly`() {
        // given
        val works = listOf(
            Work(
                key = "123",
                title = "Jimmys' Clean Code",
                editionCount = 3,
                authors = listOf(
                    Author(key = "123", name = "James Savery"),
                    Author(key = "456", name = "Robert Martin")
                ),
                hasFulltext = true,
                coverId = 12345,
                internetArchiveId = "12345"
            )
        )

        // when
        val result: List<Book> = works.toDomain()

        // then
        assertEquals(1, result.size)
        val book = result.first()
        assertEquals("123", book.key)
        assertEquals("Jimmys' Clean Code", book.title)
        assertEquals("James Savery, Robert Martin", book.authors)
        assertEquals("https://covers.openlibrary.org/b/id/12345-L.jpg", book.coverUrl)
    }

    @Test
    fun `toDomain() handles null coverId`() {
        // given
        val works = listOf(
            Work(
                key = "123",
                title = "Jimmys' Clean Code",
                editionCount = 1,
                authors = listOf(Author(key = "123", name = "James Savery")),
                hasFulltext = false,
                coverId = null,
                internetArchiveId = null
            )
        )

        // when
        val result: List<Book> = works.toDomain()

        // then
        assertEquals(1, result.size)
        val book = result.first()
        assertEquals("123", book.key)
        assertEquals("Jimmys' Clean Code", book.title)
        assertEquals("James Savery", book.authors)
        assertNull(book.coverUrl)
    }

    @Test
    fun `toCoverUrl() builds correct url`() {
        // when
        val url = 999.toCoverUrl()

        // then
        assertEquals("https://covers.openlibrary.org/b/id/999-L.jpg", url)
    }

}