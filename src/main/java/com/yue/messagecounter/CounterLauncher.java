package com.yue.messagecounter;

import com.yue.messagecounter.ui.CounterFrame;

public class CounterLauncher extends Launcher {
    public static void main(String[] args) {
        new CounterLauncher();
    }

    public CounterLauncher() {
        super();
    }

    @Override
    protected void launch() {
        CounterFrame.getInstance();
    }
}
