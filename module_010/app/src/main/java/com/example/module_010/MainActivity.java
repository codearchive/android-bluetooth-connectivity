package com.example.module_010;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Button button_Listen, button_Send, button_ListDevices;
    ListView listView_ListDevices;
    TextView messageBox_Message, textView_Status;
    EditText editText_WriteMessage;

    BluetoothAdapter myBluetoothAdapter;
    BluetoothDevice[] btArray;

    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;
    static final int STATE_CONNECTION_FAILED = 4;
    static final int STATE_MESSAGE_RECEIVED = 5;

    int REQUEST_ENABLE_BLUETOOTH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewByIdes();
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!myBluetoothAdapter.isEnabled()){
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
        }

        implementListeners();
    }

    private void implementListeners() {
        button_ListDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<BluetoothDevice> bt = myBluetoothAdapter.getBondedDevices();
                String[] strings = new String[bt.size()];
                int index = 0;

                if (bt.size() > 0){
                    for (BluetoothDevice device : bt){
                        btArray[index] = device;
                        strings[index] = device.getName();
                        index++;
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, strings);
                    listView_ListDevices.setAdapter(arrayAdapter);
                }
            }
        });
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case STATE_LISTENING:
                    textView_Status.setText("Listening");
                    break;
                case STATE_CONNECTING:
                    textView_Status.setText("Connecting");
                case STATE_CONNECTED:
                    textView_Status.setText("Connected");
                    break;
                case STATE_CONNECTION_FAILED:
                    textView_Status.setText("Connection failed");
                    break;
                case STATE_MESSAGE_RECEIVED:
                    // textView_Status.setText();
            }
            return true;
        }
    });

    private void findViewByIdes() {
        button_Listen = findViewById(R.id.button_Listen);
        button_Send = findViewById(R.id.button_Send);
        button_ListDevices = findViewById(R.id.button_ListDevices);

        listView_ListDevices = findViewById(R.id.listView_ListDevices);

        messageBox_Message = findViewById(R.id.messageBox_Message);
        textView_Status = findViewById(R.id.textView_Status);

        editText_WriteMessage = findViewById(R.id.editText_WriteMessage);
    }
}
