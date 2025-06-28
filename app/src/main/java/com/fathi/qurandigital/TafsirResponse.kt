package com.fathi.qurandigital

data class TafsirResponse(
    val code: Int,
    val message: String,
    val data: TafsirApiModel
)

data class TafsirApiModel(
    val nomor: Int,
    val nama: String,
    val namaLatin: String,
    val jumlahAyat: Int,
    val tafsir: List<TafsirAyatModel>
)

data class TafsirAyatModel(
    val ayat: Int,
    val teks: String
)
