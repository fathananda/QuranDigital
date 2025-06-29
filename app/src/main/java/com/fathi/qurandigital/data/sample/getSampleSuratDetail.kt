package com.fathi.qurandigital.data.sample

import com.fathi.qurandigital.data.model.SuratDetail
import com.fathi.qurandigital.data.model.Ayat
import com.fathi.qurandigital.data.model.Surat

fun getSampleSuratDetail(surat: Surat): SuratDetail {
    val ayatList = when (surat.nomor) {
        1 -> listOf(
            Ayat(
                1,
                "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                "Bismillahir rahmanir rahiim",
                "Dengan nama Allah Yang Maha Pengasih, Maha Penyayang."
            ),
            Ayat(
                2,
                "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ",
                "Alhamdulillahi rabbil 'alamiin",
                "Segala puji bagi Allah, Tuhan seluruh alam."
            ),
            Ayat(
                3,
                "الرَّحْمَٰنِ الرَّحِيمِ",
                "Ar rahmaanir rahiim",
                "Yang Maha Pengasih, Maha Penyayang."
            ),
            Ayat(4, "مَالِكِ يَوْمِ الدِّينِ", "Maaliki yaumiddiin", "Pemilik hari pembalasan."),
            Ayat(
                5,
                "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
                "Iyyaaka na'budu wa iyyaaka nasta'iin",
                "Hanya kepada Engkaulah kami menyembah dan hanya kepada Engkaulah kami mohon pertolongan."
            ),
            Ayat(
                6,
                "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ",
                "Ihdinash shiraathal mustaqiim",
                "Tunjukilah kami jalan yang lurus."
            ),
            Ayat(
                7,
                "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ",
                "Shiraathal ladziina an'amta 'alaihim ghairil maghdhuubi 'alaihim wa ladh dhaallin",
                "(Yaitu) jalan orang-orang yang telah Engkau beri nikmat kepadanya; bukan (jalan) mereka yang dimurkai, dan bukan (pula jalan) mereka yang sesat."
            )
        )
        else -> listOf(
            Ayat(
                1,
                "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                "Bismillahir rahmanir rahiim",
                "Dengan nama Allah Yang Maha Pengasih, Maha Penyayang."
            ),
            Ayat(2, "Sample Arabic text", "Sample latin text", "Sample terjemahan Indonesia.")
        )
    }

    return SuratDetail(
        nomor = surat.nomor,
        nama = surat.nama,
        namaLatin = surat.namaLatin,
        jumlahAyat = surat.jumlahAyat,
        tempatTurun = surat.tempatTurun,
        arti = surat.arti,
        ayat = ayatList
    )
}