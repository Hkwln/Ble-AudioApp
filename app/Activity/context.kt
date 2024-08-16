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