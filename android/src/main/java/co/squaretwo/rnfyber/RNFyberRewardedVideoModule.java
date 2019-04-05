package co.squaretwo.rnfyber;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.support.annotation.Nullable;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ActivityEventListener;

import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import com.fyber.Fyber;
import com.fyber.ads.AdFormat;
import com.fyber.ads.videos.RewardedVideoActivity;
import com.fyber.requesters.RequestCallback;
import com.fyber.requesters.RequestError;
import com.fyber.requesters.RewardedVideoRequester;


/**
 * Created by benyee on 11/15/2016.
 */


public class RNFyberRewardedVideoModule extends ReactContextBaseJavaModule implements ActivityEventListener {
    private static final String TAG = "RNFyberRewardedVideo";
    protected static final int REWARDED_VIDEO_REQUEST_CODE = 5678;

    private RequestCallback requestCallback;
    private ReactApplicationContext mContext;
    private Intent mRewardedVideoIntent;
    private Callback requestAdCallback;


    public RNFyberRewardedVideoModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext;
        reactContext.addActivityEventListener(this);
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

                    // There seems to be a bug in RewardedVideoRequester where this callback
                    // is called more than once, so this is a workaround for that.
                    private boolean shouldSendEvent = true;

                    @Override
                    public void onRequestError(RequestError requestError) {
                        if (shouldSendEvent) {
                            Log.d(TAG, "Something went wrong with the request: " + requestError.getDescription());
                            sendEvent("rewardedVideoFailedToLoad", null);
                            shouldSendEvent = false;
                        }
                    }

                    @Override
                    public void onAdAvailable(Intent intent) {
                        if (shouldSendEvent) {
                            Log.d(TAG, "Offers are available");
                            mRewardedVideoIntent = intent;
                            sendEvent("rewardedVideoReceived", null);
                            shouldSendEvent = false;
                        }
                    }

                    @Override
                    public void onAdNotAvailable(AdFormat adFormat) {
                        if (shouldSendEvent) {
                            Log.d(TAG, "No ad available");
                            sendEvent("rewardedVideoFailedToLoad", null);
                            callback.invoke("Video is not ready.");
                            shouldSendEvent = false;
                        }
                    }
                };

                RewardedVideoRequester.create(requestCallback).request(mContext);
            }
        });
    }


    private void sendEvent(String eventName, @Nullable WritableMap params) {
        getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    @Override
    public void onNewIntent(Intent intent) {
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
        // handle the closing of the video
        if (resultCode == Activity.RESULT_OK && requestCode == REWARDED_VIDEO_REQUEST_CODE) {

            // check the engagement status
            String engagementResult = intent.getStringExtra(RewardedVideoActivity.ENGAGEMENT_STATUS);
            switch (engagementResult) {
                case RewardedVideoActivity.REQUEST_STATUS_PARAMETER_FINISHED_VALUE:
                    // The user watched the entire video and will be rewarded
                    Log.d(TAG, "The video ad was dismissed because the user completed it");
                    WritableNativeMap finishedResponse = new WritableNativeMap();
                    finishedResponse.putBoolean("wasAborted", false);
                    sendEvent("rewardedVideoClosedByUser", finishedResponse);
                    break;
                case RewardedVideoActivity.REQUEST_STATUS_PARAMETER_ABORTED_VALUE:
                    // The user stopped the video early and will not be rewarded
                    Log.d(TAG, "The video ad was dismissed because the user explicitly closed it");
                    WritableNativeMap abortedResponse = new WritableNativeMap();
                    abortedResponse.putBoolean("wasAborted", true);
                    sendEvent("rewardedVideoClosedByUser", abortedResponse);
                    break;
                case RewardedVideoActivity.REQUEST_STATUS_PARAMETER_ERROR:
                    // An error occurred while showing the video and the user will not be rewarded
                    Log.d(TAG, "The video ad was dismissed error during playing");
                    sendEvent("rewardedVideoClosedByError", null);
                    break;
            }
        }
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
