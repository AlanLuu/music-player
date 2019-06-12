package com.music.util;

import java.util.ArrayList;
import java.util.List;

public interface JokeFactory {
    String generateJoke();

    default List<String> generateJokes(int numJokes) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < numJokes; i++) {
            list.add(generateJoke());
        }
        return list;
    }

    default String getTitle() {
        String[] split = getClass().getSimpleName().split("(?=\\p{Upper})");
        StringBuilder builder = new StringBuilder().append("Random");
        for (String part : split) {
            builder.append(part).append(" ");
        }
        return builder.toString();
    }
}