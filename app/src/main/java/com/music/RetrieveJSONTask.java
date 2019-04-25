package com.music;

import android.os.AsyncTask;

import com.music.util.JSONReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RetrieveJSONTask extends AsyncTask<String, Void, JSONObject> {
    @Override
    protected JSONObject doInBackground(String... strings) {
        try {
            return JSONReader.read(strings[0]);
        } catch (JSONException | IOException e) {
            return null;
        }
    }
}