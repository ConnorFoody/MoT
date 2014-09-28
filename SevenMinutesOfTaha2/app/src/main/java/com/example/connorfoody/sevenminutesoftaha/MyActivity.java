package com.example.connorfoody.sevenminutesoftaha;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;



public class MyActivity extends Activity {
    SoundReader reader = null;
    public Handler mhandle = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 1) {
                TextView text = (TextView) findViewById(R.id.output);
                text.setText("GOOD: " + msg.obj);
            }
            else if(msg.what == -1){
                TextView text = (TextView) findViewById(R.id.output);
                text.setText("ERROR: " + msg.obj);


            }
            else{
                super.handleMessage(msg);

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        //TextView text = (TextView) findViewById(R.id.output);


        reader = new SoundReader(mhandle);
        //mhandle.postDelayed(reader, 3000);
        //mhandle.post(reader);
        reader.start();
        //Thread th = new Thread(reader);
        //th.start();
    }


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
