package com.example.connorfoody.sevenminutesoftaha;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.math.BigDecimal;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
/**
 * Created by connorfoody on 9/27/14.
 *
 */
public class SoundReader extends Thread {

    // see https://code.google.com/p/splmeter/source/browse/trunk/splGUI/src/com/splGUI/SplEngine.java
    private static final int FREQUENCY = 44100;
    //private static final int CHANNEL = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private static final int CHANNEL = AudioFormat.CHANNEL_IN_MONO;
    private static final int ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    AudioRecord m_recorder = null;
    private int value = 0;
    private volatile int BUFFSIZE = 0;
    private boolean started = false;

    Handler m_handler = null;

    private static int[] mSampleRates = new int[] { 8000, 11025, 22050, 44100 };
    public AudioRecord findAudioRecord() {
        for (int rate : mSampleRates) {
            for (short audioFormat : new short[] { AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT }) {
                for (short channelConfig : new short[] { AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO }) {
                    try {
                       // Log.d(C.TAG, "Attempting rate " + rate + "Hz, bits: " + audioFormat + ", channel: "
                        //        + channelConfig);
                        int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);

                        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                            // check if we can instantiate and have a success
                            AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, rate, channelConfig, audioFormat, bufferSize);

                            if (recorder.getState() == AudioRecord.STATE_INITIALIZED)
                                return recorder;
                        }
                    } catch (Exception e) {
                        //Log.e(C.TAG, rate + "Exception, keep trying.",e);
                    }
                }
            }
        }
        return null;
    }

    public SoundReader(Handler handle){
        m_handler = handle;
        Message msg = m_handler.obtainMessage(1, "top top");
        m_handler.sendMessage(msg);
        //BUFFSIZE = AudioRecord.getMinBufferSize(FREQUENCY, CHANNEL, ENCODING);
        //m_recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, FREQUENCY, CHANNEL, ENCODING, BUFFSIZE * 2);
        //BUFFSIZE = AudioRecord.getMinBufferSize(FREQUENCY, CHANNEL, ENCODING) * 2;
        started = false;
        //this.start();
    }
    public void run(){
        if(1 == 1) {
            Message msg = m_handler.obtainMessage(1, "start");
            m_handler.sendMessage(msg);

        }
        try {
            //Thread.sleep(4000);
            if(!started || started){
               BUFFSIZE = AudioRecord.getMinBufferSize(FREQUENCY, CHANNEL, ENCODING);
               // m_recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, FREQUENCY, CHANNEL, ENCODING, BUFFSIZE * 2);
                //BUFFSIZE = AudioRecord.getMinBufferSize(FREQUENCY, CHANNEL, ENCODING) * 2;
                m_recorder = findAudioRecord();
                Thread.sleep(1000);
                if(1 == 1) {
                    Message msg = m_handler.obtainMessage(1, "tried to find");
                    m_handler.sendMessage(msg);
                }
                if(m_recorder == null){
                    BUFFSIZE = AudioRecord.getMinBufferSize(FREQUENCY, CHANNEL, ENCODING);
                    m_recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, FREQUENCY, CHANNEL, ENCODING, BUFFSIZE * 2);
                    Thread.sleep(1000);
                    Message msg = m_handler.obtainMessage(1, "looking in other");
                    m_handler.sendMessage(msg);

                }
                Thread.sleep(1000);


                started = true;
                m_recorder.startRecording();
            }
            if(!started){
                Message msg = m_handler.obtainMessage(-1, "wierd stuff");
                m_handler.sendMessage(msg);
            }


            if(m_recorder == null){
                Thread.sleep(1000);
                Message msg = m_handler.obtainMessage(-1, "failed to find anything");
                m_handler.sendMessage(msg);
            }
            else if(m_recorder.getState() == m_recorder.STATE_UNINITIALIZED){
                Thread.sleep(1000);
                Message msg = m_handler.obtainMessage(-1, "uninitialized \t" + m_recorder.getSampleRate());
                m_handler.sendMessage(msg);
            }
            else {
                m_recorder.startRecording();
                Message msg = m_handler.obtainMessage(1, "WORKS!!!!!!");
                m_handler.sendMessage(msg);
                Thread.sleep(1000);

                int SIZE = BUFFSIZE;
                short[] tmp = new short[SIZE];
                if (!started) {
                    m_recorder.startRecording();
                    started = true;
                }
                // read the sensor value into a buffer
                m_recorder.read(tmp, 0, SIZE);

                // average the sensor values
                double buff_average = 0;
                for (int i = 0; i < SIZE - 1; i++) {
                    buff_average += tmp[i] * tmp[i];
                }
                if (SIZE == 0) {
                    //return 2.0;
                }

                //buff_average /= SIZE;

                // convert it to the actual level value, this math is apparently valid
                final double k_denom = 0.000002;
                double dB = 20.0 * Math.log10(buff_average / k_denom);

                final double k_default_calibration_value = 0.0;
                dB += k_default_calibration_value;

                value++;
            }
            //return value;
        }
        catch(Exception e){
            e.printStackTrace();
            Message msg = m_handler.obtainMessage(-1,
                    e.getLocalizedMessage()+"");
            m_handler.sendMessage(msg);
        }

    }
    public double round(double d, int decimalPlace) {
        // see the Javadoc about why we use a String in the constructor
        // http://java.sun.com/j2se/1.5.0/docs/api/java/math/BigDecimal.html#BigDecimal(double)
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

}
