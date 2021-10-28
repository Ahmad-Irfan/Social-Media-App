package innovativedeveloper.com.socialapp.OfficeActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import innovativedeveloper.com.socialapp.Login;
import innovativedeveloper.com.socialapp.R;
import innovativedeveloper.com.socialapp.dataset.Office.CategoryModel;
import innovativedeveloper.com.socialapp.dataset.Office.Hashtag;
import innovativedeveloper.com.socialapp.dataset.Office.Postmember;

public class PhotoAndVideoUploadActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    ImageView imageView;
    Uri selectedUri;
    private static final int PICK_FILE = 1;
    UploadTask uploadTask ;
    AutoCompleteTextView etdesc;
    Button btnchoosefile,btnuploadfile;
    VideoView videoView;
//    String url,name;
    String uname,email,uid,key,categoryId;
    StorageReference storageReference;
    String timeStamp;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference db1, db2, db3,userDbRef,postcategory,hashtag;

    MediaController mediaController;
    String type;
    Postmember postmember;
    String desc;
    List<Hashtag> hashtagList;
    ArrayList<String> hashtagstring;
    ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_and_video_upload);
        mediaController = new MediaController(this);

        videoView = findViewById(R.id.vv_post);
        imageView = findViewById(R.id.iv_post);
        btnchoosefile = findViewById(R.id.btn_choosefile_post);
        btnuploadfile = findViewById(R.id.btn_uploadfile_post);
        etdesc = findViewById(R.id.et_desc_post);
//        autoCompleteTextView = findViewById(R.id.et_desc_testing);
        postmember = new Postmember();
        hashtagList = new ArrayList<Hashtag>();
        hashtagstring = new ArrayList<String>();



        storageReference = FirebaseStorage.getInstance().getReference("user posts");
        firebaseAuth = FirebaseAuth.getInstance();
        checkUserStatus();




        db1 = database.getReference("All Images").child(uid);
        db2 = database.getReference("All Videos").child(uid);
        db3 = database.getReference("All Posts");
        hashtag = database.getReference("hashtag");
        userDbRef = database.getReference("Users");
        postcategory = database.getReference("Post Category");







        etdesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().contains("#")) {
                    searchHashTag(s);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        postcategory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final List<String> arrayLists = new ArrayList<String>();
                List<CategoryModel> categoryModelList = new ArrayList<CategoryModel>();

                for(DataSnapshot ds:snapshot.getChildren()){
                    CategoryModel categoryModel = ds.getValue(CategoryModel.class);
                    arrayLists.add(categoryModel.getCategory());
                    categoryModelList.add(categoryModel);
                }

                Spinner spin = (Spinner) findViewById(R.id.spinner1);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(PhotoAndVideoUploadActivity.this, android.R.layout.simple_spinner_item,arrayLists);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(adapter);
                spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        categoryId = categoryModelList.get(position).getCategoryId();
                        Log.d("TAG", "onItemSelected: "+categoryId);



                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query query = userDbRef.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    uname = ""+ds.child("name").getValue();
                    email = ""+ds.child("email").getValue();
                    Log.d("MyTag","UserName "+uname+"user email "+email);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        btnuploadfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Dopost();



            }
        });

        btnchoosefile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

    }

    private void searchHashTag(CharSequence s) {

        hashtag.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hashtagList.clear();
                ArrayList<String> stringArrayList = new ArrayList<String>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Hashtag value = dataSnapshot.getValue(Hashtag.class);

                    if(value.getName().toLowerCase().contains(s)){
                        Log.d("TAG", "onDataChange: "+value.getName());
                        if(!hashtagstring.contains(value.getName()))
                            hashtagstring.add(value.getName());

                    }

                }
                arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,hashtagstring);
                etdesc.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/* video/*");
       // intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_FILE);
    }

    private void Dopost() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();
        desc = etdesc.getText().toString();

        Calendar cdate = Calendar.getInstance();
        SimpleDateFormat currentdata = new SimpleDateFormat("dd-MMMM-yyyy");
        final String savedate = currentdata.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;
        timeStamp = String.valueOf(System.currentTimeMillis());

            if (TextUtils.isEmpty(desc) || selectedUri != null) {
                final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getFileExt(selectedUri));
                uploadTask = reference.putFile(selectedUri);

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return reference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            if (type.equals("iv")) {
                                HashMap<Object, String> hashMap = new HashMap<>();
                                hashMap.put("uid", currentuid);
                                hashMap.put("uName", uname);
                                hashMap.put("UEmail", email);
                                hashMap.put("categoryId", categoryId);
                                hashMap.put("pId", timeStamp);
//                                hashMap.put("category",timeStamp);
//                        hashMap.put("pTitle",title);
                                hashMap.put("pDescr", desc);
                                hashMap.put("pImage", downloadUri.toString());
                                hashMap.put("pTime", savetime);
                                hashMap.put("pLikes", "0");
                                hashMap.put("type", "iv");


                               DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                                ref.child(timeStamp).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(PhotoAndVideoUploadActivity.this, "Category Post Published successfuly", Toast.LENGTH_LONG).show();
                                                etdesc.setText("");
                                                imageView.setImageURI(null);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(PhotoAndVideoUploadActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });



                            } else if (type.equals("vv")) {

                                HashMap<Object, String> hashMap = new HashMap<>();

                                hashMap.put("uid", currentuid);
                                hashMap.put("uName", uname);
                                hashMap.put("UEmail", email);
                                hashMap.put("categoryId", categoryId);
                                hashMap.put("pId", timeStamp);
//                        hashMap.put("pTitle",title);
                                hashMap.put("pDescr", desc);
                                hashMap.put("pImage", downloadUri.toString());
                                hashMap.put("pTime", timeStamp);
                                hashMap.put("pLikes", "0");
                                hashMap.put("type", "vv");


                              DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                                ref.child(timeStamp).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(PhotoAndVideoUploadActivity.this, "Post Published successfuly", Toast.LENGTH_LONG).show();
                                                etdesc.setText("");
                                                imageView.setImageURI(null);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(PhotoAndVideoUploadActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });


                            } else {
                                Toast.makeText(PhotoAndVideoUploadActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_FILE || requestCode == RESULT_OK ||
        data!=null || data.getData()!=null){
            selectedUri = data.getData();
            if(selectedUri.toString().contains("image")){
                Picasso.with(this).load(selectedUri).into(imageView);
                imageView.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.INVISIBLE);
                type = "iv";
            }else if(selectedUri.toString().contains("video")){
                videoView.setMediaController(mediaController);
                videoView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                videoView.setVideoURI(selectedUri);
                videoView.start();
                type="vv";
            }
        }
    }

    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void checkUserStatus(){

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){

            email = user.getEmail();
            uid = user.getUid();
        }else{
            startActivity(new Intent(PhotoAndVideoUploadActivity.this, Login.class));
            finish();
        }
    }

}
