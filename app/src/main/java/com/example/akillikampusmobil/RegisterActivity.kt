package com.example.akillikampusmobil

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // 1. Tasarımdaki Elemanları Tanımla
        val etName = findViewById<EditText>(R.id.etRegisterName)
        val etEmail = findViewById<EditText>(R.id.etRegisterEmail)
        val etPassword = findViewById<EditText>(R.id.etRegisterPassword)
        val etUnit = findViewById<EditText>(R.id.etRegisterUnit)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvBackToLogin = findViewById<TextView>(R.id.tvBackToLogin)

        // 2. "Giriş Ekranına Dön" Yazısına Tıklanınca
        tvBackToLogin.setOnClickListener {
            // Bu sayfayı kapat, zaten altta Giriş ekranı açık bekliyor
            finish()
        }

        // 3. "KAYIT OL" Butonuna Tıklanınca
        btnRegister.setOnClickListener {
            // Verileri al
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val unit = etUnit.text.toString()

            // Boşluk Kontrolü
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || unit.isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun!", Toast.LENGTH_SHORT).show()
            } else {
                // Başarılı Kayıt Simülasyonu
                Toast.makeText(this, "Kayıt Başarılı! Aramıza hoş geldin, $name", Toast.LENGTH_LONG).show()

                // Kayıt olduktan sonra giriş ekranına geri gönder
                finish()
            }
        }
    }
}