package com.example.connorfoody.sevenminutesoftaha;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

/**
 * Created by connorfoody on 9/27/14.
 */
public class MyThreadRunner extends Thread{
    private SoundReader mic = null;
    private Handler m_handle = null;
    private int count = 0;
    public MyThreadRunner(Handler handle){
        //mic = new SoundReader();
        m_handle = handle;
        this.start();
    }

    public void run(){
        try{
            for(int i = 0; i < 60; i++){
                Thread.sleep(1000);
            }
            count++;
            Message msg = m_handle.obtainMessage(1,  "\t" + count);
            m_handle.sendMessage(msg);
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
            Message msg = m_handle.obtainMessage(1, "err");
            m_handle.sendMessage(msg);
        }
        Message msg = m_handle.obtainMessage(1, "done");
        m_handle.sendMessage(msg);
    }
}
