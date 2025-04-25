# CardScanner

**CardScanner** is a lightweight and efficient Android library for scanning credit/debit cards using your device camera. It decodes YUV camera frames to extract high-quality RGB data and supports card number recognition.

---

## ✨ Features

- 📷 Fast and reliable card scanner
- ⚡ Native YUV to RGB/ARGB image conversion using JNI for performance
- 🔐 Lightweight and privacy-focused (no data sent externally)
- 🔄 Easy to integrate into existing Android apps

---
## 📷 Demo Video
![Demo GIF](assets/demo_video.gif)
---

## 🛠 Installation
[![](https://jitpack.io/v/husniddin-fido/CardScanner.svg)](https://jitpack.io/#husniddin-fido/CardScanner)

### Add dependency
```groovy
implementation 'com.github.husniddin-fido:CardScanner:0.0.1'
```

### Check and request a camera permission
```kotlin
private fun checkForCameraPermission(): Boolean {
        val permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            cameraPermission.launch(Manifest.permission.CAMERA)
            return false
        }
        return true
}

private val cameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
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
```
### After getting camera permission you can open CardScannerActivity:
```kotlin
private fun openCameraForCardRead() {
        val intent = CardScannerActivity.buildIntent(
            activity,
            delayShowingExpiration,
            titleText,
            hintText,
            torchOnIcon,
            torchOffIcon
        )
        getActivityResult.launch(intent)
}
```
### Get result from CardScannerActivity:
```kotlin
private val getActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                val scanResult = CardScannerActivity.creditCardFromResult(it.data)
                val cardNumber = scanResult?.number
                val expireDate = scanResult?.expiryForDisplay()
                
        }
}
```



