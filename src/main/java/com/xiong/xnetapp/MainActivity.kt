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


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var sp = this.run { getSharedPreferences("SP", Context.MODE_PRIVATE) } as SharedPreferences
        Volume.run {
            setOnEditorActionListener(
                TextView.OnEditorActionListener(
                    fun(textView: TextView, actionId: Int, event: KeyEvent?): Boolean {
                        if (actionId == EditorInfo.IME_ACTION_DONE ||
                            (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER &&
                                    event.action == KeyEvent.ACTION_DOWN)
                        ) {
                            var volume = Integer.parseInt(textView.text.toString())
                            if (volume in 0..100) {
                                //存入数据
                                var editor = sp.edit() as SharedPreferences.Editor
                                editor.putInt("Volume", volume)
                                editor.commit()
                                var audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                                audioManager.setStreamVolume(
                                    AudioManager.STREAM_MUSIC,
                                    Math.round(volume / 100.0 * audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)).toInt(),
                                    AudioManager.FLAG_PLAY_SOUND
                                )
                                return false
                            }
                            return true
                        }
                        return true
                    })
            )
            setText(sp.getInt("Volume", 40).toString())
        }

//        var netBroadcastReceiver = NetworkStateReceiver()
//        //实例化意图
//        var filter = IntentFilter()
//        //设置广播的类型
//        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
//        //注册广播，有网络变化的时候会触发onReceive
//        registerReceiver(netBroadcastReceiver, filter)
        // 设置监听
        //netBroadcastReceiver.setNetEvent(this)
    }
}
