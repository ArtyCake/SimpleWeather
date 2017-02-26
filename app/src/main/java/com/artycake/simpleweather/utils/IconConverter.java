package com.artycake.simpleweather.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.Log;

import com.artycake.simpleweather.R;

import java.util.HashMap;

/**
 * Created by artycake on 2/12/17.
 */
public class IconConverter {
    private static IconConverter ourInstance = null;
    private HashMap<Integer, Integer> iconset = new HashMap<>();
    private Context context;

    public static IconConverter getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new IconConverter(context);
        }
        return ourInstance;
    }

    private IconConverter(Context context) {
        this.context = context;
        for (int id : new int[]{200, 201, 202, 210, 230, 231, 232}) {
            iconset.put(id, R.drawable.ic_thunderstorm_light);
        }
        for (int id : new int[]{211, 212, 221}) {
            iconset.put(id, R.drawable.ic_thunderstorm);
        }
        for (int id : new int[]{300, 301, 302, 310, 311, 500}) {
            iconset.put(id, R.drawable.ic_rain_light);
        }
        for (int id : new int[]{312, 313, 314, 321, 501, 502, 503, 504, 511, 520, 521, 522, 531}) {
            iconset.put(id, R.drawable.ic_rain);
        }
        for (int id : new int[]{600, 601}) {
            iconset.put(id, R.drawable.ic_snow_heavy);
        }
        for (int id : new int[]{602, 620, 621, 622}) {
            iconset.put(id, R.drawable.ic_snow);
        }
        for (int id : new int[]{611, 612, 615, 616}) {
            iconset.put(id, R.drawable.ic_sleed);
        }
        for (int id : new int[]{701, 711, 721, 731, 741, 751, 761, 762, 771, 781}) {
            iconset.put(id, R.drawable.ic_mist);
        }
        iconset.put(800, R.drawable.ic_clear);
        iconset.put(801, R.drawable.ic_clouds);
        iconset.put(802, R.drawable.ic_clouds_full);
        for (int id : new int[]{803, 804}) {
            iconset.put(id, R.drawable.ic_clouds_heavy);
        }
        for (int id : new int[]{952, 953, 954, 955, 956, 957, 958, 959}) {
            iconset.put(id, R.drawable.ic_wind);
        }
    }

    public Drawable getIconDrawable(int id) {
        int iconId = getIconDrawableId(id);
        return VectorDrawableCompat.create(context.getResources(), iconId, context.getTheme());

    }

    public int getIconDrawableId(int id) {
        Integer iconId = iconset.get(id);
        if (iconId == null) {
            Log.d("No icon", String.valueOf(id));
            iconId = R.drawable.ic_na;
        }
        return iconId;
    }
}
