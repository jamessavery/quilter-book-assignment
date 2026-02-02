package com.quilter.bookapplication.data.service

import com.quilter.bookapplication.data.service.model.BookDto
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenLibraryApi {

    @GET("subjects/{subject}.json")
    fun getSubjectBooks(
        @Path("subject") subject: String,
        @Query("limit") limit: Int = 12,
        @Query("offset") offset: Int = 0,
        @Query("details") details: Boolean = true,
    ): Single<BookDto>

    companion object {
        const val BASE_URL = "https://openlibrary.org/"
    }
}