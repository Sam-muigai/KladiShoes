package com.sam.kladishoes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import com.sam.kladishoes.presentation.components.showMessage


class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.provider.Telephony.SMS_RECEIVED") {
            showMessage(context!!,"Payment done")
        }
    }
}


