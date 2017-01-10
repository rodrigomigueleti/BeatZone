package com.neuroengine.beatzone;

import android.bluetooth.BluetoothAdapter;
import android.os.Handler;

import java.util.List;

/**
 * Created by tr301097 on 10/05/2016.
 */
public class DeviceScan {
    private BluetoothAdapter adapter;
    private boolean mScanning;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 20000;

    public DeviceScan(BluetoothAdapter adapter) {
        this.adapter = adapter;
        mHandler = new Handler();
    }

    public void scanLeDevice(final boolean enable, final BluetoothAdapter.LeScanCallback mLeScanCallback) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    adapter.stopLeScan(mLeScanCallback);

                }
            }, SCAN_PERIOD);
            mScanning = true;
            adapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            adapter.stopLeScan(mLeScanCallback);
        }

    }



}
