package com.example.akillikampusmobil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

// ---------------- MODELLER ----------------
enum class Rol { KULLANICI, YONETICI }
enum class BildirimDurumu { ACIK, ISLEMDE, COZULDU }
enum class BildirimTipi { PARK, AYDINLATMA, TEMIZLIK, GUVENLIK, DIGER }

data class UygulamaKullanicisi(val id: String, val adi: String, val rol: Rol)
data class Konum(val enlem: Double, val boylam: Double)

data class Rapor(
    val id: String = UUID.randomUUID().toString(),
    val tip: BildirimTipi,
    var baslik: String,
    var aciklama: String,
    val olusturulmaZamani: Long = System.currentTimeMillis(),
    var durum: BildirimDurumu = BildirimDurumu.ACIK,
    val yazarId: String,
    val konum: Konum? = null,
    val takipciler: MutableSet<String> = mutableSetOf()
)

// ---------------- REPOSITORY ----------------
class RaporlarDeposu {
    private val _raporlar = mutableStateListOf<Rapor>()
    val raporlar: List<Rapor> get() = _raporlar

    init {
        // Test Verileri
        _raporlar += Rapor(
            tip = BildirimTipi.AYDINLATMA,
            baslik = "Sokak lambası kırık",
            aciklama = "A2 binası önündeki sokak lambası yanmıyor.",
            yazarId = "kullanici1",
            durum = BildirimDurumu.ACIK,
            konum = Konum(41.015, 28.979)
        )
        _raporlar += Rapor(
            tip = BildirimTipi.GUVENLIK,
            baslik = "Şüpheli Paket",
            aciklama = "Kütüphane girişinde sahipsiz çanta var.",
            yazarId = "kullanici2",
            durum = BildirimDurumu.ISLEMDE
        )
        _raporlar += Rapor(
            tip = BildirimTipi.TEMIZLIK,
            baslik = "Çöp Konteyneri",
            aciklama = "Yemekhane arkasındaki çöpler taşmış.",
            yazarId = "kullanici3",
            durum = BildirimDurumu.COZULDU
        )
    }

    fun ekle(r: Rapor) { _raporlar += r }
    fun guncelle(r: Rapor) { _raporlar[_raporlar.indexOfFirst { it.id == r.id }] = r }
    fun idIleGetir(id: String) = _raporlar.find { it.id == id }
}

// ---------------- VIEWMODEL ----------------
class RaporlarViewModel(val depo: RaporlarDeposu, val gecerliKullanici: UygulamaKullanicisi) : ViewModel() {
    private val _raporlar = MutableStateFlow(depo.raporlar.toList())
    val raporlar: StateFlow<List<Rapor>> = _raporlar.asStateFlow()

    fun takibiDegistir(id: String) {
        val r = depo.idIleGetir(id) ?: return
        if (r.takipciler.contains(gecerliKullanici.id)) r.takipciler.remove(gecerliKullanici.id)
        else r.takipciler.add(gecerliKullanici.id)
        // Listeyi yenilemek için tetikle
        _raporlar.value = ArrayList(depo.raporlar)
    }
}

class RaporlarViewModelFactory(private val depo: RaporlarDeposu, private val k: UygulamaKullanicisi) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RaporlarViewModel(depo, k) as T
    }
}

// ---------------- ANA EKRAN (HOME ACTIVITY) ----------------
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val depo = RaporlarDeposu()

        // Rol Bilgisini Al
        val gelenRol = intent.getStringExtra("USER_ROLE") ?: "User"
        val rolEnum = if (gelenRol == "Admin") Rol.YONETICI else Rol.KULLANICI
        val kullanici = UygulamaKullanicisi("aktif_user_id", "Aktif Kullanıcı", rolEnum)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val vm: RaporlarViewModel = viewModel(factory = RaporlarViewModelFactory(depo, kullanici))
                    AnaEkran(vm)
                }
            }
        }
    }
}

// ---------------- UI (ARAYÜZ) ----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnaEkran(vm: RaporlarViewModel) {
    // Verileri Dinle
    val tumRaporlar = vm.raporlar.collectAsState().value

    // Arama ve Filtreleme Durumları (State)
    var aramaMetni by remember { mutableStateOf("") }
    var seciliFiltre by remember { mutableStateOf<BildirimTipi?>(null) } // Null ise "Hepsi" demek

    // Filtreleme Mantığı
    val filtrelenmisListe = tumRaporlar.filter { rapor ->
        // 1. Arama Metni Kontrolü
        val aramaUygun = rapor.baslik.contains(aramaMetni, ignoreCase = true) ||
                rapor.aciklama.contains(aramaMetni, ignoreCase = true)

        // 2. Kategori Filtresi Kontrolü
        val tipUygun = if (seciliFiltre == null) true else rapor.tip == seciliFiltre

        aramaUygun && tipUygun
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // --- ÜST BAŞLIK VE ARAMA ---
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Kampüs Bildirimleri",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Arama Çubuğu
            OutlinedTextField(
                value = aramaMetni,
                onValueChange = { aramaMetni = it },
                label = { Text("Bildirim Ara...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // --- YATAY FİLTRE BUTONLARI ---
        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = (seciliFiltre == null),
                    onClick = { seciliFiltre = null },
                    label = { Text("Tümü") }
                )
            }
            items(BildirimTipi.values()) { tip ->
                FilterChip(
                    selected = (seciliFiltre == tip),
                    onClick = { seciliFiltre = if (seciliFiltre == tip) null else tip },
                    label = { Text(tip.name) } // Tip ismini göster
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- LİSTE ---
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filtrelenmisListe) { rapor ->
                RaporSatiri(rapor, vm)
            }
        }
    }
}

// Tarih Formatlayıcı
fun zamaniFormatla(zaman: Long): String {
    val f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.systemDefault())
    return f.format(Instant.ofEpochMilli(zaman))
}

// İkon Seçici Fonksiyon
fun getIconForType(tip: BildirimTipi): ImageVector {
    return when (tip) {
        BildirimTipi.GUVENLIK -> Icons.Default.Warning
        BildirimTipi.AYDINLATMA -> Icons.Default.Info
        BildirimTipi.TEMIZLIK -> Icons.Default.Delete
        BildirimTipi.PARK -> Icons.Default.LocationOn
        else -> Icons.Default.Notifications
    }
}

// Renk Seçici Fonksiyon
fun getColorForStatus(durum: BildirimDurumu): Color {
    return when (durum) {
        BildirimDurumu.ACIK -> Color(0xFFB00020) // Kırmızı
        BildirimDurumu.ISLEMDE -> Color(0xFFF57C00) // Turuncu
        BildirimDurumu.COZULDU -> Color(0xFF388E3C) // Yeşil
    }
}

@Composable
fun RaporSatiri(rapor: Rapor, vm: RaporlarViewModel) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // 1. TÜR İKONU
                Icon(
                    imageVector = getIconForType(rapor.tip),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))

                // 2. BAŞLIK VE TARİH
                Column(modifier = Modifier.weight(1f)) {
                    Text(rapor.baslik, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text(zamaniFormatla(rapor.olusturulmaZamani), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }

                // 3. DURUM BİLGİSİ (Renkli)
                Surface(
                    color = getColorForStatus(rapor.durum).copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = rapor.durum.name,
                        color = getColorForStatus(rapor.durum),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            // AÇIKLAMA
            Text(rapor.aciklama, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(12.dp))

            // TAKİP BUTONU
            Button(
                onClick = { vm.takibiDegistir(rapor.id) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (rapor.takipciler.contains(vm.gecerliKullanici.id)) Color.Gray else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(if (rapor.takipciler.contains(vm.gecerliKullanici.id)) "Takipten Çık" else "Takip Et")
            }
        }
    }
}