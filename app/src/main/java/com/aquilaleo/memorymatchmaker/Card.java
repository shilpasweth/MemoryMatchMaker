package com.aquilaleo.memorymatchmaker;

import android.graphics.drawable.Drawable;

public class Card {
    int element;
    int number;
    int frontDrawable;
    Drawable scaledFrontDrawable;
    int backDrawable;
    Drawable scaledBackDrawable;

    Card(int e, int n, int b, int f){
        this.element = e;
        this.number = n;
        this.backDrawable = f;
        this.frontDrawable = b;
    }
}
