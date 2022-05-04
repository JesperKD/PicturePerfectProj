package com.example.pictureperfectproj;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

public class MainActivity extends AppCompatActivity {

    public static boolean isLoading = false;

    private Button colorBtn1;
    private Button colorBtn2;
    private Button colorBtn3;
    private Button colorBtn4;
    private Button colorBtn5;
    private ProgressBar loadingBar;
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
        loadingBar = findViewById(R.id.progressBar1);

        Button button = findViewById(R.id.PictureButton);
        button.setOnClickListener(v -> dispatchTakePictureIntent());

        Thread loadingThread = new Thread(() -> {
            handleLoadingSpinner();
        });
        loadingThread.start();
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
            e.printStackTrace();
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

            Thread newThread = new Thread(() -> {
                ShowCommonColors(ColorCalc.GetColors(imageBitmap));
            });
            newThread.start();
        }
    }

    public void ShowCommonColors(int[] colorArray) {
        changeButtonColor(colorBtn1, colorArray[0]);
        changeButtonColor(colorBtn2, colorArray[1]);
        changeButtonColor(colorBtn3, colorArray[2]);
        changeButtonColor(colorBtn4, colorArray[3]);
        changeButtonColor(colorBtn5, colorArray[4]);
    }

    private void changeButtonColor(Button btn, int color) {

        Drawable buttonDrawable = btn.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        //the color is a direct color int and not a color resource
        DrawableCompat.setTint(buttonDrawable, color);
        btn.setBackground(buttonDrawable);

    }

    private void handleLoadingSpinner() {
        while (true) {
            while (isLoading == true) {
                loadingBar.setVisibility(View.VISIBLE);
            }
            while (isLoading == false) {
                loadingBar.setVisibility(View.INVISIBLE);
            }
        }
    }
}
