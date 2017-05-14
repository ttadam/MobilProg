package com.mobilprog.tadam.mobilprog.Model;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

/**
 * Created by thoma on 2017. 05. 14..
 */

public class ThumbnailGenerator {

    private static ColorGenerator generator;
    private static int color;
    private static TextDrawable drawable;


    public static TextDrawable generateMaterial(String name) {
        generator = ColorGenerator.MATERIAL;
        color = generator.getRandomColor();
        drawable = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .buildRound(name.substring(0, 1), color);
        return drawable;
    }
}