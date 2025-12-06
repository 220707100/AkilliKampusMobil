package com.example.akillikampusmobil

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity




class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

     //XML bileşenlerini bulup kullanmak için yazıyoruz
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email =etEmail.text.toString()
            val password = etPassword.text.toString()

            if(email.isBlank() || password.isBlank() ){
                Toast.makeText(this, "Lutfen alanlari doldurun", Toast.LENGTH_SHORT).show()}
            else {
                Toast.makeText(this, "Giris Basarili!", Toast.LENGTH_SHORT).show()}

            }

        }


    }





