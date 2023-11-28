package com.example.retromarket

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.retromarket.databinding.ActivityModifyBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ModifyActivity : AppCompatActivity(){
    private lateinit var modifyBinding: ActivityModifyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        modifyBinding = ActivityModifyBinding.inflate(layoutInflater)
        val modifyView = modifyBinding.root
        setContentView(modifyView)
        //--------------------바인딩 설정 ----------------------

        //------------------------인텐트로 데이터 받아오기
        val postId = intent.getStringExtra("post_id").toString()
        val postTitle = intent.getStringExtra("post_title")
        val postContent = intent.getStringExtra("post_content")
        val postPrice = intent.getStringExtra("post_price")
        var postOnSale = intent.getBooleanExtra("post_onSale", false)
        val postName = intent.getStringExtra("post_name")
        val postUserId = intent.getStringExtra("post_UID")
        //-------------------------인텐트로 데이터 받아옴.

        //----------------------받은 데이터 보여주기 -------------------
        modifyBinding.editPostLongText.setText(postContent)
        modifyBinding.editPriceText.setText(postPrice)
        modifyBinding.editPostTitleText.setText(postTitle)
        modifyBinding.onSaleSwitch.isChecked = postOnSale

        modifyBinding.onSaleSwitch.setOnCheckedChangeListener { _, isChecked->
            postOnSale = isChecked
            println("{$postOnSale}")
        }
//--------------------------수정하기 버튼----------------------

        modifyBinding.completePostBtn.setOnClickListener {
            //---------- 위젯에 있는 값들을 가져온다.
            val user = Firebase.auth.currentUser //현재 로그인 된 유저 객체
            val userId: String = user?.uid ?: "Admin"
            lateinit var userName :String

            val db : FirebaseFirestore = Firebase.firestore //DB에 대한 레퍼런스

            db.collection("users").document(userId).get().addOnSuccessListener {document->
                userName = document["name"].toString() //현재 유저 이름을 가져옴.

                //Listener
                val title :String = modifyBinding.editPostTitleText.text.toString()

                val content :String = modifyBinding.editPostLongText.text.toString()
                val priceString :String = modifyBinding.editPriceText.text.toString()
                val price =  priceString.toIntOrNull()?:-1 // NumberFormatException 방지
//--------------위젯에 있는 값들을 가져온다
                modify(postId,title, content, userName, price, postOnSale)
            }.addOnFailureListener {
                Toast.makeText(this,"수정하기에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }

        }//글쓰기버튼






        //----------------------수정하기 버튼 -----------------------------



        //--------------뒤로가기---------------------
        modifyBinding.backToMainBtn.setOnClickListener {
            //startActivity(Intent(this,MainActivity::class.java))
            finish()
        }


    }//onCreateEnd

    private fun modify(postId: String,title : String, content : String, userName :String, price : Int, onSale:Boolean){ //글을 작성한다.

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
            postsCollectionRef.document(postId).set(
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
                    Toast.makeText(this, "판매글 수정 완료!", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "판매글 수정 오류!", Toast.LENGTH_SHORT).show()
                }
            startActivity(Intent(this, MainActivity::class.java)) //메인엑티비의 메인화면을 실행
            finish()
        }//else
    }//modify
}//classEnd

