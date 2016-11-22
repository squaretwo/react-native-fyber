package co.squaretwo.rnfyber;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.support.annotation.Nullable;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import com.fyber.Fyber;
import com.fyber.ads.AdFormat;
import com.fyber.requesters.RequestCallback;
import com.fyber.requesters.RequestError;
import com.fyber.requesters.RewardedVideoRequester;


/**
 * Created by benyee on 11/15/2016.
 */
public class RNFyberRewardedVideoModule extends ReactContextBaseJavaModule {
    private static final String TAG = "RNFyberRewardedVideo";
    protected static final int REWARDED_VIDEO_REQUEST_CODE = 5678;

    private RequestCallback requestCallback;
    private ReactApplicationContext mContext;
    private Intent mRewardedVideoIntent;
    private Callback requestAdCallback;


    public RNFyberRewardedVideoModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext;
    }

    @Override
    public String getName() {
        return TAG;
    }

    @ReactMethod
    public void requestRewardedVideo(final Callback callback) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, ">> Requesting Rewarded Video");
                requestCallback = new RequestCallback() {
                    @Override
                    public void onRequestError(RequestError requestError) {
                        Log.d(TAG, "Something went wrong with the request: " + requestError.getDescription());
                        sendEvent("rewardedVideoFailedToLoad", null);
                    }

                    @Override
                    public void onAdAvailable(Intent intent) {
                        Log.d(TAG, "Offers are available");
                        mRewardedVideoIntent = intent;
                        sendEvent("rewardedVideoReceived", null);

                    }

                    @Override
                    public void onAdNotAvailable(AdFormat adFormat) {
                        Log.d(TAG, "No ad available");
                        sendEvent("rewardedVideoFailedToLoad", null);
                        callback.invoke("Video is not ready.");
                    }
                };
                RewardedVideoRequester.create(requestCallback).request(mContext);
            }
        });
    }


    private void sendEvent(String eventName, @Nullable WritableMap params) {
        getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    @ReactMethod
    public void showRewardedVideo() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "showRewardedVideo started!!");
                sendEvent("rewardedVideoDidStart", null);
                mContext.startActivityForResult(mRewardedVideoIntent, REWARDED_VIDEO_REQUEST_CODE, null);
            }
        });
    }
}
