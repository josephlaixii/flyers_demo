package com.flyers.db_software_incorporateion.db_flyers;

/**
 * Created by House on 12/9/2016.
 */

import android.app.Application;
import android.content.Context;

/**
 * This custom class is used to Application level things.
 *
 * @author Chintan Rathod (http://www.chintanrathod.com)
 */
public class YourApplication extends Application {

    private static Context mContext;

    public static YourApplication instace;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        instace = this;
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    public static YourApplication getIntance() {
        return instace;
    }
}