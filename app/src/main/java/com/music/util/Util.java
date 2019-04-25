package com.music.util;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import com.music.BuildConfig;
import com.music.R;

import java.util.List;

/**
 * Provides various static methods and fields to make my life easier.
 */
public final class Util {
    public static final String API_ERROR = "Sorry, something happened. Either the server is down, or you're not connected to the internet.";
    public static final String EMAIL = "alanluu4@gmail.com";
    public static final String EMAIL_ERROR = "There is no email client installed on this device.";
    public static final String INFO = "Music: Version " + BuildConfig.VERSION_NAME;
    public static final String NAME = "Alan Luu";
    public static final String REPO = "https://github.com/AlanLuu/music-player";

    public static final int REQUEST_PERMISSION = 1;

    /**
     * Don't even try...
     */
    private Util() {
        throw new AssertionError("No com.music.util.Util instances for you!");
    }

    public static void alert(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", listener);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.show();
    }

    public static <T extends Comparable<? super T>> int binarySearch(List<T> list, T target) {
        int min = 0;
        int max = list.size() - 1;

        while (min <= max) {
            int guess = (min + max) / 2;
            T t = list.get(guess);
            if (t.compareTo(target) == 0) {
                return guess;
            } else if (t.compareTo(target) < 0) {
                min = guess + 1;
            } else {
                max = guess - 1;
            }
        }
        return -1;
    }

    public static void confirm(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("OK", listener);
        builder.setIcon(R.drawable.warning);
        builder.show();
    }

    public static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    public static void sendEmail(Context context, String to, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + to));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        try {
            context.startActivity(Intent.createChooser(intent, "Send email"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, EMAIL_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    public static void share(Context context, String subject, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(intent, "Share"));
    }

    public static String versionInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException ignore) {
        }
        return "App version: " + (info != null ? info.versionName : null) + "\nAndroid version: " + android.os.Build.VERSION.RELEASE +
                "\n-----------------------------------\n";
    }
}