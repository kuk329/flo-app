package com.example.flo

import androidx.room.*


@Dao
interface AlbumDao {
    @Insert
    fun insert(album: Album)

    @Update
    fun update(album: Album)

    @Delete
    fun delete(album: Album)

    @Query("SELECT * FROM AlbumTable") // 테이블의 모든 값을 가져와라
    fun getAlbums(): List<Album>

    @Query("SELECT * FROM AlbumTable WHERE id = :id")
    fun getAlbum(id: Int): Album

    @Insert
    fun likeAlbum(like:Like) // 좋아요 처리

    @Query("SELECT id From LikeTable WHERE userId= :userId AND albumId=:albumId")
    fun isLikeAlbum(userId:Int , albumId:Int):Int? // 좋아요를 한 엘범인지

    @Query("DELETE FROM LikeTable WHERE userId=:userId AND albumId=:albumId")
    fun disLikeAlbum(userId:Int , albumId:Int) // 싫어요 처리


    @Query("SELECT AT.*FROM LikeTable as LT LEFT JOIN AlbumTable as AT ON LT.albumId = AT.id WHERE LT.userId=:userId")
    fun getLikedAlbums(userId:Int):List<Album> // 좋아요한 엘범 전부 가져옴

}