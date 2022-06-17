package ru.itis.kpfu.mockdataserver.utils;

import java.util.ArrayList;

public class StringGenerationUtils {

    public StringGenerationUtils() {
    }

    private final NumberGenerationUtils utils = new NumberGenerationUtils();

    public String generateDefaultRandomString(String locale) {
        StringBuilder result = new StringBuilder();
        int randomValue = utils.getRandomNumberInRange(3, 9);
        for (int i = 0; i < randomValue; i++) {
            if (locale.equals("English")) {
                result.append(getRandomEnglishChar());
            } else result.append(getRandomRussianChar());
        }
        result.append(" ");
        randomValue = utils.getRandomNumberInRange(3, 9);
        for (int i = 1; i < randomValue; i++) {
            if (locale.equals("English")) {
                result.append(getRandomEnglishChar());
            } else result.append(getRandomRussianChar());
        }
        return result.toString();
    }

    public String generateMessageRandomString(String locale) {
        StringBuilder result = new StringBuilder();
        int wordCountRandomValue = utils.getRandomNumberInRange(3, 7);
        int commasCountRandomValue = utils.getRandomNumberInRange(0, wordCountRandomValue / 3);
        ArrayList<Integer> commaPositions = new ArrayList<>();
        if (commasCountRandomValue > 0) {
            for (int i = 0; i < commasCountRandomValue; i++) {
                commaPositions.add(utils.getRandomNumberInRange(2, wordCountRandomValue));
            }
        }
        for (int i = 0; i < wordCountRandomValue; i++) {
            result.append(generateHeaderRandomString(locale));
            if (commaPositions.contains(i)) result.append(", ");
            else result.append(" ");
        }
        return result.toString();
    }

    public String generateHeaderRandomString(String locale) {
        StringBuilder result = new StringBuilder();
        int randomValue = utils.getRandomNumberInRange(3, 9);
        for (int i = 0; i < randomValue; i++) {
            if (locale.equals("English")) {
                result.append(getRandomEnglishChar());
            } else result.append(getRandomRussianChar());
        }
        return result.toString();
    }

    public char getRandomEnglishChar() {
        int rnd = (int) (Math.random() * 52); // or use Random or whatever
        char base = (rnd < 26) ? 'A' : 'a';
        return (char) (base + rnd % 26);
    }

    public char getRandomRussianChar() {
        int rnd = (int) (Math.random() * 66); // or use Random or whatever
        char base = (rnd < 33) ? 'А' : 'а';
        return (char) (base + rnd % 33);
    }

    public boolean isSingleString(String pluginType) {
        return !pluginType.contains("[") && !pluginType.contains("]") && pluginType.contains("String");
    }
}
