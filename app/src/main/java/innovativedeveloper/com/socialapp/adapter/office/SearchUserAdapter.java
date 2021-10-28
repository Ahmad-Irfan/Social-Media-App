package innovativedeveloper.com.socialapp.adapter.office;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;
import innovativedeveloper.com.socialapp.OfficeActivity.UserProfileActivity;
import innovativedeveloper.com.socialapp.R;
import innovativedeveloper.com.socialapp.dataset.Office.FriendRequestModelClass;
import innovativedeveloper.com.socialapp.dataset.Office.MobileInterstitialAd;
import innovativedeveloper.com.socialapp.dataset.Office.SendFriendRequest;
import innovativedeveloper.com.socialapp.dataset.Office.UserModelClass;
import innovativedeveloper.com.socialapp.officeInterface.mobileAdInterface;
import innovativedeveloper.com.socialapp.officeInterface.showAndHideButton;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.MyViewHolder>{

    Context mContext;
    List<UserModelClass> userModelClassList;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference users;
    DatabaseReference sendRequest;
    String myUid;
    String name;
    Activity activity;
    showAndHideButton ShowAndHideButton;
    List<SendFriendRequest>  sendFriendRequestList;



    public SearchUserAdapter(Context mContext, List<UserModelClass> userModelClassList,Activity activity,showAndHideButton ShowAndHideButton) {
        this.mContext = mContext;
        this.userModelClassList = userModelClassList;
        firebaseDatabase = FirebaseDatabase.getInstance();
        users = firebaseDatabase.getReference("Users");
        sendRequest = firebaseDatabase.getReference("Users");
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.activity = activity;
        this.ShowAndHideButton = ShowAndHideButton;
        sendFriendRequestList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


       hideAndShowFriendBtn();



        holder.textName.setText(userModelClassList.get(position).getUsername());
        holder.linearLayoutName.setTag(position);
        holder.linearLayoutName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) v.getTag();
                Intent intent = new Intent(mContext, UserProfileActivity.class);
                intent.putExtra("uid",userModelClassList.get(tag).getUid());
                mContext.startActivity(intent);
/*                Intent intent = new Intent(mContext, UserProfileActivity.class);
                intent.putExtra("uid",userModelClassList.get(tag).getUid());
                mContext.startActivity(intent);
                holder.progressDialog = new ProgressDialog(mContext);
                holder.progressDialog.setMessage("Please Wait Loading...");
                holder.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                holder.progressDialog.setIndeterminate(true);
                holder.progressDialog.show();

                //  old implementation

                MobileInterstitialAd mobileInterstitialAd = new MobileInterstitialAd(mContext.getApplicationContext());
//               InterstitialAd interstitialAd = mobileInterstitialAd.loadAd();
                InterstitialAd interstitialAd = new InterstitialAd(mContext.getApplicationContext());
               if(interstitialAd!=null){
                   interstitialAd.show((Activity) mContext);
                   Log.d("TAG", "onClick: ad loaded in adapter");
               }else{
                   Log.d("TAG", "onClick: ad loaded in adapter null");
               }

                AdRequest adRequest = new AdRequest.Builder().build();

                InterstitialAd.load(mContext,"ca-app-pub-3940256099942544/1033173712", adRequest,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                // The mInterstitialAd reference will be null until
                                // an ad is loaded.
                                holder.mInterstitialAd = interstitialAd;
                                if (holder.mInterstitialAd != null) {

                                    holder.mInterstitialAd.show((Activity) mContext);
                                    holder.mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                                        @Override
                                        public void onAdDismissedFullScreenContent() {
                                            // Called when fullscreen content is dismissed.
                                            Log.d("TAG", "The ad was dismissed.");
                                            holder.progressDialog.dismiss();
                                            Intent intent = new Intent(mContext, UserProfileActivity.class);
                                            intent.putExtra("uid",userModelClassList.get(tag).getUid());
                                            mContext.startActivity(intent);
                                        }

                                        @Override
                                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                                            // Called when fullscreen content failed to show.
                                            Log.d("TAG", "The ad failed to show.");
                                            holder.progressDialog.dismiss();
                                            Intent intent = new Intent(mContext, UserProfileActivity.class);
                                            intent.putExtra("uid",userModelClassList.get(tag).getUid());
                                            mContext.startActivity(intent);
                                        }

                                        @Override
                                        public void onAdShowedFullScreenContent() {
                                            // Called when fullscreen content is shown.
                                            // Make sure to set your reference to null so you don't
                                            // show it a second time.
                                            holder.mInterstitialAd = null;
                                            holder.progressDialog.dismiss();
                                            Log.d("TAG", "The ad was shown.");
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error
                                Log.i("TAG", loadAdError.getMessage());
                                holder.mInterstitialAd = null;
                                holder.progressDialog.dismiss();
                                Intent intent = new Intent(mContext, UserProfileActivity.class);
                                intent.putExtra("uid",userModelClassList.get(tag).getUid());
                                mContext.startActivity(intent);
                            }
                        });*/

            }
        });

        holder.btnAddFrnd.setTag(position);
        holder.btnAddFrnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              int tag = (int) v.getTag();


              holder.btnAddFrnd.setVisibility(View.GONE);
              holder.imageDone.setVisibility(View.VISIBLE);
              String hisUid = userModelClassList.get(tag).getUid();

              users.child(myUid).addValueEventListener(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if(snapshot.exists()){
                   name =  snapshot.child("username").getValue(String.class);
                     String uid = userModelClassList.get(tag).getUid();
                     HashMap hashMap = new HashMap();
                     hashMap.put("id",myUid);
                     hashMap.put("name",name);
                     hashMap.put("status","Received");
                     users.child(uid).child("ReceiveRequests").orderByChild("id").equalTo(myUid).addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot snapshot) {
                             if(snapshot.exists()){

                             }else{
                                 users.child(uid).child("ReceiveRequests").push().setValue(hashMap);
                                 users.child(hisUid).addValueEventListener(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        String name = snapshot.child("name").getValue(String.class);
                                        FriendRequestModelClass friendRequestModelClass = new FriendRequestModelClass(hisUid,name,"send");

                                        users.child(myUid).child("SendRequest").orderByChild("id").equalTo(hisUid).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists()){

                                                }else{
                                                    FirebaseDatabase.getInstance().getReference("Users").child(myUid)
                                                            .child("SendRequest").push().setValue(friendRequestModelClass);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });



                                    }
                                     }

                                     @Override
                                     public void onCancelled(@NonNull DatabaseError error) {

                                     }
                                 });

                             }
                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError error) {

                         }
                     });
     /*                holder.btnAddFrnd.setVisibility(View.GONE);
                     holder.imageDone.setVisibility(View.VISIBLE);*/
                 }
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {

                  }
              });
            }
        });


    }


    private void hideAndShowFriendBtn() {
        users.child(myUid).child("SendRequest").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             if(snapshot.exists()){
                 sendFriendRequestList.clear();
                 for(DataSnapshot snapshot1: snapshot.getChildren()){


                     SendFriendRequest sendFriendRequest = snapshot1.getValue(SendFriendRequest.class);
                     sendFriendRequestList.add(sendFriendRequest);
                 }




                 for(int i=0; i<userModelClassList.size(); i++){
                     for(int j=0; j<sendFriendRequestList.size(); j++){



                         if(userModelClassList.get(i).getUid().equals(sendFriendRequestList.get(j).getId())){

                             ShowAndHideButton.hideButton(i);
                         }

                     }
                 }
             }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return userModelClassList.size();
    }

/*
    @Override
    public void onAdLoaded(InterstitialAd ad,Activity activity) {
      ad.show(activity);
        Intent intent = new Intent(mContext, UserProfileActivity.class);
        intent.putExtra("uid",userModelClassList.get().getUid());
        mContext.startActivity(intent);
    }

    @Override
    public void onAdFailedToLoad(String error,Activity activity) {

    }
*/

    class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView circleImageView;
        TextView textName;
        LinearLayout linearLayoutName;
        Button btnAddFrnd;
        ProgressDialog progressDialog;
        ImageView imageDone;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.icon);
            textName = itemView.findViewById(R.id.txtName);
            linearLayoutName = itemView.findViewById(R.id.linearLayoutName);
            btnAddFrnd = itemView.findViewById(R.id.btnAddFriend);
            imageDone = itemView.findViewById(R.id.image);


        }

    }
}
