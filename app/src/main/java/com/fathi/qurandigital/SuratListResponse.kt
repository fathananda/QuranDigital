package com.fathi.qurandigital

data class SuratListResponse(
    val code: Int,
    val message: String,
    val data: List<SuratApiModel>
)
