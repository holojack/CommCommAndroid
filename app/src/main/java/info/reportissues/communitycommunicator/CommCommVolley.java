package info.reportissues.communitycommunicator;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by howardpassmore on 8/2/17.
 */

public class CommCommVolley {
    private static CommCommVolley mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private CommCommVolley(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized CommCommVolley getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CommCommVolley(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
