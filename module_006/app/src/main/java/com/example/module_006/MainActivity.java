package com.example.module_006;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button scanButton;
    ListView scanListView;
    // stringArrayList contains names or addresses of devices.
    ArrayList<String> stringArrayList = new ArrayList<String>();

    ArrayAdapter<String> arrayAdapter;
    BluetoothAdapter myAdapter = BluetoothAdapter.getDefaultAdapter();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create buttons and List View widgets.
        scanButton = (Button) findViewById(R.id.scanButton);
        scanListView = (ListView) findViewById(R.id.scanListView);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAdapter.startDiscovery();
            }
        });

        // Create IntentFilter for Bluetooth Action (
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        // Register BroadcastReceiver to be run in the main thread and Filter for it.
        registerReceiver(myReceiver, intentFilter);

        // ArrayAdapter lets us to provide views (to display names or addresses) for ListView widget.
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, stringArrayList);
        // Display list of names or addresses
        scanListView.setAdapter(arrayAdapter);

        int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
        permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
        if (permissionCheck != 0) {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
        }
    }

    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getName() != null)
                {
                    stringArrayList.add(device.getName());
                }
                else
                {
                    stringArrayList.add(device.getAddress());
                }
                arrayAdapter.notifyDataSetChanged();
            }

            // When discovery cycle finished
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (stringArrayList == null || stringArrayList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "No Devices", Toast.LENGTH_LONG).show();
                }
            }
        }
    };
}