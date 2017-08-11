package com.example.edwardmpro.smartlight;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class ControlActivity extends AppCompatActivity {

    RadioButton btnOn, btnOff;
    TextView txtString, txtStringLength, sensorView0, sensorView1, sensorView2, sensorView3;
    TextView txtSendorLDR;
    Handler bluetoothIn;
    final int handlerState = 0;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();
    private ConnectedThread mConnectedThread;
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_control);

        btnOn = (RadioButton) findViewById(R.id.buttonOn);
        btnOff = (RadioButton) findViewById(R.id.buttonOff);

        sensorView0 = (TextView) findViewById(R.id.sensorView0);
        sensorView1 = (TextView) findViewById(R.id.sensorView1);
        sensorView2 = (TextView) findViewById(R.id.sensorView2);
        sensorView3 = (TextView) findViewById(R.id.sensorView3);


        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {
                    String readMessage = (String) msg.obj;
                    recDataString.append(readMessage);
                    int endOfLineIndex = recDataString.indexOf("~");
                    if (endOfLineIndex > 0) {
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);
                        txtString.setText("Datos recibidos = " + dataInPrint);
                        int dataLength = dataInPrint.length();
                        txtStringLength.setText("Tamaño del String = " + String.valueOf(dataLength));

                        if (recDataString.charAt(0) == '#')
                        {
                            String sensor0 = recDataString.substring(1, 5);
                            String sensor1 = recDataString.substring(6, 10);
                            String sensor2 = recDataString.substring(11, 15);
                            String sensor3 = recDataString.substring(16, 20);

                            if(sensor0.equals("1.00"))
                                sensorView0.setText("Encendido");
                            else
                                sensorView0.setText("Apagado");
                            sensorView1.setText(sensor1);
                            sensorView2.setText(sensor2);
                            sensorView3.setText(sensor3);
                        }
                        recDataString.delete(0, recDataString.length());
                    }
                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();


        btnOff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mConnectedThread.write("2");
                Toast.makeText(getBaseContext(), "LED Apagado", Toast.LENGTH_SHORT).show();
            }
        });

        btnOn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mConnectedThread.write("1");
                Toast.makeText(getBaseContext(), "LED Encendido", Toast.LENGTH_SHORT).show();
            }
        });


        CircleMenu circleMe = (CircleMenu) findViewById(R.id.circle_menuOnLED);
        circleMe.setMainMenu(Color.parseColor("#CDCDCD"), R.mipmap.icon_menu, R.mipmap.icon_cancel)
                .addSubMenu(Color.parseColor("#1565C0"), R.drawable.ic_ledon)
                .addSubMenu(Color.parseColor("#C62828"), R.drawable.ic_ledon)
                .addSubMenu(Color.parseColor("#558B2F"), R.drawable.ic_ledon)
                .addSubMenu(Color.parseColor("#EF6C00"), R.drawable.ic_ledon)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {

                    @Override
                    public void onMenuSelected(int index) {
                        switch (index){
                            case 0:
                                mConnectedThread.write("6");
                                Toast.makeText(getBaseContext(), "LED Multicolor", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                mConnectedThread.write("3");
                                Toast.makeText(getBaseContext(), "LED Rojo", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                mConnectedThread.write("4");
                                Toast.makeText(getBaseContext(), "LED Verde", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                mConnectedThread.write("5");
                                Toast.makeText(getBaseContext(), "LED Naranja", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }

                }).setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {

            @Override
            public void onMenuOpened() {}

            @Override
            public void onMenuClosed() {}

        });


        CircleMenu circleMeOff = (CircleMenu) findViewById(R.id.circle_menuOffLED);
        circleMeOff.setMainMenu(Color.parseColor("#CDCDCD"), R.mipmap.icon_menu, R.mipmap.icon_cancel)
                .addSubMenu(Color.parseColor("#1565C0"), R.drawable.ic_leoff)
                .addSubMenu(Color.parseColor("#C62828"), R.drawable.ic_leoff)
                .addSubMenu(Color.parseColor("#558B2F"), R.drawable.ic_leoff)
                .addSubMenu(Color.parseColor("#EF6C00"), R.drawable.ic_leoff)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {

                    @Override
                    public void onMenuSelected(int index) {
                        switch (index){
                            case 0:
                                mConnectedThread.write("2");
                                Toast.makeText(getBaseContext(), "LED Off", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                mConnectedThread.write("2");
                                Toast.makeText(getBaseContext(), "LED Off", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                mConnectedThread.write("2");
                                Toast.makeText(getBaseContext(), "LED Off", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                mConnectedThread.write("2");
                                Toast.makeText(getBaseContext(), "LED Off", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }

                }).setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {

            @Override
            public void onMenuOpened() {}

            @Override
            public void onMenuClosed() {}

        });

    }


    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    @Override
    public void onResume() {
        super.onResume();

        Intent intent = getIntent();

        address = intent.getStringExtra(ListaDevices.EXTRA_DEVICE_ADDRESS);

        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "La creacción del Socket fallo", Toast.LENGTH_LONG).show();
        }
        try
        {
            btSocket.connect();
        } catch (IOException e) {
            try
            {
                btSocket.close();
            } catch (IOException e2)
            {
            }
        }
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();
        mConnectedThread.write("x");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        try
        {
            btSocket.close();
        } catch (IOException e2) {
        }
    }

    private void checkBTState() {

        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "El dispositivo no soporta bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }


        public void run() {
            byte[] buffer = new byte[256];
            int bytes;
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "La Conexión fallo", Toast.LENGTH_LONG).show();
                finish();

            }
        }
    }
}
