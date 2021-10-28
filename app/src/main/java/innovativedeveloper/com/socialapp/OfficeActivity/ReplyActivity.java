package innovativedeveloper.com.socialapp.OfficeActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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

import de.hdodenhof.circleimageview.CircleImageView;
import innovativedeveloper.com.socialapp.R;
import innovativedeveloper.com.socialapp.adapter.office.MyCommentsAdapter;
import innovativedeveloper.com.socialapp.adapter.office.ReplyCommentAdapter;
import innovativedeveloper.com.socialapp.dataset.Office.MyCommentClass;
import innovativedeveloper.com.socialapp.dataset.Office.ReplyComments;

public class ReplyActivity extends AppCompatActivity {
    Toolbar toolbar;

    RecyclerView recyclerView;
    CircleImageView icon;
    TextView txtComment;
    TextView commentReply;
    TextView txtDate;
    TextView txtReply;
    TextView txtReplies;
    TextView Name;
    ImageView btnMore;
    ImageView btnSend;
    String commentId;
    String postId;
    String uid;
    ArrayList<ReplyComments> ReplyCommentsList;
    ReplyCommentAdapter replyCommentAdapter;



    DatabaseReference commentref, userref,ReplyComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        icon = (CircleImageView) findViewById(R.id.icon);
        txtComment = (TextView) findViewById(R.id.txtComment);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        txtDate = (TextView) findViewById(R.id.txtDate);
        btnMore = (ImageView) findViewById(R.id.btnMore);
        btnSend = (ImageView) findViewById(R.id.btnSend);
        txtReply = (TextView) findViewById(R.id.txtReply);
        txtReplies = (TextView) findViewById(R.id.txtReplies);
        commentReply = (TextView) findViewById(R.id.Comment);
        Name =(TextView)findViewById(R.id.txtName);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        /*mAdView = (AdView) findViewById(R.id.adView);*/
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ReplyCommentsList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ReplyActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
         replyCommentAdapter = new ReplyCommentAdapter(ReplyActivity.this,ReplyCommentsList);
        recyclerView.setAdapter(replyCommentAdapter);

        commentId = getIntent().getStringExtra("cmntId");
         postId = getIntent().getStringExtra("postId");
        Log.d("MyTag",postId);
//        FirebaseDatabase.getInstance().getReference("Posts").child(postKey).child("comments")
        commentref = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("comments");
        userref = FirebaseDatabase.getInstance().getReference("Users");
        ReplyComments = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("comments").child(commentId).child("ReplyComments");
//        FirebaseDatabase.getInstance().getReference("Posts").child(postKey).child("comments")

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        uid   = user.getUid();

        loadPosts();

        commentref.child(commentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    String username = snapshot.child("username").getValue().toString();
                    String msg = snapshot.child("usermsg").getValue().toString();
                    txtComment.setText(msg);
                    Name.setText(username);
                }

  /*              for(DataSnapshot ds:snapshot.getChildren()){
                    MyCommentClass value = ds.getValue(MyCommentClass.class);
                    String username = value.getUsername().toString();
                    String usermsg = value.getUsermsg().toString();

                    Log.d("MyTag",usermsg+"  "+username);


                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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

    private void processComment(String name) {

        String comments = commentReply.getText().toString().trim();
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
        Cmnt.put("commentid",randomPostKey);
        Cmnt.put("postId",postId);

        ReplyComments.child(randomPostKey).updateChildren(Cmnt)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
//                            txtComment.setText("");
                            commentReply.setText("");
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

    private void loadPosts() {
        ReplyComments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReplyCommentsList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    ReplyComments replyComments = ds.getValue(ReplyComments.class);
                    ReplyCommentsList.add(replyComments);
                    replyCommentAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}