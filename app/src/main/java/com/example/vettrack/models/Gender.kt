package com.example.vettrack.models

import com.example.vettrack.R

enum class Gender(val id: Int, val stringId: Int) {
    UNKNOWN(0, R.string.txt_unknown),
    MALE(1, R.string.txt_male),
    FEMALE(2, R.string.txt_female);

    companion object {
        fun fromId(id: Int): Gender = entries.firstOrNull { it.id == id } ?: UNKNOWN
    }
}