/*bei context.kt wurde nur geschaut, ob die berechtigung gegeben wird, 
jetzt wird erst so richtig gescannt :) */
private val bleScanner by lazy{
    bluetoothAdapter.bluetoothLEScanner
}
//von context.kt
private val bluetoothAdapter by lazy{
    val bluetoothManager =getSystemService(Context.BLUETOOTH_SERVICE) as bluetoothManager
    bluetoothManager.adapter
}
//man braucht Scansettings, das ist jetzt eines der Simpleren:
private val scanSettings = ScanSettings.Builder()
    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
    .build
//hier die scancallback function

private val scancallback = object :ScanCallback(){
    override fun onScanResult(callbackType: Int,result: ScanResult){
        with(result.deice) {
            Log.i("ScanCallback","BLE Gerät gefunden! Name ${name ?: "Unnamed"}, address: $address")
        }
    }
}
//jetzt gehts an die start BLEscann function :)