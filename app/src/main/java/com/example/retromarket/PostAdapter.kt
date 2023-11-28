package com.example.retromarket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.retromarket.databinding.ActivityMainBinding
import com.example.retromarket.databinding.ItemPostBinding

class PostAdapter(private var posts: List<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

     var onItemClick: ((Post) -> Unit)? = null

   inner class PostViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root){ //뷰를 홀딩중임.
        //뷰홀더를 만드는 것은 뷰를 만드는 것과 동일
       init {
           binding.root.setOnClickListener{
               onItemClick?.invoke(posts[adapterPosition])
           }
       }

        fun bind(post: Post){
            binding.postTitleTextView.text = post.title //제목 받아옴
            binding.priceTextView.text = post.price.toString() + "원" //가격 받아옴
            binding.userNameText.text = post.name
            if(post.onSale==false){ //만약 팔고있는 중이면
                binding.saleTextView.text = "판매 완료"
            }//if_end
            else{
                binding.saleTextView.text ="판매 중"
            }//else_end
        }//bind_end

    }

    fun updateList(newList: List<Post>){
        posts = newList // 아이템을 갱신, 리스트를 새로 교체해줌.
        notifyDataSetChanged() //리싸이클러뷰에 데이터가 바뀜을 알려줌. onBindViewHolder를 호출

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return posts.size //posts 안에 들어있는 것만큼 그려주면 됨.
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) { //스크롤 시 호출
        //만들어진 뷰 홀더에정보를 바인딩함. postiton과 holder.adapterPosition은 동일한 값.
        holder.bind(posts[position])
    }
}