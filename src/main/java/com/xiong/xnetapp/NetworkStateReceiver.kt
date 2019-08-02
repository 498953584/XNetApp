package com.xiong.xnetapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.SharedPreferences
import android.media.AudioManager
import android.net.ConnectivityManager


class NetworkStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        //判断广播的类型为网络action后
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            var audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            if (NetworkAvailable(context)) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_PLAY_SOUND)
            } else {
                var sp = context.getSharedPreferences("SP", Context.MODE_PRIVATE) as SharedPreferences
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, sp.getInt("Volume",40), AudioManager.FLAG_PLAY_SOUND)
            }
        }
    }

    /**
     * 检测网络是否连接
     */
    private fun NetworkAvailable(context: Context): Boolean {
        try {
            //得到网络连接信息
            var manager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            // 获取NetworkInfo对象
            val networkInfo = manager.activeNetworkInfo
            //去进行判断网络是否连接
            if (networkInfo != null || networkInfo!!.isAvailable) {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}
