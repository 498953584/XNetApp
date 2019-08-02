package com.xiong.xnetapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioManager
import android.net.ConnectivityManager
import android.util.Log

/**音量变化接收器*/
class VolumeBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if ("android.media.VOLUME_CHANGED_ACTION" == intent.action && (intent.getIntExtra(
                "android.media.EXTRA_VOLUME_STREAM_TYPE",
                -1
            ) == AudioManager.STREAM_MUSIC)
        ) {
            var mAudioManager = context.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            var current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            Log.d("MUSIC", "current : $current")
            if (current > 0) {
                //数据存储SharedPreferences
                var sp = context.getSharedPreferences("SP", Context.MODE_PRIVATE) as SharedPreferences
                var editor = sp.edit() as SharedPreferences.Editor
                editor.putInt("Volume", current)
                editor.commit()
            }
        }
    }
}
