package com.xiong.xnetapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity



class BootBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        /**
         * 如果 系统 启动的消息，则启动 APP 主页活动
         */
        if ("android.intent.action.BOOT_COMPLETED" == intent.action) {
            val i = Intent(context, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(i)
            Toast.makeText(context, intent.action, Toast.LENGTH_LONG).show()
        }

    }
}
