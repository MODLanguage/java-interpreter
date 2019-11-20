package uk.modl.parser;

import org.junit.Test;

import java.net.IDN;

public class IDNTest {
    @Test(expected = IllegalArgumentException.class)
    public void testStrictLDH_IDN_1() {
        IDN.toASCII("test!", IDN.USE_STD3_ASCII_RULES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStrictLDH_IDN_2() {
        IDN.toASCII("question?", IDN.USE_STD3_ASCII_RULES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStrictLDH_IDN_3() {
        IDN.toASCII("hello,goodbye", IDN.USE_STD3_ASCII_RULES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStrictLDH_IDN_4() {
        IDN.toASCII("this is a test", IDN.USE_STD3_ASCII_RULES);
    }
}
