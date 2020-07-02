package com.example.freshauctionapp.model

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/* 데이터베이스 액세스를 위한 Dao 인터페이스 */
@Dao
interface FreshDao {

    @Insert
    fun insertFresh(freshData: List<FreshData>)

    @Insert
    fun insertSave(saveItem: SaveItem): Long

    @Query("SELECT * FROM SaveItem")
    fun loadSaveItems(): DataSource.Factory<Int, SaveItem>

    @Query("SELECT * FROM Fresh WHERE saveId = :saveId")
    fun loadFreshData(saveId: Long): DataSource.Factory<Int, FreshData>

    @Query("DELETE FROM SaveItem WHERE id = :saveId")
    fun deleteSaveData(saveId: Long)
}
