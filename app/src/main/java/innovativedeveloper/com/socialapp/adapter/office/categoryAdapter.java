package innovativedeveloper.com.socialapp.adapter.office;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.klinker.android.link_builder.Link;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import innovativedeveloper.com.socialapp.R;
import innovativedeveloper.com.socialapp.dataset.Feed;
import innovativedeveloper.com.socialapp.dataset.Office.CategoryModel;
import innovativedeveloper.com.socialapp.dataset.Office.ModelPost;
import innovativedeveloper.com.socialapp.officeInterface.AdapterCallBack;

public class categoryAdapter extends RecyclerView.Adapter<categoryAdapter.MyHolder>{


    Context context;
    List<CategoryModel> categoryModelList;
    //    private DatabaseReference likesRef;
    private DatabaseReference postsRef;
    String myUid;
     AdapterCallBack mAdapterCallBack;


    public categoryAdapter(Context context, List<CategoryModel> modelList,AdapterCallBack callback) {
        this.context = context;
        this.categoryModelList = modelList;
        this.mAdapterCallBack = callback;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.categoryitem,parent,false);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String category = categoryModelList.get(position).getCategory();
        String image = categoryModelList.get(position).getImage();
        String categoryId = categoryModelList.get(position).getCategoryId();
        Log.d("TAG", "onBindViewHolder: "+category);

        holder.imgText.setText(category);
        Picasso.with(context).load(image).into(holder.imageView);

        holder.imageView.setTag(position);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            int pos  = (int)v.getTag();
                mAdapterCallBack.onMethodCallback(categoryModelList.get(pos).getCategoryId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ShapeableImageView imageView;
        TextView imgText;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            imgText = itemView.findViewById(R.id.imageText);

        }
    }
}
