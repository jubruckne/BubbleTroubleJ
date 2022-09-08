package com.jubruckne.bubbletrouble;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Utils {
    public static String format(String format, Object... args) {
        String str = format;

        for (Object arg: args) {
            if (arg instanceof Float) {
                str = str.replaceFirst("%.0f", Integer.toString(Math.round((Float) arg)));
                str = str.replaceFirst("%.1f", Float.toString(Math.round((Float) arg)));
                str = str.replaceFirst("%f", Float.toString((Float) arg));
            } else if (arg instanceof String) {
                str = str.replaceFirst("%s", arg.toString());
            } else if (arg instanceof Integer) {
                str = str.replaceFirst("%i", arg.toString());
            } else if (arg instanceof Vector2) {
                str = str.replaceFirst("%.0f",
                        "("
                                + Math.round(((Vector2) arg).x) + ", "
                                + Math.round(((Vector2) arg).y) + ")");
                str = str.replaceFirst("%.1f",
                        "("
                                + Math.round(((Vector2) arg).x * 10) / 10 + ", "
                                + Math.round(((Vector2) arg).y * 10) / 10 + ")");
                str = str.replaceFirst("%f",
                        "("
                                + ((Vector2) arg).x + ", "
                                + ((Vector2) arg).y + ")");
            } else {
                Gdx.app.log("Utils.format", arg.getClass().getCanonicalName());
                str = str.replaceFirst("%", arg.toString());
            }
        }

        return str;
    }
}
