package com.example.vettrack.models

import android.os.Parcel
import android.os.Parcelable

data class VisitModel(
    var id: String = "",
    val date: String = "",
    val reason: String = "",
    val petName: String = "",
    val dogWeight: String = "",
    val clinicName: String = "",
    val city: String? = "",
    val totalPaid: String = "",
    val observations: String? = "",
    val userId: String = "",
    val pending: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(date)
        parcel.writeString(reason)
        parcel.writeString(petName)
        parcel.writeString(dogWeight)
        parcel.writeString(clinicName)
        parcel.writeString(city)
        parcel.writeString(totalPaid)
        parcel.writeString(observations)
        parcel.writeString(userId)
        parcel.writeByte(if (pending) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VisitModel> {
        override fun createFromParcel(parcel: Parcel): VisitModel {
            return VisitModel(parcel)
        }

        override fun newArray(size: Int): Array<VisitModel?> {
            return arrayOfNulls(size)
        }
    }
}