package com.xiong.xnetapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.SharedPreferences
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_MOBILE
import android.net.NetworkInfo
import android.util.Log
import android.net.ConnectivityManager.TYPE_WIFI

/**网络状态接收器*/
class NetworkStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        //判断广播的类型为网络action后
        if (intent.action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            var audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            var sp = context.getSharedPreferences("SP", Context.MODE_PRIVATE) as SharedPreferences
            var max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            var current = sp.getInt("Volume", audioManager.getStreamVolume(AudioManager.STREAM_MUSIC))
            Log.d("MUSIC", "max : $max current : $current")
            if (isNetworkAvailable(context)) {
//                current = sp.getInt("Volume", 40) / 100.0 * max
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current, AudioManager.FLAG_PLAY_SOUND)
            } else {
//                var editor = sp.edit() as SharedPreferences.Editor
//                current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / max * 100.0
//                editor.putInt("Volume", Math.round(current).toInt())
//                editor.commit()
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_PLAY_SOUND)
            }
        }
    }

    /**
     * 检测网络是否连接
     */
    private fun isNetworkAvailable(context: Context): Boolean {
        try {
            //得到网络连接信息
            var manager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            // 获取NetworkInfo对象
            val networkInfo: NetworkInfo? = manager.activeNetworkInfo
            //去进行判断网络是否连接
            if (networkInfo != null && networkInfo.isAvailable) {
                when (networkInfo.type) {
                    TYPE_MOBILE -> Toast.makeText(context, "正在使用移动网络", Toast.LENGTH_SHORT).show()
                    TYPE_WIFI -> Toast.makeText(context, "正在使用WiFi连接", Toast.LENGTH_SHORT).show()
                    else -> {
                    }
                }
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Toast.makeText(context, "当前无网络连接", Toast.LENGTH_SHORT).show()
        return false
    }
}
