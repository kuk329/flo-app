package com.example.flo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemAlbumBinding

class AlbumRvAdapter(private val albumList:ArrayList<Album>): RecyclerView.Adapter<AlbumRvAdapter.ViewHolder>() {

    // 클릭 인터페이스 정의
    interface MyItemClickListener{
        fun onItemClick(album: Album)
    }
    // 리스너 객체를 전달받는 함수랑 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener : MyItemClickListener

    fun setMyItemClickListener(itemClickListener:MyItemClickListener){
        mItemClickListener = itemClickListener // 이 함수를 통해서 변수에 값 할당.
    }


    // 뷰홀더를 생성해줘야 할 때 호출되는 함수 -> 아이템 뷰 객체를 만들어서 뷰홀더에 던져줌.
    // 아이템 뷰를 만드는 처음 몇번만 호출됨.
    override fun onCreateViewHolder(viewgGroup: ViewGroup, viewType: Int): AlbumRvAdapter.ViewHolder {
        val binding: ItemAlbumBinding =  ItemAlbumBinding.inflate(LayoutInflater.from(viewgGroup.context),viewgGroup,false)

        return ViewHolder(binding)
    }

    // ViewHolder 에 data 를 바인딩 할때마다 호출되는 함수 -> 엄청나게 많이 호출
    override fun onBindViewHolder(holder: AlbumRvAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(albumList[position]) }

    }

    // data set 크기를 알려주는 함수 -> 리사이클러뷰가 마지막이 언제인지를 알게된다.
    override fun getItemCount(): Int = albumList.size

    // ViewHolder (ItemView를 재활용하기 위해 꽉 잡고있는 역할)
    inner class ViewHolder(val binding:ItemAlbumBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(album:Album){
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)
        }

    }


}