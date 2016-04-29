package com.syaona.petalierapp.Design3D;


import com.syaona.petalierapp.R;

/*
 * Created by ElaineBot on 4/25/16.
 * All classes are under MYSALE GROUP copyright
 */
public enum FlowerTexture {

    BabyPink(R.drawable.babypink),
    BlueWhite(R.drawable.blue_white),
    Fuschia(R.drawable.fuschia),
    GreenWhite(R.drawable.green_white),
    Lilac(R.drawable.lilac),
    Red(R.drawable.red),
    RedWhite(R.drawable.red_white),
    White(R.drawable.white);

    private int resource;

    FlowerTexture(int resource) {
        this.resource = resource;

    }

    public int getResource() {
        return resource;
    }

    public static FlowerTexture randomize() {
        return values()[(int) (Math.random() * values().length)];
    }
}
