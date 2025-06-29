package com.fathi.qurandigital.data.response

import com.fathi.qurandigital.data.model.SuratDetailApiModel

data class SuratDetailResponse(
    val code: Int,
    val message: String,
    val data: SuratDetailApiModel
)