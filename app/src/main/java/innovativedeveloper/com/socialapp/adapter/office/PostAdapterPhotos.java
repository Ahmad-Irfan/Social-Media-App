package innovativedeveloper.com.socialapp.adapter.office;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.klinker.android.link_builder.Link;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import innovativedeveloper.com.socialapp.OfficeActivity.LikedPostActivity;
import innovativedeveloper.com.socialapp.OfficeActivity.CommentsActivity;
import innovativedeveloper.com.socialapp.R;
import innovativedeveloper.com.socialapp.dataset.Feed;
import innovativedeveloper.com.socialapp.dataset.Office.MobileInterstitialAd;
import innovativedeveloper.com.socialapp.dataset.Office.ModelPost;
import innovativedeveloper.com.socialapp.officeInterface.mobileAdInterface;

public class PostAdapterPhotos extends RecyclerView.Adapter<PostAdapterPhotos.MyHolder> implements mobileAdInterface {

    Context context;
    List<ModelPost> postList;
    private DatabaseReference likesRef;
    private  DatabaseReference postsRef;
    Activity activity;
    String myUid;

    boolean mProcessLike=false;

    public PostAdapterPhotos(Context context, List<ModelPost> postList,Activity activity) {
        this.context = context;
        this.postList = postList;
        this.activity = activity;
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");


    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed_photo,parent,false);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String pTitle = postList.get(position).getpTitle();
        String pDescription = postList.get(position).getpDescr();
        String pImage = postList.get(position).getpImage();
        String pLikes = postList.get(position).getpLikes();
        int pLike = Integer.parseInt(postList.get(position).getpLikes());
//        String pId = postList.get(position).getpId();
        String postIde = postList.get(position).getpId();
        String type = postList.get(position).getType();
        String userName = postList.get(position).getuName();



        holder.txtDescription.setText(pDescription);
        holder.txtName.setText(userName);
        holder.txtLikesCount.setText(pLikes+" Likes");

        setLikes(holder,postIde);

        holder.txtLikesCount.setTag(position);
        holder.txtLikesCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              int pos = (int) v.getTag();
                Intent intent = new Intent(context, LikedPostActivity.class);
                intent.putExtra("PostId",postList.get(pos).getpId());
                context.startActivity(intent);
            }
        });

        holder.btnLike.setTag(position);
        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                int pLikes = Integer.parseInt(postList.get(pos).getpLikes());
                DatabaseReference likeRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(postList.get(pos).getpId()).child("totalLikes");

                mProcessLike = true;
                postsRef.child(postList.get(pos).getpId()).child("totalLikes").child(postList.get(pos).getpId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(mProcessLike){

                            if(snapshot.hasChild(myUid)){
                                postsRef.child(postList.get(pos).getpId()).child("pLikes").setValue(""+(Integer.parseInt(postList.get(pos).getpLikes())-1));
                                likeRef.child(postList.get(pos).getpId()).child(myUid).removeValue();
                                mProcessLike = false;
                                notifyDataSetChanged();

                            }else{

                                postsRef.child(postList.get(pos).getpId()).child("pLikes").setValue(""+(Integer.parseInt(postList.get(pos).getpLikes())+1));
                                likeRef.child(postList.get(pos).getpId()).child(myUid).setValue("Liked");
                                mProcessLike = false;
                                notifyDataSetChanged();

                            }
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                }
        });

        if(type.equals("iv")){

            if(pImage.equals("noImage")){
                holder.imgContent.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.GONE);
            }


            try{
                Picasso.with(context).load(pImage).into(holder.imgContent);
                holder.playerView.setVisibility(View.INVISIBLE);
                holder.progressBar.setVisibility(View.GONE);

            }catch (Exception e){

            }


            try{
                Picasso.with(context).load(R.drawable.ic_profile).into(holder.photo);
//                holder.videoView.setVisibility(View.INVISIBLE);
                holder.progressBar.setVisibility(View.GONE);

            }catch (Exception e){

            }

        }else if(type.equals("vv")){
            try {
                SimpleExoPlayer player = holder.getExoplayer(pImage);
          //      SimpleExoPlayer player = holder.getExoplayer("https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/1080/Big_Buck_Bunny_1080_10s_1MB.mp4");
               if (holder.isPlaying(player)) {
                    Log.e("TAG1", "play");
                    holder.releasePlayer(player);
                   holder.playerView.setPlayer(player);
                   holder.imgContent.setVisibility(View.INVISIBLE);
                   holder.progressBar.setVisibility(View.INVISIBLE);
                   //holder.setExoplayer("https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/1080/Big_Buck_Bunny_1080_10s_1MB.mp4");
                } else {
                    Log.e("TAG1", "empty");
                   holder.playerView.setPlayer(player);
                   holder.imgContent.setVisibility(View.INVISIBLE);
                   holder.progressBar.setVisibility(View.INVISIBLE);
                   //holder.setExoplayer("https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/1080/Big_Buck_Bunny_1080_10s_1MB.mp4");
                }

            }catch (Exception e){

            }

        }


        mobileAdInterface adInterface = this;
        holder.btnComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobileInterstitialAd mobileInterstitialAd = new MobileInterstitialAd(context,adInterface,activity);
                if(mobileInterstitialAd.loadAd()!=null){
                    Intent intent = new Intent(context, CommentsActivity.class);
                    intent.putExtra("postKey",postIde);
                    context.startActivity(intent);
                }

            }
        });

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) holder.imgContent.getDrawable();
                if(bitmapDrawable == null){
                    shareTextOnly(userName,pDescription);
                }else{
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    shareImageAndText(userName,pDescription,bitmap);
                }
            }
        });

    }

    private void shareImageAndText(String userName, String pDescription, Bitmap bitmap) {

       String shareBody = userName+"\n"+pDescription;

      Uri uri = saveImageToShare(bitmap);

      Intent intent = new Intent(Intent.ACTION_SEND);
      intent.putExtra(Intent.EXTRA_STREAM,uri);
      intent.putExtra(Intent.EXTRA_TEXT,shareBody);
      intent.putExtra(Intent.EXTRA_SUBJECT,"Subject Here");
      intent.setType("image/png");
      context.startActivity(Intent.createChooser(intent,"Share Via"));

    }

    private Uri saveImageToShare(Bitmap bitmap) {
        File imageFolder = new File(context.getCacheDir(),"images");
        Uri uri = null;
        try {
            imageFolder.mkdir();
            File file = new File(imageFolder,"shared_image.png");

            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,90,stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(context,"innovativedeveloper.com.socialapp.fileprovider",file);

        }catch (Exception e){
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

        }
        return uri;
    }

    private void shareTextOnly(String userName, String pDescription) {
     String shareBody = userName+"\n"+pDescription;

     Intent intent = new Intent(Intent.ACTION_SEND);
     intent.setType("text/plain");
     intent.putExtra(Intent.EXTRA_SUBJECT,"Subject Here");
     intent.putExtra(Intent.EXTRA_TEXT,shareBody);
     context.startActivity(Intent.createChooser(intent,"Share Via"));

    }

    private void setLikes(MyHolder holder, String postKey) {
        Log.d("TAG", "setLikes: call one time");
    //    DatabaseReference likeRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(postKey).child("totalLikes");
        postsRef.child(postKey).child("totalLikes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postKey).hasChild(myUid)){
//                    Toast.makeText(context,"Liked",Toast.LENGTH_SHORT).show();
                    holder.viewLike.setImageResource(R.drawable.ic_heart_red);

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

    @Override
    public void onAdLoaded(InterstitialAd ad, Activity activity) {
        ad.show(activity);
    }

    @Override
    public void onAdFailedToLoad(String error, Activity activity) {

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


        private long playbackPosition;
        private int currentWindow;
        private boolean playWhenReady;


        TextView videoText;
//        PlayerView playerView;

        PlayerView playerView;
      /*  ExoPlayer videoView;*/



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
            this.videoText = (TextView) itemView.findViewById(R.id.videoContent);
            this.playerView = itemView.findViewById(R.id.video_content);


//           this.videoView = itemView.findViewById(R.id.video_content);
/*            this.videoThumbnail = (ImageView) view.findViewById(R.id.video_thumbnail);
            this.likesLayout = view.findViewById(R.id.likesLayout);*/



        }
        public SimpleExoPlayer getExoplayer(String url)
        {
            //            this.playerView = itemView.findViewById(R.id.video_content);
//            this.videoText = (TextView) itemView.findViewById(R.id.videoContent);

            try {
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(context).build();
//                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                SimpleExoPlayer exoPlayer = new SimpleExoPlayer.Builder(context).build();
                Uri video = Uri.parse(url);
                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("Video");
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory,extractorsFactory).createMediaSource(MediaItem.fromUri(video));
                playerView.setPlayer(exoPlayer);
                exoPlayer.prepare(mediaSource);
                exoPlayer.setPlayWhenReady(false);
                exoPlayer.seekTo(0, 0);
                return exoPlayer;
            }catch (Exception e){

            }
            return  null;
        }

        private boolean isPlaying(SimpleExoPlayer exoPlayer) {
            return exoPlayer != null
                    && exoPlayer.getPlaybackState() != Player.STATE_ENDED
                    && exoPlayer.getPlaybackState() != Player.STATE_IDLE
                    && exoPlayer.getPlayWhenReady();
        }

        private void releasePlayer(SimpleExoPlayer exoPlayer) {
            if (exoPlayer != null) {
                playbackPosition = exoPlayer.getCurrentPosition();
                currentWindow = exoPlayer.getCurrentWindowIndex();
                playWhenReady = exoPlayer.getPlayWhenReady();
                exoPlayer.release();
                exoPlayer = null;
            }
        }
    }
}
