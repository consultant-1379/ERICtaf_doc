package com.ericsson.cifwk.docs;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringReplacer {

    private final String input;

    public StringReplacer(String input) {
        this.input = input;
    }

    public StringReplacer replace(Pattern pattern, Strategy strategy) {
        StringBuilder builder = new StringBuilder();
        Matcher matcher = pattern.matcher(input);
        int lastEnd = 0;
        while (matcher.find()) {
            String replacement = strategy.replace(matcher.toMatchResult());
            if (replacement != null) {
                String sub = input.substring(lastEnd, matcher.start());
                builder.append(sub).append(replacement);
            } else {
                String sub = input.substring(lastEnd, matcher.end());
                builder.append(sub);
            }
            lastEnd = matcher.end();
        }
        String sub = input.substring(lastEnd);
        builder.append(sub);
        String result = builder.toString();
        return new StringReplacer(result);
    }

    @Override
    public String toString() {
        return input;
    }

    public static interface Strategy {
        String replace(MatchResult result);
    }

}
