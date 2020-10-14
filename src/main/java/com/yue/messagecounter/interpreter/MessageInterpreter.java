package com.yue.messagecounter.interpreter;

import java.io.IOException;
import java.io.Writer;

public interface MessageInterpreter {
    String decode(String str);
    String unescape(String str);

    void unescape(Writer out, String str) throws IOException;
}
