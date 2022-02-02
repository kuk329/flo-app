package com.example.flo

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flo.databinding.ItemSaveSongBinding

class SongRvAdapter(val context:Context):RecyclerView.Adapter<SongRvAdapter.ViewHolder>() {

    private val songs = ArrayList<Song>()

    // 외부 작업을 위한 클릭 인터페이스 정의
    interface  MyItemClickListener{
        fun onRemoveSong(songId: Int)

    }

    // 리스너 객체를 전달받는 함ㅅ랑 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener: SaveSongRvAdapter.MyItemClickListener

    fun setMyItemClickListener(itemClickListener: SaveSongRvAdapter.MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongRvAdapter.ViewHolder {
        val binding : ItemSaveSongBinding = ItemSaveSongBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: SongRvAdapter.ViewHolder, position: Int) {

        holder.bind(songs[position])
        holder.binding.itemSaveSongMoreIv.setOnClickListener {
            mItemClickListener.onRemoveSong(songs[position].id)
            removeSong(position)
        }

    }

    override fun getItemCount(): Int = songs.size

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs:ArrayList<Song>){
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }



    fun removeSong(position: Int){ // 아이템 삭제 함수
        songs.removeAt(position)
        notifyDataSetChanged() //데이터 변경 확인

    }


    inner class ViewHolder(val binding: ItemSaveSongBinding):RecyclerView.ViewHolder(binding.root) {

        // bind 함수 생성
        fun bind(song:Song){
            if(song.coverImgUrl==""){ // 서버에서 받아오는 이미지가 없으면 더미데이터로 넣어줌
                //binding.itemSaveSongIv.setImageResource(song.coverImg!!)
                Glide.with(context).load(song.coverImg).into(binding.itemSaveSongIv)

            }else{
                Glide.with(context).load(song.coverImgUrl).into(binding.itemSaveSongIv)

            }

            binding.itemSaveSongTitle.text = song.title
            binding.itemSaveSongSinger.text = song.singer

        }

    }

}