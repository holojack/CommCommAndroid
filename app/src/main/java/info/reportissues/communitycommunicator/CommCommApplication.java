package info.reportissues.communitycommunicator;

import android.app.Application;

/**
 * Created by howardpassmore on 8/2/17.
 */

public class CommCommApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CommCommVolley.getInstance(getApplicationContext()).getRequestQueue();
    }
}
