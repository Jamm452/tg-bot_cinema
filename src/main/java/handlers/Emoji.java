package handlers;

import com.vdurmont.emoji.EmojiParser;

public enum Emoji {
    POPCORN(":popcorn:"),
    TICKET(":ticket:"),
    CLAPPER(":clapper:");

    private String value;

    public String get() {
        return EmojiParser.parseToUnicode(value);
    }

    Emoji(String value) {
        this.value = value;
    }
}