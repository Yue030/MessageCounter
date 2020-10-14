package com.yue.messagecounter.interpreter.impl;

import com.yue.messagecounter.annotaion.Interpreter;
import com.yue.messagecounter.interpreter.MessageInterpreter;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

@Interpreter(
        name = "Messager",
        type = "Unicode"
)
public class MessagerInterpreter implements MessageInterpreter {
    private static MessageInterpreter interpreter;

    private MessagerInterpreter() {}

    public static MessageInterpreter getInstance() {
        if (interpreter == null) {
            interpreter = new MessagerInterpreter();
        }

        return interpreter;
    }

    @Override
    public String decode(String str) {
        String unescaped = unescape(str);
        byte[] bytes = unescaped.getBytes(StandardCharsets.ISO_8859_1);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    public String unescape(String str) {
        if (str == null) {
            return null;
        }

        try {
            StringWriter writer = new StringWriter(str.length());
            unescape(writer, str);
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void unescape(Writer out, String str) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        }

        if (str == null) {
            return;
        }

        int sz = str.length();
        StringBuilder unicode = new StringBuilder(4);
        boolean hadSlash = false;
        boolean inUnicode = false;

        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);
            if (inUnicode) {
                unicode.append(ch);
                if (unicode.length() == 4) {
                    try {
                        int value = Integer.parseInt(unicode.toString(), 16);
                        out.write((char) value);
                        inUnicode = false;
                        hadSlash = false;
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                continue;
            }

            if (hadSlash) {
                hadSlash = false;
                if (ch == 'u') {
                    inUnicode = true;
                } else {
                    out.write("\\");
                    out.write(ch);
                }
                continue;
            } else if (ch == '\\') {
                hadSlash = true;
                continue;
            }
            out.write(ch);
        }
        if (hadSlash) {
            out.write('\\');
        }
    }
}
