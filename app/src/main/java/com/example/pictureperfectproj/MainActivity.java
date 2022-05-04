package com.example.pictureperfectproj;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

public class MainActivity extends AppCompatActivity {

    private Button colorBtn1;
    private Button colorBtn2;
    private Button colorBtn3;
    private Button colorBtn4;
    private Button colorBtn5;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colorBtn1 = findViewById(R.id.colorBtn1);
        colorBtn2 = findViewById(R.id.colorBtn2);
        colorBtn3 = findViewById(R.id.colorBtn3);
        colorBtn4 = findViewById(R.id.colorBtn4);
        colorBtn5 = findViewById(R.id.colorBtn5);

        Button button = findViewById(R.id.PictureButton);
        button.setOnClickListener(v -> dispatchTakePictureIntent());
    }

    /**
     * Opens phone camera for picture taking
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(imageBitmap);


            Thread newThread = new Thread(()->{
                ShowCommonColors(ColorCalc.GetColors(imageBitmap));
            });
            newThread.start();

        }
    }

    public void ShowCommonColors(int[] colorArray) {
/*        colorBtn1.setBackgroundColor(colorArray[0]);
        colorBtn1.setBackgroundTintList(ColorStateList.valueOf(colorArray[1]));
        colorBtn2.setBackgroundColor(colorArray[1]);
        colorBtn2.setBackgroundTintMode(PorterDuff.Mode.LIGHTEN);
        colorBtn3.setBackgroundColor(colorArray[2]);
        colorBtn3.setBackgroundTintMode(PorterDuff.Mode.LIGHTEN);
        colorBtn4.setBackgroundColor(colorArray[3]);
        colorBtn4.setBackgroundTintMode(PorterDuff.Mode.LIGHTEN);
        colorBtn5.setBackgroundColor(colorArray[4]);
        colorBtn5.setBackgroundTintMode(PorterDuff.Mode.LIGHTEN);*/


        Drawable buttonDrawable = colorBtn2.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        //the color is a direct color int and not a color resource
        DrawableCompat.setTint(buttonDrawable, colorArray[1]);
        colorBtn2.setBackground(buttonDrawable);


    }

}
