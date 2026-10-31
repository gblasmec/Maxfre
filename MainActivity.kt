package com.gblas.ffmaxoverlay

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    private val captureRequest = 4102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(28, 28, 28, 28)
        }

        val title = TextView(this).apply {
            text = "FF MAX OVERLAY LAB\nProtótipo Android"
            textSize = 22f
            gravity = Gravity.CENTER
        }
        val info = TextView(this).apply {
            text = "Esta primeira build testa a janela flutuante e a captura autorizada da tela. Ainda não detecta inimigos automaticamente."
            textSize = 15f
            setPadding(0, 24, 0, 24)
        }
        val overlayButton = Button(this).apply {
            text = "1. Autorizar sobreposição"
            setOnClickListener { requestOverlayPermission() }
        }
        val captureButton = Button(this).apply {
            text = "2. Iniciar captura + painel"
            setOnClickListener { beginCapture() }
        }
        val stopButton = Button(this).apply {
            text = "Parar serviço"
            setOnClickListener {
                stopService(Intent(this@MainActivity, CaptureOverlayService::class.java))
            }
        }

        layout.addView(title)
        layout.addView(info)
        layout.addView(overlayButton)
        layout.addView(captureButton)
        layout.addView(stopButton)
        setContentView(layout)
    }

    private fun requestOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
        }
    }

    private fun beginCapture() {
        if (!Settings.canDrawOverlays(this)) {
            requestOverlayPermission()
            return
        }
        val manager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(manager.createScreenCaptureIntent(), captureRequest)
    }

    @Deprecated("Using the platform result callback keeps this starter project dependency-free.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == captureRequest && resultCode == RESULT_OK && data != null) {
            val serviceIntent = Intent(this, CaptureOverlayService::class.java).apply {
                action = CaptureOverlayService.ACTION_START
                putExtra(CaptureOverlayService.EXTRA_RESULT_CODE, resultCode)
                putExtra(CaptureOverlayService.EXTRA_RESULT_DATA, data)
            }
            startForegroundService(serviceIntent)
        }
    }
}
