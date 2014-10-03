package com.example.connorfoody.bluetoothtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import java.util.UUID;
import android.widget.TextView;


public class myActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        // start a new thread to connect and read the serial
        BluetoothReader m_reader = new BluetoothReader(handler,"20:14:04:18:23:59", UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        m_reader.start();

    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            TextView text = (TextView) findViewById(R.id.output);
            if(msg.what == 1){
                text.append( "\n" + msg.obj);
            }
            else if(msg.what == -1){
                text.append("\nERROR: " + msg.obj + "\n");
            }
            else{
                super.handleMessage(msg);
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
