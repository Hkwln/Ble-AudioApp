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