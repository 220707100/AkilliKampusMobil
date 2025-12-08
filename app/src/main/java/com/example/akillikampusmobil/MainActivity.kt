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
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Lutfen alanlari doldurun", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Hata varsa kodu burada KES!
            }

            // 3. ROL MANTIĞI
            // Gerçek hayatta burayı veritabanından soracağız.
            // Şimdilik sadece "özel admin hesabı" Admin olsun, geri kalan herkes User olsun.
            val role: String

            if (email == "admin@akillikampus.edu.tr" && password == "123456") {
                // Özel yönetici girişi
                role = "Admin"
            } else {
                // Diğer herkes (Kayıt olanlar varsayılan User'dır )
                role = "User"
            }

            // 4. Sonuç ve Yönlendirme
            // İleride buraya "if (role == "Admin") AdminActivity'yi aç" diyeceğiz.


            Toast.makeText(this, "Giriş Başarılı! Rolünüz: $role", Toast.LENGTH_LONG).show()
        }
        // 1. Yazıları Tanımla
        val tvRegister = findViewById<TextView>(R.id.tvRegister)
        val tvForgotPassword = findViewById<TextView>(R.id.tvForgotPassword)

        // 2. Kayıt Ol'a Tıklanınca (PDF Madde: Kayıt Ol Ekranına Geçiş)
        tvRegister.setOnClickListener {
            // RegisterActivity'yi (Kayıt Ekranını) başlat
            val intent = android.content.Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // 3. Şifremi Unuttum'a Tıklanınca (PDF Madde: Bilgilendirme Simülasyonu)
        tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Şifre sıfırlama bağlantısı e-postanıza gönderildi.", Toast.LENGTH_LONG).show()
        }

    }
}













