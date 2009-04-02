/*
 * StringUtils.java
 *
 * Created on August 26, 2002, 3:05 PM
 */

package com.infusion.util;


import static com.infusion.util.CollectionUtil.CollectionValues;
import static com.infusion.util.CollectionUtil.StringToMap;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Utility class for dealing with String (mostly)
 *
 * @author eric
 */
public class StringUtils implements Serializable {
// ========================================================================================================================
//    Static Fields
// ========================================================================================================================

    /**
     * Defines regex special characters
     * <p/>
     * '*'
     * '.'
     * '('
     * ')'
     * '?'
     * '#'
     * '|'
     * '{'
     * '}'
     * '+'
     */
    public static final Set<Character> RegExSpecial = new HashSet<Character>();

    /**
     * Active = 1
     */
    public static final int STATUS_ACTIVE = 1;

    /**
     * Inactive = 0
     */
    public static final int STATUS_INACTIVE = 0;

    /**
     * YesNoOpts.put("0", "No");
     * YesNoOpts.put("1", "Yes");
     */
    public static Map<String, String> YesNoOpts;

    /**
     * YesNo.put("Yes", "Yes");
     * YesNo.put("No", "No");
     */
    public static Map<String, String> YesNo;

    /**
     * None, View, Edit
     */
    public static Map<String, String> ViewOpts;

    /**
     * activeMapInt.put("0", "Inactive");
     * activeMapInt.put("1", "Active");
     */
    public static Map<String, String> activeMapInt;

    /**
     * activeMapStr.put("Inactive", "Inactive");
     * activeMapStr.put("Active", "Active")
     */
    public static Map<String, String> activeMapStr;

    /**
     * Chart of funky Windows characters and their respective sane ascii counterparts
     */
    public static Map<Character, Object> conversionChart;

    /**
     * Pattern to search for digits.
     */
    public static Pattern digitPattern = Pattern.compile("[0-9]");

    /**
     * Pattern for letters.
     */
    public static Pattern letterPattern = Pattern.compile("[A-Za-z]");

    /**
     * Pattern for word starting with a Vowel (A, E, I, O, U)
     */
    public static Pattern startwithVowelPattern = Pattern.compile("^[A,a,E,e,I,i,O,o,U,u]");

    /**
     * Simple map with enable/disable instead of yes/no.
     */
    public static Map<String, String> enabledMap;

    private static final String[] hexTable = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * static priority map
     */

    public static final Map<String, String> priorityMap;

    /**
     * static detailed priority map
     */

    public static final Map<String, String> detailedPriorityMap;

// ========================================================================================================================
//    Static Methods
// ========================================================================================================================

    static {
        priorityMap = new LinkedHashMap<String, String>();
        priorityMap.put("1", "1");
        priorityMap.put("2", "2");
        priorityMap.put("3", "3");

        detailedPriorityMap = new LinkedHashMap<String, String>();
        detailedPriorityMap.put("1", "1. Critical");
        detailedPriorityMap.put("2", "2. Essential");
        detailedPriorityMap.put("3", "3. Non-Essential");
    }

    static {
        RegExSpecial.add('*');
        RegExSpecial.add('.');
        RegExSpecial.add('(');
        RegExSpecial.add(')');
        RegExSpecial.add('?');
        RegExSpecial.add('#');
        RegExSpecial.add('|');
        RegExSpecial.add('{');
        RegExSpecial.add('}');
        RegExSpecial.add('+');
    }

    static {
        YesNoOpts = new LinkedHashMap<String, String>();
        YesNoOpts.put("1", "Yes");
        YesNoOpts.put("0", "No");

        YesNo = new LinkedHashMap<String, String>();
        YesNo.put("Yes", "Yes");
        YesNo.put("No", "No");

        enabledMap = new LinkedHashMap<String, String>();
        enabledMap.put("true", "Enable");
        enabledMap.put("false", "Disable");

        ViewOpts = new LinkedHashMap<String, String>();
        ViewOpts.put("None", "None");
        ViewOpts.put("View", "View");
        ViewOpts.put("Edit", "Edit");
    }

    static {
        activeMapInt = new LinkedHashMap<String, String>();
        activeMapInt.put(String.valueOf(STATUS_ACTIVE), "Active");
        activeMapInt.put(String.valueOf(STATUS_INACTIVE), "Inactive");

        activeMapStr = new LinkedHashMap<String, String>();
        activeMapStr.put("Active", "Active");
        activeMapStr.put("Inactive", "Inactive");
    }

    static {
        conversionChart = new HashMap<Character, Object>();
        conversionChart.put((char) 129, "");
        conversionChart.put((char) 141, "");
        conversionChart.put((char) 143, "");
        conversionChart.put((char) 144, "");
        conversionChart.put((char) 157, "");
        conversionChart.put((char) 133, "...");
        conversionChart.put((char) 139, '<');
        conversionChart.put((char) 145, '\'');
        conversionChart.put((char) 146, '\'');
        conversionChart.put((char) 147, '"');
        conversionChart.put((char) 148, '"');
        conversionChart.put((char) 149, '-');
        conversionChart.put((char) 150, '-');
        conversionChart.put((char) 151, '-');
        conversionChart.put((char) 160, ' ');
    }

    /**
     * Returns the proposed data if boolean value is true, otherwise empty string
     *
     * @param ifStr The boolean to determine what to return
     * @param data  The data
     */
    public static String BoolPrint(boolean ifStr, String data) {
        return ifStr ? data : "";
    }

    /**
     * Returns a list of the most common email names
     * <p/>
     * yahoo.com<BR>
     * hotmail.com<BR>
     * google.com<BR>
     * aol.com<BR>
     * msn.com<BR>
     * gmail.com<BR>
     */
    public static ArrayList<String> CommonEmailNames() {
        ArrayList<String> names = new ArrayList<String>();
        names.add("yahoo.com");
        names.add("hotmail.com");
        names.add("google.com");
        names.add("aol.com");
        names.add("msn.com");
        names.add("gmail.com");
        return names;
    }

    public static final String DoubleSQLEscapted(String str) {
        str = StringUtils.Nz(str);
        if (str.equals("") || str.equals("null"))
            return null;
        else
            return replace(replace(str, "\'", "\\\'"), "\\", "\\\\");
    }

    public static int FirstIndexOf(StringBuffer check, int startPos, String... args) {
        int index = check.length() + 1;
        for (String s : args) {
            int _index = check.indexOf(s, startPos);
            if (_index > -1) {
                index = Math.min(_index, index);
            }
        }
        if (index == check.length() + 1) {
            return -1;
        }
        return index;
    }

    /**
     * A null safe firstLetterUpper
     *
     * @see #firstLetterUpper(String)
     */
    public static String FirstUp(String str) {
        if (str == null) {
            return str;
        }
        return firstLetterUpper(str.toLowerCase());
    }

    /**
     * Determines if a given String is a primary key field
     */
    public static boolean IsIdFld(String fldName) {
        if (fldName == null) return false;
        return fldName.indexOf("Id") == fldName.length() - 2
                || fldName.indexOf("ID") == fldName.length() - 2
                || fldName.indexOf("ID") == 0
                || fldName.indexOf("Id") == 0;
    }

    /**
     * Checks val for a value, if null, returns empty string
     */
    public static String Nz(String val) {
        if (val == null || "null".equals(val))
            return "";
        else
            return val;
    }

    /**
     * Checks if the object is null, if it is, returns "", otherwise returns object.toString
     */
    public static String Nz(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    /**
     * Returns the non-null of two objects
     *
     * @see #x(Object...)
     */
    public static Object Nz(Object obj1, Object obj2) {
        return x(obj1, obj2);
    }

    /**
     * Checks a String (val) for a value, if none found, returns valIfNull
     */
    public static final String Nz(String val, String valIfNull) {
        if (Nz(val).equals("") || Nz(val).equalsIgnoreCase("null")) {
            if (valIfNull == null)
                return "";
            else
                return valIfNull;
        } else
            return val;
    }

    /**
     * Checks if the int is 0, if so returns a second parameter
     */
    public static String Nz(int d, int dd) {
        return d == 0 ? "" + dd : "" + d;
    }

    /**
     * Checks if the double is 0, if so returns a second parameter
     */
    public static String Nz(double d, double dd) {
        return d == 0.0 ? "" + dd : "" + d;
    }

    /**
     * Returns newVal if val is null or empty
     */
    public static String NzEz(String val, String newVal) {
        val = Nz(val).trim();
        if (val == null || val.equals(""))
            if (newVal == null || newVal.equals(""))
                return "";
            else
                return newVal;
        else
            return val;
    }

    /**
     * Returns the first non-null and non-emptystring value in a List
     *
     * @return First non-null and non-emptystring value (or empty-string if none exist)
     */
    public static String NzList(List<String> list) {
        for (String string : list) {
            if (StringUtils.is(string)) {
                return string;
            }
        }
        return "";
    }

    public static String NzNa(String strToCheck) {
        return Nz(strToCheck, "N/A");
    }

    /**
     * If a string is null, returns "0"
     */
    public static String NzNum(String str) {
        return Nz(str, "0");
    }

    /**
     * Returns a second number if the first is not greater than 0
     */
    public static String NzNum(String string, String s) {
        int num = NumberUtils.parseInt(string);
        return num > 0 ? string : s;
    }

    /**
     * If a String is null, returns 1
     */
    public static String NzNumOne(String str) {
        return Nz(str, "1");
    }

    /**
     * Returns whatever is between the < > in the String
     *
     * @param email email to parse
     * @return parsed email
     */

    public static String ParseEmail(String email) {
        String parsed = null;

        if (email != null) {
            int start = 0;
            int stop = 0;
            if ((email.contains("<")) && (email.contains(">"))) {
                start = email.lastIndexOf("<") + 1;
                stop = email.lastIndexOf(">");
            } else if ((email.contains("&lt;")) && (email.contains(">"))) {
                start = email.lastIndexOf("&lt;") + 4;
                stop = email.lastIndexOf(">");
            }
            if (start < stop) {
                parsed = email.substring(start, stop);
            } else {
                parsed = email;
            }
        }

        if (parsed != null) {
            return parsed.replace("<", "").replace("&lt;", "").replace(">", "").replace("\"", "").trim().replace(" ", "");
        } else {
            return "";
        }
    }

    /**
     * get the priority string
     *
     * @param priority priority
     * @return priority string
     */

    public static String priorityStr(int priority) {
        switch (priority) {
            case 0:
                return "All";
            case 1:
                return "1. Critical";
            case 2:
                return "2. Essential";
            case 3:
                return "3. Non-Essential";
        }
        return "Unknown";
    }

    /**
     * Proper cases a string
     * <p/>
     * eg:
     * <p/>
     * JOHN -> John<BR>
     * martineau -> Martineau<BR>
     * fooBoy -> Fooboy
     */
    public static String ProperCase(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return "";
        }
        if (str.length() == 1) {
            return str.toUpperCase();
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    /**
     * Replaces all quotes and newlines in a String
     */
    public static String QuoteEscape(String data) {
        return "\"" + StringUtils.Nz(data).replaceAll("\"", "").replaceAll("\n", "") + "\"";
    }

    /**
     * Generates a random email address
     *
     * @see #RandomEmailName(long)
     * @see #RandomLowerString(long,int)
     */
    public static String RandomEmailAddress(long seed) {
        int random = NumberUtils.randomInt(seed, 9, 15);
        int randomName = NumberUtils.randomInt(random, 0, 4);
        String email = RandomLowerString(seed, random);
        email += "@" + CommonEmailNames().get(randomName);
        return email;
    }

    /**
     * Returns a random item from CommonEmailNames()
     *
     * @see #CommonEmailNames()
     */
    public static String RandomEmailName(long seed) {
        List<String> emailNames = CommonEmailNames();
        int random = NumberUtils.randomInt(seed, 0, emailNames.size());
        return emailNames.get(random);
    }

    /**
     * Returns a random bunch of lower-case poo given a certain length
     * <p/>
     * ie: length 7 -> pfxlvek
     *
     * @return Random String
     */
    public static String RandomLowerString(long seed, int size) {
        StringBuffer tmp = new StringBuffer();
        Random random = new Random(seed);
        for (int i = 0; i < size; i++) {
            long newSeed = random.nextLong();
            int currInt = (int) (26 * random.nextFloat());
            currInt += 97;
            random = new Random(newSeed);
            tmp.append((char) currInt);
        }
        return tmp.toString();
    }

    /**
     * Returns a random bunch of numbers given a certain length
     * <p/>
     * ie: length 7 -> "1598560"
     *
     * @return Random String
     */
    public static String RandomNumberString(long seed, int size) {
        StringBuffer tmp = new StringBuffer();
        Random random = new Random(seed);
        for (int i = 0; i < size; i++) {
            long newSeed = random.nextLong();
            int currInt = (int) (10 * random.nextFloat());
            currInt += 48;
            random = new Random(newSeed);
            tmp.append((char) currInt);
        }
        return tmp.toString();
    }

    /**
     * Generates a random phone number
     *
     * @see #RandomNumberString(long,int)
     */
    public static String RandomPhoneNumber(long seed) {
        Random random = new Random(seed);
        StringBuffer tmp = new StringBuffer();
        tmp.append("(" + RandomNumberString(random.nextLong(), 3) + ") ");
        tmp.append(RandomNumberString(random.nextLong(), 3) + "-");
        tmp.append(RandomNumberString(random.nextLong(), 4));
        return tmp.toString();
    }

    /**
     * Returns a random bunch of upper-case poo given a certain length
     * <p/>
     * ie: length 7 -> PFXLVEK
     *
     * @return Random String
     */
    public static String RandomUpperString(long seed, int size) {
        StringBuffer tmp = new StringBuffer();
        Random random = new Random(seed);
        for (int i = 0; i < size; i++) {
            long newSeed = random.nextLong();
            int currInt = (int) (26 * random.nextFloat());
            currInt += 65;
            random = new Random(newSeed);
            tmp.append((char) currInt);
        }
        return tmp.toString();
    }

    /**
     * Replaces qualifiers around a string with another set of qualifiers.
     * <p/>
     * ie: 'Replace my Qualifier' with "Replace My Qualifier"
     *
     * @param source      The source StringBUilder
     * @param lookFor     What qualifier to look for
     * @param replaceWith What qualifier to replace with
     */
    public static StringBuilder ReplaceQualifier(StringBuilder source, char lookFor, char replaceWith) {
        if (source.length() > 0) {
            int end = source.length() - 1;
            if (source.charAt(0) == lookFor && source.charAt(end) == lookFor) {
                source.replace(0, 1, "" + replaceWith);
                source.replace(end, end + 1, "" + replaceWith);
            }
        }
        return source;
    }

    /**
     * Returns the right 'i' characters of a String
     */
    public static String Right(String str, int i) {
        if (str == null || str.length() < i)
            return str;
        else
            return str.substring(str.length() - i);
    }

    public static final String SQLEscaped(String str) {
        str = StringUtils.Nz(str);
        if (str.equals("") || str.equals("null")) return null;
        else return replace(replace(str, "\\", "\\\\"), "\'", "\\\'");
    }



    /**
     * Escapes out and single quotes a String
     *
     * @deprecated
     */
    public static final String SQLString(String str) {
        return SingleQuoted(SQLEscaped(str));
    }

    /**
     * @see #SQLString(String)
     */
    public static String SQLString(int i) {
        return SQLString(String.valueOf(i));
    }

    /**
     * @see #SQLString(String)
     */
    public static String SQLString(double d) {
        return SQLString(String.valueOf(d));
    }

    /**
     * Single quotes a String
     * <p/>
     * eric -> 'eric'
     */
    public static final String SingleQuoted(String str) {
        if (str == null)
            return null;
        else
            return "'" + str + "'";
    }

    /**
     * Removes all quotes from a String
     */
    public static String StripQuotes(String s) {
        if (!is(s)) {
            return "";
        }

        String stripped = s.replace("'", "");
        stripped.replace("\"", "");

        return stripped;
    }

    /**
     * Removes all spaces from a String
     */
    public static String StripSpaces(String s) {
        return s == null ? "" : s.replaceAll(" ", "");
    }

    public static String StripWhiteSpace(String s) {
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(Nz(s));
        return matcher.replaceAll("");
    }

    /**
     * Returns true if an email address is valid
     *
     * @param email
     * @return
     */
    public static boolean ValidEmail(String email) {
        boolean ret = false;
        String parsed = ParseEmail(email);
        if (is(parsed)) {
            String filter = "^([a-zA-Z0-9_\\.-])+@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9])+$";
            ret = parsed.matches(filter);
        }
        return ret;
    }

    /**
     * Trims a string to include only data after the first occurrence of a certain value
     *
     * @param data  The original string
     * @param query The value to look for in the string
     * @return The contents of the string after (not including) the searched for data
     */
    public static String after(String data, String query) {
        if (data == null || query == null) {
            return null;
        }

        int index = data.indexOf(query);
        if (index == -1) {
            return data;
        } else {
            return data.substring(index + 1);
        }
    }

    /**
     * Takes in a String array and creates an arraylist
     */
    public static ArrayList<String> arrayToList(String[] s) {
        ArrayList<String> a = new ArrayList<String>();
        if (s == null) return a;
        for (String value : s) {
            a.add(value);
        }
        return a;
    }

    /**
     * Wraps text to a certain width
     */
    public static String blockText(String textToBlock, int blockwidth) {
        StringBuffer returnVal = new StringBuffer();
        StringBuffer tmp = new StringBuffer();
        if (textToBlock == null || textToBlock == "" || blockwidth == 0) {
            return null;
        } else {
            returnVal = new StringBuffer();
            tmp = new StringBuffer(textToBlock);
            int curr = 0;
            for (int i = 0; i < tmp.length(); i++) {
                curr++;
                char c = tmp.charAt(i);
                if (c == '\n') {
                    curr = 0;
                }
                if (curr >= (blockwidth - 4)) {
                    if (c == ' ' || curr >= blockwidth + 6) {
                        returnVal.append('\n');
                        //tmp.insert(i+1, '\n');
                        while (c == ' ') {
                            if (tmp.length() <= i) {
                                break;
                            }
                            c = tmp.charAt(i);
                            i++;
                        }
                        i--;
                        curr = 0;
                    }
                }
                returnVal.append(c);
            }
            return returnVal.toString();
        }
    }

    /**
     * Returns false for 0, 1 for everything else
     */
    public static boolean bool(int i) {
        return intToBool(i);
    }

    public static String bytesToHexString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(toHex(b));
        }
        return hex.toString();

    }

    /**
     * Checks the front of a String for a value, if it doesn't find it, it inserts
     * that value i.e. http://www.enovasys.com vs www.enovasys.com (http://)
     */
    public static String checkFront(String str, String front, String plop) {
        if (str == null) return str;
        if (front == null) return str;
        if (str.length() < front.length())
            return Nz(plop) + str;
        else if (Nz(str.subSequence(0, front.length()).toString()).equals(front))
            return str;
        else
            return Nz(plop) + str;
    }

    /**
     * Chomp deletes the last two characters of a String
     */
    public static String chomp(String str) {
        if (str == null || str.length() < 2) {
            return str;
        }

        return str.substring(0, str.length() - 2);
    }

    /**
     * Returns a String with the last character chopped off
     */
    public static String chop(String str) {
        if (str != null && !str.equals("")) {
            return str.substring(0, str.length() - 1);
        } else
            return str;
    }

    /**
     * Concats a bunch of strings together (descriptions included), separating by a separator.
     * <p/>
     * Will not print separator if data doesn't exist.
     */
    public static String concatWDesc(String fvSep, String separator, String... data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i += 2) {
            String field = data[i];
            String fData = data[i + 1];
            if (StringUtils.is(fData)) {
                sb.append(field).append(fvSep);
                sb.append(fData);
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    /**
     * Determines if a String contains a space character
     */
    public static boolean containsSpace(String string) {
        return string != null && string.indexOf(" ") > -1;
    }

    /**
     * Creates a labeled email address in the format ~label~ <~email~>. (ie My Name < myemail@email.com >)
     * @param label first part of email
     * @param rawEmail raw email address
     * @return combined result
     */
    public static String createLabeledEmailAddress(String label, String rawEmail) {
        return label + "<" + rawEmail + ">";
    }

    /**
     * Removes whitespace on either side of a String
     */
    public static String crop(String str) {
        return Nz(str).replaceAll("(^\\s*|\\s*$)", "");
    }

    /**
     * Gets rid of all parenthesis in a string
     */
    public static String deParenth(String str) {
        return str.replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)");
    }

    /**
     * Takes in a delimited String and creates an arraylist
     */
    public static ArrayList<String> delStringToList(String s) {
        return delStringToList(s, ",");
    }

    /**
     * Takes in a delimited String and creates an arraylist
     */
    public static ArrayList<String> delStringToList(String s, String del) {
        return arrayToList(Nz(s).split(del));
    }

    /**
     * Converts an enum to a map of values
     */
    public static Map<String, String> enumValues(Enum[] anEnum) {
        Map<String, String> values = new LinkedHashMap<String, String>();
        for (Enum anEnum1 : anEnum) {
            String name = anEnum1.name();
            values.put(name, name);
        }
        return values;
    }

    /**
     * Checks if an object test is equal (using .equals()) to any of items
     * against (varargs) is null-safe
     *
     * @param test    The item you are testing for
     * @param against The varargs you are testing against
     * @return true if test equals ANY of the varargs
     */
    public static <X> boolean eq(X test, X... against) {
        if (test == null) return false;
        for (X x : against) {
            if (x != null) {
                if (x.equals(test)) return true;
            }
        }
        return false;
    }

    /**
     * Checks if two Strings are equal.  It first Nzs both the Strings,
     * so it's null-safe
     */
    public static boolean eq(String str, String str2) {
        return (Nz(str).equals(Nz(str2)));
    }

    /**
     * @see #eq(String,String)
     */
    public static boolean equals(String str, String str2) {
        return eq(str, str2);
    }

    /**
     * Checks to see if a String exists in a String array (ignore case)
     */
    public static boolean equals(String str, String[] str2) {
        if (str == null || str2 == null) return false;
        for (String aStr2 : str2) {
            if (str.equalsIgnoreCase(aStr2)) return true;
        }
        return false;
    }

    /**
     * Escapes all &lt; and \\n characters in html
     */
    public static String escapeHTML(String string) {
        if (string == null)
            return string;
        else {
            return string.replaceAll("<", "&lt;");
        }
    }

    /**
     * Escapes quotes in String by converting them to single quotes
     *
     * @param str (null-safe)
     */
    public static String escapeQuote(String str) {
        if (str == null) return null;
        return replace(str, "\"", "'");
    }

    /**
     * Returns a String with the first letter lowercased
     */
    public static String firstLetterLower(String str) {
        if (str == null || str == "") {
            return str;
        } else {
            String firstLetter = str.substring(0, 1).toLowerCase();
            String lastPart = str.substring(1);
            return firstLetter.concat(lastPart);
        }
    }

    /**
     * Capitalizes the first letter in a String
     */
    public static String firstLetterUpper(String str) {
        if (str == null) return "";
        if (str.length() > 1) {
            String firstLetter = str.substring(0, 1).toUpperCase();
            String lastPart = str.substring(1);
            return firstLetter.concat(lastPart);
        } else if (str.length() == 1) {
            return str.toUpperCase();
        } else {
            return "";
        }
    }

    /**
     * Formats a phone number given a String
     * <p/>
     * 4803328222 -> (480) 332-8222<BR>
     * 3328222 -> 332-8002<BR>
     * 14803328222 -> 1 (480) 332-8222<BR>
     * +4803328222 -> +4803328002 (start with +)<BR>
     * 12345678901234 -> 12345678901234 (greater than 11 chars)
     */
    public static String formatPhone(String str) {
        if (!StringUtils.is(str)) return "";
        //Be more friendly to international users...
        if (str.charAt(0) == '+'
                ||
                str.charAt(0) == '0')
            return str;
        String strWork = stripNonNumberNoPeriod(str);
        if (strWork.length() == 7) {
            return strWork.substring(0, 3) + "-" + strWork.substring(3);
        } else if (strWork.length() == 10) {
            return "(" + strWork.substring(0, 3) + ") " + strWork.substring(3, 6) + "-" + strWork.substring(6);
        } else if (strWork.length() == 11) {
            return strWork.charAt(0) + " (" + strWork.substring(1, 4) + ") " +
                    strWork.substring(4, 7) + "-" + strWork.substring(7);
        } else
            return str;
    }

    /**
     * Takes a phone number and country code and returns a number in the format Protus wants.
     */
    public static String formatProtusNumber(String number, String countryCode) {
        if (!StringUtils.is(number)) return number;// make sure it's not null or empty already
        String strippedFax = StringUtils.stripNonNumberNoPeriod(number);// get rid of all non numbers

        if (!is(strippedFax)) return "";// number is null or empty after being stripped

        //if there is a 0 at the beginning of the number than get rid of it.
        strippedFax = (strippedFax.charAt(0) == '0') ? strippedFax.substring(1, strippedFax.length()) : strippedFax;

        // if there is a + or if the number is 11 digits or larger then it must already have the country code.
        if (number.charAt(0) == '+' || strippedFax.length() >= 11) return strippedFax;

        number = strippedFax;

        // make sure no invalid country codes get in
        String ctryCode = StringUtils.stripNonNumberNoPeriod(countryCode);

        //check the country code...if it is empty/null then assume American country code
        ctryCode = (!StringUtils.is(ctryCode)) ? "1" : ctryCode;

//        int index = 0;
        // go through the whole country code and see if the number already has it at the beginning. If it doesn't match
        // then return the number with the country code prepended. Otherwise return the number as is.
        if (!number.startsWith(ctryCode)) {
            number = ctryCode + number;
        }
        return number;
    }

    /**
     * @return map with values active and inactive, but with integers as the keys.
     * @see #activeMapInt
     */
    public static Map<String, String> getActiveMapInt() {
        return activeMapInt;
    }

    /**
     * @return map with active and inactive options.
     * @see #activeMapStr
     */
    public static Map<String, String> getActiveTypeOh() {
        return activeMapStr;
    }

    /**
     * Returns just the name of a class
     * com.infusion.util.StringUtils -> StringUtils
     *
     * @param aClass class object associated with the object you want a name for.
     * @return name of the class
     * @see #getExtension(String)
     */
    public static String getClassLastName(Class aClass) {
        return getClassLastName(aClass.getName());
    }

    /**
     * Returns just the name of a class
     * com.infusion.util.StringUtils -> StringUtils
     *
     * @param name full class path name
     * @return name of the specific class
     * @see #getExtension(String)
     */
    public static String getClassLastName(String name) {
        return getExtension(name);
    }

    /**
     * Returns everything after the last dot
     * <p/>
     * Useful for:<BR>
     * <p/>
     * Getting filename extensions <BR>
     * Getting classnames (name only)
     *
     * @param str file name or path
     * @return ext String
     */
    public static String getExtension(String str) {
        assert str != null : "Extension String should not be null";
        int start = str.lastIndexOf(".");
        return StringUtils.Nz(str.substring(start + 1, str.length()));
    }

    /**
     * Gets the last 4 digits of a credit card
     *
     * @param creditCard
     * @return
     */

    public static String getLast4(String creditCard) {
        if (creditCard == null) {
            return null;
        } else if (creditCard.length() < 4) {
            return creditCard;
        } else {
            int startAt = creditCard.length() - 4;
            return creditCard.substring(startAt);
        }
    }

    public static byte[] hexStringToBytes(String hexStr) {
        if (hexStr == null) {
            return new byte[0];
        }
        if (hexStr.length() % 2 != 0){
            return new byte[0];
        }
        byte bArray[] = new byte[hexStr.length() / 2];
        for (int i = 0; i < (hexStr.length() / 2); i++) {
            byte firstNibble = Byte.parseByte(hexStr.substring(2 * i, 2 * i + 1), 16); // [x,y)
            byte secondNibble = Byte.parseByte(hexStr.substring(2 * i + 1, 2 * i + 2), 16);
            int finalByte = (secondNibble) | (firstNibble << 4); // bit-operations only with numbers, not bytes.
            bArray[i] = (byte) finalByte;
        }
        return bArray;
    }

    /**
     * Hides all but the last 4 chars in a String (useful for credit cards)
     * <p/>
     * 4111111111111111<BR>
     * ************1111<BR>
     *
     * @param cardNumber credit card string
     * @return same number, but with all digits, but the last four replaced by astericks.
     */
    public static String hiddenCardNumber(String cardNumber) {
        return !is(cardNumber) ? "" : repeat("*", 12) + Right(cardNumber, 4);
    }

    /**
     * If the first String has a value, then it prints the first String plus the second String
     *
     * @param ifStr   the string to check
     * @param thenStr the string to print with the first string if the first one has a value
     * @return the first string or both, otherwise an empty string
     */
    public static String ifThenPrint(String ifStr, String thenStr) {
        if (Nz(ifStr).equals(""))
            return "";
        else
            return ifStr + Nz(thenStr);
    }

    /**
     * If the first String has a value, then it prints the second String preceded by the first String
     *
     * @param ifStr   string to check
     * @param thenStr add this to the string when the first has a value
     * @return the then string in front of the first if the first has a value, otherwise an empty string
     */
    public static String ifThenPrintBefore(String ifStr, String thenStr) {
        if (!is(ifStr)) {
            return "";
        } else {
            return Nz(thenStr) + ifStr.trim();
        }
    }

    /**
     * If a string ifStr is not null, it will surround it with beforeStr and afterStr
     * <BR>
     * If null, will return "";
     *
     * @param ifStr     String in the middle
     * @param beforeStr what to print before
     * @param afterStr  what to print after
     * @return string with the ifStr surrounded by the other two if the first has a value, otherwise empty string
     */
    public static String ifThenSurround(String ifStr, String beforeStr, String afterStr) {
        if (!StringUtils.is(ifStr))
            return "";
        else
            return beforeStr + ifStr + afterStr;
    }

    /**
     * Converts an int to a boolean (i<=0 false)
     *
     * @param i an integer
     * @return true if the integer value is greater than 0
     */
    public static boolean intToBool(int i) {
        return (i > 0);
    }

    /**
     * Returns false if the String equals null or empty string, true otherwise
     */
    public static boolean is(String str) {
        return (str != null && !str.equals("") && !str.equals("null"));
    }

    /**
     * Returns false if the String equals null or empty string, true otherwise
     *
     * @param str string or array of strings
     * @return true if all the strings are not null and not empty
     */
    public static boolean is(String... str) {
        for (String s : str) {
            if (Nz(s).equals("")) {
                return false;
            }
        }
        return true;
    }

    /**
     * perform the is function on an object
     * @param obj obj
     * @return true or false
     */
    public static boolean is(Object obj) {
        return is(String.valueOf(obj));
    }

    /**
     * Appends the strings together and returns a builder
     *
     * @param data
     * @return
     */
    public static StringBuilder append(Object... data) {
        StringBuilder builder = new StringBuilder();
        if (data != null) {
            for (Object string : data) {
                if (string != null) {
                    builder.append(string);
                }
            }
        }
        return builder;
    }

    /**
     * Determines whether or not a given String is an integer
     */
    public static boolean isInteger(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        //check that every char in the string is a number.
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        //the first char cannot be a 0 unless it is the only character
        return str.charAt(0) != '0' || (str.charAt(0) == '0' && str.length() == 1);
    }

    /**
     * Takes in a character and determines if it's a vowel or not
     */
    public static boolean isVowel(char c) {
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' ||
               c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U';
    }

    /**
     * Method that will chop a string to a certain number of characters and then let you
     * append a string to signify that it's been chopped
     *
     * @param str     The String to chop (potentially)
     * @param limit   The max number of characters
     * @param endText What to append to chopped strings (note: String less than limit will not have this appended)
     */
    public static String limitChars(String str, int limit, String endText) {
        if (str == null || endText == null){
            return null;
        }
        if (str.length() > limit)
            return str.substring(0, limit) + endText;
        else
            return str;
    }

    /**
     * Converts a string into a String array, using a String as a delimiter
     */
    public static String[] makeStringArray(String str, String strToken) {
        if (str == null) return new String[0];
        StringTokenizer st = new StringTokenizer(str, strToken);
        String[] a = new String[st.countTokens()];
        int i = 0;
        while (st.hasMoreElements()) {
            a[i] = st.nextElement().toString().trim();
            i++;
        }
        return a;
    }

    /**
     * returns the concatenation of two strings or an empty string if either string is null.
     */
    public final static String maybe(String s1, String s2) {
        if (s1 == null || s2 == null) return "";
        return s1 + s2;
    }

    /**
     * returns the concatenation of three strings or an empty string if any string is null.
     */
    public final static String maybe(String s1, String s2, String s3) {
        if (s1 == null || s2 == null || s3 == null) return "";
        return s1 + s2 + s3;
    }

    /**
     * Returns an md5 hash of text that is passed in.
     *
     * @param text some text
     * @return hashed string
     * @throws NoSuchAlgorithmException
     */
    public static String md5Hash(String text) throws NoSuchAlgorithmException {
        if (text == null) {
            return text;
        }
        return bytesToHexString(MessageDigest.getInstance("MD5").digest(text.getBytes()));
    }

    /**
     * Converts newlines in a String to <br> for HTML
     */
    public static String newToBr(String str) {
        str = replace(str, "\n", "<br>");
        return str;
    }

    /**
     * Performs a String nz check (null and empty-string) on
     * a list of Strings
     *
     * @see #Nz(String)
     * @see #is(String)
     */
    public static String nz(String... strings) {
        for (String s : strings) {
            if (StringUtils.is(s)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Nzs an enum (returns the name of the enum)
     */
    public static String nze(Enum anEnum) {
        if (anEnum == null) {
            return "";
        } else return anEnum.name();
    }

    /**
     * Strips all non-letter characters from a String
     */
    public static String onlyLetters(String lastName) {
        StringBuffer retVal = new StringBuffer();
        if (lastName != null) {
            for (int i = 0; i < lastName.length(); i++) {
                char c = lastName.charAt(i);
                if (Character.isLetter(c)) {
                    retVal.append(c);
                }
            }
        }
        return retVal.toString();
    }

    public static String onlyLettersAndNumbers(String str) {
        StringBuffer retVal = new StringBuffer();
        if (str != null) {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (Character.isLetter(c) || Character.isDigit(c)) {
                    retVal.append(c);
                }
            }
        }
        return retVal.toString();
    }

    /**
     * Returns only the numbers in the given string
     */
    public static String onlyNumbers(String string) {
        if (string == null) {
            return string;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            if (Character.isDigit(ch)) {
                builder.append(ch);
            }
        }
        return builder.toString();
    }

    /**
     * Pads the left side of a String to a specified target number of characters.  It's helpful when you need to
     * zerofill
     * <p/>
     * Let's say you need to force two digit days of the month, like this:<BR>
     * 01,02,03...11,12,13 instead of <BR>
     * 1,2,3...11,12,13
     * <p/>
     * This would be a useful method
     *
     * @param orig         Original String
     * @param targetLength Length string should be
     * @param padWith      What you want to pad with
     * @return Padded String
     */
    public static String padLeft(String orig, int targetLength, char padWith) {
        if (orig == null) orig = "";
        for (int i = orig.length(); i < targetLength; i++) {
            orig = padWith + orig;
        }
        return orig;
    }

    /**
     * If the String passed in is not null, it returns the String in parenthesis
     */
    public static String parent(String str) {
        if (Nz(str).equals(""))
            return "";
        else
            return " (" + str + ")";
    }

    /**
     * If the Object passed in is not null, it returns the String in parenthesis
     */
    public static String parent(Object obj) {
        if (obj == null) {
            return "";
        }
        return parent(obj.toString());
    }

    /**
     * @see #parent(String)
     */
    public static String parenth(String str) {
        return parent(str);
    }

    /**
     * Splits a String name into an array
     * <p/>
     * Eric Martineau -> Eric,Martineau<BR>
     * <p/>
     * Favoring last:<BR>
     * John Van Der Kamp -> John,Van Der Kamp<BR>
     * <p/>
     * Favoring First:<BR>
     * Beth Ann Martinelli -> Beth Ann,Martinelli
     *
     * @param favorLast Whether to favor the lastName (or firstname)
     */
    public static String[] parseName(String contactName, boolean favorLast) {
        String[] retName = new String[]{"", ""};
        String[] split = StringUtils.Nz(contactName).split(" ");
        if (split != null) {
            if (favorLast) {
                retName[0] = split[0];
                if (split.length > 1) {
                    for (int i = 1; i < split.length; i++) {
                        retName[1] += split[i] + " ";
                    }
                }
            } else {
                for (int i = 0; i < split.length; i++) {
                    if (i < (split.length - 1)) {
                        retName[0] += split[i] + " ";
                    } else {
                        retName[1] = split[i];
                    }
                }
            }
        }
        return retName;
    }

    /**
     * Parses a given line into a list of Strings
     * <p/>
     * The String:<BR>
     * "apple", "orange", "banana", 3, "word"
     * <p/>
     * where:<BR>
     * delim = ','<BR>
     * qualif = '"'<BR>
     * <p/>
     * would split into:<BR>
     * {apple,orange,banana,3,word}
     *
     * @param str    String to be parsed
     * @param delim  Character separating entries
     * @param qualif Text qualifier
     * @return List of Strings
     */
    public static ArrayList<String> parseString(String str, char delim, char qualif) {
        if (str == null) return new ArrayList<String>();
        ArrayList<String> list = new ArrayList<String>();
        StringBuffer tmp = new StringBuffer("");
        int cnt = 0;
        char prevChar = '\n';

        for (int i = 0; i < str.length(); i++) {
            char at = str.charAt(i);
            if (at == qualif) {
                cnt++;
            } else if (at == delim) {
                if (cnt % 2 == 0 || prevChar == delim || prevChar == qualif) {
                    list.add(tmp.toString());
                    tmp = new StringBuffer("");
                } else {
                    tmp.append(at);
                }
            } else {
                tmp.append(at);
            }
            prevChar = at;
        }
        list.add(tmp.toString());
        return list;
    }

    public static String pathToClassName(String path, String prefix) {
        if (path == null || prefix == null)
            return "";
        prefix = prefix.replaceAll("\\\\", "\\\\\\\\");
        String sep = System.getProperty("file.separator").replaceAll("\\\\", "\\\\\\\\");
        return path.replaceAll(prefix, "")
                .replaceAll("\\.class", "")
                .replaceAll(sep, ".");
    }

    /**
     * Takes in a string and prints out the plural of it
     * <p/>
     * "string" -> "strings"<BR>
     * "box" -> "boxes"<BR>
     * "fairy" -> "fairies"<BR>
     */
    public static String plural(String str) {
        return plural(str, 2);
    }

    /**
     * Prints out either the singular version of a noun or the plural
     * depending on how many of the item there is (specified by count)
     */
    public static String plural(String str, int count) {
        if (count < 2) {
            return str;
        }
        if (!is(str)) return str;
        char t = str.charAt(str.length() - 1);
        if (t == 'x' || t == 's' || t == 'i' || str.endsWith("ch") ||
                str.endsWith("sh")) {
            return str + "es";
        }
        if (t == 'y') {
            char e = str.charAt(str.length() - 2);
            if (isVowel(e))
                return str + "s";
            else
                return str.substring(0, str.length() - 1) + "ies";
        }
        return str + "s";
    }

    /**
     * Returns the result of plural (see above) with the count prepended
     * <p/>
     * eg:<BR>
     * 1 dog<BR>
     * 2 cats<BR>
     * 3 boxes<BR>
     *
     * @see #plural(String,int)
     */
    public static String pluralWithCount(String str, int count) {
        return count + " " + plural(str, count);
    }

    /**
     * @see CollectionUtil#CollectionValues(java.util.Collection,String,boolean)
     */
    public static String printArray(ArrayList a) {
        return printArray(a, ",");
    }

    /**
     * Takes in an array and returns a comma delimited string of the values
     *
     * @param ary The object used to print the string
     */
    public static String printArray(Object[] ary) {
        return printArry(ary, ",");
    }

    /**
     * @see CollectionUtil#CollectionValues(java.util.Collection,String,boolean)
     */
    public static String printArray(ArrayList a, String del) {
        return CollectionValues(a, del, false);
    }

    /**
     * Prints out the values of an array to a String
     *
     * @param ary            Array to print
     * @param del            Delimiter to separate entries
     * @param includeEmpties Whether to include null or empty-string values
     * @return String
     */
    public static String printArray(Object[] ary, String del, boolean includeEmpties) {
        del = Nz(del);
        if (ary == null) return "";
        StringBuffer results = new StringBuffer();
        for (Object anAry : ary) {
            if (includeEmpties || is(anAry + "")) {
                results.append(anAry).append(del);
            }
        }
        return StringUtils.rtrim(results.toString(), del.length());
    }

    /**
     * Takes in an array and returns a String with the items in the array delimited
     * by a certain String
     *
     * @param ary The array to print out
     * @param del The String used as a delimiter
     */
    public static String printArry(Object[] ary, String del) {
        return printArray(ary, del, true);
    }

    /**
     * Repeats a string x number of times
     */
    public static String repeat(String s, int count) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(Nz(s));
        }
        return result.toString();
    }

    /**
     * Replaces String (find) in original String (data) with replacement (replace)
     */
    public static String replace(String data, String find, String replace) {
        if (data == null) data = "";
        StringBuffer sb = new StringBuffer();
        int a = 0, b;
        int findLength = find.length();
        while ((b = data.indexOf(find, a)) != -1) {
            sb.append(data.substring(a, b));
            sb.append(replace);
            a = b + findLength;
        }
        if (a < data.length()) {
            sb.append(data.substring(a));
        }
        return sb.toString();
    }

    /**
     * Replace the last occurrence of 'replace' with 'replaceWith'.  If 'replace' is not found, do nothing.
     *
     * @param string      - the string to be altered
     * @param replace     - the char that needs to be replaced
     * @param replaceWith - the char to replace into the string in place of the char replace
     */
    public static String replaceLastCharOccurrence(String string, char replace, char replaceWith) {
        //Loop through it backwards and replace the first instance of 'replace'
        if (string == null){
            return "";
        }
        int lastIndex = string.length() - 1;
        char[] array = string.toCharArray();
        for (int i = lastIndex; i > 0; i--) {
            if (array[i] == replace) {
                array[i] = replaceWith;
                break;
            }
        }
        return new String(array);
    }

    /**
     * Attempts to get rid of some funky special windows characters, like those goofy quotes that always turn into
     * ? when you get the wrong encoding type
     *
     * @see #conversionChart
     */
    public static String replaceWindowsCharacters(String in) {
        if (in == null) return null;
        String copy = in;
        for (Map.Entry<Character, Object> entry : conversionChart.entrySet()) {
            Character character = entry.getKey();
            if (entry.getValue() instanceof Character) {
                Character replace = (Character) entry.getValue();
                copy = copy.replace(character, replace);
            } else {
                String replace = entry.getValue().toString();
                copy = copy.replaceAll(character.toString(), replace);
            }
        }

        return copy;
    }

    /**
     * Actually Trims off the right of the String.  Oops!!
     */
    public static String rtrim(String str, int trimSize) {
        if (str == null) return "";
        int realSize = Math.min(str.length(), trimSize);
        return str.substring(0, str.length() - realSize);
    }

    /**
     * Uses a simple dencryption algorithm to dencrypt a string.
     *
     * @param key THe key used to dencrypt the String.  (longer is better)
     * @param str The String to dencrypt
     * @see #simpleEncrypt(String,String)
     */
    public static String simpleDecrypt(String key, String str) {
        if (str == null){
            return "";
        }
        StringBuffer scrambled = new StringBuffer(str);
        int i = 0;
        while (i < str.length()) {
            for (int x = 0; x < key.length(); x++) {
                if (i == str.length()) break;
                scrambled.setCharAt(i, (char) (((str.charAt(i++) - 32) - (key.charAt(x) - 32) + 96) % 96 + 32));
            }
        }
        return scrambled.toString();
    }

    /**
     * Uses a simple encryption algorithm to encrypt a string.
     *
     * @param key THe key used to encrypt the String.  (longer is better)
     * @param str The String to encrypt
     * @see #simpleDecrypt(String,String)
     */
    public static String simpleEncrypt(String key, String str) {
        if (str == null) {
            return null;
        }
        StringBuffer scrambled = new StringBuffer(str);
        int i = 0;
        while (i < str.length()) {
            for (int x = 0; x < key.length(); x++) {
                if (i == str.length()) break;
                scrambled.setCharAt(i, (char) ((((str.charAt(i++) - 32) + (key.charAt(x) - 32)) % 96 + 32)));
            }
        }
        return scrambled.toString();
    }

    /**
     * Puts spaces in any time where there's a capital letter (Java names like FirstNameAddress)
     */
    public static String spacify(String s) {
        return crop(Nz(s).replaceAll("[A-Z][a-z]", " $0"));
    }

    public static String trimmedHash(String source) throws NoSuchAlgorithmException {
        return trim(StringUtils.md5Hash(source), 8);
    }

    /**
     * Splits up a raw email address into address and name parts:
     * <p/>
     * Eric Martineau <eric@infusionsoft.com><BR>
     * would be split into:
     * <p/>
     * eric@infusionsoft.com [0]
     * <BR>AND<BR>
     * Eric Martineau [1]
     */
    public static String[] splitEmail(String str) {
        String[] tmp = Nz(str).split("<");
        if (tmp.length == 0) return new String[2];
        if (tmp.length == 1) return new String[]{tmp[0].replaceAll(">", ""), ""};
        return new String[]{tmp[1].replaceAll(">", ""), tmp[0].trim()};
    }

    /**
     * Takes in a String that is made up of key/value pairs that are separated by a common delimiter.  The delimiter is
     * specified as a parameter to this method.  Each key value pair is of the form "key=value", meaning that the key
     * and the value must be separated by the equals ('=') character.
     * <br><br>
     * Example: <br>If target = "animal=dog;automobile=car;color=red" and delim = ";"
     * then a map would be returned that contained the following 3 entries (the first value is the key, the second is the value):
     * <br>animal       dog
     * <br>automobile   car
     * <br>color        red
     *
     * @param delim  - the delimiter that separates the key value pairs that are contained in target.
     * @param target - the String that contains the key value pairs.
     */
    public static Map<String, String> splitIntoMap(String delim, String target) {
        if (delim == null || target == null){
            return null;
        }
        Map<String, String> returnMap = new LinkedHashMap<String, String>();
        //Split the string up by delim.
        String[] splitUpString = target.split(delim);
        //loop through and
        for (String keyValuePair : splitUpString) {
            StringTokenizer token = new StringTokenizer(keyValuePair, "=");
            if (token.countTokens() == 2) {
                returnMap.put(token.nextToken(), token.nextToken());
            }
        }
        return returnMap;
    }

    /**
     * Splits a String name into an array (favoring the last name)
     * <p/>
     * Eric Martineau -> Eric,Martineau<BR>
     * John Van Der Kamp -> John,Van Der Kamp<BR>
     *
     * @see #parseName(String,boolean)
     */
    public static String[] splitName(String string) {
        return parseName(string, true);
    }

    /**
     * Converts a string to an integer, converts null to 0
     *
     * @param str       The String to converts
     * @param allowZero boolean value indicating whether or not to allow zeroes (false will convert zeros to 1)
     */
    public static int strToInt(String str, boolean allowZero) {
        return Integer.parseInt(((allowZero) ? NzNum(str) : NzNumOne(str)));
    }

    /**
     * Converts a String to a Map, using a delimiter (matching keys and values)
     *
     * @return Converted Map
     * @see CollectionUtil#StringToMap(String,String)
     */
    public static Map<String, String> stringToMap(String string, String del) {
        return StringToMap(string, del);
    }

    /**
     * Takes in a String (as a file name) and Strings the extension off. The string must have an extension or this
     * method will fail.
     *
     * @param str string with extension
     * @return array of name and extension
     */
    public static String[] stripExtension(String str) {
        if (str == null) return null;
        int start = str.lastIndexOf(".");
        String name = str.substring(0, start);
        String ext = str.substring(start + 1, str.length());
        return new String[]{name, ext};
    }

    /**
     * Returns a string minus its extension. Returns null if a null is passed in and returns the passed in string if
     * the string has no extension. If there are multiple extensions, then it only gets rid of the last one.
     *
     * @param str string
     * @return string without its extension
     */
    public static String getNameMinusExtension(String str) {
        if (str == null) return null;
        if (!str.contains(".")) {
            return str;
        }
        return stripExtension(str)[0];
    }

    /**
     * Strips all non-letter characters
     */
    public static String stripNonLetter(String str) {
        if (str == null) return "";
        StringBuffer tmp = new StringBuffer("");
        for (int i = 0; i < str.length(); i++) {
            if (tmp.length() == 0 && str.charAt(i) == '-') {
                tmp.append(str.charAt(i));
                continue;
            }
            if (Character.isLetter(str.charAt(i))) {
                tmp.append(str.charAt(i));
            }
        }
        return tmp.toString();
    }

    /**
     * Strips all non-numeric characters
     */
    public static String stripNonNumber(String str) {
        if (str == null) return "";
        StringBuffer tmp = new StringBuffer("");
        for (int i = 0; i < str.length(); i++) {
            if (tmp.length() == 0 && str.charAt(i) == '-') {
                tmp.append(str.charAt(i));
                continue;
            }
            if (Character.isDigit(str.charAt(i)) || str.charAt(i) == '.') tmp.append(str.charAt(i));
        }
        return tmp.toString();
    }

    /**
     * Strips out all non-numeric characters, also ignoring periods
     */
    public static String stripNonNumberNoPeriod(String str) {
        if (str == null) return "";
        StringBuffer tmp = new StringBuffer("");
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) tmp.append(str.charAt(i));
        }
        return tmp.toString();
    }

    /**
     * Returns a substring of a string, starting from the given value fromi
     * <p/>
     * ie.
     * <p/>
     * ("eric_martineau", "_") would return "martineau"
     */
    public static String subStringFrom(String string, String from) {
        if (string == null || from == null) {
            return null;
        }
        int index = string.indexOf(from);
        if (index == -1 || index == string.length() - 1) {
            return string;
        } else {
            return string.substring(index + from.length());
        }
    }



    /**
     * Returns a substring of a string up until a certain string
     * <p/>
     * String: eric martinea<BR>
     * Until: tin<BR>
     * Return: eric mar<BR>
     */
    public static String subStringUntil(String string, String until) {
        if (string == null || until == null) {
            return null;
        }
        int index = string.indexOf(until);
        if (index == -1) {
            return string;
        }
        return string.substring(0, index);
    }

    public static String toHex(byte b) {
        return hexTable[(b >> 4) & 0x0F] + hexTable[b & 0x0F];
    }

    /**
     * convert a document to xml
     * @param doc document
     * @return xml
     */
    public static String toXML(Document doc) {
        String xml = null;
        try {
            TransformerFactory tfFac = TransformerFactory.newInstance();
            Transformer tf = tfFac.newTransformer();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            tf.transform(new DOMSource(doc), new StreamResult(out));
            xml = new String(out.toByteArray());
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return Nz(xml);
    }

    /**
     * Converts a String to a ByteArrayInputStream
     */
    public static ByteArrayInputStream toStream(String string) {
        return new ByteArrayInputStream(Nz(string).getBytes());
    }

    /**
     * Checks a list of variables for null values, throws IllegalStateException if any are null.
     *
     * @param checks List of Duples, containing the variable to be checked and the name of the variable.
     */
    public static void nullCheck(Duple<Object, String>... checks) {
        Joiner joiner = new Joiner("\n");
        for (Duple<Object, String> check : checks) {
            if (check.f1 == null) {
                joiner.add("Variable " + check.f2 + " can't be null");
            }
        }
        if (joiner.size() > 0) {
            throw new IllegalStateException(joiner.toString());
        }
    }

    /**
     * Trims a String down to a certain size
     */
    public static String trim(String str, int trimSize) {
        if (str == null) return "";
        int realSize = Math.min(str.length(), trimSize);
        return str.substring(0, realSize);
    }

    /**
     * Trims all currency characters from a String
     */
    public static String unformatCurrency(String str) {
        if (str == null) return "";
        return str.replaceAll("(%|\\$|,)", "");
    }

    /**
     * Returns the first non-null object in a list
     *
     * @param obj1 List of objects
     * @return first valid object in a list.
     */
    public static <X> X x(X... obj1) {
        for (X x : obj1) {
            if (x != null) return x;
        }
        return null;
    }

    /**
     * ensure that a str is at least as long as the min
     *
     * @param str string
     * @param min min length
     * @return string
     */

    public static String minLength(String str, int min) {
        return minLength(str, '0', min);
    }

    /**
     * ensure that a str is at least as long as the min
     *
     * @param str string
     * @param c   character to insert if str not long enough
     * @param min min length
     * @return string
     */

    public static String minLength(String str, Character c, int min) {
        if (str == null){
            return null;
        }
        String retval = "";

        for (int i = min; i > str.length(); i--) {
            retval += c;
        }

        return retval + str;
    }

    public static boolean isHTML(String data) {
        if (!StringUtils.is(data)) {
            return false;
        }

        String lower = data.toLowerCase();
        return lower.indexOf("<br>") > -1 || lower.indexOf("</p>") > -1;
    }

    /**
     * Returns the currency symbol for given locale.
     *
     * @param locale location of user in world
     * @return symbol
     */
    public static String getCurrencySymbol(Locale locale) {
        String countrySymbol = "$";
        String country = locale.getCountry();
        if ("GB".equals(country)) {
            countrySymbol = "Â£";
        } else if ("ZA".equals(country)) {
            countrySymbol = "R";
        }
        return countrySymbol;
    }

    /**
     * Does this string contain at least 1 letter?
     *
     * @param str the string
     * @return true or false
     */
    public static boolean containsLetter(String str) {
        if (!is(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (Character.isLetter(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Does this string contain at least 1 number?
     *
     * @param str the string
     * @return true or false
     */
    public static boolean containsNumber(String str) {
        if (!is(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Does this string contain at least 1 upper case letter?
     *
     * @param str the string
     * @return true or false
     */
    public static boolean containsUpper(String str) {
        if (!is(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (Character.isUpperCase(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * determines whethor a string starts with a letter or not
     *
     * @param str string
     * @return true or false
     */
    public static boolean startsWithLetter(String str) {
        return is(str) && Character.isLetter(str.charAt(0));
    }

    /**
     * Checks whether a string starts with any of the provided prefixes
     *
     * @param compareString
     * @param prefixes
     * @return
     */
    public static boolean startsWith(String compareString, String... prefixes) {
        if (prefixes == null || compareString == null) {
            return false;
        }
        for (String prefix : prefixes) {
            if (compareString.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    public static String[] splitByChar(String data, char c) {
        if (data == null){
            return null;
        }
        String str[] = data.split(String.valueOf(c));

        return str;
    }

    public static int count(String sourceString, char lookFor) {
        if(sourceString == null){
            return -1;
        }
        int count = 0;
        for (int i = 0; i < sourceString.length(); i++) {
            final char c = sourceString.charAt(i);
            if (c == lookFor) {
                count++;
            }
        }
        return count;
    }

    /**
     * figure out if it is grammitcally correct to say "a [nextWord]" and, if not, use "an [nextWord]" instead
     * @param nextWord next word
     * @return "a [nextWord]" or "an [nextWord]"
     */
    public static String aOrAn(String nextWord) {
        if (nextWord == null || nextWord.length() < 1) { // don't use is() because "null" is a valid option here
            return "a";
        }
        char t = nextWord.charAt(0);
        if (isVowel(t)) {
            return "an " + nextWord;
        } else {
            return "a " + nextWord;
        }
    }

    /**
     * pluralize a count as necessary
     * @param num num
     * @param str str
     * @return the count
     */
    public static String count(int num, String str) {
        return count(num, str, -1);
    }

    /**
     * pluralize a count as necessary and enforce a limit
     * @param num num
     * @param str str
     * @param limit limit
     * @return the count
     */
    public static String count(int num, String str, int limit) {
        boolean overLimit = limit > 0 && num >= limit;
        String divider = " ";
        if (overLimit) {
            num = limit;
            divider = "+ ";
        }
        return num != 1?
                num + divider + plural(Nz(str)) :
                num + divider + Nz(str);
    }
    
    /**
     * Evaluates if a string starts if with a vowel or not.
     *
     * @param str - a String to evaluate.
     * @return true if the string starts with a vowel.
     */
    public static boolean startsWithVowel(String str) {
        Matcher m = startwithVowelPattern.matcher(Nz(str));
        return m.matches();
    }

    public static String getIndefiniteArticle(String str) {
        String rtnIndefiniteArticle;
        if (startsWithVowel(str)) {
            rtnIndefiniteArticle = "an";
        } else {
            rtnIndefiniteArticle = "a";
        }
        return rtnIndefiniteArticle;
    }

    /**
     * Prints out html to view all the fields and values in the parameter map. This method should only be used during
     * testing.
     *
     * @param parameterMap request parameter map
     * @return html to view the values in parameter map
     */
    public static String printParameterMap(Map<String, String[]> parameterMap) {
        StringBuilder builder = new StringBuilder();
        builder.append("<table border=\"1\">");
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            builder.append("<tr>");
            builder.append("<td>").append(entry.getKey()).append("</td>");
            builder.append("<td><table border=\"1\">");
            for (String value : entry.getValue()) {
                builder.append("<tr>");
                builder.append("<td>").append(value).append("</td>");
                builder.append("</tr>");
            }
            builder.append("</table></td>");
            builder.append("</tr>");
        }
        builder.append("</table>");
        return builder.toString();
    }

// ========================================================================================================================
//    Inner Classes
// ========================================================================================================================

    /**
     * Class that determines whether or not a given word should be proper cased or not
     */
    public static class NoCaps {
// ========================================================================================================================
//    Static Fields
// ========================================================================================================================

        public static Collection<String> noCapSet;

// ========================================================================================================================
//    Static Methods
// ========================================================================================================================

        static {
            noCapSet = new HashSet<String>();
            noCapSet.add("and");
            noCapSet.add("or");
            noCapSet.add("the");
            noCapSet.add("an");
            noCapSet.add("it");
            noCapSet.add("on");
            noCapSet.add("in");
            noCapSet.add("at");
            noCapSet.add("of");
            noCapSet.add("this");
            noCapSet.add("then");
            noCapSet.add("that");
            noCapSet.add("for");
            noCapSet.add("from");
            noCapSet.add("is");
            noCapSet.add("are");
        }
    }
}