/*
 * Utility.java
 * 
 * 15 lug 2022
 */
package com.cucco.stipendi.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Executive summary - A precise and concise description for the object. Useful to describe groupings of methods and introduce major terms.</p>
 * <p>State Information - Specify the state information associated with the object, described in a manner that decouples the states from the operations that may query or change these states. This should also include whether instances of this class are thread safe. (For multi-state objects, a state diagram may be the clearest way to present this information.) If the class allows only single state instances, such as java.lang.Integer, and for interfaces, this section may be skipped.</p>
 * 
 *
 * @author giovanni -- Auriga S.p.A.
 */
public class Utility {
    /**
     * 
     * @param path
     * @return
     */
    public static String normalizePath(String path) {
        String normalizedPath = path;
        if (path != null) {
            String fs = System.getProperty("file.separator");
            char lastPathChar = path.charAt(path.length() - 1);
            if (lastPathChar != '\\' && lastPathChar != '/') {
                normalizedPath += fs;
            }
            Pattern pattern = null;
            if (fs.equals("/")) {
                /*
                 * Trasformazione di tutti gli '\' in '/'.
                 */
                pattern = Pattern.compile("\\\\");
            } else if (fs.equals("\\")) {
                /*
                 * Trasformazione di tutti gli '/' in '\'.
                 */
                pattern = Pattern.compile("/");
            }
            if (pattern != null) {
                fs = Matcher.quoteReplacement(fs);

                Matcher matcher = pattern.matcher(normalizedPath);
                normalizedPath = matcher.replaceAll(fs);
            }
        }
        return normalizedPath;
    }

    /**
     * Sostituisce in una stringa i riferimenti alle system properties con i valori che queste assumono. Nella stringa devono essere specificate nel formato ${nome_system_property}, dove
     * nome_system_property può essere composta da cifre, lettere maiuscole e minuscole, undescore, punto.
     * 
     * @param input
     * @return
     */
    public static String replaceEnvRefereces(String input) {
        String output = null;
        if (input != null) {
            output = new String(input);
            Pattern pattern = Pattern.compile("\\$\\{([\\p{Alnum}\\_\\.])+\\}");
            Matcher matcher = pattern.matcher(output);
            boolean found = matcher.find();
            while (found) {
                String group = matcher.group();
                String value = System.getProperty(group.substring(2, group.length() - 1));
                if (value == null) {
                    value = "null";
                }
                output = matcher.replaceFirst(Matcher.quoteReplacement(value));
                matcher = pattern.matcher(output);
                found = matcher.find();
            }
        }
        return output;
    }

}
