package com.example.connorfoody.sevenminutesoftaha;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.math.BigDecimal;
/**
 * Created by connorfoody on 9/27/14.
 *
 */
public class SoundReader {

    // see https://code.google.com/p/splmeter/source/browse/trunk/splGUI/src/com/splGUI/SplEngine.java
    private static final int FREQUENCY = 44100;
    private static final int CHANNEL = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private static final int ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord m_recorder = null;

    private volatile int BUFFSIZE = 0;

    public SoundReader(){
        BUFFSIZE = AudioRecord.getMinBufferSize(FREQUENCY, CHANNEL, ENCODING);
        m_recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, FREQUENCY, CHANNEL, ENCODING, BUFFSIZE * 2);
        BUFFSIZE = AudioRecord.getMinBufferSize(FREQUENCY, CHANNEL, ENCODING) * 2;
    }
    public void Calibrate(){

    }
    public double do_run(){
        int SIZE = BUFFSIZE;
        short[] tmp = new short[SIZE];

        // read the sensor value into a buffer
        m_recorder.read(tmp, 0, SIZE);

        // average the sensor values
        double buff_average = 0;
        for(int i = 0; i < SIZE; i++){
            buff_average += Math.abs(tmp[i]);
        }
        buff_average /= SIZE;

        // convert it to the actual level value, this math is apparently valid
        final double k_denom = 0.000002;
        double dB = 20 * Math.log10(buff_average / k_denom);

        final double k_default_calibration_value = -80;
        dB += k_default_calibration_value;

        return dB;

    }
    public double round(double d, int decimalPlace) {
        // see the Javadoc about why we use a String in the constructor
        // http://java.sun.com/j2se/1.5.0/docs/api/java/math/BigDecimal.html#BigDecimal(double)
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

}
