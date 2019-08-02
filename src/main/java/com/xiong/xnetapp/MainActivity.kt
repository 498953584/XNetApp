package com.xiong.xnetapp

import android.content.Context
import android.content.IntentFilter
import android.content.SharedPreferences
import android.media.AudioManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.net.ConnectivityManager
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Build
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    private var networkChangeReceiver: NetworkStateReceiver? = null
    private var volumeBroadcastReceiver: VolumeBroadcastReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var audioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        var sp = this.run { getSharedPreferences("SP", Context.MODE_PRIVATE) } as SharedPreferences
        var curr = sp.getInt("Volume", audioManager.getStreamVolume(AudioManager.STREAM_MUSIC))
        var max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        curr = Math.round(curr * 100.0 / max).toInt()
        //控件绑定事件
        Volume.run {
            setOnEditorActionListener(
                TextView.OnEditorActionListener(
                    fun(textView: TextView, actionId: Int, event: KeyEvent?): Boolean {
                        //完成，或键盘回车
                        if (actionId == EditorInfo.IME_ACTION_DONE ||
                            (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER &&
                                    event.action == KeyEvent.ACTION_DOWN)
                        ) {
                            curr = Integer.parseInt(textView.text.toString())
                            return if (curr in 1..100) {
                                audioManager.setStreamVolume(
                                    AudioManager.STREAM_MUSIC,
                                    Math.round(curr / 100.0 * max).toInt(),
                                    AudioManager.FLAG_PLAY_SOUND
                                )
                                false
                            } else {
                                Toast.makeText(context, "请输入1-100的数字！", Toast.LENGTH_SHORT).show()
                                true
                            }
                        }
                        return true
                    })
            )
            setText(curr.toString())
        }
        //动态注册接收器
        var intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        networkChangeReceiver = NetworkStateReceiver()
        registerReceiver(networkChangeReceiver, intentFilter)
        intentFilter = IntentFilter("android.media.VOLUME_CHANGED_ACTION")
        volumeBroadcastReceiver = VolumeBroadcastReceiver()
        registerReceiver(volumeBroadcastReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (networkChangeReceiver != null) {
            unregisterReceiver(networkChangeReceiver)
        }
        if (volumeBroadcastReceiver != null) {
            unregisterReceiver(volumeBroadcastReceiver)
        }
    }
}