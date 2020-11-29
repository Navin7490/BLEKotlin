package com.example.blekotlin

import android.app.Activity
import android.app.ListActivity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.LeScanCallback
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.Array


class DeviceScanFragment :Fragment() {
    private var mLeDeviceListAdapter: LeDeviceListAdapter? =
        null
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mScanning = false
    private var mHandler: Handler? = null

    private val REQUEST_ENABLE_BT = 1
   lateinit var recyclerView: RecyclerView
    // Stops scanning after 10 seconds.
    private val SCAN_PERIOD: Long = 10000
    var mlist:ArrayList<BluetoothDevice> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // actionBar!!.setTitle(R.string.title_devices)
        mHandler = Handler()
       mLeDeviceListAdapter= LeDeviceListAdapter(requireContext().applicationContext,mlist )
         // Use this check to determine whether BLE is supported on the device.  Then you can
        //selectively disable BLE-related features.

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!requireActivity().packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(requireActivity(), R.string.ble_not_supported, Toast.LENGTH_SHORT).show()
            requireActivity(). finish()
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        val bluetoothManager =
            requireActivity(). getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            mBluetoothAdapter = bluetoothManager.adapter

          //Checks if Bluetooth is supported on the device.

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(requireActivity(),R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show()
            requireActivity().finish()
            return
        }



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment_device_scan, container, false)

        recyclerView=view.findViewById(R.id.RvDevicelist)
        recyclerView.layoutManager=LinearLayoutManager(requireContext())
        recyclerView.adapter=mLeDeviceListAdapter
        scanLeDevice(true)

        return view
    }






    override fun onResume() {
        super.onResume()

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(
                enableBtIntent,
               REQUEST_ENABLE_BT
            )
        }

        // Initializes list view adapter.
      //  mLeDeviceListAdapter = LeDeviceListAdapter(requireActivity())
            // setListAdapter(mLeDeviceListAdapter)

        Toast.makeText(requireActivity(),"$mLeDeviceListAdapter",Toast.LENGTH_LONG).show()
    }

    private fun scanLeDevice(enable: Boolean) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler!!.postDelayed({
                mScanning = false
                mBluetoothAdapter!!.stopLeScan(mLeScanCallback)
            }, SCAN_PERIOD)
            mScanning = true
            mBluetoothAdapter!!.startLeScan(mLeScanCallback)
        } else {
            mScanning = false
            mBluetoothAdapter!!.stopLeScan(mLeScanCallback)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode==REQUEST_ENABLE_BT && resultCode==Activity.RESULT_CANCELED){
            requireActivity(). finish()
            return

        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    // Device scan callback.
    private val mLeScanCallback =
        LeScanCallback { device, rssi, scanRecord ->
            requireActivity(). runOnUiThread(Runnable {
                mLeDeviceListAdapter!!.adddevice(device)
                mLeDeviceListAdapter!!.notifyDataSetChanged()
            })
        }


  // LeDevicelist adapter
     class LeDeviceListAdapter(var context: Context,var mlist:ArrayList<BluetoothDevice>):RecyclerView.Adapter<ViewHolder>(){


        private var mDevicelist:ArrayList<BluetoothDevice>  = ArrayList()
        private var mlayoutinfalter:LayoutInflater= LayoutInflater.from(context)


        fun adddevice(device: BluetoothDevice){

            if (!mDevicelist.contains(device)){
                mDevicelist.add(device)
            }

        }

        fun getDevice(i:Int):BluetoothDevice{

            return mDevicelist[i]

        }


        fun  clearlist(){

            mDevicelist.clear()
        }

      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

          var layoutInflater=LayoutInflater.from(context)
          var viewlist=layoutInflater.inflate(R.layout.device_list_item,parent,false)
          return ViewHolder(viewlist)
      }

      override fun getItemCount(): Int {
          return mlist.size
      }

      override fun onBindViewHolder(holder: ViewHolder, position: Int) {

          holder.deviceaddress.text=mlist.get(position).address
          holder.devicename.text=mlist.get(position).name
      }


  }





    class ViewHolder(itemview: View):RecyclerView.ViewHolder(itemview){
        var devicename:TextView=itemview.findViewById(R.id.text_viewdevicename)
        var deviceaddress:TextView=itemview.findViewById(R.id.text_viewdeviceaddress)




    }
}