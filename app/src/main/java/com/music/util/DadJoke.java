package com.music.util;

import com.music.RetrieveJSONTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class DadJoke implements JokeFactory {
    @Override
    public String generateJoke() {
        RetrieveJSONTask retrieveJSONTask = new RetrieveJSONTask();
        try {
            JSONObject jsonObject = retrieveJSONTask.execute("https://icanhazdadjoke.com/slack").get()
                    .getJSONArray("attachments").getJSONObject(0);
            String result = jsonObject.getString("text");
            result = result.replace("&quot;", "\"");
            return result;
        } catch (ExecutionException | InterruptedException | JSONException e) {
            return null;
        }
    }
}