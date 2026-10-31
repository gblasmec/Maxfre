package com.gblas.ffmaxoverlay

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.media.projection.MediaProjectionManager
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
            text = "FF MAX OVERLAY LAB"
            textSize = 24f
            gravity = Gravity.CENTER
        }

        val overlayButton = Button(this).apply {
            text = "Autorizar sobreposição"
            setOnClickListener {
                if (!Settings.canDrawOverlays(this@MainActivity)) {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")
                    )
                    startActivity(intent)
                }
            }
        }

        val captureButton = Button(this).apply {
            text = "Iniciar captura + painel"
            setOnClickListener {
                if (!Settings.canDrawOverlays(this@MainActivity)) {
                    overlayButton.performClick()
                    return@setOnClickListener
                }

                val manager = getSystemService(
                    Context.MEDIA_PROJECTION_SERVICE
                ) as MediaProjectionManager

                startActivityForResult(
                    manager.createScreenCaptureIntent(),
                    captureRequest
                )
            }
        }

        val stopButton = Button(this).apply {
            text = "Parar serviço"
            setOnClickListener {
                stopService(
                    Intent(
                        this@MainActivity,
                        CaptureOverlayService::class.java
                    )
                )
            }
        }

        layout.addView(title)
        layout.addView(overlayButton)
        layout.addView(captureButton)
        layout.addView(stopButton)

        setContentView(layout)
    }

    @Deprecated("Legacy activity result callback")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == captureRequest &&
            resultCode == RESULT_OK && data != null
        ) {
            val serviceIntent = Intent(
                this,
                CaptureOverlayService::class.java
            ).apply {
                action = CaptureOverlayService.ACTION_START
                putExtra(
                    CaptureOverlayService.EXTRA_RESULT_CODE,
                    resultCode
                )
                putExtra(
                    CaptureOverlayService.EXTRA_RESULT_DATA,
                    data
                )
            }

            startForegroundService(serviceIntent)
        }
    }
}
