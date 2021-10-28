package innovativedeveloper.com.socialapp.fragment.officeFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import innovativedeveloper.com.socialapp.R;
import innovativedeveloper.com.socialapp.adapter.office.SearchUserAdapter;
import innovativedeveloper.com.socialapp.dataset.Office.UserModelClass;
import innovativedeveloper.com.socialapp.dataset.User;
import innovativedeveloper.com.socialapp.officeInterface.mobileAdInterface;
import innovativedeveloper.com.socialapp.officeInterface.showAndHideButton;

public class SearchPeopleFragment extends Fragment implements showAndHideButton {


    RecyclerView recyclerView;
    List<UserModelClass> userModelClassList;
    SearchUserAdapter searchUserAdapter;

    FirebaseDatabase firebaseDatabase;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference user;
    EditText editTextusername;



    public SearchPeopleFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search_people2, container, false);
        recyclerView= v.findViewById(R.id.userRecyclerView);
        editTextusername = v.findViewById(R.id.etusername);
        database = FirebaseDatabase.getInstance();
        user = database.getReference("Users");
        userModelClassList = new ArrayList<>();




        LinearLayoutManager linearLayoutManagernew = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManagernew);
        searchUserAdapter = new SearchUserAdapter(getActivity(),userModelClassList,getActivity(),this);
        recyclerView.setAdapter(searchUserAdapter);
        loadusers("");





// banner add

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(getActivity());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        adView = v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
// TODO: Add adView to your view hierarchy.







        editTextusername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                    loadusers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {




            }
        });



        return v;
    }

    private void loadusers(String s) {

        if(s.isEmpty()){
            user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userModelClassList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);

                            userModelClassList.add(userModelClass);

                    }

                    searchUserAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }else{
            user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userModelClassList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                        if (userModelClass.getUsername().toLowerCase().contains(s.toLowerCase())) {

                            userModelClassList.add(userModelClass);


                        }

                    }

                    searchUserAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    @Override
    public void ShowButton(int position) {

        RecyclerView.ViewHolder rv_view = recyclerView.findViewHolderForAdapterPosition(position);
        Log.d("TAG", "hideButton: else condition"+position);
        rv_view.itemView.findViewById(R.id.image).setVisibility(View.GONE);
       rv_view.itemView.findViewById(R.id.btnAddFriend).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideButton(int position) {


        RecyclerView.ViewHolder rv_view = recyclerView.findViewHolderForAdapterPosition(position);
        Log.d("TAG", "hideButton: if condition"+position);
        rv_view.itemView.findViewById(R.id.image).setVisibility(View.VISIBLE);
        rv_view.itemView.findViewById(R.id.btnAddFriend).setVisibility(View.GONE);


    }

}