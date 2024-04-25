package com.dicoding.asclepius.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.tensorflow.lite.schema.BuiltinOperator.SELECT

@Dao
interface ResultDao {
    @Insert
    fun insert(result: ResultEntity)

    @Query("SELECT * FROM CancerResult")
    fun getAllResultLive(): LiveData<List<ResultEntity>>
}