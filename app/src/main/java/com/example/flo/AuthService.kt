package com.example.flo

import android.util.Log
import android.view.View
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthService {
    private lateinit var loginView:LoginView
    private lateinit var signUpView: SignUpView
    private val retrofit = Retrofit.Builder()
            .baseUrl("http://13.125.121.202")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun setLoginView(loginView: LoginView){
        this.loginView = loginView
    }


    fun setSignUpView(signUpView:SignUpView){
        this.signUpView = signUpView
    }

    fun signUp(user:User){
        val authService = retrofit.create(AuthRetrofitInterface::class.java)

        signUpView.onSignUpLoading()

        authService.signUp(user).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) { // 제대로 응답을 받았을때
                Log.d("test",response.toString())

                val resp = response.body()!!

                Log.d("test-success",resp.toString())
                Log.d("test-success",resp.message.toString())

                when(resp.code){
                    1000 -> signUpView.onSignUpSuccess()
                    else-> signUpView.onSignUpFailure(resp.code, resp.message)

                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) { // 네트워크 응답 실패시
                Log.d("test",t.message.toString())
                signUpView.onSignUpFailure(400,"네트워크 오류가 발생했습니다.")
            }
        })
    }// end of signUp()


    fun login(user:User){
        val authService = retrofit.create(AuthRetrofitInterface::class.java)

        loginView.onLoginLoading()

        authService.login(user).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) { // 제대로 응답을 받았을때
                Log.d("login",response.toString())

                val resp = response.body()!!

                Log.d("login-success",resp.toString())

                when(resp.code){
                    1000 -> loginView.onLoginSuccess(resp.result!!)
                    else-> loginView.onLoginFailure(resp.code, resp.message)

                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) { // 네트워크 응답 실패시
                Log.d("login",t.message.toString())
                loginView.onLoginFailure(400,"네트워크 오류가 발생했습니다.")
            }
        })
    }// end of signUp()




}// end of class