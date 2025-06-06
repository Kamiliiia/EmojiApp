package com.example.emojiapp

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattServer
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
import java.util.UUID
import android.bluetooth.BluetoothGattServerCallback
import android.bluetooth.BluetoothGattService
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.os.ParcelUuid
import android.os.Build
import com.example.emojiapp.ui.utils.Constants
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import android.media.AudioAttributes
import android.net.Uri


const val REQUEST_ENABLE_BT = 1
const val REQUEST_BLUETOOTH_SCAN_PERMISSION = 2
const val GATT_SERVICE_UUID = "0000dcba-0000-1000-8000-0080dfdfabba"
const val GATT_CHARACTERISTIC_UUID = "0000dcba-0000-1000-8000-0080fdfdbaab"

class BLEGattServerManager(private val context: Context) {
    private var gattServer: BluetoothGattServer? = null
    private val bluetoothManager = context.getSystemService(BluetoothManager::class.java)
    private val bluetoothAdapter = bluetoothManager?.adapter

    private val gattServerCallback = object : BluetoothGattServerCallback() {
        override fun onConnectionStateChange(device: BluetoothDevice?, status: Int, newState: Int) {
            Log.d("BLEServer", "Connection state changed: $device $newState")
        }

        override fun onCharacteristicReadRequest(
            device: BluetoothDevice?, requestId: Int, offset: Int,
            characteristic: BluetoothGattCharacteristic?
        ) {
            Log.d("BLEServer", "Read request from $device for ${characteristic?.uuid}")
            val response = byteArrayOf(0x42)
            gattServer?.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, response)
        }

        override fun onCharacteristicWriteRequest(
            device: BluetoothDevice?, requestId: Int, characteristic: BluetoothGattCharacteristic?,
            preparedWrite: Boolean, responseNeeded: Boolean, offset: Int, value: ByteArray?
        ) {
            Log.d("BLEServer", "Write to ${characteristic?.uuid} from $device: ${value?.joinToString()}")
            val message = value?.toString(Charsets.UTF_8) ?: "\uD83D\uDE0E"
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelId = "emoji_app_channel2"
            val channelIDSOS = "emoji_app_channelsos"
            val emoji = Constants.CATEGORY_EMOJI[message] ?: ""
            val soundUri = Uri.parse("android.resource://${context.packageName}/${R.raw.bell}")
            val soundUriSOS = Uri.parse("android.resource://${context.packageName}/${R.raw.sos}")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if(message == "SOS")
                {
                    val channel = NotificationChannel(
                        channelIDSOS,
                        "Emoji Notifications SOS",
                        NotificationManager.IMPORTANCE_HIGH
                    ).apply {
                        setSound(soundUriSOS, AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                        )
                    }
                    notificationManager.createNotificationChannel(channel)
                }
                else
                {
                    val channel = NotificationChannel(
                        channelId,
                        "Emoji Notifications 2",
                        NotificationManager.IMPORTANCE_HIGH
                    ).apply {
                        setSound(soundUri, AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                        )
                    }
                    notificationManager.createNotificationChannel(channel)
                }
            }

            if(message=="SOS")
            {
                val builder = NotificationCompat.Builder(context, channelIDSOS)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("$emoji$emoji$emoji $message $emoji$emoji$emoji $message $emoji$emoji$emoji")
                    .setContentText("$emoji$emoji$emoji $message $emoji$emoji$emoji $message $emoji$emoji$emoji")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)

                builder.setSound(soundUriSOS)

                notificationManager.notify(1, builder.build())
            }
            else
            {
                val builder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("$emoji$emoji$emoji $message $emoji$emoji$emoji $message $emoji$emoji$emoji")
                    .setContentText("$emoji$emoji$emoji $message $emoji$emoji$emoji $message $emoji$emoji$emoji")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)

                builder.setSound(soundUri)

                notificationManager.notify(1, builder.build())
            }


            if (responseNeeded) {

                gattServer?.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, null)
            }
        }
    }

    fun startServer() {
        gattServer = bluetoothManager?.openGattServer(context, gattServerCallback)

        val service = BluetoothGattService(
            UUID.fromString(GATT_SERVICE_UUID),
            BluetoothGattService.SERVICE_TYPE_PRIMARY
        )

        val characteristic = BluetoothGattCharacteristic(
            UUID.fromString(GATT_CHARACTERISTIC_UUID),
            BluetoothGattCharacteristic.PROPERTY_READ or BluetoothGattCharacteristic.PROPERTY_WRITE,
            BluetoothGattCharacteristic.PERMISSION_READ or
                    BluetoothGattCharacteristic.PERMISSION_WRITE or
                    BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED
        )
        service.addCharacteristic(characteristic)
        gattServer?.addService(service)
        Log.d("BLEServer", "Uruchomiono BLE GATT SERVER naprawde")
        startAdvertising()

    }

    private fun startAdvertising() {
        val advertiser = bluetoothAdapter?.bluetoothLeAdvertiser
        if (advertiser == null) {
            Log.e("BLEServer", "BLE Advertiser not supported")
            return
        }

        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .setConnectable(true)
            .setTimeout(0)
            .build()

        val data = AdvertiseData.Builder()
            .setIncludeDeviceName(false)
            .addServiceUuid(ParcelUuid(UUID.fromString(GATT_SERVICE_UUID)))
            .build()


        Log.d("BLEServer", "przed advertisingiem")
        advertiser.startAdvertising(settings, data, object : AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
                Log.d("BLEServer", "Advertising started")
            }

            override fun onStartFailure(errorCode: Int) {
                Log.e("BLEServer", "Advertising failed: $errorCode")
            }
        })
    }

    fun stopServer() {
        gattServer?.close()
    }
}

class BLEManager(private val context: Context, private val bluetoothAdapter: BluetoothAdapter?) {
    private var bluetoothGatt: BluetoothGatt? = null
    private val connectedDevices = mutableSetOf<String>()

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            result?.let { processScanResult(it) }
        }

        override fun onBatchScanResults(results: List<ScanResult>) {
            Log.d("BLEManager", "Batch scan results received: ${results.size}")
            for (result in results) {
                processScanResult(result)
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("BLEManager", "Scan failed with error code: $errorCode")
        }

        private fun processScanResult(result: ScanResult) {
            val device = result.device ?: return
            val deviceName = device.name ?: return
            val deviceAddress = device.address

            //Log.d("BLEManager", "Found device: $deviceAddress with name: $deviceName")

            if (deviceName.contains("amil", ignoreCase = true)) {
                Log.i("BLEManager", "Found emoji device: $deviceName")
                connectToDevice(device)
            }
        }

    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            val deviceAddress = gatt?.device?.address ?: return

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                connectedDevices.add(deviceAddress)
                Log.i("BLEManager", "Connected to urzadzenie GATT server.")
                stopScanning()
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT)
                    == PackageManager.PERMISSION_GRANTED) {
                    gatt.discoverServices()
                }
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i("BLEManager", "Disconnected from urzadzenie GATT server.")
                connectedDevices.remove(deviceAddress)
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i("BLEManager", "Services discovered")
                gatt?.services?.forEach { service ->
                    Log.d("BLEManager", "Service UUID: ${service.uuid}")
                    service.characteristics.forEach { characteristic ->
                        Log.d("BLEManager", "  Characteristic UUID: ${characteristic.uuid}")
                    }
                }
            } else {
                Log.w("BLEManager", "Service discovery failed with status: $status")
            }
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS && characteristic != null) {
                val value = characteristic.value
                Log.d("BLEManager", "Read from ${characteristic.uuid}: ${value?.joinToString()}")
            } else {
                Log.e("BLEManager", "Read failed from ${characteristic?.uuid}")
            }
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("BLEManager", "Successfully wrote to ${characteristic?.uuid}")
            } else {
                Log.e("BLEManager", "Failed to write to ${characteristic?.uuid}, status=$status")
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

    fun startScanning() {
        if (bluetoothAdapter != null) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                Log.d("BLEManager", "Brak uprawnień do skanowania Bluetooth")
                return
            }
            val settings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
                .build()

            bluetoothAdapter.bluetoothLeScanner.startScan(null, settings, scanCallback)
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

    fun readCharacteristic(serviceUUID: UUID, characteristicUUID: UUID) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.d("BLEManager", "No permission to read characteristic")
            return
        }

        val service = bluetoothGatt?.getService(serviceUUID)
        val characteristic = service?.getCharacteristic(characteristicUUID)

        if (characteristic != null) {
            bluetoothGatt?.readCharacteristic(characteristic)
        } else {
            Log.w("BLEManager", "Characteristic $characteristicUUID not found in service $serviceUUID")
        }
    }

    fun writeCharacteristic(value: ByteArray) {
        val serviceUUID = UUID.fromString(GATT_SERVICE_UUID)
        val characteristicUUID = UUID.fromString(GATT_CHARACTERISTIC_UUID)
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.d("BLEManager", "No permission to write characteristic")
            return
        }

        val service = bluetoothGatt?.getService(serviceUUID)
        val characteristic = service?.getCharacteristic(characteristicUUID)
        Log.d("BLEManager", "przed wyslaniem, znalezione service")

        if (characteristic != null) {
            characteristic.value = value
            val success = bluetoothGatt?.writeCharacteristic(characteristic) ?: false
            Log.d("BLEManager", "Write initiated: $success")
        } else {
            Log.w("BLEManager", "Characteristic $characteristicUUID not found in service $serviceUUID")
        }
    }
}

class MainActivity : ComponentActivity() {
    private lateinit var bleManager: BLEManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothSERVER: BLEGattServerManager

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
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Log.e("MainActivity", "BLE not supported on this device")
            finish()
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val context = this
        val bluetoothManager = context.getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter
        bleManager = BLEManager(context, bluetoothAdapter)
        bluetoothSERVER = BLEGattServerManager(context)


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
                    AppNavigation(bleManager = bleManager)
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.BLUETOOTH_ADVERTISE)
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            val isSamsung = Build.MANUFACTURER == "samsung"
            val isXiaomi = Build.MANUFACTURER == "Xiaomi"

            if (isSamsung) {
                Log.d("MainActivity", "MAMY SAMSUNG")
                bluetoothSERVER.startServer()
            }

            if (isXiaomi) {
                Log.d("MainActivity", "MAMY XIAOMI")
                bleManager.startScanning()
            }

        }
    }
}
