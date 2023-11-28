package com.example.retromarket

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retromarket.databinding.ActivityLoginBinding
import com.example.retromarket.databinding.ActivityMainBinding
import com.example.retromarket.databinding.ActivitySignupBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding : ActivityMainBinding
    private var adapter: PostAdapter? = null
    private val db: FirebaseFirestore = Firebase.firestore
    private val  postsCollectionRef = db.collection("posts")
    //  lateinit var loginBinding : ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
    //    loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val mainView = mainBinding.root
   //   val loginView = loginBinding.root
        setContentView(mainView)

        //로그인 안되어있을 때랑 로그인 되어있을 때 화면 구분하기


        mainBinding.signOutBtn.setOnClickListener { //로그아웃 버튼
            logOut() //로그아웃하고
            startActivity(Intent(this,LoginActivity::class.java)) //다시 로그인 액티비티로 돌아간다.
            finish()
        }//로그아웃 버튼
        mainBinding.makePostBtn.setOnClickListener {  //글쓰기 버튼 누르면
            startActivity(Intent(this,PostingActivity::class.java))
            finish() //-> 어차피 글쓰기 완료나 뒤로가기 누르면 이걸로 해야하나..? 흠. .
            //Toast.makeText(this,"forTest1",Toast.LENGTH_SHORT).show()
        }//글쓰기 버튼

        if(!isOnLogin()){ //만약 로그인 안되어있으면 로그인 액티비티로 넘어감.
            startActivity(
                Intent(this,LoginActivity::class.java))
            finish()
        }

        //--------------------------------옵션 메뉴 달기 스피너 설정
        val filterOption = arrayOf("전체 보기", "판매 중", "판매 완료")
        val filterAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,filterOption)
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinner = mainBinding.filterSpinner
        spinner.adapter = filterAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when(p2){
                    0->{
                        initializeMainViewAllPost() //어뎁터 장착
                        //println(0)

                    }//전체 보기 선택
                    1->{
                        initializeMainViewOnsale()
                        //println(1)

                    }//판매 중 선택
                    2->{
                        initializeMainViewDonesale()
                       //println(2)

                    }//판매 완료 선택
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //----
            }

        }
        //------------------------------------스피너 설정

        //----------------쪽지 메일 버튼
        mainBinding.mailCheckBtn.setOnClickListener {
            startActivity(Intent(this,MailCheckActivity::class.java))
            //finish()
        }

        //-----------------------------------------쪽지 메일 버튼



        initializeMainViewAllPost() //어뎁터 장착


        //유저 현재 이름 띄우기
        val user = Firebase.auth.currentUser //현재 로그인 된 유저 객체
        val userId: String = user?.uid ?: "Admin"

        val usersCollectionRef = db.collection("users") //

        var userName = "유저이름"

       db.collection("users").document(userId).get().addOnSuccessListener {
           userName = it["name"].toString() //유저 아이디를 받아서
           mainBinding.currentUserText.text = userName //좌측상단에 이름을 띄운다.
       }
    }

    private fun isOnLogin() : Boolean{
        return Firebase.auth.currentUser != null //사용자가 null이 아니면 true, null 이맞으면 false를 리턴함.
    }

    private fun logOut(){
        Firebase.auth.signOut()  //로그아웃 호출
    }

    private fun initializeMainViewAllPost(){//뷰를 초기화하는함수
        mainBinding.postList.layoutManager = LinearLayoutManager(this) //vertical 방식으로 배열

        adapter = PostAdapter(emptyList()) //일단 빈 리스트 넘겨줌.
        updatePosts()

        adapter?.onItemClick = { post->
            //클릭된 아이템에 대한 처리
            //  Toast.makeText(this, "Clicked {${post.name}}",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,ItemPostActivity::class.java)
            intent.putExtra("post_id",post.id)
            intent.putExtra("post_title",post.title)
            intent.putExtra("post_content",post.content)
            intent.putExtra("post_price",post.price)
            intent.putExtra("post_onSale",post.onSale)
            intent.putExtra("post_name",post.name)
            intent.putExtra("post_UID", post.userId)
            startActivity(intent)
            finish()
        }
        mainBinding.postList.adapter = adapter //리싸이클러뷰에 어댑터 장착
    }//initializeMainViewAllPost

    private fun initializeMainViewOnsale(){//뷰를 초기화하는함수
        mainBinding.postList.layoutManager = LinearLayoutManager(this) //vertical 방식으로 배열

        adapter = PostAdapter(emptyList()) //일단 빈 리스트 넘겨줌.
        updatePostsOnsale()

        adapter?.onItemClick = { post->
            //클릭된 아이템에 대한 처리
          //  Toast.makeText(this, "Clicked {${post.name}}",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,ItemPostActivity::class.java)
            intent.putExtra("poit_id",post.id)
            intent.putExtra("post_title",post.title)
            intent.putExtra("post_content",post.content)
            intent.putExtra("post_price",post.price)
            intent.putExtra("post_onSale",post.onSale)
            intent.putExtra("post_name",post.name)
            intent.putExtra("post_UID", post.userId)

              startActivity(intent)
             finish()
        }
        mainBinding.postList.adapter = adapter //리싸이클러뷰에 어댑터 장착
    }//initializeMainViewOnsale

    private fun initializeMainViewDonesale(){//뷰를 초기화하는함수
        mainBinding.postList.layoutManager = LinearLayoutManager(this) //vertical 방식으로 배열

        adapter = PostAdapter(emptyList()) //일단 빈 리스트 넘겨줌.
        updatePostsDoneSale()

        adapter?.onItemClick = { post->
            //클릭된 아이템에 대한 처리
            //  Toast.makeText(this, "Clicked {${post.name}}",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,ItemPostActivity::class.java)
            intent.putExtra("poit_id",post.id)
            intent.putExtra("post_title",post.title)
            intent.putExtra("post_content",post.content)
            intent.putExtra("post_price",post.price)
            intent.putExtra("post_onSale",post.onSale)
            intent.putExtra("post_name",post.name)
            intent.putExtra("post_UID", post.userId)

            startActivity(intent)
            finish()
        }
        mainBinding.postList.adapter = adapter //리싸이클러뷰에 어댑터 장착
    }//initializeMainViewDonesale

    private fun updatePosts(){ //upDateList
        postsCollectionRef.get().addOnSuccessListener{
            val posts = mutableListOf<Post>() //DB에서 posts를 받아서 저장 할 리스트
            for(doc in it){
                posts.add(Post(doc)) //쿼리 스냅샷으로 생성함.
            }    //forEnd
            adapter?.updateList(posts)
        }//ListenerEnd
    }//upDatePosts

    @SuppressLint("SuspiciousIndentation")
    private fun updatePostsOnsale(){ //upDateList
        postsCollectionRef.get().addOnSuccessListener{
            val posts = mutableListOf<Post>() //DB에서 posts를 받아서 저장 할 리스트
            for(doc in it){
                if(doc.getBoolean("onSale") == true) //파는것만 리스트에 추가
                posts.add(Post(doc)) //쿼리 스냅샷으로 생성함.
            }    //forEnd
            adapter?.updateList(posts)
        }//ListenerEnd
    }//upDatePosts

    @SuppressLint("SuspiciousIndentation")
    private fun updatePostsDoneSale(){ //upDateList
        postsCollectionRef.get().addOnSuccessListener{
            val posts = mutableListOf<Post>() //DB에서 posts를 받아서 저장 할 리스트
            for(doc in it){
                if(doc.getBoolean("onSale") == false) //안파는것만 리스트에 추가
                posts.add(Post(doc)) //쿼리 스냅샷으로 생성함.
            }    //forEnd
            adapter?.updateList(posts)
        }//ListenerEnd
    }//upDatePosts




}