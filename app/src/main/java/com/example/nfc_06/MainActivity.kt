package com.example.nfc_06

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var nfcAdapter: NfcAdapter? = null
    private lateinit var statusTextView: TextView

    private val nfcStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getIntExtra(NfcAdapter.EXTRA_ADAPTER_STATE, NfcAdapter.STATE_OFF)) {
                NfcAdapter.STATE_ON -> {
                    Log.d("MainActivity", "NFC Enabled")
                    updateNfcStatus(true)
                    startService(Intent(this@MainActivity, MyHostApduService::class.java))
                }
                NfcAdapter.STATE_OFF -> {
                    Log.d("MainActivity", "NFC Disabled")
                    updateNfcStatus(false)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusTextView = findViewById(R.id.statusTextView)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            statusTextView.text = "NFC is not supported on this device"
            Toast.makeText(this, "NFC is not supported on this device", Toast.LENGTH_LONG).show()
        } else {
            checkNfcStatus()
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(nfcStateReceiver, IntentFilter(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED))
        nfcAdapter?.let { checkNfcStatus() }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(nfcStateReceiver)
    }

    private fun checkNfcStatus() {
        nfcAdapter?.let { adapter ->
            if (adapter.isEnabled) {
                updateNfcStatus(true)
                startService(Intent(this, MyHostApduService::class.java))
            } else {
                updateNfcStatus(false)
                showNfcPrompt()
            }
        }
    }

    private fun updateNfcStatus(isEnabled: Boolean) {
        statusTextView.text = if (isEnabled) {
            "NFC URL Emulator is active"
        } else {
            "NFC is disabled. Please enable NFC to use this app."
        }
    }

    private fun showNfcPrompt() {
        AlertDialog.Builder(this)
            .setTitle("NFC is Disabled")
            .setMessage("NFC is required for this app. Would you like to enable it?")
            .setPositiveButton("Yes") { _, _ ->
                startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }
}
