package com.example.daily.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.daily.R

class LoginActivity : AppCompatActivity() {
    private lateinit var mEtUserName : EditText
    private lateinit var mEtPassword : EditText
    private lateinit var mBtnLogin : Button
    private lateinit var mBtnRegister : Button
    private lateinit var mBtnLoginBack : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mBtnLogin = findViewById(R.id.button_login)
        mBtnRegister = findViewById(R.id.button_register)
        mBtnLoginBack = findViewById(R.id.button_login_back)
        mEtUserName = findViewById(R.id.et_login_username)
        mEtPassword = findViewById(R.id.et_login_password)
        initClick()
    }

    private fun initClick() {
        mBtnLogin.setOnClickListener {
            val userName = mEtUserName.editableText.toString()
            val password = mEtPassword.editableText.toString()
            if (userName.isNotEmpty() && password.isNotEmpty()) {
                val prefs = getSharedPreferences("user", MODE_PRIVATE)
                val saveuserNsme = prefs.getString("userName", "")
                val savePassword = prefs.getString("password", "")
                if (userName == saveuserNsme && password == savePassword) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show()
                }
            }else {
                Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show()
            }
        }
        mBtnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        mBtnLoginBack.setOnClickListener {
            finish()
        }
    }
}

