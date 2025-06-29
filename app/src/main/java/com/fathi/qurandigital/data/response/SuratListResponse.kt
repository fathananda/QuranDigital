package com.fathi.qurandigital.data.response

import com.fathi.qurandigital.data.model.SuratApiModel

data class SuratListResponse(
    val code: Int,
    val message: String,
    val data: List<SuratApiModel>
)