package co.squaretwo.rnfyber;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.fyber.Fyber;
import com.fyber.ads.AdFormat;
import com.fyber.requesters.OfferWallRequester;
import com.fyber.requesters.RequestCallback;
import com.fyber.requesters.RequestError;

/**
 * Created by benyee on 16/01/2016.
 */
public class RNFyberOfferWallModule extends ReactContextBaseJavaModule {
    private static final String TAG = "RNFyberOfferWall";
    private static final int OFFER_WALL_REQUEST = 1;

    private RequestCallback requestCallback;
    private ReactApplicationContext mContext;
    private Intent mOfferWallIntent;

    public RNFyberOfferWallModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext;
    }

    @Override
    public String getName() {
        return TAG;
    }

    @ReactMethod
    public void initializeOfferWall(final String appId, final String securityToken, final String userId) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Settings appId:" + appId);
                Fyber.Settings settings = Fyber.with(appId, getCurrentActivity()).withUserId(userId).withSecurityToken(securityToken).start();
                requestCallback = new RequestCallback() {
                    @Override
                    public void onRequestError(RequestError requestError) {
                        Log.d(TAG, "Something went wrong with the request: " + requestError.getDescription());
                    }

                    @Override
                    public void onAdAvailable(Intent intent) {
                        Log.d(TAG, "Offers are available");
                        mOfferWallIntent = intent;
                    }

                    @Override
                    public void onAdNotAvailable(AdFormat adFormat) {
                        Log.d(TAG, "No ad available");
                    }
                };
                OfferWallRequester.create(requestCallback).request(mContext);
            }
        });
    }

    @ReactMethod
    public void showOfferWall() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
              mContext.startActivityForResult(mOfferWallIntent, OFFER_WALL_REQUEST, null);
              Log.d(TAG, "showOfferWall started");
            }
        });
    }
}
