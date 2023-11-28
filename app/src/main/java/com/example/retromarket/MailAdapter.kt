package com.example.retromarket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.retromarket.databinding.ActivityMainBinding
import com.example.retromarket.databinding.ItemMailBinding
import com.example.retromarket.databinding.ItemPostBinding
import com.google.firebase.firestore.QueryDocumentSnapshot


class MailAdapter(private var mails: List<Mail>) : RecyclerView.Adapter<MailAdapter.MailViewHolder>() {

     var onItemClick: ((Mail) -> Unit)? = null


   inner class MailViewHolder(private val binding: ItemMailBinding) : RecyclerView.ViewHolder(binding.root){ //뷰를 홀딩중임.
        //뷰홀더를 만드는 것은 뷰를 만드는 것과 동일
       init {
           binding.root.setOnClickListener{
               onItemClick?.invoke(mails[adapterPosition])
           }
       }

        fun bind(mail: Mail){
            binding.contentMailText.text = mail.content//제목 받아옴
            binding.senderNameText.text = mail.sender
        }//bind_end

    }

    fun updateList(newList: List<Mail>){
        mails = newList // 아이템을 갱신, 리스트를 새로 교체해줌.
        notifyDataSetChanged() //리싸이클러뷰에 데이터가 바뀜을 알려줌. onBindViewHolder를 호출

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MailViewHolder {
        val binding = ItemMailBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MailViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mails.size //posts 안에 들어있는 것만큼 그려주면 됨.
    }

    override fun onBindViewHolder(holder: MailViewHolder, position: Int) { //스크롤 시 호출
        //만들어진 뷰 홀더에정보를 바인딩함. postiton과 holder.adapterPosition은 동일한 값.
        holder.bind(mails[position])
    }
}