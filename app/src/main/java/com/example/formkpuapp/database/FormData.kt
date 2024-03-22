package com.example.formkpuapp.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class FormData (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "nik")
    var nik: String? = null,

    @ColumnInfo(name = "nama_panjang")
    var nama: String? = null,

    @ColumnInfo(name = "nomor_handphone")
    var noHand: String? = null,

    @ColumnInfo(name = "jenis_kelamin")
    var gender: String? = null,

    @ColumnInfo(name = "tanggal_pendataan")
    var tanggal: String? = null,

    @ColumnInfo(name = "lokasi")
    var lokasi: String? = null,

    @ColumnInfo(name = "latitude")
    var latitude: Double? = null,

    @ColumnInfo(name = "longitude")
    var longitude: Double? = null,

    @ColumnInfo(name = "gambar")
    var gambar: ByteArray? =null,

    @ColumnInfo(name = "tanggal_ubah")
    var date: String? =null,
): Parcelable