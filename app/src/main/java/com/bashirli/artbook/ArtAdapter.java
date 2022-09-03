package com.bashirli.artbook;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bashirli.artbook.databinding.RecyclerXmlBinding;

import java.util.ArrayList;

public class ArtAdapter extends RecyclerView.Adapter<ArtAdapter.ArtHolder> {
   ArrayList<Art> artArrayList;

   public ArtAdapter(ArrayList<Art> artArrayList){
      this.artArrayList=artArrayList;
   }
   public ArtHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      RecyclerXmlBinding recyclerXmlBinding=RecyclerXmlBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
      return new ArtHolder(recyclerXmlBinding);
   }

   @Override
   public void onBindViewHolder(@NonNull ArtHolder holder, int position) {
holder.binding.textview.setText(artArrayList.get(position).name);
holder.itemView.setOnClickListener(new View.OnClickListener() {
   @Override
   public void onClick(View view) {
      Intent intent=new Intent(holder.itemView.getContext(),ArtAdd.class);
      intent.putExtra("info","old");
      intent.putExtra("artId",artArrayList.get(position).id);
      holder.itemView.getContext().startActivity(intent);
   }
});


   }

   @Override
   public int getItemCount() {
      return artArrayList.size();
   }

   public class ArtHolder extends RecyclerView.ViewHolder{
private RecyclerXmlBinding binding;
   public ArtHolder(RecyclerXmlBinding binding){
      super(binding.getRoot());
      this.binding=binding;
   }

}

}
