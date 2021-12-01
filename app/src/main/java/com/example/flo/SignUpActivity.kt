package com.example.flo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySignUpBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.sign

class SignUpActivity:AppCompatActivity(),SignUpView {

    lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("test","onCreate")

        binding.signUpBtn.setOnClickListener{
            signUp()
      //      finish()
        }

    }// end of onCreate

    private fun getUser():User{
        val email :String = binding.signUpIdEt.text.toString()+"@"+binding.signUpEmailEt.text.toString()
        val password : String = binding.signUpPasswordEt.text.toString()
        val name : String = binding.signUpNameEt.text.toString()

        Log.d("test",email+ password+name)

        return User(email,password,name)
    }

//    private fun signUp(){
//        if(binding.signUpIdEt.text.toString().isEmpty() || binding.signUpPasswordEt.text.toString().isEmpty()){
//            Toast.makeText(this,"이메일 형식이 잘못되었습니다.",Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        if(binding.signUpPasswordEt.text.toString() != binding.signUpPasswordCheckEt.text.toString()){
//            Toast.makeText(this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val userDB = SongDatabase.getInstance(this)!!
//        userDB.userDao().insert(getUser()) // 사용자 정보 db에 추가
//        Toast.makeText(this,"회원가입이 완료되었습니다.",Toast.LENGTH_SHORT).show()
//
//
//        val users = userDB.userDao().getUsers()
//        Log.d("signup ",users.toString())
//    }

    private fun signUp(){
        Log.d("test","signUp")

        if(binding.signUpIdEt.text.toString().isEmpty() || binding.signUpPasswordEt.text.toString().isEmpty()){
            Toast.makeText(this,"이메일 형식이 잘못되었습니다.",Toast.LENGTH_SHORT).show()
            return
        }

        if(binding.signUpPasswordEt.text.toString().isEmpty()){
            Toast.makeText(this,"이름을 입력해 주세요",Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.signUpPasswordEt.text.toString() != binding.signUpPasswordCheckEt.text.toString()){
            Toast.makeText(this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
            return
        }

//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://13.125.121.202")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()

      //  val signUpService : SignUpService = retrofit.create(SignUpService::class.java)

        val authService = AuthService()
        authService.setSignUpView(this)

        authService.signUp(getUser())


        Log.d("test","Hello")

     //   finish()

    }// end of signUp()

    override fun onSignUpLoading() {
        binding.signUpLoadingPb.visibility = View.VISIBLE

    }

    override fun onSignUpSuccess() {
        binding.signUpLoadingPb.visibility = View.GONE

        finish()

    }

    override fun onSignUpFailure(code: Int, message: String) {
        binding.signUpLoadingPb.visibility = View.GONE

        when(code){
            2016,2017 ->{
                binding.signUpEmailEt.visibility = View.VISIBLE
                binding.signUpErrorTv.text = message
            }
        }

    }
}