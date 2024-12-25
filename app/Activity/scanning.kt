/*bei context.kt wurde nur geschaut, ob die berechtigung gegeben wird, 
jetzt wird erst so richtig gescannt :) */
private val bleScanner by lazy {
    bluetoothAdapter.bluetoothLeScanner
}
//von context.kt
private val bluetoothAdapter by lazy {
    val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    bluetoothManager.adapter
}
//man braucht Scansettings, das ist jetzt eines der Simpleren:
private val scanSettings = ScanSettings.Builder()
    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
    .build()
//hier die scancallback function

private val scanCallback = object : ScanCallback() {
    override fun onScanResult(callbackType: Int, result: ScanResult) {
        with(result.device) {
            Log.i("ScanCallback", "BLE device found! Name: ${name ?: "Unnamed"}, address: $address")
        }
    }
}