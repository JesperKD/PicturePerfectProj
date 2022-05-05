package com.example.pictureperfectproj;

import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class ColorPresenter {

    static List<RgbObj> rgbValues = new ArrayList<>();

    /**
     * Finds the 5 most common RGB values from a given Bitmap image
     */
    public static int[] setCommonColors(Bitmap imageBitmap) {
        MainActivity.isLoading = true;

        GetRgbValues(imageBitmap);

        int color1 = Color.rgb(rgbValues.get(0).Red, rgbValues.get(0).Green, rgbValues.get(0).Blue);
        int color2 = Color.rgb(rgbValues.get(1).Red, rgbValues.get(1).Green, rgbValues.get(1).Blue);
        int color3 = Color.rgb(rgbValues.get(2).Red, rgbValues.get(2).Green, rgbValues.get(2).Blue);
        int color4 = Color.rgb(rgbValues.get(3).Red, rgbValues.get(3).Green, rgbValues.get(3).Blue);
        int color5 = Color.rgb(rgbValues.get(4).Red, rgbValues.get(4).Green, rgbValues.get(4).Blue);

        int[] colorArray = {color1, color2, color3, color4, color5};

        MainActivity.isLoading = false;

        return colorArray;
    }

    /**
     * Finds all RGB Values in a Bitmap Image
     */
    private static void GetRgbValues(@NonNull Bitmap imageBitmap) {
        for (int y = 0; y < imageBitmap.getHeight() / 2; y++) {
            for (int x = 0; x < imageBitmap.getWidth() / 2; x++) {
                // Getting pixel color by position x and y
                int clr = imageBitmap.getPixel(x, y);
                int red = (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                int blue = clr & 0x000000ff;

                RgbObj freshObj = new RgbObj(red, green, blue);
                rgbValues.add(freshObj);
            }
        }

        for (RgbObj item : rgbValues) {
            item.Occurrence = getOccurrences(rgbValues, item);
        }

        sortListByOccurrence();
    }

    /**
     * returns the amount of times a given object occurs in a collection
     */
    private static int getOccurrences(@NonNull Collection<?> c, Object o) {
        int count = 0;
        for (Object e : c)
            if (e.equals(o))
                count++;
        return count;
    }

    /**
     * Sorts the list of RgbValues by occurrences
     */
    private static void sortListByOccurrence() {
        rgbValues.sort(new Comparator<RgbObj>() {
            @Override
            public int compare(RgbObj a1, RgbObj a2) {
                return a1.Occurrence - a2.Occurrence;
            }
        }.reversed());
    }
}
