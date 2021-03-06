package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TaskInfo;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapplication.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMain1Binding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Main1Activity extends AppCompatActivity {

    private ActivityMain1Binding binding;

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_WRITE = 786;

    //TODO: Test if storing bitmaps in array doesn't crash app when possible
    static ArrayList<Bitmap> prevImages = new ArrayList<>();

    //imageview objects for setting previous wallpapers
    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img6;
    ImageView img4;
    ImageView img5;
    Layout layoutToAdd;
    ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //boilerplate code auto generated by android studio
        super.onCreate(savedInstanceState);

        binding = ActivityMain1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main1);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

//        findViewById(R.id.loadingCircle).setVisibility(View.GONE);

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(Main1Activity.this);

        Button resetWallPaperBtn = (Button) findViewById(R.id.reset_wallpaper);

    }

    public void getImage(View arg0) { //getImage and onActivityResult both work together to get image from user gallery
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getImageLauncher.launch(i);

    }


    public ActivityResultLauncher<Intent> getImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();
                        ImageView imageView = (ImageView) findViewById(R.id.imgView);
                        imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                        Bitmap imageBitmap = BitmapFactory.decodeFile(picturePath);
                        try {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(Main1Activity.this);
                            builder1.setMessage("Are you sure you would like to change your wallpaper?");
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            try {
//                                                if(saveImage(imageBitmap)) {
//                                                    Toast.makeText(getBaseContext(), "success",
//                                                            Toast.LENGTH_SHORT).show();
//                                                }
                                                changeWallpaper(imageBitmap);
                                                Toast.makeText(getBaseContext(), "Wallpaper changed",
                                                        Toast.LENGTH_SHORT).show();
                                            } catch (IOException e) {
                                                Toast.makeText(getBaseContext(), "Error",
                                                        Toast.LENGTH_SHORT).show();
                                                e.printStackTrace();
                                            }
                                            dialog.cancel();
                                        }
                                    });


                            builder1.setNegativeButton(
                                    "No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Toast.makeText(getBaseContext(), "Wallpaper unchanged",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        } catch (Exception e) {
                            Toast.makeText(getBaseContext(), "There was an error",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    protected void imgToDb(Bitmap bitmap) {
        DbBitmapUtility util = new DbBitmapUtility();

        util.getBytes(bitmap);
    }

    protected boolean saveImage(Bitmap imageToSave) {

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/images/";
        String fileName = new SimpleDateFormat("yyMMddHHmmss").format(Calendar.getInstance().getTime()) + ".png";

        File dir = new File(path);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(path, fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }


    protected void changeWallpaper(Bitmap bitmap) throws IOException {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        wallpaperManager.setBitmap(bitmap);
    }





    protected void changeToPrevWallpaper() throws IOException {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(Main1Activity.this);
        if(prevImages.size() == 0) {
            Toast.makeText(getBaseContext(), "No previous wallpaper detected",
                    Toast.LENGTH_SHORT).show();
        } else {
            try {
                wallpaperManager.setBitmap(prevImages.get(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(getBaseContext(), "Wallpaper successfully reverted",
                    Toast.LENGTH_SHORT).show();
        }
    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
//            Uri selectedImage = data.getData();
//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//            Cursor cursor = getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            cursor.close();
//            ImageView imageView = (ImageView) findViewById(R.id.imgView);
//            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//            Bitmap imageBitmap = BitmapFactory.decodeFile(picturePath);
//            try {
//                AlertDialog.Builder builder1 = new AlertDialog.Builder(Main1Activity.this);
//                builder1.setMessage("Are you sure you would like to change your wallpaper?");
//                builder1.setCancelable(true);
//
//                builder1.setPositiveButton(
//                        "Yes",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                try {
//                                    changeWallpaper(imageBitmap);
//                                    Toast.makeText(getBaseContext(), "Wallpaper changed",
//                                            Toast.LENGTH_SHORT).show();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                                dialog.cancel();
//                            }
//                        });
//
//
//                builder1.setNegativeButton(
//                        "No",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                Toast.makeText(getBaseContext(), "Wallpaper unchanged",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                AlertDialog alert11 = builder1.create();
//                alert11.show();
//            } finally {
//
//            }
//        } else {
//
//        }
//    }

//    protected Bitmap getCurrentWallpaper() {
//        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
//        try {
//            Bitmap currentWallpaper = ((BitmapDrawable) wallpaperManager.getDrawable()).getBitmap();
//            return currentWallpaper;
//        } catch(SecurityException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
