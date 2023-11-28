package com.example.retromarket

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retromarket.databinding.ActivityMailcheckBinding
import com.example.retromarket.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MailCheckActivity : AppCompatActivity() {
    private lateinit var mailCheckBinding : ActivityMailcheckBinding
    private var adapter: MailAdapter? = null
    private val db: FirebaseFirestore = Firebase.firestore
    private val  mailsCollectionRef = db.collection("mails")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mailCheckBinding = ActivityMailcheckBinding.inflate(layoutInflater)
        val mailCheckView = mailCheckBinding.root
        setContentView(mailCheckView)
        //----------------바인딩 설정

        val user = Firebase.auth.currentUser //현재 로그인 된 유저 객체
        val userId: String = user?.uid ?: "Admin"


        mailCheckBinding.backToMainBtn3.setOnClickListener {
            finish()
        }

        initializeMailCheckView()



    }//onCreate
    private fun initializeMailCheckView(){//뷰를 초기화하는함수
        mailCheckBinding.mailList.layoutManager = LinearLayoutManager(this) //vertical 방식으로 배열

        adapter = MailAdapter(emptyList()) //일단 빈 리스트 넘겨줌.
        updateMails()

        adapter?.onItemClick = { mail->
            //클릭된 아이템에 대한 처리
            //
            val intent = Intent(this,MailViewActivity::class.java)
            intent.putExtra("mail_content",mail.content)
            intent.putExtra("mail_sender",mail.sender)

            startActivity(intent)
        }
        mailCheckBinding.mailList.adapter = adapter //리싸이클러뷰에 어댑터 장착
    }//initializeMailCheckView

    private fun updateMails(){ //upDateList
        val user = Firebase.auth.currentUser //현재 로그인 된 유저 객체
        val userId: String = user?.uid ?: "Admin"
        println(userId)
        mailsCollectionRef.document(userId).collection("myMail").get().addOnSuccessListener{
            val mails = mutableListOf<Mail>() //DB에서 posts를 받아서 저장 할 리스트
            for(doc in it){
                mails.add(Mail(doc)) //쿼리 스냅샷으로 생성함.
                println(Mail(doc).content)
                println(Mail(doc).sender)
            }    //forEnd
            adapter?.updateList(mails)
        }//ListenerEnd
    }//upDatePosts

}//ClassEnd