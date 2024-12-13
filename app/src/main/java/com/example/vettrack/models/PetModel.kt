package com.example.vettrack.models

import android.os.Parcel
import android.os.Parcelable

data class PetModel(
    var id: String = "",
    val name: String = "",
    val gender: Int = 0,
    val species: String = "",
    val birthdate: String? = "",
    val breed: String? = "",
    val color: String = ""
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeInt(gender)
        parcel.writeString(species)
        parcel.writeString(birthdate)
        parcel.writeString(breed)
        parcel.writeString(color)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PetModel> {
        override fun createFromParcel(parcel: Parcel): PetModel {
            return PetModel(parcel)
        }

        override fun newArray(size: Int): Array<PetModel?> {
            return arrayOfNulls(size)
        }
    }
}