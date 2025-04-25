package uz.maryam.cardscanner

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        checkForCameraPermission()
        findViewById<MaterialButton>(R.id.material_button).setOnClickListener {
            openCameraForCardRead()
        }
    }

    private fun openCameraForCardRead() {
        val intent = CardScannerActivity.buildIntent(
            this, true, null, R.string.card_scan_position_card, null, null
        )
        getActivityResult.launch(intent)
    }

    private val getActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                val scanResult = CardScannerActivity.creditCardFromResult(it.data)
                val cardNumber = scanResult?.number
                val expireDate = scanResult?.expiryForDisplay()
                if (cardNumber != null) {
                    findViewById<TextView>(R.id.info_text).text = cardNumber + "/n" + expireDate
                }
            }
        }

    private val cameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
            if (permissionGranted) {
                //we have a permission for camera
            } else {
                Toast.makeText(
                    this,
                    "App not working without CAMERA permission",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun checkForCameraPermission(): Boolean {
        val permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            cameraPermission.launch(Manifest.permission.CAMERA)
            return false
        }
        return true
    }

}