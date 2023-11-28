package com.example.retromarket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.retromarket.databinding.ActivityMailcheckBinding
import com.example.retromarket.databinding.ActivityMailviewBinding

class MailViewActivity: AppCompatActivity() {
    private lateinit var mailViewBinding: ActivityMailviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mailViewBinding = ActivityMailviewBinding.inflate(layoutInflater)
        val mailViewView = mailViewBinding.root
        setContentView(mailViewView)
//----------------------------------바인딩 설정 ------------------

        //--------------------인텐트로 데이터 받기_-------------------
        val mailContent = intent.getStringExtra("mail_content")
        val mailSender = intent.getStringExtra("mail_sender")

        mailViewBinding.mailContentTextView.text = mailContent
        mailViewBinding.mailSenderText.text = mailSender



//-----------------------------------뒤로가기
        mailViewBinding.imageButton.setOnClickListener {
            finish()
        }




    }//OnCreate
}