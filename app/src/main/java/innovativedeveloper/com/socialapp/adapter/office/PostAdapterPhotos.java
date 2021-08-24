package innovativedeveloper.com.socialapp.adapter.office;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.klinker.android.link_builder.Link;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import innovativedeveloper.com.socialapp.Comments;
import innovativedeveloper.com.socialapp.OfficeActivity.CommentsActivity;
import innovativedeveloper.com.socialapp.R;
import innovativedeveloper.com.socialapp.dataset.Comment;
import innovativedeveloper.com.socialapp.dataset.Feed;
import innovativedeveloper.com.socialapp.dataset.Office.ModelPost;

public class PostAdapterPhotos extends RecyclerView.Adapter<PostAdapterPhotos.MyHolder>{

    Context context;
    List<ModelPost> postList;
    private DatabaseReference likesRef;
    private  DatabaseReference postsRef;
    String myUid;

    boolean mProcessLike=false;

    public PostAdapterPhotos(Context context, List<ModelPost> postList) {
        this.context = context;
        this.postList = postList;
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed_photo,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {

        String pTitle = postList.get(position).getpTitle();
        String pDescription = postList.get(position).getPdescr();
        String pImage = postList.get(position).getpImage();
        String pLikes = postList.get(position).getpLikes();
       /* String pId = postList.get(position).getpId();*/
        String postIde = postList.get(position).getpId();



        holder.txtDescription.setText(pDescription);
        holder.txtName.setText(pTitle);
        holder.txtLikesCount.setText(pLikes+" Likes");

        setLikes(holder,postIde);





        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    int pLikes = Integer.parseInt(postList.get(position).getpLikes());

                    Log.d("MyTag","Error "+pLikes);
                    mProcessLike = true;
                    likesRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(mProcessLike){
                                if(snapshot.child(postIde).hasChild(myUid)){
                                    postsRef.child(postIde).child("pLikes").setValue(""+(pLikes-1));
                                    likesRef.child(postIde).child(myUid).removeValue();
                                    mProcessLike = false;
                                }else{
                                    postsRef.child(postIde).child("pLikes").setValue(""+(pLikes+1));
                                    likesRef.child(postIde).child(myUid).setValue("Liked");
                                    mProcessLike = false;
                                }
                            }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
        });

        if(pImage.equals("noImage")){
            holder.imgContent.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.GONE);
        }


        try{
            Picasso.with(context).load(pImage).into(holder.imgContent);
            holder.progressBar.setVisibility(View.GONE);

        }catch (Exception e){

        }


        try{
            Picasso.with(context).load(R.drawable.ic_profile).into(holder.photo);
            holder.progressBar.setVisibility(View.GONE);

        }catch (Exception e){

        }


        holder.btnComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentsActivity.class);
                intent.putExtra("postKey",postIde);
                context.startActivity(intent);
            }
        });




    }

    private void setLikes(MyHolder holder, String postKey) {
        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postKey).hasChild(myUid)){
                    /*Toast.makeText(context,"Liked",Toast.LENGTH_SHORT).show();*/
                    holder.viewLike.setImageResource(R.drawable.ic_heart_red_big);

                }else{

                    holder.viewLike.setImageResource(R.drawable.ic_like);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        CircleImageView photo;
        ImageView imgContent;
        View btnComments;
        View btnShare;
        View btnLike;
        ImageView viewLike;
        ImageView btnMore;
        TextView txtDescription;
        TextView txtDate;
        TextView txtName;
        TextView txtLikes;
        TextView txtComments;
        TextView txtShares;
        TextView txtLikesCount;
        ImageView verifiedIcon;
        Feed feedItem;
        ProgressBar progressBar;
        ImageView audience;
        TextView txtShared;
        View sharedView;
        Link sharedProfileLink, sharedPostProfile;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            this.photo = (CircleImageView) itemView.findViewById(R.id.icon);
            this.btnLike = itemView.findViewById(R.id.btnLike);
            this.btnComments = itemView.findViewById(R.id.btnComment);
            this.btnShare = itemView.findViewById(R.id.btnShare);
            this.btnMore = (ImageView) itemView.findViewById(R.id.btnMore);
            this.txtDescription = (TextView) itemView.findViewById(R.id.txtContent);
            this.txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            this.imgContent = (ImageView) itemView.findViewById(R.id.photo_content);
            this.txtName = (TextView) itemView.findViewById(R.id.txtName);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            this.verifiedIcon = (ImageView) itemView.findViewById(R.id.verifiedIcon);
            this.txtLikes = (TextView) itemView.findViewById(R.id.txtLikes);
            this.txtComments = (TextView) itemView.findViewById(R.id.txtComments);
            this.txtShares = (TextView) itemView.findViewById(R.id.txtShares);
            this.audience = (ImageView) itemView.findViewById(R.id.audience);
            this.sharedView = itemView.findViewById(R.id.sharedView);
            this.txtShared = (TextView) itemView.findViewById(R.id.txtShared);
            this.viewLike = (ImageView) itemView.findViewById(R.id.viewLike);
            this.txtLikesCount = (TextView) itemView.findViewById(R.id.txtTotalLikes);
/*           this.videoView = view.findViewById(R.id.videoView);
            this.videoThumbnail = (ImageView) view.findViewById(R.id.video_thumbnail);
            this.likesLayout = view.findViewById(R.id.likesLayout);*/

        }
    }
}
