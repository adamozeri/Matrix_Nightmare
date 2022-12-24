package com.example.android_hw1.Utils;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

public class SignalGenerator {

    private static SignalGenerator signalGenerator = null;

    private Context context;
    private static Vibrator v;


    public SignalGenerator(Context context) {
        this.context = context;
    }

    public static void init(Context context){
        if(signalGenerator == null){
            signalGenerator = new SignalGenerator(context);
            v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
    }

    public static SignalGenerator getInstance(){
        return signalGenerator;
    }

    public void toast(String string){
        Toast
                .makeText(context, string, Toast.LENGTH_SHORT)
                .show();
    }

    public void vibrate(long milliseconds){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(milliseconds);
        }
    }
}
