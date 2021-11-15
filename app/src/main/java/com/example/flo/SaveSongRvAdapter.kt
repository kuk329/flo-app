package com.example.flo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemSaveSongBinding

class SaveSongRvAdapter(private val albumList : ArrayList<Album>):RecyclerView.Adapter<SaveSongRvAdapter.ViewHolder>() {

   // 외부 작업을 위한 클릭 인터페이스 정의
    interface  MyItemClickListener{
        fun onRemoveAlbum(position: Int)

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

    fun removeItem(position: Int){ // 아이템 삭제 함수
        albumList.removeAt(position)
        notifyDataSetChanged() //데이터 변경 확인

    }

    override fun onBindViewHolder(holder: SaveSongRvAdapter.ViewHolder, position: Int) {

        holder.bind(albumList[position])
        holder.binding.itemSaveSongMoreIv.setOnClickListener {
            mItemClickListener.onRemoveAlbum(position)
        }

    }


    override fun getItemCount(): Int = albumList.size

   inner class ViewHolder(val binding: ItemSaveSongBinding):RecyclerView.ViewHolder(binding.root) {

       // bind 함수 생성
       fun bind(album:Album){
           binding.itemSaveSongIv.setImageResource(album.coverImg!!)
           binding.itemSaveSongTitle.text = album.title
           binding.itemSaveSongSinger.text = album.singer

       }

    }


}// end of class