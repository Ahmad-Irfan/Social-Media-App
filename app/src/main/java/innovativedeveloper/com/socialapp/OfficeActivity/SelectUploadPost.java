package innovativedeveloper.com.socialapp.OfficeActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.interstitial.InterstitialAd;

import innovativedeveloper.com.socialapp.R;
import innovativedeveloper.com.socialapp.dataset.Office.MobileInterstitialAd;
import innovativedeveloper.com.socialapp.officeInterface.mobileAdInterface;

public class SelectUploadPost extends AppCompatActivity implements mobileAdInterface {

    TextView etdescription;
    LinearLayout btnimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_upload_post);

        btnimage = findViewById(R.id.btn_image);
        etdescription = findViewById(R.id.txtDescription);

        mobileAdInterface mobileAdInterface = this;
        btnimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobileInterstitialAd mobileInterstitialAd = new MobileInterstitialAd(SelectUploadPost.this,mobileAdInterface,SelectUploadPost.this);
                if(mobileInterstitialAd.loadAd()!=null){
                    startActivity(new Intent(SelectUploadPost.this,PhotoAndVideoUploadActivity.class));
                }

            }
        });


        etdescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectUploadPost.this,TextActivity.class));
            }
        });


    }

    @Override
    public void onAdLoaded(InterstitialAd ad, Activity activity) {
        ad.show(activity);
    }

    @Override
    public void onAdFailedToLoad(String error, Activity activity) {

    }
}