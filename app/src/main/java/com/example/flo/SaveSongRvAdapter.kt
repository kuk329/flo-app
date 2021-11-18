package com.example.flo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemSaveSongBinding

class SaveSongRvAdapter():RecyclerView.Adapter<SaveSongRvAdapter.ViewHolder>() {

    private val songs = ArrayList<Song>()

   // 외부 작업을 위한 클릭 인터페이스 정의
    interface  MyItemClickListener{
        fun onRemoveSong(position: Int)

    }

    // 리스너 객체를 전달받는 함ㅅ랑 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveSongRvAdapter.ViewHolder {
        val binding : ItemSaveSongBinding = ItemSaveSongBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: SaveSongRvAdapter.ViewHolder, position: Int) {

        holder.bind(songs[position])
        holder.binding.itemSaveSongMoreIv.setOnClickListener {
            mItemClickListener.onRemoveSong(songs[position].id)
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
           binding.itemSaveSongIv.setImageResource(song.coverImg!!)
           binding.itemSaveSongTitle.text = song.title
           binding.itemSaveSongSinger.text = song.singer

       }

    }


}// end of class