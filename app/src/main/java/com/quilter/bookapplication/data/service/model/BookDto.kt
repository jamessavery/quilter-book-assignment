package com.quilter.bookapplication.data.service.model

import com.quilter.bookapplication.domain.model.Book
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookDto(
    @SerialName("key") val key: String,
    @SerialName("name") val name: String,
    @SerialName("subject_type") val subjectType: String,
    @SerialName("work_count") val workCount: Int,
    @SerialName("works") val works: List<Work>
)

@Serializable
data class Work(
    @SerialName("key") val key: String,
    @SerialName("title") val title: String,
    @SerialName("edition_count") val editionCount: Int,
    @SerialName("authors") val authors: List<Author>,
    @SerialName("has_fulltext") val hasFulltext: Boolean = false,
    @SerialName("cover_id") val coverId: Int? = null,
    @SerialName("ia") val internetArchiveId: String? = null
)

@Serializable
data class Author(
    @SerialName("key") val key: String,
    @SerialName("name") val name: String
)

fun List<Work>.toDomain() = this.map {
    Book(
        key = it.key,
        title = it.title,
        authors = it.authors.joinToString(", ") { authorName -> authorName.name },
        coverUrl = it.coverId?.toCoverUrl()
    )
}

fun Int.toCoverUrl() = "https://covers.openlibrary.org/b/id/${this}-L.jpg"
