package com.fathi.qurandigital


import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuranRepository @Inject constructor(
    private val apiService: QuranApiService
) {
    suspend fun getSuratList(): Result<List<Surat>> {
        return try {
            val response = apiService.getSuratList()
            if (response.isSuccessful && response.body() != null) {
                val suratList = response.body()!!.data.map { it.toDomainModel() }
                Result.success(suratList)
            } else {
                Result.failure(Exception("Failed to load surat list"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSuratDetail(id: Int): Result<SuratDetail> {
        return try {
            val response = apiService.getSuratDetail(id)
            if (response.isSuccessful && response.body() != null) {
                val suratDetail = response.body()!!.data.toDomainModel()
                Result.success(suratDetail)
            } else {
                Result.failure(Exception("Failed to load surat detail"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

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
        ayat = ayat.map { it.toDomainModel() }
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