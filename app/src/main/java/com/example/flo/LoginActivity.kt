package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityLoginBinding
import com.example.flo.databinding.ActivitySignUpBinding

class LoginActivity:AppCompatActivity(),LoginView {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignUpTv.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java)) // 회원가입 페이지로 이동
        }

        binding.loginLoginBtn.setOnClickListener{
            login()
        }
        binding.loginCloseIv.setOnClickListener {
            finish()
        }

    }
//    private fun login(){
//        if(binding.loginIdEt.text.toString().isEmpty() || binding.loginEmailEt.text.toString().isEmpty()){
//            Toast.makeText(this,"이메일을 입력해 주세요", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        if(binding.loginPasswordEt.text.toString().isEmpty()){
//            Toast.makeText(this,"비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show()
//            return
//        }
//        val email :String = binding.loginIdEt.text.toString()+"@"+binding.loginEmailEt.text.toString()
//        val password : String = binding.loginPasswordEt.text.toString()
//
//        val songDB = SongDatabase.getInstance(this)!!
//
//        val user = songDB.userDao().getUser(email,password)
//
//        user?.let{ // user가 null 이 아닐때 실행
//            Log.d("LoginActivity ","userId : ${user.id}, $user ")
//            // 발급받은 jwt를 저장해주는 함수 (여기서는 임시로 user.id 사용)
//            saveJwt(user.id) // 서버에서 받은 jwt 저장
//            startMainActivity()
//            return
//        }
//
//        Toast.makeText(this,"회원 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
//
//
//    }// end of login()


    private fun login(){
        if(binding.loginIdEt.text.toString().isEmpty() || binding.loginEmailEt.text.toString().isEmpty()){
            Toast.makeText(this,"이메일을 입력해 주세요", Toast.LENGTH_SHORT).show()
            return
        }

        if(binding.loginPasswordEt.text.toString().isEmpty()){
            Toast.makeText(this,"비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        val email :String = binding.loginIdEt.text.toString()+"@"+binding.loginEmailEt.text.toString()
        val password : String = binding.loginPasswordEt.text.toString()

        val user = User(email,password,"")

        val authService = AuthService()
        authService.setLoginView(this)

        authService.login(user)


    }// end of login()


    private fun startMainActivity(){
        val intent = Intent(this,MainActivity::class.java)
        intent.flags  = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
    }

    private fun saveJwt(jwt:String){
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()
        editor.putString("jwt",jwt)
        editor.apply()
    }

    private fun saveUserIdx(jwt:Int){
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()
        editor.putInt("jwt",jwt)
        editor.apply()
    }
    override fun onLoginLoading() {
        binding.loginLoadingPb.visibility = View.VISIBLE

    }

    override fun onLoginSuccess(auth:Auth) {
        binding.loginLoadingPb.visibility = View.GONE

        saveJwt(auth.jwt)
        saveUserIdx(auth.userIdx) // Room DB 충돌 방지

        startMainActivity()
    }

    override fun onLoginFailure(code:Int, message:String) {
        binding.loginLoadingPb.visibility = View.GONE

        when(code){
            2015,2019,3014->{
                binding.loginErrorTv.visibility = View.VISIBLE
                binding.loginErrorTv.text = message
            }
        }
    }

}