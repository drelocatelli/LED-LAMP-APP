package com.home.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.home.db.GroupDevice;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

/* loaded from: classes.dex */
public class BluetoothLeServiceSingle extends Service {
    public static final String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public static final String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public static final String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public static final String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public static final String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";
    public static final String LED = "LED-";
    public static final String LEDCAR_00_ = "LEDCAR-00-";
    public static final String LEDCAR_01_ = "LEDCAR-01-";
    public static final String LEDDMX = "DMX";
    public static final String LEDDMX_00_ = "LEDDMX-00-";
    public static final String LEDDMX_01_ = "LEDDMX-01-";
    public static final String LEDDMX_02_ = "LEDDMX-02-";
    public static final String LEDDMX_03_ = "LEDDMX-03-";
    public static final String LED_ = "LED_";
    public static final String NAME_START_LED = "LED";
    private static final int STATE_CLOSE = 4;
    private static final int STATE_CONNECTED = 2;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_OPEN = 3;
    private static final String TAG = "BluetoothLeServiceSingle";
    public static final UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);
    public static BluetoothGatt mBluetoothGatt;
    private BluetoothAdapter mBluetoothAdapter;
    public String mBluetoothDeviceAddress;
    private BluetoothManager mBluetoothManager;
    public int mConnectionState = 0;
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() { // from class: com.home.service.BluetoothLeServiceSingle.1
        @Override // android.bluetooth.BluetoothGattCallback
        public void onConnectionStateChange(BluetoothGatt bluetoothGatt, int i, int i2) {
            if (i2 == 2) {
                BluetoothLeServiceSingle.this.mConnectionState = 2;
                BluetoothLeServiceSingle.this.broadcastUpdate(BluetoothLeServiceSingle.ACTION_GATT_CONNECTED, bluetoothGatt.getDevice().getName(), bluetoothGatt.getDevice().getAddress());
                BluetoothLeServiceSingle.mBluetoothGatt.discoverServices();
            } else if (i2 == 0) {
                BluetoothLeServiceSingle.this.mConnectionState = 0;
                BluetoothLeServiceSingle.this.broadcastUpdate(BluetoothLeServiceSingle.ACTION_GATT_DISCONNECTED, bluetoothGatt.getDevice().getName(), bluetoothGatt.getDevice().getAddress());
                bluetoothGatt.close();
                Log.e(BluetoothLeServiceSingle.TAG, "onConnectionStateChange: ");
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onServicesDiscovered(BluetoothGatt bluetoothGatt, int i) {
            if (i == 0) {
                BluetoothLeServiceSingle.this.broadcastUpdate(BluetoothLeServiceSingle.ACTION_GATT_SERVICES_DISCOVERED, bluetoothGatt.getDevice().getName(), bluetoothGatt.getDevice().getAddress());
                return;
            }
            String str = BluetoothLeServiceSingle.TAG;
            Log.w(str, "onServicesDiscovered received: " + i);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            if (i == 0) {
                BluetoothLeServiceSingle.this.broadcastUpdate(BluetoothLeServiceSingle.ACTION_DATA_AVAILABLE, bluetoothGattCharacteristic);
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            BluetoothLeServiceSingle.this.broadcastUpdate(BluetoothLeServiceSingle.ACTION_DATA_AVAILABLE, bluetoothGattCharacteristic);
        }
    };
    private final IBinder mBinder = new LocalBinder();

    private static boolean refreshDeviceCache() {
        BluetoothGatt bluetoothGatt = mBluetoothGatt;
        if (bluetoothGatt != null) {
            try {
                Method method = bluetoothGatt.getClass().getMethod("refresh", new Class[0]);
                if (method != null) {
                    return ((Boolean) method.invoke(bluetoothGatt, new Object[0])).booleanValue();
                }
            } catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
        return false;
    }

    public void removePairDevice() {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter != null) {
            for (BluetoothDevice bluetoothDevice : bluetoothAdapter.getBondedDevices()) {
                unpairDevice(bluetoothDevice);
            }
        }
    }

    private void unpairDevice(BluetoothDevice bluetoothDevice) {
        try {
            Class[] clsArr = null;
            Object[] objArr = null;
            bluetoothDevice.getClass().getMethod("removeBond", null).invoke(bluetoothDevice, null);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void broadcastUpdate(String str, String str2, String str3) {
        Intent intent = new Intent(str);
        intent.putExtra(GroupDevice.ADDRESSNUM, str3);
        intent.putExtra("name", str2);
        sendBroadcast(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void broadcastUpdate(String str, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        int i;
        Intent intent = new Intent(str);
        if (UUID_HEART_RATE_MEASUREMENT.equals(bluetoothGattCharacteristic.getUuid())) {
            if ((bluetoothGattCharacteristic.getProperties() & 1) != 0) {
                i = 18;
                Log.d(TAG, "Heart rate format UINT16.");
            } else {
                i = 17;
                Log.d(TAG, "Heart rate format UINT8.");
            }
            int intValue = bluetoothGattCharacteristic.getIntValue(i, 1).intValue();
            Log.d(TAG, String.format("Received heart rate: %d", Integer.valueOf(intValue)));
            intent.putExtra(EXTRA_DATA, String.valueOf(intValue));
        } else {
            byte[] value = bluetoothGattCharacteristic.getValue();
            if (value != null && value.length > 0) {
                StringBuilder sb = new StringBuilder(value.length);
                int length = value.length;
                for (int i2 = 0; i2 < length; i2++) {
                    sb.append(String.format("%02X ", Byte.valueOf(value[i2])));
                }
                intent.putExtra(EXTRA_DATA, sb.toString());
            }
        }
        sendBroadcast(intent);
    }

    /* loaded from: classes.dex */
    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public BluetoothLeServiceSingle getService() {
            return BluetoothLeServiceSingle.this;
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public boolean initialize() {
        if (this.mBluetoothManager == null) {
            BluetoothManager bluetoothManager = (BluetoothManager) getSystemService("bluetooth");
            this.mBluetoothManager = bluetoothManager;
            if (bluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
        BluetoothAdapter adapter = this.mBluetoothManager.getAdapter();
        this.mBluetoothAdapter = adapter;
        if (adapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        return true;
    }

    public boolean connect(String str, String str2) {
        mBluetoothGatt = this.mBluetoothAdapter.getRemoteDevice(str).connectGatt(this, false, this.mGattCallback);
        this.mBluetoothDeviceAddress = str;
        this.mConnectionState = 1;
        return true;
    }

    public void disconnect() {
        BluetoothGatt bluetoothGatt;
        if (this.mBluetoothAdapter == null || (bluetoothGatt = mBluetoothGatt) == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
        } else {
            bluetoothGatt.disconnect();
        }
    }

    public void close() {
        BluetoothGatt bluetoothGatt = mBluetoothGatt;
        if (bluetoothGatt == null) {
            return;
        }
        bluetoothGatt.close();
        mBluetoothGatt = null;
    }

    public void readCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothGatt bluetoothGatt;
        if (this.mBluetoothAdapter == null || (bluetoothGatt = mBluetoothGatt) == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
        } else {
            bluetoothGatt.readCharacteristic(bluetoothGattCharacteristic);
        }
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic bluetoothGattCharacteristic, boolean z) {
        BluetoothGatt bluetoothGatt;
        if (this.mBluetoothAdapter == null || (bluetoothGatt = mBluetoothGatt) == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        bluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, z);
        if (UUID_HEART_RATE_MEASUREMENT.equals(bluetoothGattCharacteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = bluetoothGattCharacteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    public List<BluetoothGattService> getSupportedGattServices() {
        BluetoothGatt bluetoothGatt = mBluetoothGatt;
        if (bluetoothGatt == null) {
            return null;
        }
        return bluetoothGatt.getServices();
    }

    public BluetoothGatt getBluetoothGatt() {
        return mBluetoothGatt;
    }
}
