package uk.modl.interpreter;

import org.junit.Assert;
import org.junit.Test;

import uk.modl.utils.StringEscapeReplacer;

public class UnicodeTest {
  @Test
  public void test() {
    Assert.assertEquals("ȫȫ", StringEscapeReplacer.replace("\\u022b\\u022b"));
  }

  @Test
  public void testSlashes() {
    Assert.assertEquals("\"b\"", StringEscapeReplacer.replace("\\u0022b\\u0022"));
  }

  @Test
  public void testTilde() {
    Assert.assertEquals("\"b\"", StringEscapeReplacer.replace("~u0022b~u0022"));
  }

}
