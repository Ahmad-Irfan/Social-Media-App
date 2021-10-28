package innovativedeveloper.com.socialapp.OfficeActivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import innovativedeveloper.com.socialapp.Login;
import innovativedeveloper.com.socialapp.R;
import innovativedeveloper.com.socialapp.dataset.Office.Hashtag;

public class TextActivity extends AppCompatActivity {

    AutoCompleteTextView  mEditText;
    FirebaseAuth firebaseAuth;

    ConstraintLayout mPictureView;

    int[] textSizes;

    private int currentTextSize = 0;

    int[] backgroundColors;

    private int currentColor = 0;

    String[] fonts;

    private int currentFont = 0;

    ImageButton imageButton,imageButtonChangeAlignment,imageButtonTextSize,imageButtonChangeColor,imageButtonFontChange,imageButtonSave;


    String uname,email,uid,categoryId;
    StorageReference storageReference;
    String timeStamp;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userDbRef,hashtag;

    List<Hashtag> hashtagList;
    ArrayList<String> hashtagstring;
    ArrayAdapter<String> arrayAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        mEditText = findViewById(R.id.editText);
        mPictureView = findViewById(R.id.pictureView);
        imageButton = findViewById(R.id.imageButtonChangeColor);
        imageButtonTextSize = findViewById(R.id.imageButtonTextSize);
        imageButtonChangeColor = findViewById(R.id.imageButtonChangeColor);
        imageButtonChangeAlignment = findViewById(R.id.imageButtonChangeAlignment);
//        autoCompleteTextView = findViewById(R.id.autotext);
        imageButtonSave = findViewById(R.id.imageButtonSaveImage);
        imageButtonFontChange = findViewById(R.id.imageButtonFontChange);
        backgroundColors = getResources().getIntArray(R.array.backgroundColors);
        textSizes = getResources().getIntArray(R.array.fontSize);
        fonts = getResources().getStringArray(R.array.fonts);

        hashtagList = new ArrayList<Hashtag>();
        hashtagstring = new ArrayList<String>();


        storageReference = FirebaseStorage.getInstance().getReference("user posts");
        userDbRef = database.getReference("Users");
        hashtag = database.getReference("hashtag");
        firebaseAuth = FirebaseAuth.getInstance();
        checkUserStatus();





        mEditText.addTextChangedListener(new TextWatcher() {
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


        imageButtonChangeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentColor == 0) currentColor = backgroundColors.length;
                currentColor -= 2;
                changeColor(v);
            }
        });

        imageButtonFontChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFont++;
                if (currentFont >= fonts.length) currentFont = 0;
                String path = fonts[currentFont];

                Typeface typeface = Typeface.createFromAsset(getAssets(), path);
                mEditText.setTypeface(typeface);

                Log.d("FontInfo", path);

            }
        });


        imageButtonTextSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentTextSize == 0) currentTextSize = textSizes.length;
                currentTextSize -= 2;
                changeTextSize(v);

            }
        });

        imageButtonChangeAlignment.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                int alignment = mEditText.getTextAlignment();
                if (alignment == View.TEXT_ALIGNMENT_CENTER) {
                    mEditText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    imageButtonChangeAlignment.setImageResource(R.drawable.ic_baseline_format_align_center_24);
                } else {
                    mEditText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    imageButtonChangeAlignment.setImageResource(R.drawable.ic_baseline_format_align_right_24);
                }

            }
        });

        imageButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap bitmap = Bitmap.createBitmap(
                        mPictureView.getWidth(),
                        mPictureView.getHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                mEditText.setCursorVisible(false);
                mPictureView.draw(canvas);

                Dopost(bitmap);









            }
        });







    }



    void changeColor(View view) {
        currentColor++;
        if (currentColor >= backgroundColors.length) currentColor = 0;
        int color = backgroundColors[currentColor];

        ConstraintLayout rootView = (ConstraintLayout) findViewById(R.id.rootView);
        rootView.setBackgroundColor(color);
        mPictureView.setBackgroundColor(color);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }


    void changeTextSize(View view) {
        currentTextSize++;
        if (currentTextSize >= textSizes.length) currentTextSize = 0;

        int size = textSizes[currentTextSize];
        mEditText.setTextSize(size);
    }


    private void checkUserStatus(){

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){

            email = user.getEmail();
            uid = user.getUid();
        }else{
            startActivity(new Intent(TextActivity.this, Login.class));
            finish();
        }
    }




    private void Dopost(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();

        Calendar cdate = Calendar.getInstance();
        SimpleDateFormat currentdata = new SimpleDateFormat("dd-MMMM-yyyy");
        final String savedate = currentdata.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;
        timeStamp = String.valueOf(System.currentTimeMillis());

        if ( bitmap != null) {
            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getFileExt(bitmap));


            UploadTask uploadTask = reference.putBytes(data);

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
                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("uid", currentuid);
                            hashMap.put("uName", uname);
                            hashMap.put("UEmail", email);
                            hashMap.put("categoryId", categoryId);
                            hashMap.put("pId", timeStamp);
//                                hashMap.put("category",timeStamp);
//                        hashMap.put("pTitle",title);
                            hashMap.put("pDescr", "des");
                            hashMap.put("pImage", downloadUri.toString());
                            hashMap.put("pTime", savetime);
                            hashMap.put("pLikes", "0");
                            hashMap.put("type", "iv");


                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                            ref.child(timeStamp).setValue(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(TextActivity.this, "Category Post Published successfuly", Toast.LENGTH_LONG).show();
                                            /*etdesc.setText("");
                                            imageView.setImageURI(null);*/
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(TextActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                    }
                }
            });
        }
    }


    private Bitmap getFileExt(Bitmap uri){
        return uri;
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
                        InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                    }

                }
                arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,hashtagstring);
                mEditText.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}