package com.lin.aidltest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.baby.dataservice.BabyAidlInterface;

import java.util.Objects;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private BabyAidlInterface mIBinder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIBinder = BabyAidlInterface.Stub.asInterface(service);
            Log.d(TAG, "绑定服务成功！");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private EditText etBabyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnBindService = findViewById(R.id.btn_bind_service);
        Button btnGetData = findViewById(R.id.btn_get_data);
        btnBindService.setOnClickListener(this);
        btnGetData.setOnClickListener(this);
        etBabyId = findViewById(R.id.et_baby_id);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bind_service:
                Intent intent = new Intent();
                intent.setAction("com.baby.dataservice.MyService");
                intent.setPackage("com.baby.dataservice");
                bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btn_get_data:
                if (mIBinder == null || Objects.equals(etBabyId.getText().toString(), "")) {
                    Log.d(TAG, "绑定服务没有成或者输入id为空！");
                    return;
                }
                try {
                    String name = mIBinder.getBabyName(Integer.parseInt(etBabyId.getText().toString()));
                    Log.d(TAG, "获取到的名字为:" + name);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
