package com.example.pictureperfectproj;

import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 *  Class for handling all color calculation and sorting
 *
 *
 * @author Jesp446c
 * @version 1.0
 * @since 1.0
 * */
public class ColorPresenter {

    /**
     * Finds the 5 most common RGB values from a given Bitmap image
     *
     * @param imageBitmap given image
     * @return integer array of 5 most commonly found colors in the given image
     */
    public int[] setCommonColors(Bitmap imageBitmap) {
        MainActivity.isLoading = true;

        List<RgbObj> rgbValues = GetRgbValues(imageBitmap);

        int color1 = 0;
        int color2 = 0;
        int color3 = 0;
        int color4 = 0;
        int color5 = 0;

        try {
            color1 = Color.rgb(rgbValues.get(0).Red, rgbValues.get(0).Green, rgbValues.get(0).Blue);
            color2 = Color.rgb(rgbValues.get(1).Red, rgbValues.get(1).Green, rgbValues.get(1).Blue);
            color3 = Color.rgb(rgbValues.get(2).Red, rgbValues.get(2).Green, rgbValues.get(2).Blue);
            color4 = Color.rgb(rgbValues.get(3).Red, rgbValues.get(3).Green, rgbValues.get(3).Blue);
            color5 = Color.rgb(rgbValues.get(4).Red, rgbValues.get(4).Green, rgbValues.get(4).Blue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int[] colorArray = {color1, color2, color3, color4, color5};

        MainActivity.isLoading = false;

        return colorArray;
    }

    /**
     * Finds all distinct RGB Values in a Bitmap Image
     *
     * @param imageBitmap image to find pixels from
     * @return list of RgbObjects containing the color values from given image
     */
    @NonNull
    private List<RgbObj> GetRgbValues(@NonNull Bitmap imageBitmap) {
        List<RgbObj> rgbValues = new ArrayList<>();
        RgbObj lastObj = null;
        int minDistance = 5;

        //looping through all pixels of the image
        for (int y = 0; y < imageBitmap.getHeight(); y++) {
            for (int x = 0; x < imageBitmap.getWidth(); x++) {

                // Getting pixel color by position x and y
                int clr = imageBitmap.getPixel(x, y);
                int red = (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                int blue = clr & 0x000000ff;

                // Saving pixel colors in Rgb Object
                RgbObj freshObj = new RgbObj(red, green, blue);

                //Determines whether the rgb of the pixel is black or white
                if (isNotBlack(freshObj) && isNotWhite(freshObj)) {

                    //Null check
                    if (lastObj == null) {
                        rgbValues.add(freshObj);
                        lastObj = freshObj;
                    } else {
                        //Calculating the euclidean distance between 2 RGB values to only save distinct colors
                        double distance = calculateEuclideanDistance(lastObj, freshObj);
                        if (distance > minDistance) {
                            rgbValues.add(freshObj);
                            lastObj = freshObj;
                        }
                    }

                }
            }
        }

        for (RgbObj item : rgbValues) {
            item.Occurrence = getOccurrences(rgbValues, item);
        }

        sortListByOccurrence(rgbValues);

        return rgbValues;
    }

    /**
     * returns the amount of times a given object occurs in a collection
     *
     * @param c Collection of objects
     * @param o object to find occurrence off
     * @return amount of occurrences of object in collection
     */
    private int getOccurrences(@NonNull Collection<?> c, Object o) {
        int count = 0;
        for (Object e : c)
            if (e.equals(o))
                count++;
        return count;
    }

    /**
     * Sorts the list of RgbValues by occurrences, using custom comparator
     *
     * @param rgbValues RgbObj listToBeSorted
     */
    private void sortListByOccurrence(@NonNull List<RgbObj> rgbValues) {
        rgbValues.sort(new Comparator<RgbObj>() {
            @Override
            public int compare(RgbObj a1, RgbObj a2) {
                return a1.Occurrence - a2.Occurrence;
            }
        }.reversed());
    }

    /**
     * Method to determine if a given rgbObject is black
     *
     * @param rgbObject rgbObject to test
     * @return True if not Black, False if Black
     */
    private boolean isNotBlack(RgbObj rgbObject) {
        RgbObj blackObj = new RgbObj(0, 0, 0);
        double distance = calculateEuclideanDistance(blackObj, rgbObject);
        //Returns true if condition is met
        return distance > 3;
    }

    /**
     * Method to determine if a given rgbObject is white
     *
     * @param rgbObject RgbObject to test
     * @return True if not White, False if white
     */
    private boolean isNotWhite(RgbObj rgbObject) {
        RgbObj whiteObj = new RgbObj(255, 255, 255);
        double distance = calculateEuclideanDistance(rgbObject, whiteObj);
        //Returns true if condition is met
        return distance > 3;
    }

    /**
     * Calculates the Euclidean distance between 2 pixels
     *
     * @param rgb1 rgb value 1
     * @param rgb2 rgb value 2
     * @return double Result of the Euclidean Equation
     */
    private double calculateEuclideanDistance(@NonNull RgbObj rgb1, @NonNull RgbObj rgb2) {
        return Math.sqrt(
                (rgb2.Red - rgb1.Red) ^ 2 + (rgb2.Green - rgb1.Green) ^ 2 + (rgb2.Blue - rgb1.Blue) ^ 2
        );
    }
}
