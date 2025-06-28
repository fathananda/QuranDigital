package com.fathi.qurandigital.response

import com.fathi.qurandigital.SuratDetailApiModel

data class SuratDetailResponse(
    val code: Int,
    val message: String,
    val data: SuratDetailApiModel
)