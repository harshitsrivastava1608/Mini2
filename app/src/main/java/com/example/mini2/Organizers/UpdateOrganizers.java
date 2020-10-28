package com.example.mini2.Organizers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mini2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateOrganizers extends AppCompatActivity {
    private FloatingActionButton fab;
    private RecyclerView cse,ece,me;
    private LinearLayout cseNoData,eceNoData,meNoData;
    private List<Organizers> list1,list2,list3,list4;
    private OrganizerAdapter adapter;
    private DatabaseReference reference,dbref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_organizers);
        fab=findViewById(R.id.fab);
        cseNoData=findViewById(R.id.cseNoData);
        eceNoData=findViewById(R.id.eceNoData);
        meNoData=findViewById(R.id.meNoData);
        cse=findViewById(R.id.cse);
        ece=findViewById(R.id.ece);
        me=findViewById(R.id.me);
        reference= FirebaseDatabase.getInstance().getReference().child("Organizers");
        cseDept();
        eceDept();
        meDept();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateOrganizers.this,AddOrganizers.class));
            }
        });
    }

    private void cseDept() {
        dbref=reference.child("CSE");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1=new ArrayList<>();
                if(!dataSnapshot.exists()){
                    cseNoData.setVisibility(View.VISIBLE);
                    cse.setVisibility(View.GONE);
                }
                else{
                    cseNoData.setVisibility(View.GONE);
                    cse.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Organizers data=snapshot.getValue(Organizers.class);
                        list1.add(data);
                    }
                    cse.setHasFixedSize(true);
                    cse.setLayoutManager(new LinearLayoutManager(UpdateOrganizers.this));
                    adapter=new OrganizerAdapter(list1,UpdateOrganizers.this);
                    cse.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateOrganizers.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void eceDept() {
        dbref=reference.child("ECE");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list2=new ArrayList<>();
                if(!dataSnapshot.exists()){
                    eceNoData.setVisibility(View.VISIBLE);
                    ece.setVisibility(View.GONE);
                }
                else{
                    eceNoData.setVisibility(View.GONE);
                    ece.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Organizers data=snapshot.getValue(Organizers.class);
                        list2.add(data);
                    }
                    ece.setHasFixedSize(true);
                    ece.setLayoutManager(new LinearLayoutManager(UpdateOrganizers.this));
                    adapter=new OrganizerAdapter(list2,UpdateOrganizers.this);
                    ece.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateOrganizers.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void meDept() {
        dbref=reference.child("ME");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list3=new ArrayList<>();
                if(!dataSnapshot.exists()){
                    meNoData.setVisibility(View.VISIBLE);
                    me.setVisibility(View.GONE);
                }
                else{
                    meNoData.setVisibility(View.GONE);
                    me.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Organizers data=snapshot.getValue(Organizers.class);
                        list3.add(data);
                    }
                    me.setHasFixedSize(true);
                    me.setLayoutManager(new LinearLayoutManager(UpdateOrganizers.this));
                    adapter=new OrganizerAdapter(list3,UpdateOrganizers.this);
                    me.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateOrganizers.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}