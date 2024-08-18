package kr.bp.butterLib.message;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageColor {
    private static final Pattern HEX_PATTERN = Pattern.compile(
            "&#([a-fA-F0-9]{6})(.*?)"
    );

    public static String colorText(String text) {
        Matcher hexMatcher = HEX_PATTERN.matcher(text);
        StringBuffer sb = new StringBuffer();

        while (hexMatcher.find()) {
            String colorHex = hexMatcher.group(1);
            String message = hexMatcher.group(2);

            ChatColor chatColor = ChatColor.of("#" + colorHex);
            if (chatColor != null) {
                String coloredText = chatColor + message;
                hexMatcher.appendReplacement(sb, coloredText);
            }
        }
        hexMatcher.appendTail(sb);

        return ChatColor.translateAlternateColorCodes('&', sb.toString());
    }
}
