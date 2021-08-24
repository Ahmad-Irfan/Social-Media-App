package innovativedeveloper.com.socialapp.OfficeActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import innovativedeveloper.com.socialapp.AddPostActivity;
import innovativedeveloper.com.socialapp.Login;
import innovativedeveloper.com.socialapp.R;
import innovativedeveloper.com.socialapp.adapter.CommentsAdapter;
import innovativedeveloper.com.socialapp.adapter.office.MyCommentsAdapter;
import innovativedeveloper.com.socialapp.dataset.Comment;
import innovativedeveloper.com.socialapp.dataset.Office.MyCommentClass;

public class CommentsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText txtComment;
    ImageView btnSend;
    LinearLayout mainLayout, commentLayout;
    Toolbar toolbar;
    CommentsAdapter cAdapter;
    ArrayList<Comment> commentsList;
    String postId, commentId;
    boolean isDisabled;
    boolean isOwnPost = false;
    boolean isReply = false;
    View bottomView;
    AdView mAdView;
    BroadcastReceiver broadcastReceiver;
    String postKey;
   private DatabaseReference userref,commentref;
    String uid;
    ArrayList<MyCommentClass> CommentsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.push_up, R.anim.slide_out);
        setContentView(R.layout.activity_comments2);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        mAdView = (AdView) findViewById(R.id.adView);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        bottomView = findViewById(R.id.bottomView);
        commentsList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        txtComment = (EditText) findViewById(R.id.txtComment);
        btnSend = (ImageView) findViewById(R.id.btnSend);
        mainLayout = (LinearLayout) findViewById(R.id.layoutMain);
        commentLayout = (LinearLayout) findViewById(R.id.commentLayout);
        CommentsList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommentsActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);


        postKey = getIntent().getStringExtra("postKey");
        userref = FirebaseDatabase.getInstance().getReference("Users");
        commentref = FirebaseDatabase.getInstance().getReference("Posts").child(postKey).child("comments");



       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        uid   = user.getUid();

        loadPosts();

       btnSend.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               userref.child(uid).addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                  if(snapshot.exists()){

                      String Name = snapshot.child("name").getValue().toString();
                      processComment(Name);
                  }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
           }
       });



    }

    private void loadPosts() {
        commentref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CommentsList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                   MyCommentClass myCommentClass = ds.getValue(MyCommentClass.class);
                   CommentsList.add(myCommentClass);
                    MyCommentsAdapter myCommentsAdapter = new MyCommentsAdapter(CommentsActivity.this,CommentsList);
                    recyclerView.setAdapter(myCommentsAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void processComment(String name) {
       String comments = txtComment.getText().toString().trim();
       String randomPostKey = uid+""+new Random().nextInt(1000);

        Calendar dateValue = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yy");
        String cdate = dateFormat.format(dateValue.getTime());

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String ctimme = timeFormat.format(dateValue.getTime());

        HashMap Cmnt = new HashMap();
        Cmnt.put("uid",uid);
        Cmnt.put("username",name);
        Cmnt.put("usermsg",comments);
        Cmnt.put("date",cdate);
        Cmnt.put("time",ctimme);

        commentref.child(randomPostKey).updateChildren(Cmnt)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            txtComment.setText("");
                            Toast.makeText(getApplicationContext(),"Comment Added in firebase",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"Comment not Added in firebase",Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Error"+e,Toast.LENGTH_SHORT).show();
            }
        });




    }


}