package com.example.retromarket

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.retromarket.databinding.ActivityLoginBinding
import com.example.retromarket.databinding.ActivitySignupBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding : ActivityLoginBinding
    lateinit var signUpBinding : ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val loginView =loginBinding.root

        setContentView(loginView) //초기화면은 로그인 화면으로 띄움.



        loginBinding.signInBtn.setOnClickListener{ //로그인 기능 구현
            val email = loginBinding.editTextTextEmailAddress.text.toString()
            val password = loginBinding.editTextTextPassword2.text.toString()
            doLogin(email,password)
        }

        loginBinding.signUpBtn.setOnClickListener{ //회원가입 -> 회원가입 창으로 이동함.
            startActivity(Intent(this,SignUpActivity::class.java))
         //   finish()
        }


    }

    private fun doLogin(userEmail :String, password: String) {
        if (userEmail == "" || password == "") {
            Toast.makeText(this, "이메일과 비밀번호를 입력해주세요!", Toast.LENGTH_SHORT).show()
        } else {
            Firebase.auth.signInWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) { //만약 로그인성공하면
                        startActivity(Intent(this, MainActivity::class.java)) //메인엑티비의 메인화면을 실행
                        finish() //이 액티비티는 종료함.
                    }//ifEnd
                    else { //로그인 실패 시
                        Toast.makeText(this, "Email or Password Error!!", Toast.LENGTH_SHORT).show()
                    }
                }//addOnCompleteListener
        }
    }//doLogin
}