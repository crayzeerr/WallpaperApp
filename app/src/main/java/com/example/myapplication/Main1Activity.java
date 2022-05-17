package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TaskInfo;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.io.IOException;
import java.util.ArrayList;

public class Main1Activity extends AppCompatActivity {

    private ActivityMain1Binding binding;
    private static int RESULT_LOAD_IMAGE = 1;
    ArrayList<Bitmap> prevImages = new ArrayList<>();
    ImageView img1 = (ImageView) findViewById(R.id.imageView1);
    ImageView img2 = (ImageView) findViewById(R.id.imageView2);
    ImageView img3 = (ImageView) findViewById(R.id.imageView3);
    ImageView img6 = (ImageView) findViewById(R.id.imageView6);
    ImageView img4 = (ImageView) findViewById(R.id.imageView4);
    ImageView img5 = (ImageView) findViewById(R.id.imageView5);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(Main1Activity.this);

        // PLACEHOLDER IMAGE VIEW OBJECTS FOR STORING HISTORY
//


        Button resetWallPaperBtn = (Button) findViewById(R.id.reset_wallpaper);

    }

    public void getImage(View arg0) { //getImage and onActivityResult both work together to get image from user gallery
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getImageLauncher.launch(i);
//        startActivityForResult(i, RESULT_LOAD_IMAGE);

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
                                                changeWallpaper(imageBitmap);
                                                if(prevImages.size() < 9) { prevImages.add(imageBitmap); }
                                                if(prevImages.size() == 9) {
                                                    prevImages.remove(prevImages.size() - 1);
                                                    prevImages.add(0, imageBitmap);

                                                }
                                                if(prevImages.size() == 1) {
                                                    img1.setImageBitmap(prevImages.get(0));


                                                } else if (prevImages.size() == 2) {
                                                    img1.setImageBitmap(prevImages.get(0));
                                                    img2.setImageBitmap(prevImages.get(1));

                                                } else if (prevImages.size() == 3) {
                                                    img1.setImageBitmap(prevImages.get(0));
                                                    img2.setImageBitmap(prevImages.get(1));
                                                    img3.setImageBitmap(prevImages.get(2));

                                                } else if (prevImages.size() == 4) {
                                                    img1.setImageBitmap(prevImages.get(0));
                                                    img2.setImageBitmap(prevImages.get(1));
                                                    img3.setImageBitmap(prevImages.get(2));
                                                    img4.setImageBitmap(prevImages.get(3));

                                                } else if (prevImages.size() == 5) {
                                                    img1.setImageBitmap(prevImages.get(0));
                                                    img2.setImageBitmap(prevImages.get(1));
                                                    img3.setImageBitmap(prevImages.get(2));
                                                    img4.setImageBitmap(prevImages.get(3));
                                                    img5.setImageBitmap(prevImages.get(4));


                                                } else if (prevImages.size() == 6) {
                                                    img1.setImageBitmap(prevImages.get(0));
                                                    img2.setImageBitmap(prevImages.get(1));
                                                    img3.setImageBitmap(prevImages.get(2));
                                                    img4.setImageBitmap(prevImages.get(3));
                                                    img5.setImageBitmap(prevImages.get(4));
                                                    img6.setImageBitmap(prevImages.get(5));
                                                }
                                                Toast.makeText(getBaseContext(), "Wallpaper changed",
                                                        Toast.LENGTH_SHORT).show();
                                            } catch (IOException e) {
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
                        } finally {

                        }
                    }
                }
            });

    protected void changeWallpaper(Bitmap bitmap) throws IOException {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        wallpaperManager.setBitmap(bitmap);
    }

    protected Bitmap getCurrentWallpaper() {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            Bitmap currentWallpaper = ((BitmapDrawable) wallpaperManager.getDrawable()).getBitmap();
            return currentWallpaper;
        } catch(SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }

//    protected void changeToPrevWallpaper() throws IOException {
//        WallpaperManager wallpaperManager = WallpaperManager.getInstance(Main1Activity.this);
//        if(previousWallpaper == null) {
//            Toast.makeText(getBaseContext(), "No previous wallpaper detected",
//                    Toast.LENGTH_SHORT).show();
//        } else {
//            try {
//                wallpaperManager.setBitmap(previousWallpaper);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Toast.makeText(getBaseContext(), "Wallpaper successfully reverted",
//                    Toast.LENGTH_SHORT).show();
//        }
//    }
}
