package com.mattsmeets.macrokey.language;

import java.util.regex.Pattern;

/**
 * Created by Matt on 5/20/2016.
 */
public class TokenData {

    private Pattern pattern;
    private TokenType type;

    public TokenData(Pattern pattern, TokenType type) {
        this.pattern = pattern;
        this.type = type;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public TokenType getType() {
        return type;
    }
}
