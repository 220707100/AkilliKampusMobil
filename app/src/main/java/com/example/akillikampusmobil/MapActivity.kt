package com.example.akillikampusmobil

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
// AŞAĞIDAKİ SATIRLAR ÇOK ÖNEMLİ (Kütüphaneyi çağıran kısımlar)
import org.maplibre.android.MapLibre
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.OnMapReadyCallback
import org.maplibre.android.maps.Style

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var mapLibreMap: MapLibreMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. MapLibre Başlat
        MapLibre.getInstance(this)

        setContentView(R.layout.activity_map)

        // XML'deki haritayı bul
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(map: MapLibreMap) {
        mapLibreMap = map

        // 2. Harita Stilini Yükle
        mapLibreMap.setStyle("https://demotiles.maplibre.org/style.json") { style ->

            // 3. Konum Ayarla (Ankara)
            val campusLocation = LatLng(39.9208, 32.8541)
            val position = CameraPosition.Builder()
                .target(campusLocation)
                .zoom(14.0)
                .build()
            mapLibreMap.cameraPosition = position

            // 4. Pinleri Ekle
            addSampleMarkers()

            // 5. Tıklama Özelliği
            mapLibreMap.setOnMarkerClickListener { marker ->
                Toast.makeText(this, "${marker.title}: ${marker.snippet}", Toast.LENGTH_LONG).show()
                true
            }
        }
    }

    private fun addSampleMarkers() {
        mapLibreMap.addMarker(MarkerOptions()
            .position(LatLng(39.9210, 32.8545))
            .title("Aydınlatma Sorunu")
            .snippet("Sokak lambası yanmıyor"))

        mapLibreMap.addMarker(MarkerOptions()
            .position(LatLng(39.9250, 32.8500))
            .title("Güvenlik İhlali")
            .snippet("Şüpheli paket"))
    }

    // --- Yaşam Döngüsü Kodları (Harita Çökmemesi İçin Şart) ---
    override fun onStart() { super.onStart(); mapView.onStart() }
    override fun onResume() { super.onResume(); mapView.onResume() }
    override fun onPause() { super.onPause(); mapView.onPause() }
    override fun onStop() { super.onStop(); mapView.onStop() }
    override fun onLowMemory() { super.onLowMemory(); mapView.onLowMemory() }
    override fun onDestroy() { super.onDestroy(); mapView.onDestroy() }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}