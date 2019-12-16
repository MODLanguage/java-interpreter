package uk.modl.interpreter;

import org.junit.Test;

public class StringEscapeReplacerTest {
    @Test
    public void test_concurrent_modification_protection() throws InterruptedException {
        final Thread thread1 = new Thread(() -> StringEscapeReplacer.replace("Just a test string"));
        final Thread thread2 = new Thread(() -> StringEscapeReplacer.replace("Just a test string"));

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }
}