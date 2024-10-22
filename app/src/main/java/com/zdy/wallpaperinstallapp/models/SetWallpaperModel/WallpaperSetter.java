package com.zdy.wallpaperinstallapp.models.SetWallpaperModel;

import static androidx.core.content.ContextCompat.getString;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.zdy.wallpaperinstallapp.R;

public class WallpaperSetter {

    // Set Home wallpaper the image
    public static void setHomeWallpaper (Drawable drawableImage, Context context) {
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawableImage);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        String bitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "", null);
        Uri bitmapUri = Uri.parse(bitmapPath);
        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA).setDataAndType(bitmapUri,  "image/jpeg")
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .putExtra("mimeType", "image/jpeg");

        context.startActivity(Intent.createChooser(intent,"Wallpaper"));

    }

}
