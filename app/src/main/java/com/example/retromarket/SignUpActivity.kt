package com.example.retromarket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.retromarket.databinding.ActivitySignupBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
     private lateinit var signupBinding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signupBinding = ActivitySignupBinding.inflate(layoutInflater)
        val signUpView = signupBinding.root
        setContentView(signUpView)

        signupBinding.backToLoginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        signupBinding.signUpButton.setOnClickListener { //회원가입버튼
            val userEmail = signupBinding.editTextTextEmailAddress2.text.toString()
            val passWord = signupBinding.editTextTextPassword.text.toString()
            val userName = signupBinding.editTextText.text.toString()
            doSignUp(userEmail,passWord,userName)
        }//회원가입 버튼



        // 연도 데이터
        val years = (1900..2100).toList()
        val adapterYear = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        signupBinding.spinner.adapter = adapterYear


        // 월 데이터
        val months = (1..12).toList()
        val adapterMonth = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        signupBinding.spinner2.adapter = adapterMonth

        // 일 데이터
        val days = (1..31).toList()
        val adapterDay = ArrayAdapter(this, android.R.layout.simple_spinner_item, days)
        signupBinding.spinner3.adapter = adapterDay

    }//onCrete

    @SuppressLint("SuspiciousIndentation")
    private fun doSignUp(userEmail: String, password: String, userName : String){

        if(userEmail=="" || password==""||userName ==""){
            Toast.makeText(this, "빈칸을 모두 입력해주세요!", Toast.LENGTH_SHORT).show()
        }
        else {
            Firebase.auth.createUserWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) { //만약 회원가입에 성공하면
                        val db : FirebaseFirestore = Firebase.firestore //DB에 대한 레퍼런스
                        val usersCollectionRef = db.collection("users") //usersCollectionRef.document(ID) 를 하면 users collection 밑에 있는 document에
                        //대한 레퍼런스임.
                        val mailsCollectionRef = db.collection("mails")
                        val user = Firebase.auth.currentUser //현재 로그인 된 유저 객체
                        val userId: String = user?.uid ?: "Admin"
//                            //유저의 이름을 DB에 추가함.
                            usersCollectionRef.document(userId).set(mapOf("name" to userName, "UID" to userId))
                                .addOnSuccessListener {  }.addOnFailureListener{
                                    Toast.makeText(this,"유저 아이디 등록 실패!",Toast.LENGTH_SHORT).show()
                                }

                        //유저의 이름을 DB에 추가함
                        //---------------유저의 전용 쪽지함 생성한다.
                       val myMail =  mailsCollectionRef.document(userId).collection("myMail").document() //문서 생성
                        val mailData = mapOf(
                            "sender" to "ADMIN",
                            "content" to "회원가입을 환영합니다! RetroMarket을 잘 이용해주세요!"
                        )
                        myMail.set(mailData).addOnSuccessListener {
                            Toast.makeText(this, "환영합니다!", Toast.LENGTH_SHORT).show()

                        }.addOnFailureListener {
                            Toast.makeText(this, "메일함 생성 실패", Toast.LENGTH_SHORT).show()
                        }

                        //-----------------유저의 전용 쪽지함 생성
                        startActivity(Intent(this, MainActivity::class.java)) //메인엑티비의 메인화면을 실행
                        finish() //이 액티비티는 종료함.
                    }//ifEnd
                    else { //로그인 실패 시
                        Toast.makeText(this, "회원가입 오류발생! ", Toast.LENGTH_SHORT).show()
                    }
                }
        }//if
    }//doSignUp
}//ClassEnd