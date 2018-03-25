package android.example.com.foodoo.utils;

import android.example.com.foodoo.models.BaseModel;
import android.graphics.Color;

import java.util.List;

/**
 * Borrowed from https://stackoverflow.com/a/4415113
 */

public class ColorTools {
    public static int getColor(float p, int startColor, int middleColor, int endColor) {

        int c0;
        int c1;
        if (p <= 0.5f) {
            p *= 2;
            c0 = startColor;
            c1 = middleColor;
        } else {
            p = (p - 0.5f) * 2;
            c0 = middleColor;
            c1 = endColor;
        }
        int a = ave(Color.alpha(c0), Color.alpha(c1), p);
        int r = ave(Color.red(c0), Color.red(c1), p);
        int g = ave(Color.green(c0), Color.green(c1), p);
        int b = ave(Color.blue(c0), Color.blue(c1), p);
        return Color.argb(a, r, g, b);
    }

    private static int ave(int src, int dst, float p) {
        return src + Math.round(p * (dst - src));
    }

    public static List<? extends BaseModel> colorizeList(List<? extends BaseModel> data, int startColor, int endColor) {

        int dataSize = data.size();
        for (int i = 0; i < dataSize; ++i) {

            int percent = (int)((float)i / (float)dataSize * 100f);
            int color = ColorTools.getColor((float)percent / 100.f,
                    startColor,
                    Color.BLUE ,
                    endColor);
            data.get(i).setCardColor(color);

        }
        return data;
    }


}
