package com.example.retromarket

import android.app.DownloadManager.Query
import com.google.firebase.firestore.QueryDocumentSnapshot


data class Post(val id :String, val title:String, val content: String, val name :String,val price:Int, var onSale: Boolean, val userId:String){
//id -> Firestore DB에 있는 posts 콜렉션 밑에 있는 id를 뜻함.
    constructor(doc : QueryDocumentSnapshot) :
        this(doc.id, doc["title"].toString(),doc["content"].toString(),doc["name"].toString(),doc["price"].toString().toIntOrNull()?:0,
            doc.getBoolean("onSale")?:false, doc["UID"].toString()) //지정된 값이 없으면 , 기본으로 가져오는 값은 true임.

}

