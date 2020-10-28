package com.example.mini2.Organizers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mini2.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrganizerAdapter  extends RecyclerView.Adapter<OrganizerAdapter.OrganizerViewAdapter> {
    private List<Organizers> list;
    private Context context;

    public OrganizerAdapter(List<Organizers> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public OrganizerViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.organizer_layout,parent,false);
        return new OrganizerViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrganizerViewAdapter holder, int position) {
    Organizers item=list.get(position);
        holder.name.setText(item.getName());
        holder.email.setText(item.getEmail());
        holder.contact.setText(item.getPhone());
        holder.name.setText(item.getName());
        holder.name.setText(item.getName());
        Picasso.get().load(item.getImage()).into(holder.imageView);
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Update Info",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OrganizerViewAdapter extends RecyclerView.ViewHolder {
        private TextView name,email,contact;
        private Button update;
        private ImageView imageView;
        public OrganizerViewAdapter(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            email=itemView.findViewById(R.id.email);
            contact=itemView.findViewById(R.id.contact);
            imageView=itemView.findViewById(R.id.imgOrg);
            update=itemView.findViewById(R.id.orgUpdate);


        }
    }
}
