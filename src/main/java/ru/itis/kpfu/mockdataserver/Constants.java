package ru.itis.kpfu.mockdataserver;

public class Constants {

    public static final String TYPE_INT = "Int";
    public static final String TYPE_LONG = "Long";
    public static final String TYPE_DOUBLE = "Double";
    public static final String TYPE_FLOAT = "Float";
    public static final String TYPE_SHORT = "Short";
    public static final String TYPE_BYTE = "Byte";
    public static final String TYPE_NUMBER = "Number";
    public static final String TYPE_CHAR = "Char";
    public static final String TYPE_CHAR_ARRAY = "CharArray";
    public static final String TYPE_STRING = "String";
    public static final String TYPE_BOOLEAN = "Boolean";
    
    public static final String[] PRIMITIVE_TYPES = new String[] {
            "Int", "Long", "Double", "Float", "Short", "Byte", "Number", "Char", "Boolean"
    };

    public static final String[] HEADER_OCCURRENCES = new String[]{
            "name", "title", "label", "mention", "header"
    };

    public static final String[] MESSAGE_OCCURRENCES = new String[] {
        "message", "comment", "subtitle", "desc", "description", "letter", "instruction"
    };

    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_PRIMITIVE_SINGLE = 1;
    public static final int TYPE_STRING_SINGLE = 2;
    public static final int TYPE_PRIMITIVE_LIST = 3;
    public static final int TYPE_STRING_LIST = 4;
    public static final int TYPE_MAP_SINGLE = 5;

}
