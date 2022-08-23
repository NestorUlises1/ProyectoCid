package com.example.cidv2.Objetos;

import android.content.Context;
import android.provider.Settings;

public class Device {
    public static final String getSecureId(Context context){
        String id= Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return id;
    }
}
