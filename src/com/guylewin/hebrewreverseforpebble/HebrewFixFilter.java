package com.guylewin.hebrewreverseforpebble;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HebrewFixFilter {
    /**
     * Regex patterns used in identifying and fixing Hebrew words, so we can
     * reverse them
     */

    private static final String  HebrewCharacterSequence           = "[[[\\u0591-\\u05F4][\\uFB1D-\\uFB4F][\\uFB1D-\\uFB4F]][\\u0591-\\u05C7]]+";
    private static final Pattern nonHebrewCharacterSequencePattern = Pattern
                                                                           .compile("[^[[\\u0591-\\u05F4][\\uFB1D-\\uFB4F][\\uFB1D-\\uFB4F]][\\u0591-\\u05C7]]+");
    private static final Pattern HebrewPatternAll                  = Pattern.compile("\\s*(\\s*+"
                                                                           + HebrewCharacterSequence + ")+");

    private static final String sAlreadyFixedMagic = "\r  ";
    
    public String applyFixForHebrew(String text) {    	
    	String textLines[] = text.split("\r?\\n");

        String outputText = "";

        for (int i = 0; i < textLines.length; i++) {
            if (i > 0) {
                outputText += "\n";
            }

            Matcher m = HebrewPatternAll.matcher(text);
            StringBuffer sb = new StringBuffer();

            while (m.find()) {
                String HebrewText = m.group();
                String reversed = new StringBuffer(HebrewText).reverse().toString();
                m.appendReplacement(sb, reversed);
            }
            m.appendTail(sb);

            outputText += sb.toString();
        }

        return outputText;
    }
}