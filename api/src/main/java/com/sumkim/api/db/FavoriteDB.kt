package com.sumkim.api.db

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteQuery
import com.sumkim.api.RoomListConverters
import com.sumkim.api.response.Document

@Database(entities = [Document::class], exportSchema = false, version = 1)
@TypeConverters(RoomListConverters::class)
abstract class FavoriteDB: RoomDatabase() {
    abstract fun getFavoriteDao(): FavoriteDao

    companion object {
        private var instance: FavoriteDB? = null

        @Synchronized
        fun getDatabase(context: Context) : FavoriteDB {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    FavoriteDB::class.java,
                    "favorite_db"
                ).build()
            }
            return instance as FavoriteDB
        }
    }
}

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_table")
    fun getFavoriteList(): List<Document>

    @RawQuery
    fun getFavoriteQuerySortFilterList(query: SupportSQLiteQuery): List<Document>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(document: Document)

    @Delete
    suspend fun deleteFavorite(document: Document)

    @Query("SELECT * FROM favorite_table WHERE isbn = :isbn")
    fun getFavorite(isbn: String): List<Document>
}