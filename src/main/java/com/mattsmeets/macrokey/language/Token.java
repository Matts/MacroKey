package com.mattsmeets.macrokey.language;

/**
 * Created by Matt on 5/20/2016.
 */
public class Token {
    private String token;
    private TokenType type;

    public Token(String token, TokenType type) {
        this.token = token;
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public TokenType getType() {
        return type;
    }
}
