package com.neuroengine.beatzone;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neurengine.beatzone.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tr301097 on 07/05/2016.
 */
public class MyBTAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<BluetoothDevice> devicesList;
    public MyBTAdapter(Context context) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        devicesList = new ArrayList<BluetoothDevice>();
    }

    public void addDevice(BluetoothDevice device) {
        if (!devicesList.contains(device)) {
            devicesList.add(device);
        }
    }

    public BluetoothDevice getDevice(int position) {
        return devicesList.get(position);
    }

    public void clear() {
        devicesList.clear();
    }

    @Override
    public int getCount() {
        return this.devicesList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.devicesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BtAdapterViewHolder holder = new BtAdapterViewHolder();;

        View rowView = this.inflater.inflate(R.layout.list_row, null);
        holder.nameAddress = (TextView)rowView.findViewById(R.id.lirTextView);
        holder.nameAddress.setText(this.devicesList.get(position).getName());

        return rowView;
    }
}
