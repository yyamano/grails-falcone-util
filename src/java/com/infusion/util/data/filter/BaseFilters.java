package com.infusion.util.data.filter;

import com.infusion.util.StringUtils;


/**
 * A class to provide basic DataFilter implementations to be used for basic data types.
 *
 * @author eric
 */
public class BaseFilters {
// ========================================================================================================================
//    Static Fields
// ========================================================================================================================

    /**
     * Filter for empty strings
     */
    public static final DataFilter<String> EmptyStringFilter = new DataFilter<String>() {
        public boolean filter(String s) {
            return StringUtils.is(s);
        }
    };
}


