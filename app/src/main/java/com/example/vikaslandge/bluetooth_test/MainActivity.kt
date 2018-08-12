package com.example.vikaslandge.bluetooth_test

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var bAdapter = BluetoothAdapter.getDefaultAdapter()

        if(bAdapter==null){
            Toast.makeText(this,"Bluetooth not supported",Toast.LENGTH_LONG).show()
        }else {
            s1.isChecked = bAdapter.isEnabled
            s1.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    bAdapter.enable()
                } else {
                    bAdapter.disable()
                }
            }
        }
        searchbt.setOnClickListener {
            var list: MutableList<String> = mutableListOf()
            var arrAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_single_choice,list)
            lview.adapter = arrAdapter

            bAdapter.startDiscovery()
            var iFilter = IntentFilter()
            iFilter.addAction(BluetoothDevice.ACTION_FOUND)
            registerReceiver(object : BroadcastReceiver(){
                override fun onReceive(p0: Context?, p1: Intent?) {
                     var device = p1!!.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    list.add(device.name+"\n"+device.address)
                    arrAdapter.notifyDataSetChanged()
                }

            },iFilter)

        }

        var wManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        var state = wManager.wifiState
        if(state==0 || state==1){
            s2.isChecked = false
        }else if (state == 2 || state == 3){
            s2.isChecked = true
        }
        s2.setOnCheckedChangeListener { compoundButton, b ->
            wManager.setWifiEnabled(b)

        }
        searchwifi.setOnClickListener {
            var list : List<ScanResult> = wManager.scanResults
            var temp_List : MutableList<String> = mutableListOf()
            for (device in list){
                temp_List.add(device.SSID+"\n"+device.frequency)

            }
            var myAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_single_choice,temp_List)
            lview.adapter = myAdapter
        }
         wifipaired.setOnClickListener {
            var list : List<WifiConfiguration> = wManager.configuredNetworks
            var temp_List : MutableList<String> = mutableListOf()
            for (device in list){
                temp_List.add(device.SSID+"\n"+device.status)

            }
            var myAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_single_choice,temp_List)
            lview.adapter = myAdapter
        }
        lview.setOnClickListener {

        }



    }//onCreate
}
