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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 *  Activity for loading layout resources
 *
 * @author Jesp446c
 */
public class MainActivity extends AppCompatActivity {

    public static boolean isLoading = false;

    private Button colorBtn1;
    private Button colorBtn2;
    private Button colorBtn3;
    private Button colorBtn4;
    private Button colorBtn5;
    private ProgressBar loadingBar;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    ColorPresenter cp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cp = new ColorPresenter();

        colorBtn1 = findViewById(R.id.colorBtn1);
        colorBtn2 = findViewById(R.id.colorBtn2);
        colorBtn3 = findViewById(R.id.colorBtn3);
        colorBtn4 = findViewById(R.id.colorBtn4);
        colorBtn5 = findViewById(R.id.colorBtn5);
        loadingBar = findViewById(R.id.progressBar1);

        Button button = findViewById(R.id.PictureButton);
        button.setOnClickListener(v -> dispatchTakePictureIntent());

        //Todo: Find way to avoid thread collision with main thread.
        /*startLoadingThread();*/
    }

    /**
     * Opens phone camera for picture taking
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            setImageView(imageBitmap);
            startColorThread(imageBitmap);
        }
    }

    /**
     * Sets the imageview to a given Bitmap image
     *
     * @param imageBitmap Bitmap
     */
    private void setImageView(Bitmap imageBitmap) {
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(imageBitmap);
    }

    /**
     * Starts the entire pixel finding/sorting process
     *
     * @param imageBitmap Bitmap
     */
    private void startColorThread(Bitmap imageBitmap) {
        Thread colorThread = new Thread(() -> ShowCommonColors(cp.setCommonColors(imageBitmap)));
        colorThread.start();
    }

    /**
     * Updates colors on all 5 buttons
     *
     * @param colorArray Array of most commonly found RGB Values
     */
    public void ShowCommonColors(@NonNull int[] colorArray) {
        changeButtonColor(colorBtn1, colorArray[0]);
        changeButtonColor(colorBtn2, colorArray[1]);
        changeButtonColor(colorBtn3, colorArray[2]);
        changeButtonColor(colorBtn4, colorArray[3]);
        changeButtonColor(colorBtn5, colorArray[4]);
    }

    /**
     * Sets a new background color on a button.
     *
     * @param color given color
     * @param btn   given button
     */
    private void changeButtonColor(@NonNull Button btn, int color) {
        Drawable buttonDrawable = btn.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        DrawableCompat.setTint(buttonDrawable, color);
        btn.setBackground(buttonDrawable);
    }

    /**
     * Starts spinner manager thread
     */
    private void startLoadingThread() {
        Thread loadingThread = new Thread(this::handleLoadingSpinner);
        loadingThread.start();
    }

    /**
     * Shows or hides progress Spinner, based on a boolean.
     */
    private void handleLoadingSpinner() {
        while (true) {
            while (isLoading) {
                loadingBar.setVisibility(View.VISIBLE);
            }
            while (!isLoading) {
                loadingBar.setVisibility(View.INVISIBLE);
            }
        }
    }
}
