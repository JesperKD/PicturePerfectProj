package com.example.pictureperfectproj;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class ColorCalc {

    static List<RgbObj> rgbValues = new ArrayList<>();

    public static int[] GetColors(Bitmap imageBitmap) {

        for (int y = 0; y < imageBitmap.getHeight(); y++) {
            for (int x = 0; x < imageBitmap.getWidth(); x++) {
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

        sortByOccurrence();

        int color1 = Color.rgb(rgbValues.get(1).Red, rgbValues.get(1).Green, rgbValues.get(1).Blue);
        int color2 = Color.rgb(rgbValues.get(2).Red, rgbValues.get(2).Green, rgbValues.get(2).Blue);
        int color3 = Color.rgb(rgbValues.get(3).Red, rgbValues.get(3).Green, rgbValues.get(3).Blue);
        int color4 = Color.rgb(rgbValues.get(4).Red, rgbValues.get(4).Green, rgbValues.get(4).Blue);
        int color5 = Color.rgb(rgbValues.get(5).Red, rgbValues.get(5).Green, rgbValues.get(5).Blue);

        int[] colorArray = {color1, color2, color3, color4, color5};

        return colorArray;
    }

    private static void sortByOccurrence() {
        rgbValues.sort(new Comparator<RgbObj>() {
            @Override
            public int compare(RgbObj a1, RgbObj a2) {
                return a1.Occurrence - a2.Occurrence;
            }
        }.reversed());
    }

    private static int getOccurrences(Collection<?> c, Object o) {
        int count = 0;
        for (Object e : c)
            if (e.equals(o))
                count++;
        return count;
    }
}
