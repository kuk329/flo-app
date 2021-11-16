package com.example.flo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemAlbumBinding
import com.example.flo.databinding.ItemAlbumSongsBinding

class SongListItemRvAdapter(private val songList:ArrayList<Song>):RecyclerView.Adapter<SongListItemRvAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListItemRvAdapter.ViewHolder {
        val binding: ItemAlbumSongsBinding = ItemAlbumSongsBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongListItemRvAdapter.ViewHolder, position: Int) {
        holder.bind(songList[position])


    }

    override fun getItemCount(): Int = songList.size

    inner class ViewHolder(val binding:ItemAlbumSongsBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(song:Song){
        //    binding.itemAlbumSongsOrder.text = song.order
            binding.itemAlbumSongsTitle.text = song.title
            binding.itemAlbumSongsSinger.text = song.singer
          //  val titleCheck:Boolean = song.titleSong
//            if(!titleCheck){
//                binding.itemAlbumSongsTitleCheck.visibility = View.GONE
//                //binding.itemAlbumSongsTitle.marginStart.
//            }

        }
    }
}