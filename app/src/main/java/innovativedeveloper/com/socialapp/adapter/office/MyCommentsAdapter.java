package innovativedeveloper.com.socialapp.adapter.office;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;
import innovativedeveloper.com.socialapp.OfficeActivity.ReplyActivity;
import innovativedeveloper.com.socialapp.R;
import innovativedeveloper.com.socialapp.adapter.CommentsAdapter;
import innovativedeveloper.com.socialapp.dataset.Comment;
import innovativedeveloper.com.socialapp.dataset.Office.MyCommentClass;

public class MyCommentsAdapter extends RecyclerView.Adapter<MyCommentsAdapter.MyViewHolder>{

    private Context context;
    private int lastAnimatedPosition = -1;
    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;
    private ArrayList<MyCommentClass> comments;
    private CommentsAdapter.OnItemClickListener onItemClickListener;
    private boolean isOwnPost = false;
    private boolean isReply = false;


    public MyCommentsAdapter(Context context, ArrayList<MyCommentClass> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String uid = comments.get(position).getUid();
        String usermsg = comments.get(position).getUsermsg();
        String username = comments.get(position).getUsername();
        String data = comments.get(position).getData();
        String time = comments.get(position).getTime();
        String commentId = comments.get(position).getCommentid();
        String postId = comments.get(position).getPostId();

        holder.txtComment.setText(usermsg);
        holder.txtDate.setText(time);
        holder.Name.setText(username);
        holder.txtReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReplyActivity.class);
                intent.putExtra("cmntId",commentId);
                intent.putExtra("postId",postId);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView icon;
        TextView txtComment;
        TextView txtDate;
        TextView txtReply;
        TextView txtReplies;
        TextView Name;
        ImageView btnMore;

       public MyViewHolder(@NonNull View view) {
           super(view);

           icon = (CircleImageView) view.findViewById(R.id.icon);
           txtComment = (TextView) view.findViewById(R.id.txtComment);
           txtDate = (TextView) view.findViewById(R.id.txtDate);
           btnMore = (ImageView) view.findViewById(R.id.btnMore);
           txtReply = (TextView) view.findViewById(R.id.txtReply);
           txtReplies = (TextView) view.findViewById(R.id.txtReplies);
           Name =(TextView) view.findViewById(R.id.txtName);
       }
   }
}
