package io.ghoedev.whatsweb;


import android.app.Activity;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class AdManager {
    // Static fields are shared between all instances.
    private static InterstitialAd interstitialAd;
    private static CountDownTimer mCountDownTimer;
    private static final long GAME_LENGTH_MILLISECONDS = 15000;
    private static boolean mGameIsInProgress;
    private static long mTimerMilliseconds;
    private static boolean isInterAdsShowed = false;
    private final Activity activity;
    private String AD_UNIT_ID;


    AdManager(Activity activity, String AD_UNIT_ID) {

        this.activity = activity;
        this.AD_UNIT_ID = activity.getString(R.string.ad_unit_id);
        createAd();
    }

    public void createAd() {
        // Create an ad.
        interstitialAd = new InterstitialAd(activity);
        interstitialAd.setAdUnitId(AD_UNIT_ID);


        new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                interstitialAd = interstitialAd;


                interstitialAd.setFullScreenContentCallback(
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                AdManager.interstitialAd = null;

                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                AdManager.interstitialAd = null;

                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.

                            }
                        });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error

                interstitialAd = null;


            }
        };
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //.addTestDevice(TEST_DEVICE_ID)
                .build();

        // Load the interstitial ad.
        interstitialAd.loadAd(adRequest);
    }

    public static InterstitialAd getAd() {
        if (interstitialAd != null && interstitialAd.isLoaded() && !isInterAdsShowed && !mGameIsInProgress) {
            isInterAdsShowed = true;
            return interstitialAd;
        } else {
            startGame();
        }
        return null;

    }
    public static InterstitialAd showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            return interstitialAd;
        } else {

            startGame();
            return null;
        }
    }
    private static void createTimer(final long milliseconds) {

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        mCountDownTimer = new CountDownTimer(milliseconds, 50) {
            @Override
            public void onTick(long millisUnitFinished) {
                mTimerMilliseconds = millisUnitFinished;

            }
            @Override
            public void onFinish() {
                mGameIsInProgress = false;

            }
        };
    }






    private static void startGame() {
        if (!interstitialAd.isLoaded() && !interstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            interstitialAd.loadAd(adRequest);
        }

        resumeGame(GAME_LENGTH_MILLISECONDS);
    }
    private static void resumeGame(long milliseconds) {
        mGameIsInProgress = true;
        mTimerMilliseconds = milliseconds;
        createTimer(milliseconds);
        mCountDownTimer.start();
    }

}

