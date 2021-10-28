package innovativedeveloper.com.socialapp.OfficeActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import innovativedeveloper.com.socialapp.R;
import innovativedeveloper.com.socialapp.adapter.office.LikedPostAdapter;
import innovativedeveloper.com.socialapp.dataset.Office.LikedUser;

public class LikedPostActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<LikedUser> LikedUserList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_post);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        LikedUserList = new ArrayList<>();
        String postId = getIntent().getStringExtra("PostId");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Likes");
        DatabaseReference likeRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(postId).child("totalLikes");
        likeRef.child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    String hisId = ""+ds.getRef().getKey();
                    getUser(hisId);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void getUser(String hisId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(hisId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    LikedUser likedUser = ds.getValue(LikedUser.class);
                    LikedUserList.add(likedUser);
                }

                LikedPostAdapter likedPostAdapter = new LikedPostAdapter(LikedPostActivity.this,LikedUserList);
                recyclerView.setAdapter(likedPostAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}