package uk.modl.interpreter;

import org.junit.Assert;
import org.junit.Test;
import uk.modl.modlObject.ModlObject;
import uk.modl.modlObject.ModlValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NestedObjectReferenceTest {

    private final String nestedTestString01 = "_test=(\n" +
                                              "  numbers=[[1;2;3;4;5];[6;7;8;9;10]]\n" +
                                              ");\n" +
                                              " \n" +
                                              "testing=%test.numbers.0.0";

    private final String nestedTestString02 = "_test=(\n" +
                                              "  numbers=(\"one\"=1)\n" +
                                              ");\n" +
                                              " \n" +
                                              "testing = this is a string that includes a reference with a letter s after it `%test.numbers.one`s";

    private final String nestedTestString03 = "_test=(\n" +
                                              "  numbers=(\"one\"=1)\n" +
                                              ");\n" +
                                              " \n" +
                                              "testing = this is a string that includes a reference with a letter s after it %test.numbers.ones";

    private final String nestedTestString04 = "_test=(\n" +
                                              "  numbers=(\"v\"=TEST)\n" +
                                              ");\n" +
                                              " \n" +
                                              "testing = this is a string that includes extra transforms for the value `%test.numbers.v.d`_value";

    private final String nestedTestString05 = "_test=(\n" +
                                              "  first=(\"v\"=TEST);\n" +
                                              "  second=(\"v\"=TEST2)\n" +
                                              ");\n" +
                                              " \n" +
                                              "testing = this is a string that includes extra transforms for the value `%test.second.v.d`_value";

    private final String nestedTestString06 = "_test=(\n" +
                                              "  first=(\"v1\"=one);\n" +
                                              "  second=(\"v2\"=two:three)\n" +
                                              ");\n" +
                                              " \n" +
                                              "testing = \"`%test.second.v2.1`\"";

    private final String nestedTestString07 = "_test=(\n" +
                                              "  first=(\"v1\"=[one]);\n" +
                                              "  second=(\"v2\"=two:three)\n" +
                                              ");\n" +
                                              " \n" +
                                              "testing = \"`%test.first.v1.0``%test.second.v2.0``%test.second.v2.1`\"";

    private final String nestedTestString08 = "_test=(\n" +
                                              "  first=(\"v1\"=(one=(two=three)))\n" +
                                              ");\n" +
                                              " \n" +
                                              "testing = \"`%test.first.v1.0.0.0`\"";

    private final String nestedTestString09 = "_test=(\n" +
                                              "  first=(\"v1\"=(one=(two=three)))\n" +
                                              ");\n" +
                                              " \n" +
                                              "testing = \"`%test.first.v1.one.two`\"";

    private final String nestedTestString10 = "test=(a=b=c=d=f);\n" +
                                              "testing=%test.a.b.c.d";

    private final String nestedTestString11 = "a(b(c(d(e(f=1)))));\n" +
                                              "testing=%a.b.c.d.e.f";

    private final String nestedTestString12 = "test=(a=b=c=d=f);\n" +
                                              "x=%test.a.b.c.d";

    @Test
    public void test_01() {
        try {
            final ModlObject modlObject = Interpreter.interpret(nestedTestString01, new ArrayList<String>());
            Assert.assertNotNull("ModlObject should not be null", modlObject);

            final List<ModlObject.Structure> structures = modlObject.getStructures();
            Assert.assertNotNull("structures should not be null", structures);
            Assert.assertEquals("structure should have only one element.", 1, structures.size());

            ModlObject.Structure structure = structures.get(0);
            Assert.assertNotNull("structure should not be null", structure);

            final List<String> keys = structure.getKeys();
            Assert.assertNotNull("keys should not be null", keys);
            Assert.assertEquals("keys should have only one element.", 1, keys.size());

            final String key = keys.get(0);
            Assert.assertNotNull("key should not be null", key);
            Assert.assertEquals("key has incorrect value.", "testing", key);

            final List<? extends ModlValue> modlValues = structure.getModlValues();
            Assert.assertNotNull("modlValues should not be null", modlValues);
            Assert.assertEquals("modlValues should have only one element.", 1, modlValues.size());

            final ModlValue modlValue = modlValues.get(0);
            Assert.assertNotNull("modlValue should not be null", modlValue);
            Assert.assertTrue("modlValue should be a ModlValue.Number", modlValue instanceof ModlObject.Number);

            final ModlObject.Number numericValue = (ModlObject.Number) modlValue;
            Assert.assertEquals("modlValue has incorrect value.", "1", numericValue.number);


        } catch (IOException e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    @Test
    public void test_02() {
        try {
            final ModlObject modlObject = Interpreter.interpret(nestedTestString02, new ArrayList<String>());
            Assert.assertNotNull("ModlObject should not be null", modlObject);

            final List<ModlObject.Structure> structures = modlObject.getStructures();
            Assert.assertNotNull("structures should not be null", structures);
            Assert.assertEquals("structure should have only one element.", 1, structures.size());

            ModlObject.Structure structure = structures.get(0);
            Assert.assertNotNull("structure should not be null", structure);

            final List<String> keys = structure.getKeys();
            Assert.assertNotNull("keys should not be null", keys);
            Assert.assertEquals("keys should have only one element.", 1, keys.size());

            final String key = keys.get(0);
            Assert.assertNotNull("key should not be null", key);
            Assert.assertEquals("key has incorrect value.", "testing", key);

            final List<? extends ModlValue> modlValues = structure.getModlValues();
            Assert.assertNotNull("modlValues should not be null", modlValues);
            Assert.assertEquals("modlValues should have only one element.", 1, modlValues.size());

            final ModlValue modlValue = modlValues.get(0);
            Assert.assertNotNull("modlValue should not be null", modlValue);
            Assert.assertTrue("modlValue should be a ModlValue.String", modlValue instanceof ModlObject.String);

            final ModlObject.String stringValue = (ModlObject.String) modlValue;
            Assert.assertEquals("modlValue has incorrect value.", "this is a string that includes a reference with a letter s after it 1s", stringValue.string);


        } catch (IOException e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    @Test
    public void test_03() {
        try {
            final ModlObject modlObject = Interpreter.interpret(nestedTestString03, new ArrayList<String>());
            Assert.assertNotNull(modlObject);
            Assert.fail("Expected a RuntimeException since the variable name doesn't match anything from the input string.");
        } catch (IOException e) {
            Assert.fail(e.getLocalizedMessage());
        } catch (RuntimeException r) {
            if (!r.getLocalizedMessage().startsWith("Interpreter Error: Cannot resolve reference in")) {
                Assert.fail("Wrong error message received, expected 'Invalid Object Reference'.");
            }
            // Ok, the exception is valid.
        }
    }

    @Test
    public void test_04() {
        try {
            final ModlObject modlObject = Interpreter.interpret(nestedTestString04, new ArrayList<String>());
            Assert.assertNotNull("ModlObject should not be null", modlObject);

            final List<ModlObject.Structure> structures = modlObject.getStructures();
            Assert.assertNotNull("structures should not be null", structures);
            Assert.assertEquals("structure should have only one element.", 1, structures.size());

            ModlObject.Structure structure = structures.get(0);
            Assert.assertNotNull("structure should not be null", structure);

            final List<String> keys = structure.getKeys();
            Assert.assertNotNull("keys should not be null", keys);
            Assert.assertEquals("keys should have only one element.", 1, keys.size());

            final String key = keys.get(0);
            Assert.assertNotNull("key should not be null", key);
            Assert.assertEquals("key has incorrect value.", "testing", key);

            final List<? extends ModlValue> modlValues = structure.getModlValues();
            Assert.assertNotNull("modlValues should not be null", modlValues);
            Assert.assertEquals("modlValues should have only one element.", 1, modlValues.size());

            final ModlValue modlValue = modlValues.get(0);
            Assert.assertNotNull("modlValue should not be null", modlValue);
            Assert.assertTrue("modlValue should be a ModlValue.String", modlValue instanceof ModlObject.String);

            final ModlObject.String stringValue = (ModlObject.String) modlValue;
            Assert.assertEquals("modlValue has incorrect value.", "this is a string that includes extra transforms for the value test_value", stringValue.string);


        } catch (IOException e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    @Test
    public void test_05() {
        try {
            final ModlObject modlObject = Interpreter.interpret(nestedTestString05, new ArrayList<String>());
            Assert.assertNotNull("ModlObject should not be null", modlObject);

            final List<ModlObject.Structure> structures = modlObject.getStructures();
            Assert.assertNotNull("structures should not be null", structures);
            Assert.assertEquals("structure should have only one element.", 1, structures.size());

            ModlObject.Structure structure = structures.get(0);
            Assert.assertNotNull("structure should not be null", structure);

            final List<String> keys = structure.getKeys();
            Assert.assertNotNull("keys should not be null", keys);
            Assert.assertEquals("keys should have only one element.", 1, keys.size());

            final String key = keys.get(0);
            Assert.assertNotNull("key should not be null", key);
            Assert.assertEquals("key has incorrect value.", "testing", key);

            final List<? extends ModlValue> modlValues = structure.getModlValues();
            Assert.assertNotNull("modlValues should not be null", modlValues);
            Assert.assertEquals("modlValues should have only one element.", 1, modlValues.size());

            final ModlValue modlValue = modlValues.get(0);
            Assert.assertNotNull("modlValue should not be null", modlValue);
            Assert.assertTrue("modlValue should be a ModlValue.String", modlValue instanceof ModlObject.String);

            final ModlObject.String stringValue = (ModlObject.String) modlValue;
            Assert.assertEquals("modlValue has incorrect value.", "this is a string that includes extra transforms for the value test2_value", stringValue.string);


        } catch (IOException e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    @Test
    public void test_06() {
        try {
            final ModlObject modlObject = Interpreter.interpret(nestedTestString06, new ArrayList<String>());
            Assert.assertNotNull("ModlObject should not be null", modlObject);

            final List<ModlObject.Structure> structures = modlObject.getStructures();
            Assert.assertNotNull("structures should not be null", structures);
            Assert.assertEquals("structure should have only one element.", 1, structures.size());

            ModlObject.Structure structure = structures.get(0);
            Assert.assertNotNull("structure should not be null", structure);

            final List<String> keys = structure.getKeys();
            Assert.assertNotNull("keys should not be null", keys);
            Assert.assertEquals("keys should have only one element.", 1, keys.size());

            final String key = keys.get(0);
            Assert.assertNotNull("key should not be null", key);
            Assert.assertEquals("key has incorrect value.", "testing", key);

            final List<? extends ModlValue> modlValues = structure.getModlValues();
            Assert.assertNotNull("modlValues should not be null", modlValues);
            Assert.assertEquals("modlValues should have only one element.", 1, modlValues.size());

            final ModlValue modlValue = modlValues.get(0);
            Assert.assertNotNull("modlValue should not be null", modlValue);
            Assert.assertTrue("modlValue should be a ModlValue.String", modlValue instanceof ModlObject.String);

            final ModlObject.String stringValue = (ModlObject.String) modlValue;
            Assert.assertEquals("modlValue has incorrect value.", "three", stringValue.string);


        } catch (IOException e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    @Test
    public void test_07() {
        try {
            final ModlObject modlObject = Interpreter.interpret(nestedTestString07, new ArrayList<String>());
            Assert.assertNotNull("ModlObject should not be null", modlObject);

            final List<ModlObject.Structure> structures = modlObject.getStructures();
            Assert.assertNotNull("structures should not be null", structures);
            Assert.assertEquals("structure should have only one element.", 1, structures.size());

            ModlObject.Structure structure = structures.get(0);
            Assert.assertNotNull("structure should not be null", structure);

            final List<String> keys = structure.getKeys();
            Assert.assertNotNull("keys should not be null", keys);
            Assert.assertEquals("keys should have only one element.", 1, keys.size());

            final String key = keys.get(0);
            Assert.assertNotNull("key should not be null", key);
            Assert.assertEquals("key has incorrect value.", "testing", key);

            final List<? extends ModlValue> modlValues = structure.getModlValues();
            Assert.assertNotNull("modlValues should not be null", modlValues);
            Assert.assertEquals("modlValues should have only one element.", 1, modlValues.size());

            final ModlValue modlValue = modlValues.get(0);
            Assert.assertNotNull("modlValue should not be null", modlValue);
            Assert.assertTrue("modlValue should be a ModlValue.String", modlValue instanceof ModlObject.String);

            final ModlObject.String stringValue = (ModlObject.String) modlValue;
            Assert.assertEquals("modlValue has incorrect value.", "onetwothree", stringValue.string);


        } catch (IOException e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    @Test
    public void test_08() {
        try {
            final ModlObject modlObject = Interpreter.interpret(nestedTestString08, new ArrayList<String>());
            Assert.assertNotNull(modlObject);
            Assert.fail("Expected a RuntimeException due to numerical indexing of maps");
        } catch (IOException e) {
            Assert.fail(e.getLocalizedMessage());
        } catch (RuntimeException r) {
            if (!r.getLocalizedMessage().startsWith("Object reference is numerical for non-Array value")) {
                System.out.println(r.getLocalizedMessage());
                Assert.fail("Wrong error message received, expected 'Object reference is numerical for non-Array value'.");
            }
            // Ok, the exception is valid.
        }
    }

    @Test
    public void test_09() {
        try {
            final ModlObject modlObject = Interpreter.interpret(nestedTestString09, new ArrayList<String>());
            Assert.assertNotNull("ModlObject should not be null", modlObject);

            final List<ModlObject.Structure> structures = modlObject.getStructures();
            Assert.assertNotNull("structures should not be null", structures);
            Assert.assertEquals("structure should have only one element.", 1, structures.size());

            ModlObject.Structure structure = structures.get(0);
            Assert.assertNotNull("structure should not be null", structure);

            final List<String> keys = structure.getKeys();
            Assert.assertNotNull("keys should not be null", keys);
            Assert.assertEquals("keys should have only one element.", 1, keys.size());

            final String key = keys.get(0);
            Assert.assertNotNull("key should not be null", key);
            Assert.assertEquals("key has incorrect value.", "testing", key);

            final List<? extends ModlValue> modlValues = structure.getModlValues();
            Assert.assertNotNull("modlValues should not be null", modlValues);
            Assert.assertEquals("modlValues should have only one element.", 1, modlValues.size());

            final ModlValue modlValue = modlValues.get(0);
            Assert.assertNotNull("modlValue should not be null", modlValue);
            Assert.assertTrue("modlValue should be a ModlValue.String", modlValue instanceof ModlObject.String);

            final ModlObject.String stringValue = (ModlObject.String) modlValue;
            Assert.assertEquals("modlValue has incorrect value.", "three", stringValue.string);

        } catch (IOException e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    @Test
    public void test_10() {
        try {
            final ModlObject modlObject = Interpreter.interpret(nestedTestString10, new ArrayList<String>());

            final List<ModlObject.Structure> structures = modlObject.getStructures();

            ModlObject.Structure structure = structures.get(1);

            final List<? extends ModlValue> modlValues = structure.getModlValues();

            final ModlValue modlValue = modlValues.get(0);

            final ModlObject.String stringValue = (ModlObject.String) modlValue;
            Assert.assertEquals("modlValue has incorrect value.", "f", stringValue.string);

        } catch (IOException e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    @Test
    public void test_11() {
        try {
            final ModlObject modlObject = Interpreter.interpret(nestedTestString11, new ArrayList<String>());

            final List<ModlObject.Structure> structures = modlObject.getStructures();

            ModlObject.Structure structure = structures.get(1);

            final List<? extends ModlValue> modlValues = structure.getModlValues();

            final ModlValue modlValue = modlValues.get(0);

            final ModlObject.Number numericValue = (ModlObject.Number) modlValue;
            Assert.assertEquals("modlValue has incorrect value.", "1", numericValue.number);

        } catch (IOException e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    @Test
    public void test_12() {
        try {
            final ModlObject modlObject = Interpreter.interpret(nestedTestString12, new ArrayList<String>());

            final List<ModlObject.Structure> structures = modlObject.getStructures();

            ModlObject.Structure structure = structures.get(1);

            final List<? extends ModlValue> modlValues = structure.getModlValues();

            final ModlValue modlValue = modlValues.get(0);

            final ModlObject.String stringValue = (ModlObject.String) modlValue;
            Assert.assertEquals("modlValue has incorrect value.", "f", stringValue.string);

        } catch (IOException e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }
}
