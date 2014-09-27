package com.example.connorfoody.sevenminutesoftaha;

import android.widget.TextView;

/**
 * Created by connorfoody on 9/27/14.
 */
public class MyThreadRunner extends Thread {
    private SoundReader mic = null;
    private TextView view = null;

    public MyThreadRunner(TextView view_){
        mic = new SoundReader();
        view = view_;
    }
    public void run(){
        try{
            for(int i = 0; i < 100; i++){
                view.setText("change: " + i);
                //view.setText("change " + mic.do_run());
                //Thread.sleep(1);
                Thread.sleep(10);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
