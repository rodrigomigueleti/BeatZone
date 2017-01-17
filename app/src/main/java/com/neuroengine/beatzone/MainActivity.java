package com.neuroengine.beatzone;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.neuroengine.beatzone.R;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_ENABLE_BT = 10;

    private MyBTAdapter adPairedDevicesArray;
    private MyBTAdapter adDiscoverDevicesArray;
    //private List<BluetoothDevice> btDevicesFound;
    private BluetoothGatt mBluetoothGatt;
    private Handler mHandler;
    private boolean mScanning;
    private static final long SCAN_PERIOD = 10000;

    private BluetoothAdapter bta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new Handler();

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Log.i("APP", "BLE nao suportado");
            finish();
        }

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bta = bluetoothManager.getAdapter();


        if (bta == null) {
            Log.i("APP", "no bluetooth");
            return;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!bta.isEnabled()) {
            Intent enableBta = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBta, REQUEST_ENABLE_BT);
        }

        adDiscoverDevicesArray = new MyBTAdapter(this);

        ListView listViewDiscovery = (ListView) findViewById(R.id.listBtDevicesDiscovery);
        listViewDiscovery.setAdapter(adDiscoverDevicesArray);
        listViewDiscovery.setOnItemClickListener(new ClickPairedListener(this));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void listaDispositivosBT(View target) {
        scanLeDevice(true);
    }

    public class ClickPairedListener implements OnItemClickListener {

        private Context contexto;

        public ClickPairedListener(Context context) {
            super();
            this.contexto = context;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView list = (ListView)findViewById(R.id.listBtDevicesDiscovery);
            BluetoothDevice itemValue = (BluetoothDevice)list.getItemAtPosition(position);

            if (itemValue == null)
                return;

            final Intent intent = new Intent(this.contexto, BTActivity.class);
            intent.putExtra(BTActivity.EXTRAS_DEVICE_NAME, itemValue.getName());
            intent.putExtra(BTActivity.EXTRAS_DEVICE_ADDRESS, itemValue.getAddress());

            if (mScanning) {
                bta.stopLeScan(mLeScanCallback);
                mScanning = false;
            }

            startActivity(intent);

            //mBluetoothGatt = itemValue.connectGatt(parent.getContext(), false, mGattCallback);

            //Toast.makeText(getApplicationContext(),itemValue.getAddress(), Toast.LENGTH_LONG).show();
            Log.i("APP", "Device selected: " + itemValue.getName());

        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adDiscoverDevicesArray.addDevice(device);
                    adDiscoverDevicesArray.notifyDataSetChanged();
                    Log.i("APP", "Device Found: " + device.getName() + " - " + device.getAddress());
                }
            });
        }
    };


    public void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    bta.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);
            mScanning = true;
            bta.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            bta.stopLeScan(mLeScanCallback);
        }

    }

}
