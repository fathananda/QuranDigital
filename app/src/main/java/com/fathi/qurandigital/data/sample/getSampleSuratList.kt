package com.fathi.qurandigital.data.sample

import com.fathi.qurandigital.data.model.Surat

fun getSampleSuratList(): List<Surat>{
    return listOf(
        Surat(1, "الفاتحة", "Al-Fatihah", 7, "Makkah", "Pembukaan"),
        Surat(2, "البقرة", "Al-Baqarah", 286, "Madinah", "Sapi Betina"),
        Surat(3, "آل عمران", "Ali 'Imran", 200, "Madinah", "Keluarga Imran"),
        Surat(4, "النساء", "An-Nisa'", 176, "Madinah", "Wanita"),
        Surat(5, "المائدة", "Al-Ma'idah", 120, "Madinah", "Hidangan")
    )
}