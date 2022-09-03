package com.bashirli.artbook;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bashirli.artbook.databinding.ActivityArtAddBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ArtAdd extends AppCompatActivity {
private ActivityArtAddBinding binding;
ArrayList<Art> artArrayList;

ActivityResultLauncher<String> permissonLauncher;
ActivityResultLauncher<Intent> activityResultLauncher;
Bitmap selectedImage;
    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    binding=ActivityArtAddBinding.inflate(getLayoutInflater());
    View view=binding.getRoot();
    setContentView(view);

        database=this.openOrCreateDatabase("Arts",MODE_PRIVATE,null);
createLauncher();

Intent intent=getIntent();
String info= intent.getStringExtra("info");
if(info.equals("new")){
binding.editTextTextPersonName.setText("");
binding.editTextTextPersonName2.setText("");
binding.button.setVisibility(View.VISIBLE);
binding.imageView.setImageResource(R.drawable.seeee);
}else{
int artId=intent.getIntExtra("artId",0);
binding.button.setVisibility(View.INVISIBLE);

try {
Cursor cursor=database.rawQuery("SELECT * FROM arts where id=?",new String[]{String.valueOf(artId)});
int artNameix=cursor.getColumnIndex("artname");
int surname=cursor.getColumnIndex("surname");
int imageIx=cursor.getColumnIndex("image");
while(cursor.moveToNext()){
    binding.editTextTextPersonName.setText(cursor.getString(artNameix));
    binding.editTextTextPersonName2.setText(cursor.getString(surname));

    byte[] bytes=cursor.getBlob(imageIx);
    Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    binding.imageView.setImageBitmap(bitmap);
}


}catch (Exception e){
    e.printStackTrace();
}

}


    }




public void art_save(View view){
String name=binding.editTextTextPersonName.getText().toString();
String Surname=binding.editTextTextPersonName2.getText().toString();
Bitmap smallImage=makeSmallImage(selectedImage,300);
    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
    smallImage.compress(Bitmap.CompressFormat.PNG,50,byteArrayOutputStream);
    byte[] arraybyte=byteArrayOutputStream.toByteArray();

    try {

       database.execSQL("CREATE TABLE IF NOT EXISTS arts (id INTEGER PRIMARY KEY, artname VARCHAR, surname VARCHAR, image BLOB)");

       String sqlsting="INSERT INTO arts(artname,surname,image) VALUES(?, ?, ?)";
        SQLiteStatement sqLiteStatement=database.compileStatement(sqlsting);
        sqLiteStatement.bindString(1,name);
        sqLiteStatement.bindString(2,Surname);
        sqLiteStatement.bindBlob(3,arraybyte);
        sqLiteStatement.execute();
    }catch (Exception e){
        System.out.println(e);
    }
    Intent intent=new Intent(ArtAdd.this,MainActivity.class);
   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);

    }


public Bitmap makeSmallImage(Bitmap image,int maxSize){
        int width=image.getWidth();
        int height=image.getHeight();
double ratio=width/height;
if(ratio>1){
    width=maxSize;
    height=(int)(width/ratio);
}else{
height=maxSize;
width=(int)(height*ratio);
}
        return image.createScaledBitmap(image,100,100,true);
}



public void select_image(View view){
if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
    if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
        Snackbar.make(view,"Izin gerekli",Snackbar.LENGTH_INDEFINITE).setAction("Icaze ver", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
permissonLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

            }
        }).show();

    }else{
        permissonLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

    }
}else{
    Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
activityResultLauncher.launch(intent);
}

}


public void createLauncher(){
        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
               if(result.getResultCode()==RESULT_OK){
               Intent getResultIntent= result.getData();
               if(getResultIntent!=null){
               Uri imageData=getResultIntent.getData();
          //     binding.imageView.setImageURI(imageData);
            try {
                if(Build.VERSION.SDK_INT>=28){

                    ImageDecoder.Source source=ImageDecoder.createSource(ArtAdd.this.getContentResolver(),imageData);
                    selectedImage=ImageDecoder.decodeBitmap(source);
                    binding.imageView.setImageBitmap(selectedImage);

                }else{
                    selectedImage=MediaStore.Images.Media.getBitmap(ArtAdd.this.getContentResolver(),imageData);
                    binding.imageView.setImageBitmap(selectedImage);
                }

            }catch (Exception e){
                e.printStackTrace();
            }

               }
               }
            }
        });

        permissonLauncher=registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){
Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
activityResultLauncher.launch(intent);
                }else{
                    Toast.makeText(ArtAdd.this, "Icaze verilmedi", Toast.LENGTH_SHORT).show();
                }
            }
        });
}


}