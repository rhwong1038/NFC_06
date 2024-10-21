package com.example.nfc_06

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import android.content.Intent

class MyHostApduService : HostApduService() {

    companion object {
        private const val URL = "https://google.com"
        private const val STATUS_SUCCESS = 0x9000
        private const val STATUS_FAILED = 0x6F00
        private val SELECT_APDU = byteArrayOf(
            0x00, // CLA
            0xA4.toByte(), // INS
            0x04, // P1
            0x00, // P2
            0x07, // Lc
            0xF0.toByte(), 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 // AID
        )
    }

    override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle?): ByteArray {
        Log.d("MyHostApduService", "Received APDU: ${commandApdu.contentToString()}")
        return createNdefMessage()
    }

    override fun onDeactivated(reason: Int) {
        // Not needed for this implementation
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("MyHostApduService", "Service created. URL to be emulated: $URL")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MyHostApduService", "Service started")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MyHostApduService", "Service destroyed")
    }

    private fun createNdefMessage(): ByteArray {
        val ndefHeader = byteArrayOf(0xD1.toByte(), 0x01, 0x0C)
        val uriHeader = byteArrayOf(0x55, 0x04) // URI Record, "https://" prefix
        val uriField = URL.substring(8).toByteArray() // Remove "https://" prefix
        val ndefMessage = ndefHeader + uriHeader + uriField

        val response = ByteArray(ndefMessage.size + 2)
        System.arraycopy(ndefMessage, 0, response, 0, ndefMessage.size)
        response[response.size - 2] = (STATUS_SUCCESS shr 8).toByte()
        response[response.size - 1] = (STATUS_SUCCESS and 0xFF).toByte()

        Log.d("MyHostApduService", "NDEF Message: ${response.contentToString()}")
        return response
    }
}
