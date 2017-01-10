package com.neuroengine.beatzone;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.neurengine.beatzone.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BTActivity extends AppCompatActivity {

    public final static String TAG = BTActivity.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    private String mDeviceName;
    private String mDeviceAddress;
    private ExpandableListView mGattServicesLlist;
    private TextView mConnectionState;
    private boolean mConnected = false;
    private TextView mDataField;
    private BTService mBTService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBTService = ((BTService.LocalBinder) service).getService();
            if (!mBTService.initialize()) {
                Log.e(TAG, "Nao foi possivel inicializar o Bluetooth");
                finish();
            }
            mBTService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBTService = null;
        }
    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BTService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);
                invalidateOptionsMenu();
            } else if (BTService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();
                clearUI();
            } else if(BTService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                displayGattServices(mBTService.getSupportedGattServices());
            } else if(BTService.ACTION_DATA_AVALIABLE.equals(action)) {
                displayData(intent.getStringExtra(BTService.EXTRA_DATA));
            }
        }
    };

    private final ExpandableListView.OnChildClickListener serviceListClickListener = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

            if (mGattCharacteristics != null) {

                final BluetoothGattCharacteristic characteristic = mGattCharacteristics.get(groupPosition).get(childPosition);
                final int charaProp = characteristic.getProperties();

                if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                    if (mNotifyCharacteristic != null) {
                        mBTService.setCharacteristicNotification(mNotifyCharacteristic, false);
                        mNotifyCharacteristic = null;
                    }
                    mBTService.readCharacteristic(characteristic);
                }

                if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                    mNotifyCharacteristic = characteristic;
                    mBTService.setCharacteristicNotification(characteristic, true);
                }
                return true;
            }
            return false;
        }
    };

    private void clearUI() {
        mGattServicesLlist.setAdapter((SimpleExpandableListAdapter)null);
        mDataField.setText(R.string.no_data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        final Intent intent = getIntent();

        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        ((TextView)findViewById(R.id.device_address)).setText(mDeviceAddress);
        mGattServicesLlist = (ExpandableListView)findViewById(R.id.gatt_services_list);
        mGattServicesLlist.setOnChildClickListener(serviceListClickListener);
        mConnectionState = (TextView) findViewById(R.id.connection_state);
        mDataField = (TextView) findViewById(R.id.data_value);

//        getActionBar().setTitle(mDeviceName);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent gattServiceIntent = new Intent(this, BTService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);

        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_connect:
                mBTService.connect(mDeviceAddress);
                return true;
            case R.id.menu_disconnect:
                mBTService.disconnect();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBTService = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBTService != null) {
            final boolean result = mBTService.connect(mDeviceAddress);
            Log.d(TAG, "Resultado da requisicao de conexao: " + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnectionState.setText(resourceId);
            }
        });
    }

    private void displayData(String data) {
        if (data != null)
            mDataField.setText(data);
    }

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null)
            return;

        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);

        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String,String>>>();

        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(LIST_NAME,  HeartBeatGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();

            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(LIST_NAME, HeartBeatGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }

            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }

        SimpleExpandableListAdapter gattServiceAdapter = new SimpleExpandableListAdapter(
                this,
                gattServiceData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] {LIST_NAME, LIST_UUID},
                new int[] {android.R.id.text1, android.R.id.text2},
                gattCharacteristicData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] {LIST_NAME, LIST_UUID},
                new int[] {android.R.id.text1, android.R.id.text2}
        );
        mGattServicesLlist.setAdapter(gattServiceAdapter);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BTService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BTService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BTService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BTService.ACTION_DATA_AVALIABLE);
        return intentFilter;
    }

}
