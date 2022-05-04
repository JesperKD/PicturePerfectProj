package com.example.pictureperfectproj;

import java.util.Objects;

public class RgbObj {
    public int Red;
    public int Green;
    public int Blue;
    public int Occurrence;


    public RgbObj(int red, int green, int blue){
        this.Red = red;
        this.Green = green;
        this.Blue = blue;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RgbObj rgbObj = (RgbObj) o;
        return Red == rgbObj.Red && Green == rgbObj.Green && Blue == rgbObj.Blue && Occurrence == rgbObj.Occurrence;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Red, Green, Blue, Occurrence);
    }
}
