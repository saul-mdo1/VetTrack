package com.example.vettrack.models

import com.example.vettrack.R

enum class Genre(val id: Int, stringId: Int) {
    OTHER(0, R.string.txt_unknown),
    MALE(1, R.string.txt_male),
    FEMALE(2, R.string.txt_female);
}