package com.example.retromarket

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.retromarket.databinding.ActivityItempostBinding
import com.example.retromarket.databinding.ActivityPostingBinding
import com.example.retromarket.databinding.ItemPostBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ItemPostActivity : AppCompatActivity() { // 글 클릭시 보이는 화면임.

    private lateinit var itemPostBinding: ActivityItempostBinding
    private val db: FirebaseFirestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemPostBinding = ActivityItempostBinding.inflate(layoutInflater)
        val itemPostView = itemPostBinding.root
        setContentView(itemPostView)
//-------------------------바인딩 설정--------------------
        //----------------인텐트로 받은 데이터들------------------
        val postId = intent.getStringExtra("post_id")
        val postTitle = intent.getStringExtra("post_title")
        val postContent = intent.getStringExtra("post_content")
        val postPrice = intent.getIntExtra("post_price", 0).toString()
        val postOnSale = intent.getBooleanExtra("post_onSale", false)
        val postName = intent.getStringExtra("post_name") //글 작성자 이름.
        val postUserId = intent.getStringExtra("post_UID")
//--------------------------------인텐트로 받은 데이터들




        //-----------------글 수정하기 버튼 ----------------------

        itemPostBinding.modifyBtn.setOnClickListener { //본인이 작성한 글만 수정할 수 있음.
            GlobalScope.launch(Dispatchers.Main) {
                val currentUserName = getCurrentUserName()
                if ( currentUserName== postName) { //글작성자 이름과, 현재 로그인한 계정 이름이 동일할경우
                    val intent = Intent(this@ItemPostActivity, ModifyActivity::class.java)
                    intent.putExtra("post_id", postId)
                    intent.putExtra("post_title", postTitle)
                    intent.putExtra("post_content", postContent)
                    intent.putExtra("post_price", postPrice)
                    intent.putExtra("post_onSale", postOnSale)
                    intent.putExtra("post_name", postName)
                    startActivity(intent)
                    //finish()
                }//ifEnd
                else { //아닐 경우 메시지 출력
                    Toast.makeText(this@ItemPostActivity, "본인이 작성한 글이 아닙니다!!", Toast.LENGTH_SHORT).show()
                }
            }//코루틴
        }//리스너 ---------------------------글 수정하기 -----------------------------


        //글 수정하기


        //---------------쪽지 보내기--------------------------

        itemPostBinding.sendMailBtn.setOnClickListener {
            val intent = Intent(this@ItemPostActivity, SendMailActivity::class.java)
            intent.putExtra("post_UID",postUserId) //글쓴 유저의 UID를 보낸다.
            startActivity(intent)
            //finish()
        }
        //------------------쪽지 보내기





        //-----------------------뒤로가기 버튼
        itemPostBinding.backToMainBtn2.setOnClickListener {
            startActivity(
                Intent(this,MainActivity::class.java))
            finish()
        }
//뒤로가기 버튼 --------------------------------



    //-------------------위젯들에 데이터들 삽입----------------------
        itemPostBinding.contentTitleText.text = postTitle
        itemPostBinding.ownerNameText.text = postName
        itemPostBinding.textViewContent.text = postContent
        itemPostBinding.postPriceText.text = postPrice+"원"
        if(postOnSale)
            itemPostBinding.ifOnSaleText.text = "판매 중"
        else
            itemPostBinding.ifOnSaleText.text = "판매 완료"
//--------------------위젯들에 데이터들 삽입 .-----------------




    }//onCreate End

    private suspend fun getCurrentUserName() :String{
        val user = Firebase.auth.currentUser
        val userId: String = user?.uid ?: "Admin"

        return try {
            val docSnapshot = db.collection("users").document(userId).get().await()
            docSnapshot["name"].toString()
        } catch (e: Exception) {
            "Admin"
        }
    } // 함수 끝

}