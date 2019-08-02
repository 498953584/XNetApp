package com.xiong.xnetapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.SharedPreferences
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log


class NetworkStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        //判断广播的类型为网络action后
        if (intent.action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            var audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            var max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            var current: Double
//            Log.d("MUSIC", "max : $max current : $current")
            Log.d("MUSIC", "current : " + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC))
            var sp = context.getSharedPreferences("SP", Context.MODE_PRIVATE) as SharedPreferences
            if (isNetworkAvailable(context)) {
                Toast.makeText(context, "网络已连接", Toast.LENGTH_SHORT)
                current = sp.getInt("Volume", 40) / 100.0 * max
                audioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    Math.round(current).toInt(),
                    AudioManager.FLAG_PLAY_SOUND
                )
            } else {
                Toast.makeText(context, "无网络连接", Toast.LENGTH_SHORT)
                var editor = sp.edit() as SharedPreferences.Editor
                current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / max * 100.0
                editor.putInt("Volume", Math.round(current).toInt())
                editor.commit()
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
            val networkInfo :NetworkInfo? = manager.activeNetworkInfo
            //去进行判断网络是否连接
            if (networkInfo != null && networkInfo.isAvailable) {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
        return false
    }
}
