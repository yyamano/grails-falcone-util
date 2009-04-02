/*
 * DateParseException.java
 *
 * Created on October 11, 2002, 7:24 PM
 */

package com.infusion.util;

/**
 * Exception thrown when trying to parse a date
 *
 * @author eric
 */
public class DateParseException extends java.lang.Exception {
// ========================================================================================================================
//    Constructors
// ========================================================================================================================

    /**
     * Creates a new instance of <code>DateParseException</code> without detail message.
     */
    public DateParseException() {
    }

    /**
     * Constructs an instance of <code>DateParseException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public DateParseException(String msg) {
        super(msg);
    }
}
