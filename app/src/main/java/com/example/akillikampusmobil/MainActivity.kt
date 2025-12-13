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

                // 1. Kutucuklar Boş mu?
                if (email.isBlank() || password.isBlank()) {
                    Toast.makeText(this, "Lütfen e-posta ve şifre girin.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // 2. Rol Belirleme (Proje PDF Kuralı)
                val role: String
                if (email == "admin@akillikampus.edu.tr" && password == "123456") {
                    role = "Admin"
                } else {
                    role = "User"
                }

                Toast.makeText(this, "Giriş Başarılı! Yönlendiriliyorsunuz...", Toast.LENGTH_SHORT).show()

                // 3. KÖPRÜ (INTENT) - İşte eksik olan parça bu olabilir!
                val intent = android.content.Intent(this, HomeActivity::class.java)

                // Rol bilgisini diğer sayfaya postala
                intent.putExtra("USER_ROLE", role)

                // Sayfayı Başlat
                startActivity(intent)

                // Giriş ekranını kapat
                finish()

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













