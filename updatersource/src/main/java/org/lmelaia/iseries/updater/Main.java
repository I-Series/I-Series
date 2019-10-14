package org.lmelaia.iseries.updater;

import org.lmelaia.iseries.common.system.AppBase;

public class Main {

    public static void main(String[] args) {
        Thread.currentThread().setName("I-Series Updater");
        AppBase.startApp(new App(), args);
    }
}
