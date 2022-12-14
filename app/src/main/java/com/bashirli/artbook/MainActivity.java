package com.bashirli.artbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bashirli.artbook.databinding.ActivityMainBinding;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
private ActivityMainBinding binding;
ArrayList<Art> artArrayList;
ArtAdapter artAdapter;


     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     binding=ActivityMainBinding.inflate(getLayoutInflater());
         View view=binding.getRoot();
         setContentView(view);
artArrayList=new ArrayList<Art>();


binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
         artAdapter=new ArtAdapter(artArrayList);
         binding.recyclerView.setAdapter(artAdapter);
getData();

     }
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menuItem){
            Intent intent=new Intent(this,ArtAdd.class);
            intent.putExtra("info","new");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

        public void getData(){
            try {
                SQLiteDatabase sqLiteDatabase=this.openOrCreateDatabase("Arts",MODE_PRIVATE,null);
                Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM arts",null);
                int idIx=cursor.getColumnIndex("id");
                int nameIx=cursor.getColumnIndex("artname");
                while(cursor.moveToNext()){
                    String name=cursor.getString(nameIx);
                    int id=cursor.getInt(idIx);
                    System.out.println(id+ " "+name);
                    Art art=new Art(name,id);
                    artArrayList.add(art);

                }
                artAdapter.notifyDataSetChanged();
                cursor.close();
            }catch (Exception e){
                System.out.println(e);
            }
        }
    }
