package com.xiong.xnetapp

import android.content.Context
import android.content.IntentFilter
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.net.ConnectivityManager
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.lang.Math.log
import kotlin.math.log


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var sp = this.run { getSharedPreferences("SP", Context.MODE_PRIVATE) } as SharedPreferences
        Volume.run {
            setOnEditorActionListener(TextView.OnEditorActionListener(
                function = fun(textView: TextView, actionId: Int, event: KeyEvent): Boolean {
                    try{
                    if (actionId == EditorInfo.IME_ACTION_SEND ||
                        (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER &&
                                event.action == KeyEvent.ACTION_DOWN)
                    ) {
                        Toast.makeText(context,"129"+textView.text,Toast.LENGTH_SHORT)
                        //存入数据
                        var editor = sp.edit() as SharedPreferences.Editor
                        editor.putInt("Volume", Integer.parseInt(textView.text.toString()))
                        editor.commit()
                        return true
                    }
                    return false
                }
                catch(ex : Exception){
                    Toast.makeText(context,ex.message,Toast.LENGTH_LONG)
                    return false
                }
                }
            ))
        }

        var netBroadcastReceiver = NetworkStateReceiver()
        //实例化意图
        var filter = IntentFilter()
        //设置广播的类型
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        //注册广播，有网络变化的时候会触发onReceive
        registerReceiver(netBroadcastReceiver, filter)
        // 设置监听
        //netBroadcastReceiver.setNetEvent(this)
    }
}
