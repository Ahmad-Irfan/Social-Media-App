package innovativedeveloper.com.socialapp.adapter.office;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import innovativedeveloper.com.socialapp.R;
import innovativedeveloper.com.socialapp.dataset.Office.FriendRequestModelClass;
import innovativedeveloper.com.socialapp.fragment.FriendRequests;
import innovativedeveloper.com.socialapp.officeInterface.showAndHideButton;

public class FirendRequestAdapter extends RecyclerView.Adapter<FirendRequestAdapter.MyviewHolder>{

    Context context;
    List<FriendRequestModelClass> friendRequestModelClassList;
    List<FriendRequestModelClass> frinedRequestAcceptedList;
    DatabaseReference databaseReference;
    String firebaseUser;
    showAndHideButton ShowAndHideButton;

    public FirendRequestAdapter(Context context, List<FriendRequestModelClass> friendRequestModelClassList, showAndHideButton ShowAndHideButton) {
        this.context = context;
        this.friendRequestModelClassList = friendRequestModelClassList;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.ShowAndHideButton = ShowAndHideButton;
        frinedRequestAcceptedList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_request,parent,false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {

        loadFriendRequest();

        holder.textView.setText(friendRequestModelClassList.get(position).getName());
        holder.addFriend.setTag(position);
        holder.addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) v.getTag();

                String uId = friendRequestModelClassList.get(tag).getId();
                FriendRequestModelClass friendRequestModelClass = friendRequestModelClassList.get(tag);
                databaseReference.child(firebaseUser).child("Friends").orderByChild("id").equalTo(uId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                        }else{
                            databaseReference.child(firebaseUser).child("Friends").push().setValue(friendRequestModelClass);
                            holder.linearLayoutBtn.setVisibility(View.GONE);
                            holder.textLayout.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });
    }

    private void loadFriendRequest() {
        databaseReference.child(firebaseUser).child("Friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    frinedRequestAcceptedList.clear();
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        FriendRequestModelClass requestModelClass = dataSnapshot.getValue(FriendRequestModelClass.class);
                        frinedRequestAcceptedList.add(requestModelClass);

                    }

                    for(int i=0; i<friendRequestModelClassList.size(); i++){
                        for(int j=0; j<frinedRequestAcceptedList.size(); j++){



                            if(friendRequestModelClassList.get(i).getId().equals(frinedRequestAcceptedList.get(j).getId())){

                                ShowAndHideButton.hideButton(i);
                            }

                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return friendRequestModelClassList.size();
    }

    class MyviewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        Button addFriend;
        LinearLayout linearLayoutBtn,textLayout;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txtName);
            addFriend = itemView.findViewById(R.id.btnAddFriend);
            linearLayoutBtn = itemView.findViewById(R.id.btnLayout);
            textLayout = itemView.findViewById(R.id.textViewLayout);
        }
    }
}
