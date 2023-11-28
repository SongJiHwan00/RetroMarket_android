package com.example.retromarket

import com.google.firebase.firestore.QueryDocumentSnapshot

data class Mail(val content :String, val sender :String){
    //id -> Firestore DB에 있는 posts 콜렉션 밑에 있는 id를 뜻함.
    constructor(doc : QueryDocumentSnapshot) :
            this(doc["content"].toString(),doc["sender"].toString()) //지정된 값이 없으면 , 기본으로 가져오는 값은 true임.

}