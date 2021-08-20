package innovativedeveloper.com.socialapp.adapter.office;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.klinker.android.link_builder.Link;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import innovativedeveloper.com.socialapp.R;
import innovativedeveloper.com.socialapp.dataset.Feed;
import innovativedeveloper.com.socialapp.dataset.Office.ModelPost;

public class PostAdapterPhotos extends RecyclerView.Adapter<PostAdapterPhotos.MyHolder>{

    Context context;
    List<ModelPost> postList;

    public PostAdapterPhotos(Context context, List<ModelPost> postList) {
        this.context = context;
        this.postList = postList;
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
        String pDescription = postList.get(position).getPdescr();
        String pImage = postList.get(position).getpImage();



        holder.txtDescription.setText(pDescription);
        holder.txtName.setText(pTitle);

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
/*            this.videoView = view.findViewById(R.id.videoView);
            this.videoThumbnail = (ImageView) view.findViewById(R.id.video_thumbnail);*//*
            this.likesLayout = view.findViewById(R.id.likesLayout);
            this.txtLikesCount = (TextView) view.findViewById(R.id.txtTotalLikes);*/
        }
    }
}
