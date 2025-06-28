package com.fathi.qurandigital

data class SuratDetail(
    val nomor: Int,
    val nama: String,
    val namaLatin: String,
    val jumlahAyat: Int,
    val tempatTurun: String,
    val arti: String,
    val ayat: List<Ayat>
)
