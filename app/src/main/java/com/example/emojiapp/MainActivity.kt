package com.example.emojiapp

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.emojiapp.ui.navigation.AppNavigation
import com.example.emojiapp.ui.theme.EmojiAppTheme

const val REQUEST_ENABLE_BT = 1
const val REQUEST_BLUETOOTH_SCAN_PERMISSION = 2

class BLEManager(private val context: Context, private val bluetoothAdapter: BluetoothAdapter?) {
    private var bluetoothGatt: BluetoothGatt? = null

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            if (result != null) {
                Log.d("BLEManager", "Found device: ${result.device.address}")
                // Tutaj obsłuż znalezione urządzenie
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.e("BLEManager", "Scan failed with error code: $errorCode")
        }
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i("BLEManager", "Connected to GATT server.")
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                    gatt?.discoverServices()
                } else {
                    Log.d("BLEManager", "Brak uprawnień do odkrywania usług Bluetooth")
                }
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i("BLEManager", "Disconnected from GATT server.")
            }
        }
    }

    fun enableBLE() {
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                context.startActivity(enableBtIntent)
            } else {
                Log.d("BLEManager", "Brak uprawnień do włączenia Bluetooth")
            }
        }
    }

    fun connectToDevice(device: BluetoothDevice?) {
        if (device != null) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Log.d("BLEManager", "Brak uprawnień do łączenia się z Bluetooth")
                return
            }
            bluetoothGatt = device.connectGatt(context, false, gattCallback)
        }
    }

    fun chooseSendMode(bool: Boolean) {
        // Tutaj zaimplementuj swoją logikę
    }

    fun startScanning() {
        if (bluetoothAdapter != null) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                Log.d("BLEManager", "Brak uprawnień do skanowania Bluetooth")
                return
            }
            bluetoothAdapter.bluetoothLeScanner.startScan(null, ScanSettings.Builder().build(), scanCallback)
        }
    }

    fun stopScanning() {
        if (bluetoothAdapter != null) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                Log.d("BLEManager", "Brak uprawnień do zatrzymania skanowania Bluetooth")
                return
            }
            bluetoothAdapter.bluetoothLeScanner.stopScan(scanCallback)
        }
    }
}

class MainActivity : ComponentActivity() {
    private lateinit var bleManager: BLEManager
    private lateinit var bluetoothAdapter: BluetoothAdapter

    private val requestBluetoothEnableLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Log.d("MainActivity", "Bluetooth enabled")
                checkBluetoothPermissionsAndStartScanning()
            } else {
                Log.d("MainActivity", "Bluetooth not enabled")
            }
        }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allPermissionsGranted = permissions.all { it.value }
            if (allPermissionsGranted) {
                Log.d("MainActivity", "Bluetooth permissions granted")
                bleManager.startScanning()
            } else {
                Log.d("MainActivity", "Bluetooth permissions denied")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val context = this
        val bluetoothManager = context.getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter
        bleManager = BLEManager(context, bluetoothAdapter)

        if (!bluetoothAdapter.isEnabled) {
            requestBluetoothEnableLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        }
        checkBluetoothPermissionsAndStartScanning()


        setContent {
            EmojiAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }

    private fun checkBluetoothPermissionsAndStartScanning() {
        val permissionsToRequest = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.BLUETOOTH_SCAN)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            bleManager.startScanning()
        }
    }
}
