package com.when.threemb.kommunity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by USER on 03-09-2017.
 */

public class PostActivity extends AppCompatActivity {
    private static final int RC_PHOTO_PICKER = 456;
    ImageButton img;
    EditText title,desc;
    Button submit;
    Uri image,downloadurl;
    ProgressBar pg;
    DatabaseReference mDatabase;
    StorageReference mStorage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        img=(ImageButton)findViewById(R.id.imageButton);
        title=(EditText)findViewById(R.id.etTitle);
        desc=(EditText)findViewById(R.id.etDesc);
        submit=(Button)findViewById(R.id.bSubmit);
        mStorage= FirebaseStorage.getInstance().getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("blog");
        pg=(ProgressBar)findViewById(R.id.progresspost);
        pg.setVisibility(View.INVISIBLE);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pg.setVisibility(View.VISIBLE);
                startPosting();
            }
        });


    }

    public void startPosting() {

        final String tit= title.getText().toString().trim();
        final String des= desc.getText().toString().trim();

        if (!TextUtils.isEmpty(tit)&& !TextUtils.isEmpty(des) && image != null){
            StorageReference filepath = mStorage.child("blog").child(image.getLastPathSegment());
            filepath.putFile(image).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //noinspection VisibleForTests
                    downloadurl =taskSnapshot.getDownloadUrl();


                    DatabaseReference newPost =mDatabase.push();
                    newPost.child("title").setValue(tit);
                    newPost.child("desc").setValue(des);
                    newPost.child("image").setValue(downloadurl.toString());
                    pg.setVisibility(View.GONE);
                    startActivity(new Intent(PostActivity.this,BlogActivity.class));


                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RC_PHOTO_PICKER && resultCode==RESULT_OK){
            image = data.getData();
            img.setImageURI(image);

        }
    }
}
