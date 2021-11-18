package com.example.flo

import androidx.room.*


@Dao
interface SongDao {
    @Insert
    fun insert(song: Song)

    @Update
    fun update(song: Song)

    @Delete
    fun delete(song: Song)

    @Query("SELECT * FROM SongTable") // 테이블의 모든 값을 가져와라
    fun getSongs(): List<Song>

    @Query("SELECT * FROM SongTable WHERE id = :id")
    fun getSong(id: Int): Song

    @Query("UPDATE SongTable SET isLike= :isLike WHERE id = :id") // 좋아요 업데이트
    fun updateIsLikeById(isLike :Boolean,id: Int)

    @Query("SELECT * FROM SongTable WHERE isLike = :isLike") // 좋아요를 한 노래만 가져오기
    fun getLikedSongs(isLike: Boolean): List<Song>

    @Query("SELECT * FROM SongTable WHERE albumIdx = :albumIdx") // 같은 엘범의 곡들만 가져오기
    fun getSongsInAlbum(albumIdx: Int): List<Song>
}