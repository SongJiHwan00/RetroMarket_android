package com.example.retromarket

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.retromarket.databinding.ActivitySendmailBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SendMailActivity :AppCompatActivity() {
    private lateinit var sendMailBinding: ActivitySendmailBinding
    private val db: FirebaseFirestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sendMailBinding = ActivitySendmailBinding.inflate(layoutInflater)
        val sendMailView = sendMailBinding.root
        setContentView(sendMailView)

        val user = Firebase.auth.currentUser //현재 로그인 된 유저 객체



        val postUserId = intent.getStringExtra("post_UID").toString() //글쓴 유저의 userId를 가져옴.


        sendMailBinding.backToPostBtn.setOnClickListener {
            finish()
        }

        sendMailBinding.mailSendingBtn.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                val currentUserName = getCurrentUserName() //현재 유저 이름.
               val content = sendMailBinding.editPostLongText.text.toString()
               val mailsCollectionRef = db.collection("mails")
                if(content =="" || currentUserName==""){//둘다 비어있는 내용이라묜
                    Toast.makeText(this@SendMailActivity,"내용을 입력하세요!",Toast.LENGTH_SHORT).show()
                }
                else{//정확하게 입력이 되었을 때
                    val myMail =  mailsCollectionRef.document(postUserId).collection("myMail").document() //문서 생성
                    val mailData = hashMapOf(
                        "sender" to currentUserName,
                        "content" to content
                    )
                    myMail.set(mailData).addOnSuccessListener {
                        Toast.makeText(this@SendMailActivity, "메일 전송 완료!", Toast.LENGTH_SHORT).show()

                    }.addOnFailureListener {
                        Toast.makeText(this@SendMailActivity, "메일 전송 실패!", Toast.LENGTH_SHORT).show()
                    }
                }
            }//코루틴
            //여기서 메인 엑티비티를 시작할때, 액티비티 스택에 있는 기존의 액티비티 모두 제거하기.
            val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
        }

    }//onCreate

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