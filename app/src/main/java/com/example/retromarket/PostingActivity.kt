package com.example.retromarket

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.retromarket.databinding.ActivityPostingBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PostingActivity :AppCompatActivity() {

    private lateinit var postingBinding : ActivityPostingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postingBinding = ActivityPostingBinding.inflate(layoutInflater)
        val postingView = postingBinding.root
        setContentView(postingView)

        postingBinding.backToMainBtn.setOnClickListener {//뒤로가기 버튼
            startActivity(Intent(this, MainActivity::class.java)) //메인엑티비의 메인화면을 실행
            finish() //이 액티비티는 종료함.
        }

        var onSale : Boolean = false //기본은 true로설정
        postingBinding.onSaleSwitch.setOnCheckedChangeListener { _, isChecked->
            onSale = isChecked
            println("{$onSale}")
        }

        postingBinding.completePostBtn.setOnClickListener {
            //---------- 위젯에 있는 값들을 가져온다.
            val user = Firebase.auth.currentUser //현재 로그인 된 유저 객체
            val userId: String = user?.uid ?: "Admin"
            lateinit var userName :String

            val db : FirebaseFirestore = Firebase.firestore //DB에 대한 레퍼런스

            db.collection("users").document(userId).get().addOnSuccessListener {document->
                userName = document["name"].toString() //현재 유저 이름을 가져옴.

                //Listener
                val title :String = postingBinding.editPostTitleText.text.toString()

                val content :String = postingBinding.editPostLongText.text.toString()
                val priceString :String = postingBinding.editPriceText.text.toString()
                val price =  priceString.toIntOrNull()?:-1 // NumberFormatException 방지
//--------------위젯에 있는 값들을 가져온다
                posting(title, content, userName, price, onSale)
            }.addOnFailureListener {
                Toast.makeText(this,"사용자 이름 인증오류", Toast.LENGTH_SHORT).show()
            }

        }//글쓰기버튼



    }//onCreate
    private fun posting(title : String, content : String, userName :String, price : Int, onSale:Boolean){ //글을 작성한다.

        if(title == "" || content ==""|| price < 0){//물건 값이 음수이거나, 제목 혹은 내용을 입력하지 않았다면
            Toast.makeText(this,"제목, 내용, 가격을 제대로 입력하세요!", Toast.LENGTH_SHORT).show()
        }
        else {
            val db: FirebaseFirestore = Firebase.firestore //DB에 대한 레퍼런스
            val postsCollectionRef = db.collection("posts") // posts에 대한 레퍼런스
            //대한 레퍼런스임.
            val user = Firebase.auth.currentUser //현재 로그인 된 유저 객체
            val userId: String = user?.uid ?: "Admin"
//
            postsCollectionRef.add(
                mapOf(
                    "content" to content,
                    "title" to title,
                    "price" to price,
                    "name" to userName,
                    "onSale" to onSale,
                    "UID" to userId
                )
            )
                .addOnSuccessListener {
                    Toast.makeText(this, "판매글 작성 완료!", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "판매글 작성 오류!", Toast.LENGTH_SHORT).show()
                }
            startActivity(Intent(this, MainActivity::class.java)) //메인엑티비의 메인화면을 실행
            finish()
        }//else
    }//posting
}