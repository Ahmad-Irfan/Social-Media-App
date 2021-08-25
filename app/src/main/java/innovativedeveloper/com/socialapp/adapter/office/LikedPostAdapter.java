package innovativedeveloper.com.socialapp.adapter.office;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import innovativedeveloper.com.socialapp.R;
import innovativedeveloper.com.socialapp.dataset.Office.LikedUser;

public class LikedPostAdapter extends RecyclerView.Adapter<LikedPostAdapter.MyViewHolder> {

    Context context;
    List<LikedUser> UsersList;

    public LikedPostAdapter(Context context, List<LikedUser> usersList) {
        this.context = context;
        UsersList = usersList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_like,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String Name = UsersList.get(position).getName();
        holder.txtName.setText(Name);
    }

    @Override
    public int getItemCount() {
        return UsersList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView icon;
        TextView txtName;

        public MyViewHolder(@NonNull View view) {
            super(view);

            icon = (CircleImageView) view.findViewById(R.id.icon);
            txtName = (TextView) view.findViewById(R.id.txtName);
        }
    }

}
