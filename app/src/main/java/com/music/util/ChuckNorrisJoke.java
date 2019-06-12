package com.music.util;

import android.support.annotation.NonNull;

import com.music.RetrieveJSONTask;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

public class ChuckNorrisJoke implements JokeFactory {
    private String firstName;
    private String lastName;

    public ChuckNorrisJoke() {
        this("Chuck", "Norris");
    }

    public ChuckNorrisJoke(String firstName) {
        this(firstName, "Norris");
    }

    public ChuckNorrisJoke(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String generateJoke() {
        RetrieveJSONTask retrieveJSONTask = new RetrieveJSONTask();
        try {
            JSONObject jsonObject = retrieveJSONTask.execute("http://api.icndb.com/jokes/random?firstName=" +
                    firstName + "&lastName=" + lastName).get().getJSONObject("value");
            String result = jsonObject.getString("joke");
            result = result.replace("&quot;", "\"");
            return "Joke #" + jsonObject.getInt("id") + ": " + result;
        } catch (ExecutionException | InterruptedException | JSONException e) {
            return null;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "First name: " + firstName + ", Last name: " + lastName;
    }
}