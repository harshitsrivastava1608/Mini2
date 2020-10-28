package com.example.mini2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class UploadPdf extends AppCompatActivity {
    private EditText pdfTitle;
    private Button uploadPdf;
    private ImageButton gallery;
    private final int REQ=1;
    private Uri pdfData;
    String downloadUrl="";
    private DatabaseReference databaseReference;
    private StorageReference mStorageRef;
    private ProgressDialog pd;
    private TextView pdfTextView;
    private String pdfname,title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);
        pd=new ProgressDialog(this);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        mStorageRef= FirebaseStorage.getInstance().getReference();
        pd=new ProgressDialog(this);
        pdfTitle=findViewById(R.id.imgCateg);
        uploadPdf=findViewById(R.id.uploadImg);//button
        gallery=findViewById(R.id.gallery);//to open file
        pdfTextView=findViewById(R.id.pdfTextView);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        uploadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title=pdfTitle.getText().toString();
                if(title.isEmpty()){
                    pdfTitle.setError("Error");
                    pdfTitle.requestFocus();
                }
                else if(pdfData==null){
                    Toast.makeText(UploadPdf.this,"Please Upload Pdf",Toast.LENGTH_SHORT).show();
                }
                else{
                    douploadPdf();
                }
            }
        });
    }

    private void douploadPdf() {
        pd.setTitle("Please Wait...");
        pd.setMessage("Uploading Pdf");
        pd.show();
    StorageReference ref=mStorageRef.child("pdf/"+pdfname+"-"+System.currentTimeMillis()+".pdf");
    ref.putFile(pdfData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isComplete());
            Uri uri=uriTask.getResult();
            uploadData(String.valueOf(uri));
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            pd.dismiss();
            Toast.makeText(UploadPdf.this,"Something Went Wrong!",Toast.LENGTH_SHORT).show();
        }
    });
    }

    private void uploadData(String valueOf) {
        String uniqueKey=databaseReference.child("pdf").push().getKey();
        HashMap data=new HashMap();
        data.put("pdfTitle",title);
        data.put("pdfUrl",downloadUrl);
        databaseReference.child("pdf").child(uniqueKey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(UploadPdf.this,"Pdf Uploaded Successfully",Toast.LENGTH_SHORT).show();
                pdfTitle.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
           Toast.makeText(UploadPdf.this,"Failed To Upload Pdf",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select File"),REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ && resultCode==RESULT_OK){
          pdfData=data.getData();
          if(pdfData.toString().startsWith("content://")){
              try {
                  Cursor cursor=null;
                  cursor=UploadPdf.this.getContentResolver().query(pdfData,null,null,null,null);
                  if(cursor!=null && cursor.moveToFirst()){
                      pdfname=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                  }
              } catch (Exception e) {
                  e.printStackTrace();
              }
          }else if(pdfData.toString().startsWith("file://")){
              pdfname=new File(pdfData.toString()).getName();
          }
          pdfTextView.setText(pdfname);
            Toast.makeText(this,""+pdfData,Toast.LENGTH_SHORT).show();
        }

    }
}