package com.example.mini2.Organizers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mini2.R;
import com.example.mini2.UploadImage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddOrganizers extends AppCompatActivity {
private ImageView addProfilePic;
private EditText name,email,phone;
private Spinner category;
private Button btnAdd;
private Bitmap bitmap;
String cat=null,downloadUrl="";
private final int REQ=1;
private ProgressDialog pd;
private StorageReference storageReference;
private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_organizers);
        pd=new ProgressDialog(this);
        addProfilePic=findViewById(R.id.addProfilePic);
        name=findViewById(R.id.addName);
        email=findViewById(R.id.addEmail);
        phone=findViewById(R.id.addPhone);
        category=findViewById(R.id.addCateg);
        btnAdd=findViewById(R.id.btnAdd);
        reference= FirebaseDatabase.getInstance().getReference().child("Organizers");
        storageReference= FirebaseStorage.getInstance().getReference();

        addProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        String[] items=new String[]{"Select Category","CSE","ECE","ME","Intercollege","Others"};
        category.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,items));
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cat=category.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
    }

    private void checkValidation() {
        if(name.getText().toString().isEmpty()){
            name.setError("Empty");
            name.requestFocus();
        }
        else if(email.getText().toString().isEmpty()){
            email.setError("Empty");
            email.requestFocus();
        }else if(category.equals("Select Category")){
            Toast.makeText(this,"Please provide category",Toast.LENGTH_SHORT).show();
        }
        else if(bitmap==null){
            insertData();
        }
        else{
            uploadImage();

        }
    }

    private void uploadImage() {
        pd.setMessage("Uploading...");
        pd.show();
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] finalImage=byteArrayOutputStream.toByteArray();
        final StorageReference filePath;
        filePath=storageReference.child("Organizers").child(finalImage+"jpg");
        final UploadTask uploadTask=filePath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(AddOrganizers.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    downloadUrl=String.valueOf(uri);
                                    insertData();
                                }
                            });
                        }
                    });
                }
                else{
                    pd.dismiss();
                    Toast.makeText(AddOrganizers.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void insertData() {
        // databaseReference=databaseReference.child("News");
        final String uniqueKey=reference.child(cat).push().getKey();
        Organizers organizers=new Organizers(name.getText().toString(),email.getText().toString(),
                phone.getText().toString(),downloadUrl,uniqueKey);
        Calendar dcalendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("dd-MM-yy");
        String date=currentDate.format(dcalendar.getTime());
        Calendar tcalendar=Calendar.getInstance();
        SimpleDateFormat currentTime=new SimpleDateFormat("hh:mm a");
        String time=currentTime.format(tcalendar.getTime());
        reference.child(cat).child(uniqueKey).setValue(organizers).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(AddOrganizers.this,"Uploaded Suceessfully!",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AddOrganizers.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent pickImage=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage,REQ);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ && resultCode==RESULT_OK){
            Uri uri=data.getData();
            try {
                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            }
            catch(IOException e){
                e.printStackTrace();
            }
            addProfilePic.setImageBitmap(bitmap);
        }

    }
}