package innovativedeveloper.com.socialapp.adapter.office;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.http.conn.ConnectionPoolTimeoutException;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import innovativedeveloper.com.socialapp.R;
import innovativedeveloper.com.socialapp.dataset.Office.ReplyComments;

public class ReplyCommentAdapter extends RecyclerView.Adapter<ReplyCommentAdapter.MyViewHolder>{

    Context context;
    List<ReplyComments> replyComments;

    public ReplyCommentAdapter(Context context, List<ReplyComments> replyComments) {
        this.context = context;
        this.replyComments = replyComments;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String uid = replyComments.get(position).getUid();
        String usermsg = replyComments.get(position).getUsermsg();
        String username = replyComments.get(position).getUsername();
        String data = replyComments.get(position).getData();
        String time = replyComments.get(position).getTime();
        String commentId = replyComments.get(position).getCommentid();
        String postId = replyComments.get(position).getPostId();

        holder.txtComment.setText(usermsg);
        holder.txtDate.setText(time);
        holder.Name.setText(username);

    }

    @Override
    public int getItemCount() {
        return replyComments.size();
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
