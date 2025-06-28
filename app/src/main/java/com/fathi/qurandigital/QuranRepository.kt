package com.fathi.qurandigital


import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuranRepository @Inject constructor( // DIPERBAIKI: Import added
    private val apiService: QuranApiService
) {
    suspend fun getSuratList(): kotlin.Result<List<Surat>> { // DIPERBAIKI: kotlin.Result
        return try {
            val response = apiService.getSuratList()
            if (response.isSuccessful && response.body() != null) { // DIPERBAIKI: Import added
                val suratList = response.body()!!.data.map { it.toDomainModel() } // DIPERBAIKI: Import added
                kotlin.Result.success(suratList)
            } else {
                kotlin.Result.failure(Exception("Failed to load surat list"))
            }
        } catch (e: Exception) {
            kotlin.Result.failure(e)
        }
    }

    suspend fun getSuratDetail(id: Int): kotlin.Result<SuratDetail> { // DIPERBAIKI: kotlin.Result
        return try {
            val response = apiService.getSuratDetail(id)
            if (response.isSuccessful && response.body() != null) { // DIPERBAIKI: Import added
                val suratDetail = response.body()!!.data.toDomainModel() // DIPERBAIKI: Import added
                kotlin.Result.success(suratDetail)
            } else {
                kotlin.Result.failure(Exception("Failed to load surat detail"))
            }
        } catch (e: Exception) {
            kotlin.Result.failure(e)
        }
    }
}

// Extension functions untuk mapping - DIPERBAIKI
fun SuratApiModel.toDomainModel(): Surat {
    return Surat(
        nomor = nomor,
        nama = nama,
        namaLatin = namaLatin,
        jumlahAyat = jumlahAyat,
        tempatTurun = tempatTurun,
        arti = arti
    )
}

fun SuratDetailApiModel.toDomainModel(): SuratDetail {
    return SuratDetail(
        nomor = nomor,
        nama = nama,
        namaLatin = namaLatin,
        jumlahAyat = jumlahAyat,
        tempatTurun = tempatTurun,
        arti = arti,
        ayat = ayat.map { it.toDomainModel() } // DIPERBAIKI: it reference
    )
}

fun AyatApiModel.toDomainModel(): Ayat {
    return Ayat(
        nomorAyat = nomorAyat,
        teksArab = teksArab,
        teksLatin = teksLatin,
        teksIndonesia = teksIndonesia
    )
}