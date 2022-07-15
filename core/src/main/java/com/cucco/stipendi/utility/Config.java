/*
 * Config.java
 * 
 * 14 lug 2022
 */
package com.cucco.stipendi.utility;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Executive summary - A precise and concise description for the object. Useful to describe groupings of methods and introduce major terms.</p>
 * <p>State Information - Specify the state information associated with the object, described in a manner that decouples the states from the operations that may query or change these states. This should also include whether instances of this class are thread safe. (For multi-state objects, a state diagram may be the clearest way to present this information.) If the class allows only single state instances, such as java.lang.Integer, and for interfaces, this section may be skipped.</p>
 * 
 *
 * @author giovanni -- Auriga S.p.A.
 */
public class Config {
    protected Properties conf;

    protected static final LinkedList<String> TRUE = new LinkedList<String>();
    static {
        TRUE.add("true");
        TRUE.add("vero");
        TRUE.add("on");
        TRUE.add("yes");
        TRUE.add("si");
        TRUE.add("1");
    }

    protected static final LinkedList<String> FALSE = new LinkedList<String>();
    static {
        FALSE.add("false");
        FALSE.add("falso");
        FALSE.add("off");
        FALSE.add("no");
        FALSE.add("0");
    }

    private static final String LIST_CLASS_NAME = List.class.getSimpleName();

    private static final String RECORD_SEPARATOR = ",";

    private static final String FIELD_SEPARATOR = ":";

    private static final String SUBFIELD_SEPARATOR = "\\|";

    protected static final Map<String, Class<?>> primitiveMap = new HashMap<String, Class<?>>();
    static {
        primitiveMap.put("Boolean", Boolean.class);
        primitiveMap.put("Byte", Byte.class);
        primitiveMap.put("Character", Character.class);
        primitiveMap.put("Short", Short.class);
        primitiveMap.put("Integer", Integer.class);
        primitiveMap.put("Long", Long.class);
        primitiveMap.put("Float", Float.class);
        primitiveMap.put("Double", Double.class);
        primitiveMap.put("String", String.class);
    }

    /**
     * <p>Default constructor.</p>
     */
    public Config() {
    }

    /**
     * 
     * @param conf
     */
    public void setConf(Properties conf) {
        this.conf = conf;
    }

    /**
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    public String get(String key, String defaultValue) {
        String value = defaultValue;
        if (conf != null) {
            synchronized (conf) {
                value = conf.getProperty(key);
                if (value == null) {
                    value = defaultValue;
                }
            }
        }
        return value;
    }

    /**
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    public int get(String key, int defaultValue) {
        int value = defaultValue;
        if (conf != null) {
            synchronized (conf) {
                String strValue = conf.getProperty(key);
                if (strValue != null) {
                    try {
                        strValue = strValue.trim();
                        if (strValue.startsWith("0x")) {
                            value = Integer.parseInt(strValue.substring(2), 16);
                        } else {
                            value = Integer.parseInt(strValue);
                        }
                    } catch (NumberFormatException e) {
                        value = defaultValue;
                    }
                }
            }
        }
        return value;
    }

    /**
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    public byte get(String key, byte defaultValue) {
        byte value = defaultValue;
        if (conf != null) {
            synchronized (conf) {
                String strValue = conf.getProperty(key);
                if (strValue != null) {
                    try {
                        strValue = strValue.trim();
                        if (strValue.startsWith("0x")) {
                            value = Byte.parseByte(strValue.substring(2), 16);
                        } else {
                            value = Byte.parseByte(strValue);
                        }
                    } catch (NumberFormatException e) {
                        value = defaultValue;
                    }
                }
            }
        }
        return value;
    }

    /**
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    public long get(String key, long defaultValue) {
        long value = defaultValue;
        if (conf != null) {
            synchronized (conf) {
                String strValue = conf.getProperty(key);
                if (strValue != null) {
                    try {
                        strValue = strValue.trim();
                        if (strValue.startsWith("0x")) {
                            value = Long.parseLong(strValue.substring(2), 16);
                        } else {
                            value = Long.parseLong(strValue);
                        }
                    } catch (NumberFormatException e) {
                        value = defaultValue;
                    }
                }
            }
        }
        return value;
    }

    /**
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    public double get(String key, double defaultValue) {
        double value = defaultValue;
        if (conf != null) {
            synchronized (conf) {
                String strValue = conf.getProperty(key);
                if (strValue != null) {
                    strValue = strValue.trim();
                    try {
                        value = Double.parseDouble(strValue);
                    } catch (NumberFormatException e) {
                        value = defaultValue;
                    }
                }
            }
        }
        return value;
    }

    /**
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    public boolean get(String key, boolean defaultValue) {
        boolean value = defaultValue;
        if (conf != null) {
            synchronized (conf) {
                String strValue = conf.getProperty(key);
                if (strValue != null) {
                    strValue = strValue.toLowerCase();
                    strValue = strValue.trim();
                    if (TRUE.contains(strValue)) {
                        value = true;
                    } else if (FALSE.contains(strValue)) {
                        value = false;
                    }
                }
            }
        }
        return value;
    }

    /**
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    public LinkedList<String> get(String key, LinkedList<String> defaultValue) {
        LinkedList<String> value = defaultValue;
        if (conf != null) {
            synchronized (conf) {
                String strValue = conf.getProperty(key);
                if (strValue != null) {
                    value = new LinkedList<String>();
                    String token[] = strValue.split(RECORD_SEPARATOR);
                    for (int i = 0; i < token.length; i++) {
                        value.add(token[i].trim());
                    }
                }
            }
        }
        return value;
    }

    /**
     * 
     * @param key
     * @param defaultValue
     * @param fieldSeparator
     * @return
     */
    public LinkedList<String> get(String key, LinkedList<String> defaultValue, String fieldSeparator) {
        LinkedList<String> value = defaultValue;
        if (conf != null) {
            synchronized (conf) {
                String strValue = conf.getProperty(key);
                if (strValue != null) {
                    value = new LinkedList<String>();
                    String token[] = strValue.split(fieldSeparator);
                    for (int i = 0; i < token.length; i++) {
                        value.add(token[i].trim());
                    }
                }
            }
        }
        return value;
    }

    /**
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    public Collection<String> get(String key, Collection<String> defaultValue) {
        Collection<String> value = defaultValue;
        if (conf != null) {
            synchronized (conf) {
                String strValue = conf.getProperty(key);
                if (strValue != null) {
                    value = new LinkedList<String>();
                    String token[] = strValue.split(RECORD_SEPARATOR);
                    for (int i = 0; i < token.length; i++) {
                        value.add(token[i].trim());
                    }
                }
            }
        }
        return value;
    }

    /**
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    public int[] get(String key, int[] defaultValue) {
        int[] value = null;

        if (conf != null) {
            synchronized (conf) {
                String strValue = conf.getProperty(key);
                if (strValue != null) {
                    String[] strValues = strValue.split(RECORD_SEPARATOR);
                    value = new int[strValues.length];
                    boolean error = false;
                    int i = 0;
                    while (i < strValues.length && !error) {
                        try {
                            value[i] = Integer.parseInt(strValues[i].trim());
                        } catch (NumberFormatException e) {
                            error = true;
                            value = null;
                        }
                        i++;
                    }
                }
            }
        }

        if (value == null) {
            value = defaultValue;
        }

        return value;
    }

    /**
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    public Hashtable<String, String> get(String key, Hashtable<String, String> defaultValue) {
        Hashtable<String, String> value = defaultValue;
        if (conf != null) {
            synchronized (conf) {
                String strValue = conf.getProperty(key);
                if (strValue != null) {
                    value = new Hashtable<String, String>();
                    String[] token = strValue.split(RECORD_SEPARATOR);
                    for (int i = 0; i < token.length; i++) {
                        String[] subToken = token[i].split(FIELD_SEPARATOR);
                        if (subToken.length == 2) {
                            value.put(subToken[0].trim(), subToken[1].trim());
                        }
                    }
                }
            }
        }
        return value;
    }

    /**
     * 
     * @param key
     * @param defaultValue
     * @param recordSeparator
     * @param fieldSeparator
     * @return
     */
    public Hashtable<String, String> get(String key, Hashtable<String, String> defaultValue, String recordSeparator, String fieldSeparator) {
        Hashtable<String, String> value = defaultValue;
        if (conf != null) {
            synchronized (conf) {
                String strValue = conf.getProperty(key);
                if (strValue != null) {
                    value = new Hashtable<String, String>();
                    String[] token = strValue.split(recordSeparator);
                    for (int i = 0; i < token.length; i++) {
                        String[] subToken = token[i].split(fieldSeparator);
                        if (subToken.length == 2) {
                            value.put(subToken[0].trim(), subToken[1].trim());
                        }
                    }
                }
            }
        }
        return value;
    }

    /**
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    public Hashtable<String, Object> get(String key, Map<String, Object> defaultValue) {
        return get(key, defaultValue, RECORD_SEPARATOR, FIELD_SEPARATOR, SUBFIELD_SEPARATOR);
    }

    /**
     * 
     * @param key
     * @param defaultValue
     * @param recordSeparator
     * @param fieldSeparator
     * @return
     */
    public Hashtable<String, Object> get(String key, Map<String, Object> defaultValue, String recordSeparator, String fieldSeparator) {
        return get(key, defaultValue, recordSeparator, fieldSeparator, SUBFIELD_SEPARATOR);
    }

    /**
     * @param key
     * @param defaultValue
     * @param recordSeparator
     * @param fieldSeparator
     * @param subfieldSeparator
     * @return
     */
    public Hashtable<String, Object> get(String key, Map<String, Object> defaultValue, String recordSeparator, String fieldSeparator, String subfieldSeparator) {
        Hashtable<String, Object> value = new Hashtable<String, Object>(defaultValue);
        if (conf != null) {
            synchronized (conf) {
                String strValue = conf.getProperty(key);
                if (strValue != null) {
                    value = new Hashtable<String, Object>();
                    String[] token = strValue.split(recordSeparator);
                    Pattern objectPattern = Pattern.compile("^\\(([A-Za-z]+)\\)(.+)$");
                    for (int i = 0; i < token.length; i++) {
                        String[] subToken = token[i].split(fieldSeparator);
                        if (subToken.length == 2) {
                            String valueToken = subToken[1].trim();
                            Matcher m = objectPattern.matcher(valueToken);
                            if (m.matches() && m.groupCount() == 2) {
                                try {
                                    Object obj = null;
                                    if (primitiveMap.containsKey(m.group(1))) {
                                        Class<?> clazz = primitiveMap.get(m.group(1));
                                        Method method = clazz.getMethod("valueOf", String.class);
                                        int mods = method.getModifiers();
                                        if (Modifier.isStatic(mods) && Modifier.isPublic(mods)) {
                                            obj = method.invoke(null, m.group(2));
                                            value.put(subToken[0].trim(), obj);
                                        }
                                    } else if (LIST_CLASS_NAME.equals(m.group(1))) {
                                        value.put(subToken[0].trim(), buildList(m.group(2), subfieldSeparator));
                                    } else {
                                        value.put(subToken[0].trim(), valueToken);
                                    }
                                } catch (Exception e) {
                                    /*
                                     * Nulla da fare.
                                     */
                                }
                            } else {
                                value.put(subToken[0].trim(), valueToken);
                            }

                        }
                    }
                }
            }
        }
        return value;
    }

    /**
     * 
     * @param key
     * @param defaultValue
     * @return BigDecimal
     */
    public BigDecimal get(String key, BigDecimal defaultValue) {
        BigDecimal value = defaultValue;
        if (conf != null) {
            synchronized (conf) {
                String strValue = conf.getProperty(key);
                if (strValue != null) {
                    try {
                        strValue = strValue.trim();
                        value = new BigDecimal(strValue);
                    } catch (NumberFormatException e) {
                        value = defaultValue;
                    }
                }
            }
        }
        return value;
    }

    /**
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    public InetSocketAddress get(String key, InetSocketAddress defaultValue) {
        InetSocketAddress value = defaultValue;
        if (conf != null) {
            synchronized (conf) {
                String strValue = conf.getProperty(key);
                if (strValue != null) {
                    String[] addrPart = strValue.trim().split(FIELD_SEPARATOR);
                    if (addrPart.length == 2) {
                        try {
                            value = new InetSocketAddress(addrPart[0].trim(), Integer.parseInt(addrPart[1].trim()));
                        } catch (NumberFormatException e) {
                            /*
                             * Nulla da fare.
                             */
                        }
                    }
                }
            }
        }

        return value;
    }

    /**
     * <p>Builds a list populated with the given value string splitted by fieldSeparator</p>
     * @param value
     * @param fieldSeparator
     * @return
     */
    private List<String> buildList(String value, String fieldSeparator) {
        List<String> list = null;
        if (value != null) {
            list = Arrays.asList(value.split(fieldSeparator));
        }
        return list;
    }

    /**
     * 
     * <p>Use this method to retrieve any kind of enum hashtable from configuration file.</p>
     * 
     * @param key - key to be searched in the configuration file
     * @param defaultValue - default value
     * @param enumClass - Enum class 
     * @return Hashtable<String, T> - returns an hashtable with the specific String value and Enum type.
     */
    public <T extends Enum<?>> Hashtable<String, T> get(String key, Hashtable<String, T> defaultValue, Class<T> enumClass) {

        Hashtable<String, T> value = defaultValue;

        if (conf != null) {
            synchronized (conf) {
                String strValue = conf.getProperty(key);
                if (strValue != null) {
                    value = new Hashtable<String, T>();

                    String[] token = strValue.split(RECORD_SEPARATOR);

                    for (int i = 0; i < token.length; i++) {

                        String[] subToken = token[i].split(FIELD_SEPARATOR);

                        if (subToken.length == 2) {

                            T enumValueToBeAdd = getEnumValue(enumClass, subToken[1].trim());
                            value.put(subToken[0].trim(), enumValueToBeAdd);

                        }

                    }

                }
            }
        }

        return value;

    }

    /**
     * 
     * <p>Use this method to retrieve any kind of enum hashtable from configuration file.</p>
     * 
     * @param key - key to be searched in the configuration file
     * @param defaultValue - default value
     * @param enumClass - Enum class 
     * @return Hashtable<T, String> - returns an hashtable with the specific Enum value and String type.
     */
    public <T extends Enum<?>> Hashtable<T, String> getMapWithEnumKey(String key, Hashtable<T, String> defaultValue, Class<T> enumClass) {

        Hashtable<T, String> value = defaultValue;

        if (conf != null) {
            synchronized (conf) {
                String strValue = conf.getProperty(key);
                if (strValue != null) {
                    value = new Hashtable<T, String>();

                    String[] token = strValue.split(RECORD_SEPARATOR);

                    for (int i = 0; i < token.length; i++) {

                        String[] subToken = token[i].split(FIELD_SEPARATOR);

                        if (subToken.length == 2) {

                            T enumValueToBeAdd = getEnumValue(enumClass, subToken[0].trim());
                            value.put(enumValueToBeAdd, subToken[1].trim());

                        }

                    }

                }
            }
        }

        return value;

    }

    /**
     * 
     * <p>Use this method to retrieve any kind of enum list from the configuration file</p>
     * 
     * @param key - key to be searched in the configuration file
     * @param enumClass - Enum class 
     * @param defaultValue - the default list
     * @return List<T> - returns a list of objects of the specific Enum type.
     */
    public <T extends Enum<?>> List<T> get(String key, List<T> defaultValue, Class<T> enumClass) {
        List<T> value = defaultValue;

        if (conf != null) {
            synchronized (conf) {
                String strValue = conf.getProperty(key);
                if (strValue != null) {
                    value = new LinkedList<T>();
                    String[] token = strValue.split(RECORD_SEPARATOR);

                    for (int i = 0; i < token.length; i++) {
                        T valueToBeAdd = getEnumValue(enumClass, token[i].trim());

                        if (valueToBeAdd != null) {
                            value.add(valueToBeAdd);
                        }
                    }
                }
            }
        }

        return value;
    }

    /**
     * <p>Checks if the specified enumName is a valid enumerator value for the enumClass.</p>
     * <p>In this case the relevant Enum value is returned.</p>
     *
     * @param enumClass - enum class name
     * @param enumName - value to be searched in the enumerator
     * @return returnEnumValue or null if the value is not present in the enum
     */
    public static <T extends Enum<?>> T getEnumValue(Class<T> enumClass, String enumName) {
        boolean matched = false;
        T returnEnumValue = null;

        java.util.Iterator<T> it = Arrays.asList(enumClass.getEnumConstants()).iterator();
        while (!matched && it.hasNext()) {
            T object = it.next();

            matched = object.toString().equals(enumName);
            if (matched) {
                returnEnumValue = object;
            }
        }

        return returnEnumValue;
    }
}
