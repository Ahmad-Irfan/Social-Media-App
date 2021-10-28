package innovativedeveloper.com.socialapp.dataset.Office;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import innovativedeveloper.com.socialapp.officeInterface.mobileAdInterface;

public class MobileInterstitialAd {
        Context context;
        InterstitialAd interstitialad;
        mobileAdInterface mobileAd;
        Activity activity;
        ProgressDialog dialog = null;
        public MobileInterstitialAd(Context context, mobileAdInterface adInterface,Activity activity) {
            this.context = context;
            mobileAd = adInterface;
            this.activity = activity;
        }

        public InterstitialAd loadAd(){
            dialog = new ProgressDialog(context);
            dialog.setTitle("Please wait ... Ad Loading");
            dialog.show();
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(context, "ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    super.onAdLoaded(interstitialAd);
                    Log.d("TAG", "onAdLoaded: ");
                    interstitialad = interstitialAd;
                    dialog.dismiss();
                    if(interstitialad!= null) {
                        Log.d("TAG", "adLoad: ");
                        mobileAd.onAdLoaded(interstitialAd,activity);
                    }

                    }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    Log.d("TAG", "onAdFailedToLoad: ");
                    dialog.dismiss();
                    interstitialad = null;
                    mobileAd.onAdFailedToLoad(loadAdError.getMessage(),activity);
                }
            });

            return interstitialad;
        }


}
