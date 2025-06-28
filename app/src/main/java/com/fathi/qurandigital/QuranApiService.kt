package com.fathi.qurandigital

import com.fathi.qurandigital.response.SuratDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface QuranApiService {
    @GET("api/v2/surat")
    suspend fun getSuratList(): Response<SuratListResponse>

    @GET("api/v2/surat/{id}")
    suspend fun getSuratDetail(@Path("id") id: Int): Response<SuratDetailResponse>

    @GET("api/v2/tafsir/{id}")
    suspend fun getTafsir(@Path("id") id: Int): Response<TafsirResponse>
}