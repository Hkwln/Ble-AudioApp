private const val PERMISSION_REQUEST_CODE =1

/** Haben Wir nen Button um BLE zu öffnen, dann entkommentiere dies:
 scanButton.setOnClickListener { startBleScan() } oder schreibe selbst ein programm, was beim buttonclick schaut, ob die Permission gegeben wurde*/
/**
 * Determine whether the current [Context] has been granted the relevant [Manifest.permission].
 */
fun Context.hasPermission(permissionType: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permissionType) ==
        PackageManager.PERMISSION_GRANTED
}

/**
 * Determine whether the current [Context] has been granted the relevant permissions to perform
 * Bluetooth operations depending on the mobile device's Android version.
 */
fun Context.hasRequiredBluetoothPermissions(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        hasPermission(Manifest.permission.BLUETOOTH_SCAN) &&
            hasPermission(Manifest.permission.BLUETOOTH_CONNECT)
    } else {
        hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}
private fun startBleScan() {
    if (!hasRequiredBluetoothPermissions()) {
        requestRelevantRuntimePermissions()
    } else { 
        bleScanner.startScan(null, Scansettings, scancallback)
    }
}

private fun Activity.requestRelevantRuntimePermissions() {
    if (hasRequiredBluetoothPermissions()) { return }
    when {
        Build.VERSION.SDK_INT < Build.VERSION_CODES.S -> {
            requestLocationPermission()
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            requestBluetoothPermissions()
        }
    }
}

private fun requestLocationPermission() = runOnUiThread {
    AlertDialog.Builder(this)
        .setTitle("Location permission required")
        .setMessage(
            "Starting from Android M (6.0), the system requires apps to be granted " +
            "location access in order to scan for BLE devices."
        )
        .setCancelable(false)
        .setPositiveButton(android.R.string.ok) { _, _ ->
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE
            )
        }
        .show()
}

@RequiresApi(Build.VERSION_CODES.S)
private fun requestBluetoothPermissions() = runOnUiThread {
    AlertDialog.Builder(this)
        .setTitle("Bluetooth permission required")
        .setMessage(
            "Starting from Android 12, the system requires apps to be granted " +
                "Bluetooth access in order to scan for and connect to BLE devices."
        )
        .setCancelable(false)
        .setPositiveButton(android.R.string.ok) { _, _ ->
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
                ),
                PERMISSION_REQUEST_CODE
            )
        }
        .show()
    }
}

override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode != PERMISSION_REQUEST_CODE) return

    val containsPermanentDenial = permissions.zip(grantResults.toTypedArray()).any {
        it.second == PackageManager.PERMISSION_DENIED &&
            !ActivityCompat.shouldShowRequestPermissionRationale(this, it.first)
    }
    val containsDenial = grantResults.any { it == PackageManager.PERMISSION_DENIED }
    val allGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
    when {
        containsPermanentDenial -> {
            // TODO: Handle permanent denial (e.g., show AlertDialog with justification)
            // Note: The user will need to navigate to App Settings and manually grant
            // permissions that were permanently denied
        }
        containsDenial -> {
            requestRelevantRuntimePermissions()
        }
        allGranted && hasRequiredBluetoothPermissions() -> {
            startBleScan()
        }
        else -> {
            // Unexpected scenario encountered when handling permissions
            recreate()
        }
    }
}