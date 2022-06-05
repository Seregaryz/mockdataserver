package ru.itis.kpfu.mockdataserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.kpfu.mockdataserver.Constants;
import ru.itis.kpfu.mockdataserver.entity.GeneratedItem;
import ru.itis.kpfu.mockdataserver.entity.dao.representative.*;
import ru.itis.kpfu.mockdataserver.repository.*;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;

@Service
public class DataGenerationService {

    @Autowired
    NounRepository nounRepository;

    @Autowired
    AdjectiveRepository adjectiveRepository;

    @Autowired
    VerbRepository verbRepository;

    @Autowired
    AdverbRepository adverbRepository;

    @Autowired
    NounRussianRepository nounRussianRepository;

    @Autowired
    AdjectiveRussianRepository adjectiveRussianRepository;

    @Autowired
    VerbRussianRepository verbRussianRepository;

    @Autowired
    AdverbRussianRepository adverbRussianRepository;

    public GeneratedItem generateValue(String pluginType, String fieldName, String locale, boolean isRepresentative) {
        if (isSinglePrimitive(pluginType)) {
            String pluginTypeClean;
            if (pluginType.endsWith("?")) {
                pluginTypeClean = pluginType.substring(0, pluginType.length() - 1);
            } else pluginTypeClean = pluginType;
            return generateSinglePrimitive(pluginTypeClean);
        } else if (isSingleString(pluginType)) {
            return generateStringValue(fieldName, locale, isRepresentative);
        } else if (isPrimitivesList(pluginType)) {
            return generatePrimitivesList(pluginType);
        } else if (isStringList(pluginType)) {
            return generateStringList(fieldName, locale, isRepresentative);
        } else return generateMapValue(pluginType, locale);
    }

    private GeneratedItem generateMapValue(String pluginType, String locale) {
        if (!isMapList(pluginType)) {
            return new GeneratedItem(0, "Example", false);
        } else {
            return new GeneratedItem(0, "Example", true);
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
        String pluginTypeClean;
        pluginTypeClean = pluginType.substring(1, pluginType.length() -1);
        if (pluginTypeClean.endsWith("?")) {
            pluginTypeClean = pluginTypeClean.substring(0, pluginType.length() - 1);
        }
        for (int i = 0; i < 3; i++) {
            list.add(generateSinglePrimitive(pluginTypeClean));
        }
        StringBuilder result = new StringBuilder();
        list.forEach(item -> {
            result.append(item.getGeneratedValue());
            result.append(";");
        });
        return new GeneratedItem(list.get(0).getTypeId(), result.toString(), true);
    }

    private GeneratedItem generateStringList(String fieldName, String locale, boolean isRepresentative) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(generateStringValue(fieldName, locale, isRepresentative).getGeneratedValue());
        }
        StringBuilder result = new StringBuilder();
        list.forEach(item -> {
            result.append(item);
            result.append(";");
        });
        return new GeneratedItem(11, result.toString(), true);
    }

    private GeneratedItem generateStringValue(String fieldName, String locale, boolean isRepresentative) {
        if (isHeaderField(fieldName)) {
            if (isRepresentative) {
                return new GeneratedItem(11, generateHeaderRepresentative(locale), false);
            } else return new GeneratedItem(11, generateHeaderRandomString(locale), false);
        } else if (isMessageField(fieldName)) {
            if (isRepresentative) {
                return new GeneratedItem(11, generateMessageRepresentativeString(locale), false);
            } else return new GeneratedItem(11, generateMessageRandomString(locale), false);
        } else {
            if (isRepresentative) {
                return new GeneratedItem(11, generateDefaultRepresentativeString(locale), false);
            } else return new GeneratedItem(11, generateDefaultRandomString(locale), false);
        }
    }

    private String generateMessageRepresentativeString(String locale) {
        int wordCountRandomValue = getRandomNumberInRange(3, 7);
        String simpleSentence = getRandomSimpleSentence(locale);
        switch (wordCountRandomValue) {
            case 3:
                return simpleSentence;
            case 4:
                return simpleSentence + " " + getRandomAdverb(locale);
            case 5:
                return simpleSentence + ", " + getRandomAdjective(locale) + " " + getRandomNoun(locale);
            case 6:
                return simpleSentence + ", " + getRandomSimpleSentence(locale);
            case 7:
                return simpleSentence + ", " + getRandomAdjective(locale) + ", " + getRandomSimpleSentence(locale);
        }
        return simpleSentence;
    }

    private String generateDefaultRepresentativeString(String locale) {
        return getRandomAdjective(locale) + " " + getRandomNoun(locale);
    }

    private String generateHeaderRepresentative(String locale) {
        return getRandomNoun(locale);
    }

    private String getRandomSimpleSentence(String locale) {
        return getRandomAdjective(locale) + " " + getRandomNoun(locale) + " " + getRandomVerb(locale);
    }

    private String getRandomAdjective(String locale) {
        if (locale.equals("English")) {
            long randomAdjectiveId = getRandomNumberInRange(1, 1347);
            Optional<Adjective> adjective = adjectiveRepository.findById(randomAdjectiveId);
            if (adjective.isPresent()) {
                return adjective.get().getValue();
            } else return "default";
        } else {
            long randomAdjectiveId = getRandomNumberInRange(1, 836);
            Optional<AdjectiveRussian> adjectiveRussian = adjectiveRussianRepository.findById(randomAdjectiveId);
            if (adjectiveRussian.isPresent()) {
                return adjectiveRussian.get().getValue();
            } else return "стандартный";
        }
    }

    private String getRandomNoun(String locale) {
        if (locale.equals("English")) {
            long randomNounId = getRandomNumberInRange(1, 6801);
            Optional<Noun> noun = nounRepository.findById(randomNounId);
            if (noun.isPresent()) {
                return noun.get().getValue();
            } else return "text";
        } else {
            long randomNounId = getRandomNumberInRange(1, 25701);
            Optional<NounRussian> nounRussian = nounRussianRepository.findById(randomNounId);
            if (nounRussian.isPresent()) {
                return nounRussian.get().getValue();
            } else return "текст";
        }
    }

    private String getRandomVerb(String locale) {
        if (locale.equals("English")) {
            long randomVerbId = getRandomNumberInRange(1, 1042);
            Optional<Verb> verb = verbRepository.findById(randomVerbId);
            if (verb.isPresent()) {
                return verb.get().getValue();
            } else return "received";
        } else {
            long randomVerbId = getRandomNumberInRange(1, 874);
            Optional<VerbRussian> verbRussian = verbRussianRepository.findById(randomVerbId);
            if (verbRussian.isPresent()) {
                return verbRussian.get().getValue();
            } else return "получен";
        }
    }

    private String getRandomAdverb(String locale) {
        if (locale.equals("English")) {
            long randomAdverbId = getRandomNumberInRange(1, 230);
            Optional<Adverb> adverb = adverbRepository.findById(randomAdverbId);
            if (adverb.isPresent()) {
                return adverb.get().getValue();
            } else return "completely";
        } else {
            long randomAdverbId = getRandomNumberInRange(1, 313);
            Optional<AdverbRussian> adverbRussian = adverbRussianRepository.findById(randomAdverbId);
            if (adverbRussian.isPresent()) {
                return adverbRussian.get().getValue();
            } else return "полностью";
        }
    }

    private String generateDefaultRandomString(String locale) {
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

    private String generateMessageRandomString(String locale) {
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
            result.append(generateHeaderRandomString(locale));
            if (commaPositions.contains(i)) result.append(", ");
            else result.append(" ");
        }
        return result.toString();
    }

    private String generateHeaderRandomString(String locale) {
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
                return new GeneratedItem(1, random.nextBoolean() + "", false);
            case Constants.TYPE_INT:
                return new GeneratedItem(2, random.nextInt() + "", false);
            case Constants.TYPE_LONG:
                return new GeneratedItem(3, random.nextLong() + "", false);
            case Constants.TYPE_DOUBLE:
                return new GeneratedItem(4, generateRandomDouble(random) + "", false);
            case Constants.TYPE_FLOAT:
                return new GeneratedItem(5, generateRandomFloat(random) + "", false);
            case Constants.TYPE_SHORT:
                return new GeneratedItem(6, random.nextInt(Short.MAX_VALUE + 1) + "", false);
            case Constants.TYPE_BYTE:
                return new GeneratedItem(7, random.nextInt(Byte.MAX_VALUE + 1) + "", false);
            case Constants.TYPE_CHAR:
                return new GeneratedItem(8, getRandomEnglishChar() + "", false);
            case Constants.TYPE_NUMBER:
                return new GeneratedItem(9, random.nextInt() + "", false);
            default:
                return new GeneratedItem(10, "null", false);
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
