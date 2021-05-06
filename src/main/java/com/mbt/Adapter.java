package com.mbt;

import java.io.File;
import org.junit.Assert;

public class Adapter {
    public void init() {
        System.out.println("Adapter: init SUT");
    }

    public void reset() {
        System.out.println("Adapter: reset SUT");
    }
}