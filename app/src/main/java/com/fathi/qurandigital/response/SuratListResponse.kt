package com.fathi.qurandigital.response

import com.fathi.qurandigital.SuratApiModel

data class SuratListResponse(
    val code: Int,
    val message: String,
    val data: List<SuratApiModel>
)