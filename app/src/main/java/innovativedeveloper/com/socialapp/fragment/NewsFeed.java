package innovativedeveloper.com.socialapp.fragment;

import static android.widget.LinearLayout.HORIZONTAL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
/*import android.support.annotation.Nullable;*/
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
/*import androidx.core.app.Fragment;
import androidx.core.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;*/
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import innovativedeveloper.com.socialapp.*;
import innovativedeveloper.com.socialapp.OfficeActivity.PhotoAndVideoUploadActivity;
import innovativedeveloper.com.socialapp.adapter.FeedItemAdapter;
import innovativedeveloper.com.socialapp.adapter.FeedItemAnimator;
import innovativedeveloper.com.socialapp.adapter.office.PostAdapterPhotos;
import innovativedeveloper.com.socialapp.adapter.office.categoryAdapter;
import innovativedeveloper.com.socialapp.config.AppHandler;
import innovativedeveloper.com.socialapp.config.AppHelper;
import innovativedeveloper.com.socialapp.config.Config;
import innovativedeveloper.com.socialapp.dataset.Feed;
import innovativedeveloper.com.socialapp.dataset.Office.CategoryModel;
import innovativedeveloper.com.socialapp.dataset.Office.ModelPost;
import innovativedeveloper.com.socialapp.dataset.User;
import innovativedeveloper.com.socialapp.officeInterface.AdapterCallBack;
import innovativedeveloper.com.socialapp.officeInterface.SearchPostInterface;
import innovativedeveloper.com.socialapp.services.AppService;

public class NewsFeed extends Fragment implements AdapterCallBack, SearchPostInterface {

    RecyclerView recyclerView;
    RecyclerView categoryRecyclerView;
    ProgressBar progressBar;
    FeedItemAdapter feedAdapter;
    categoryAdapter categoryAdapter;
    ArrayList<ModelPost> postList;
    ArrayList<CategoryModel> categoryModelArrayList;
    ConstraintLayout allPost;
    PostAdapterPhotos adapterPosts;
    LinearLayout emptyFeedView;
    SwipeRefreshLayout swipeRefreshLayout;
    boolean isRefreshing = false;
    boolean isFinalList = false;
    View v;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference post;

    String adapterValue;

    public NewsFeed() {
    }

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_news_feed, container, false);
        setHasOptionsMenu(true);
        firebaseAuth = FirebaseAuth.getInstance();
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        emptyFeedView = (LinearLayout) v.findViewById(R.id.empty_Feed);
//        allPost =v.findViewById(R.id.AllPost);
        categoryModelArrayList = new ArrayList<>();
        categoryRecyclerView = v.findViewById(R.id.categoryRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,true);
        categoryRecyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
        LoadCategory();

        categoryAdapter = new categoryAdapter(getActivity(), categoryModelArrayList,this);
        categoryRecyclerView.setAdapter(categoryAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        database = FirebaseDatabase.getInstance();
        post = database.getReference("Posts");

        postList = new ArrayList<>();

        LinearLayoutManager linearLayoutManagernew = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManagernew);
        linearLayoutManagernew.setReverseLayout(true);
        linearLayoutManagernew.setStackFromEnd(true);

        loadPosts();



        adapterPosts = new PostAdapterPhotos(getActivity(), postList,getActivity());
        recyclerView.setAdapter(adapterPosts);

                emptyFeedView.setVisibility(View.GONE);



        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent, R.color.colorAccentLight);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isRefreshing) {
                    if (AppHandler.getInstance().getAppService().IsAuthorized()) {
//                        loadFeed();
                    }
                }
            }
        });
        return v;


    }

/*    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu,menu);

        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!TextUtils.isEmpty(query)){
                    searchPosts(query);
                }else{
                    loadPosts();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!TextUtils.isEmpty(newText)){
                    searchPosts(newText);
                }else{
                    loadPosts();
                }
                return false;
            }
        });*/

        /*

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            startActivity(new Intent(getActivity(), Search.class));
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            ((MainActivity) getActivity()).setOnDataListener(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void LoadCategory() {
        FirebaseDatabase databaseReference = FirebaseDatabase.getInstance();
        DatabaseReference category = databaseReference.getReference("Post Category");
        category.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryModelArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CategoryModel categoryModel = ds.getValue(CategoryModel.class);
                    categoryModelArrayList.add(categoryModel);
                }
              categoryModelArrayList.add(new CategoryModel("All Posts","263665","https://firebasestorage.googleapis.com/v0/b/social-media-app-5bf33.appspot.com/o/user%20posts%2Fsadcover.jpg?alt=media&token=db80c9c9-82cf-4031-b006-1939a65cfc24"));
                categoryAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                emptyFeedView.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void loadPosts() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelPost modelPost = ds.getValue(ModelPost.class);
                    postList.add(modelPost);
                }
                adapterPosts.notifyDataSetChanged();

                progressBar.setVisibility(View.GONE);
                emptyFeedView.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void  searchPosts(String username, String description,String tag){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelPost modelPost = ds.getValue(ModelPost.class);


                    if((search(username,modelPost.getuName()) && search(description,modelPost.getpDescr())))
                    {
                        postList.add(modelPost);

                    }
                    else if(TextUtils.isEmpty(description) && search(username,modelPost.getuName()))
                    {
                        postList.add(modelPost);
                    }else if(TextUtils.isEmpty(username) && search(description,modelPost.getpDescr())){
                        postList.add(modelPost);
                    }

                }
                adapterPosts.notifyDataSetChanged();

                progressBar.setVisibility(View.GONE);
                emptyFeedView.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onMethodCallback(String categoryId) {
        if(categoryId.equals("263665")){
            loadPosts();

        }else{
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
            Query query = ref.orderByChild("categoryId").equalTo(categoryId);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    postList.clear();
                    if (snapshot.exists()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ModelPost value = ds.getValue(ModelPost.class);
                            postList.add(value);
                        }
                        adapterPosts.notifyDataSetChanged();

                    }


                    progressBar.setVisibility(View.GONE);
                    emptyFeedView.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }



    }

    public boolean search(String filterText,String firebasename ){

        if(!TextUtils.isEmpty(filterText) && firebasename.toLowerCase().contains(filterText) ){
            return true;
        }
        return false;
    }

    @Override
    public void onSearchPost(String username,String description,String tag) {

            searchPosts(username,description,tag);

    }
}

