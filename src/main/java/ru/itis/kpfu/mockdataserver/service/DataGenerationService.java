package ru.itis.kpfu.mockdataserver.service;

import org.springframework.stereotype.Service;
import ru.itis.kpfu.mockdataserver.Constants;
import ru.itis.kpfu.mockdataserver.entity.GeneratedItem;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

@Service
public class DataGenerationService {

    public int getTypeId() {
        return 1;
    }

    public GeneratedItem generateValue(String pluginType, String fieldName, String locale) {
        if (isSinglePrimitive(pluginType)) {
            String pluginTypeClean;
            if (pluginType.endsWith("?")) {
                pluginTypeClean = pluginType.substring(0, pluginType.length() - 2);
            } else pluginTypeClean = pluginType;
            return generateSinglePrimitive(pluginTypeClean);
        } else if (isSingleString(pluginType)) {
            return generateStringValue(fieldName, locale);
        } else if (isPrimitivesList(pluginType)) {
            return generatePrimitivesList(pluginType);
        } else if (isStringList(pluginType)) {
            return generateStringList(pluginType, locale);
        } else return generateMapValue(pluginType, locale);
    }

    private GeneratedItem generateMapValue(String pluginType, String locale) {
        if (!isMapList(pluginType)) {
            return new GeneratedItem(0, "Example");
        } else {
            return new GeneratedItem(0, "Example");
        }
    }

    private String generateMapKeyAndValue(String mapField, String locale) {
        String[] splitValue = mapField.split(",");
        if (splitValue.length == 2) {
            String keyType = splitValue[0];
            String valueType = splitValue[0];
        } else {

        }
        return "Test";
    }

    private boolean isMapList(String pluginType) {
        return pluginType.split(",").length == 2 && pluginType.charAt(0) == '[' && pluginType.charAt(1) == '[' &&
            pluginType.charAt(pluginType.length() - 1) == ']' && pluginType.charAt(pluginType.length() - 2) == ']';
    }

    private GeneratedItem generatePrimitivesList(String pluginType) {
        ArrayList<GeneratedItem> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            list.add(generateSinglePrimitive(pluginType));
        }
        StringBuilder result = new StringBuilder();
        list.forEach(item -> {
            result.append(item.getGeneratedValue());
            result.append(";");
        });
        return new GeneratedItem(list.get(0).getTypeId(), result.toString());
    }

    private GeneratedItem generateStringList(String pluginType, String locale) {
        ArrayList<GeneratedItem> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            list.add(new GeneratedItem(11, generateHeaderString(locale)));
        }
        StringBuilder result = new StringBuilder();
        list.forEach(item -> {
            result.append(item.getGeneratedValue());
            result.append(";");
        });
        return new GeneratedItem(11, result.toString());
    }

    private GeneratedItem generateStringValue(String fieldName, String locale) {
        if (isHeaderField(fieldName)) {
            return new GeneratedItem(11, generateHeaderString(locale));
        } else if (isMessageField(fieldName)) {
            return new GeneratedItem(11, generateMessageString(locale));
        } else return new GeneratedItem(11, generateDefaultString(locale));
    }

    private String generateDefaultString(String locale) {
        StringBuilder result = new StringBuilder();
        int randomValue = getRandomNumberInRange(3, 9);
        for (int i = 0; i < randomValue; i++) {
            if (locale.equals("English")) {
                result.append(getRandomEnglishChar());
            } else result.append(getRandomRussianChar());
        }
        result.append(" ");
        randomValue = getRandomNumberInRange(3, 9);
        for (int i = 1; i < randomValue; i++) {
            if (locale.equals("English")) {
                result.append(getRandomEnglishChar());
            } else result.append(getRandomRussianChar());
        }
        return result.toString();
    }

    private String generateMessageString(String locale) {
        StringBuilder result = new StringBuilder();
        int wordCountRandomValue = getRandomNumberInRange(3, 7);
        int commasCountRandomValue = getRandomNumberInRange(0, wordCountRandomValue / 3);
        ArrayList<Integer> commaPositions = new ArrayList<>();
        if (commasCountRandomValue > 0) {
            for (int i = 0; i < commasCountRandomValue; i++) {
                commaPositions.add(getRandomNumberInRange(2, wordCountRandomValue));
            }
        }
        for (int i = 0; i < wordCountRandomValue; i++) {
            result.append(generateHeaderString(locale));
            if (commaPositions.contains(i)) result.append(", ");
            else result.append(" ");
        }
        return result.toString();
    }

    private String generateHeaderString(String locale) {
        StringBuilder result = new StringBuilder();
        int randomValue = getRandomNumberInRange(3, 9);
        for (int i = 0; i < randomValue; i++) {
            if (locale.equals("English")) {
                result.append(getRandomEnglishChar());
            } else result.append(getRandomRussianChar());
        }
        return result.toString();
    }

    private boolean isSingleString(String pluginType) {
        return !pluginType.contains("[") && !pluginType.contains("]") && pluginType.contains("String");
    }

    private boolean isSinglePrimitive(String pluginType) {
        return !pluginType.contains("[") && !pluginType.contains("]") && isContainsPrimitive(pluginType);
    }

    private boolean isPrimitivesList(String pluginType) {
        long startBracketOccurrence = pluginType.chars().filter(ch -> ch == '[').count();
        long endBracketOccurrence = pluginType.chars().filter(ch -> ch == ']').count();
        return startBracketOccurrence == 1 && endBracketOccurrence == 1 && isContainsPrimitive(pluginType);
    }

    private boolean isStringList(String pluginType) {
        long startBracketOccurrence = pluginType.chars().filter(ch -> ch == '[').count();
        long endBracketOccurrence = pluginType.chars().filter(ch -> ch == ']').count();
        return startBracketOccurrence == 1 && endBracketOccurrence == 1 && pluginType.contains("String");
    }

    private boolean isContainsPrimitive(String pluginType) {
        for (String primitive : Constants.PRIMITIVE_TYPES) {
            if (pluginType.contains(primitive)) return true;
        }
        return false;
    }

    private GeneratedItem generateSinglePrimitive(String pluginType) {
        Random random = new Random();
        switch (pluginType) {
            case Constants.TYPE_BOOLEAN:
                return new GeneratedItem(1, random.nextBoolean() + "");
            case Constants.TYPE_INT:
                return new GeneratedItem(2, random.nextInt() + "");
            case Constants.TYPE_LONG:
                return new GeneratedItem(3, random.nextLong() + "");
            case Constants.TYPE_DOUBLE:
                return new GeneratedItem(4, generateRandomDouble(random) + "");
            case Constants.TYPE_FLOAT:
                return new GeneratedItem(5, generateRandomFloat(random) + "");
            case Constants.TYPE_SHORT:
                return new GeneratedItem(6, random.nextInt(Short.MAX_VALUE + 1) + "");
            case Constants.TYPE_BYTE:
                return new GeneratedItem(7, random.nextInt(Byte.MAX_VALUE + 1) + "");
            case Constants.TYPE_CHAR:
                return new GeneratedItem(8, getRandomEnglishChar() + "");
            case Constants.TYPE_NUMBER:
                return new GeneratedItem(9, random.nextInt() + "");
            default:
                return new GeneratedItem(10, "null");
        }
    }

    private double generateRandomDouble(Random random) {
        int numberOfMultiplication = getRandomNumberInRange(1, 9);
        double result = random.nextDouble();
        for (int i = 0; i < numberOfMultiplication; i++) {
            result = result * 10;
        }
        return result;
    }

    private float generateRandomFloat(Random random) {
        int numberOfMultiplication = getRandomNumberInRange(1, 6);
        float result = random.nextFloat();
        for (int i = 0; i < numberOfMultiplication; i++) {
            result = result * 10;
        }
        return result;
    }

    private boolean isHeaderField(String fieldName) {
        for (String occurrence : Constants.HEADER_OCCURRENCES) {
            if (fieldName.toLowerCase(Locale.ROOT).contains(occurrence.toLowerCase(Locale.ROOT))) {
                return true;
            }
        }
        return false;
    }

    private boolean isMessageField(String fieldName) {
        for (String occurrence : Constants.MESSAGE_OCCURRENCES) {
            if (fieldName.toLowerCase(Locale.ROOT).contains(occurrence.toLowerCase(Locale.ROOT))) {
                return true;
            }
        }
        return false;
    }

    private int getRandomNumberInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    private char getRandomEnglishChar() {
        int rnd = (int) (Math.random() * 52); // or use Random or whatever
        char base = (rnd < 26) ? 'A' : 'a';
        return (char) (base + rnd % 26);

    }

    private char getRandomRussianChar() {
        int rnd = (int) (Math.random() * 66); // or use Random or whatever
        char base = (rnd < 33) ? 'А' : 'а';
        return (char) (base + rnd % 33);
    }
}
