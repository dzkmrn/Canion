package com.dicoding.asclepius.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CancerResult")
data class ResultEntity (
    var result: String,
    var image: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}