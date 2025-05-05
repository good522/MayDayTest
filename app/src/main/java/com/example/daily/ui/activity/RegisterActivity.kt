package com.example.daily.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.daily.R

class RegisterActivity : AppCompatActivity() {
    private lateinit var mEtUserName : EditText
    private lateinit var mEtPassword : EditText
    private lateinit var mBtnRegister : Button
    private lateinit var mBtnRegisterBack : Button
    private lateinit var mBtnBack : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mEtUserName = findViewById(R.id.et_register_username)
        mEtPassword = findViewById(R.id.et_register_password)
        mBtnRegister = findViewById(R.id.button_conserve)
        mBtnBack = findViewById(R.id.button_back)
        mBtnRegisterBack = findViewById(R.id.button_register_back)
        initClick()
    }

    private fun initClick() {
        mBtnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        mBtnRegisterBack.setOnClickListener {
            finish()
        }
        mBtnRegister.setOnClickListener {
            val userName = mEtUserName.editableText.toString()
            val password = mEtPassword.editableText.toString()
            if (userName.isNotEmpty() && password.isNotEmpty()) {
                val prefs = getSharedPreferences("user", MODE_PRIVATE).edit()
                prefs.putString("userName", userName)
                prefs.putString("password", password)
                prefs.apply()
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }else {
                Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show()
            }
        }
    }
}