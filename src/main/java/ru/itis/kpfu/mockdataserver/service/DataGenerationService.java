package ru.itis.kpfu.mockdataserver.service;

import org.springframework.stereotype.Service;
import ru.itis.kpfu.mockdataserver.Constants;
import ru.itis.kpfu.mockdataserver.entity.GeneratedItem;

import java.util.Random;

@Service
public class DataGenerationService {

    public int getTypeId() {
        return 1;
    }

    public GeneratedItem generateValue(String pluginType, String fieldName) {
        if (isSinglePrimitive(pluginType)) {
            return generateSinglePrimitive(
                pluginType.endsWith("?") ? pluginType.substring(0, pluginType.length() - 2) : pluginType
            );
        } else if (isSingleString(pluginType)) {
            return generateStringValue(
                pluginType.endsWith("?") ? pluginType.substring(0, pluginType.length() - 2) : pluginType,
                fieldName
            );
        }
        return new GeneratedItem(0, "Example");
    }

    private GeneratedItem generateStringValue(String pluginType, String fieldName) {
        if (isHeaderField(fieldName)) {
            return new GeneratedItem(11, generateHeaderString());
        }
            return null;
    }

    private String generateHeaderString() {
        return null;
    }

    private boolean isSingleString(String pluginType) {
        return !pluginType.contains("[") && !pluginType.contains("]") && pluginType.contains("String");
    }

    public boolean isSinglePrimitive(String pluginType) {
        return !pluginType.contains("[") && !pluginType.contains("]") && isContainsPrimitive(pluginType);
    }

    private boolean isContainsPrimitive(String pluginType) {
        for (String primitive : Constants.PRIMITIVE_TYPES) {
            if (pluginType.contains(primitive)) return true;
        }
        return false;
    }

    public GeneratedItem generateSinglePrimitive(String pluginType) {
        Random random = new Random();
        switch (pluginType) {
            case Constants.TYPE_BOOLEAN:
                return new GeneratedItem(1, random.nextBoolean() + "");
            case Constants.TYPE_INT:
                return new GeneratedItem(2, random.nextInt() + "");
            case Constants.TYPE_LONG:
                return new GeneratedItem(3, random.nextLong() + "");
            case Constants.TYPE_DOUBLE:
                return new GeneratedItem(4, random.nextDouble() + "");
            case Constants.TYPE_FLOAT:
                return new GeneratedItem(5, random.nextFloat() + "");
            case Constants.TYPE_SHORT:
                return new GeneratedItem(6, random.nextInt(Short.MAX_VALUE + 1) + "");
            case Constants.TYPE_BYTE:
                return new GeneratedItem(7, random.nextInt(Byte.MAX_VALUE + 1) + "");
            case Constants.TYPE_CHAR:
                return new GeneratedItem(8, ((char) (random.nextInt(26) + 'a')) + "");
            case Constants.TYPE_NUMBER:
                return new GeneratedItem(9, random.nextInt() + "");
            default:
                return new GeneratedItem(10, "null");
        }
    }

    private boolean isHeaderField(String fieldName) {
        for (String occurrence : Constants.HEADER_OCCURRENCES) {
            if (fieldName.contains(occurrence)) {
                return true;
            }
        }
        return false;
    }
}
