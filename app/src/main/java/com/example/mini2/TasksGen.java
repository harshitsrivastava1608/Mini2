package com.example.mini2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TasksGen extends AppCompatActivity {
private EditText task;
private Button btnDone;
    private DatabaseReference databaseReference;
    private StorageReference mStorageRef;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_gen);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        mStorageRef= FirebaseStorage.getInstance().getReference();
        pd=new ProgressDialog(this);
        task=findViewById(R.id.task);
        btnDone=findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pd.setMessage("Uploading...");
                if(task.getText().toString().isEmpty()) {
                    task.setError("Empty");
                    task.requestFocus();
                }
                //databaseReference=databaseReference.child("Notice");
                final String uniqueKey=databaseReference.push().getKey();
                Calendar dcalendar=Calendar.getInstance();
                SimpleDateFormat currentDate=new SimpleDateFormat("dd-MM-yy");
                String date=currentDate.format(dcalendar.getTime());
                Calendar tcalendar=Calendar.getInstance();
                SimpleDateFormat currentTime=new SimpleDateFormat("hh:mm a");
                String time=currentTime.format(tcalendar.getTime());
                databaseReference.child("Notice").child(uniqueKey).setValue(task.getText().toString()+date+time).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.dismiss();
                        Toast.makeText(TasksGen.this,"Notice Uploaded",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(TasksGen.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}