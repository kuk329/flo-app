package com.example.flo

import android.annotation.SuppressLint
import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemSavedAlbumBinding

class AlbumLockerRvAdapter():RecyclerView.Adapter<AlbumLockerRvAdapter.ViewHolder>() {

    private val albums = ArrayList<Album>()

    // 외부 작업을 위한 클릭 인터페이스 정의
    interface MyItemClickListener{
        fun onRemoveSong(albumId:Int)
    }

    // 리스너 객체를 전달받는 함수와 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener :MyItemClickListener

    fun setMyItemClickListener(itemClickListener:MyItemClickListener){
        mItemClickListener = itemClickListener
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlbumLockerRvAdapter.ViewHolder {
        val binding : ItemSavedAlbumBinding = ItemSavedAlbumBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumLockerRvAdapter.ViewHolder, position: Int) {
        holder.bind(albums[position])
        holder.binding.itemSavedAlbumMoreIv.setOnClickListener {

        }
    }



    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(albums:ArrayList<Album>){
        this.albums.clear()
        this.albums.addAll(albums)

        notifyDataSetChanged()
    }



    fun removeSong(position: Int){ // 아이템 삭제 함수
        albums.removeAt(position)
        notifyDataSetChanged() //데이터 변경 확인

    }






    override fun getItemCount(): Int = albums.size

    inner class ViewHolder(val binding:ItemSavedAlbumBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(album:Album){
            binding.itemSavedAlbumIv.setImageResource(album.coverImg!!)
            binding.itemSavedAlbumTitleTv.text = album.title
            binding.itemSavedAlbumSinger.text = album.singer

        }

    }

}// end of class AlbumLockerRvAdapter