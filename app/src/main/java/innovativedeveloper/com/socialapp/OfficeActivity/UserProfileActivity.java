package innovativedeveloper.com.socialapp.OfficeActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import innovativedeveloper.com.socialapp.R;
import innovativedeveloper.com.socialapp.adapter.office.PostAdapterPhotos;
import innovativedeveloper.com.socialapp.dataset.Office.MobileInterstitialAd;
import innovativedeveloper.com.socialapp.dataset.Office.ModelPost;
import innovativedeveloper.com.socialapp.dataset.Office.UserModelClass;
import innovativedeveloper.com.socialapp.officeInterface.mobileAdInterface;

public class UserProfileActivity extends AppCompatActivity {
    TextView userName,userNameNew;

    Button addfrnd,followfrnd;
    RecyclerView recyclerView;
    Toolbar toolbar;
    ProgressBar progressBar;
    LinearLayout aboutBox, postsBox;


    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference user,post;
    String hisId,uName,currentUserId;
    PostAdapterPhotos postAdapterPhotos;
    List<ModelPost> modelPostsList;
    ImageView verifiredIcon;
    LinearLayout locationIcon;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile2);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        userName = findViewById(R.id.txtName);
        userNameNew =findViewById(R.id.userName);
        verifiredIcon = findViewById(R.id.verifiedIcon);
        locationIcon = findViewById(R.id.locationBox);
        addfrnd = findViewById(R.id.btnAddFriend);
        followfrnd = findViewById(R.id.btnFollow);
        modelPostsList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        currentUserId = firebaseAuth.getCurrentUser().getUid();
        Log.d("TAG", "onCreate: current id"+currentUserId);
        post = firebaseDatabase.getReference("Posts");
        user = firebaseDatabase.getReference("Users");

        hisId = getIntent().getStringExtra("uid");

        if(TextUtils.isEmpty(hisId)){

            user.orderByChild("uid").equalTo(currentUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for(DataSnapshot snapshot1: snapshot.getChildren()){
                            UserModelClass value = snapshot1.getValue(UserModelClass.class);
                            name = value.getUsername().toString();
                            Log.d("TAG", "onDataChange: user Name"+name);
                        }

                        userNameNew.setText(name);
                        locationIcon.setVisibility(View.VISIBLE);
                        verifiredIcon.setVisibility(View.VISIBLE);
                        addfrnd.setVisibility(View.GONE);
                        followfrnd.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            getUserInfo(currentUserId);
        }else{
            user.orderByChild("uid").equalTo(hisId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for(DataSnapshot snapshot1: snapshot.getChildren()){
                            UserModelClass value = snapshot1.getValue(UserModelClass.class);
                            name = value.getUsername().toString();
                            Log.d("TAG", "onDataChange: user Name"+name);
                        }

                        userNameNew.setText(name);
                        locationIcon.setVisibility(View.VISIBLE);
                        verifiredIcon.setVisibility(View.VISIBLE);
                        addfrnd.setVisibility(View.VISIBLE);
                        followfrnd.setVisibility(View.VISIBLE);


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            getUserInfo(hisId);
        }





        postAdapterPhotos = new PostAdapterPhotos(UserProfileActivity.this,modelPostsList,UserProfileActivity.this);
        recyclerView.setAdapter(postAdapterPhotos);
    }

    private void getUserInfo(String userid) {

        post.orderByChild("uid").equalTo(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelPostsList.clear();
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        ModelPost value = dataSnapshot.getValue(ModelPost.class);
                        modelPostsList.add(value);

                    }
                    postAdapterPhotos.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}