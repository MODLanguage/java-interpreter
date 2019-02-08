package uk.modl.interpreter;

import org.junit.Assert;
import org.junit.Test;
import uk.modl.modlObject.ModlObject;
import uk.modl.modlObject.ModlValue;

import java.io.IOException;
import java.util.List;

public class NestedObjectReferenceTest {

    private final String nestedTestString01 = "_test=(\n" +
            "  numbers=[[1;2;3;4;5];[6;7;8;9;10]]\n" +
            ")\n" +
            " \n" +
            "testing=%test>numbers>0>0";

    private final String nestedTestString02 = "_test=(\n" +
            "  numbers=(\"one\"=1)\n" +
            ")\n" +
            " \n" +
            "testing = this is a string that includes a reference with a letter s after it `%test>numbers>one`s";

    private final String nestedTestString03 = "_test=(\n" +
            "  numbers=(\"one\"=1)\n" +
            ")\n" +
            " \n" +
            "testing = this is a string that includes a reference with a letter s after it %test>numbers>ones";

    private final String nestedTestString04 = "_test=(\n" +
            "  numbers=(\"v\"=TEST)\n" +
            ")\n" +
            " \n" +
            "testing = this is a string that includes extra transforms for the value `%test>numbers>v.d`_value";

    private final String nestedTestString05 = "_test=(\n" +
            "  first=(\"v\"=TEST)\n" +
            "  second=(\"v\"=TEST2)\n" +
            ")\n" +
            " \n" +
            "testing = this is a string that includes extra transforms for the value `%test>second>v.d`_value";

    @Test
    public void test_01() {
        try {
            final ModlObject modlObject = Interpreter.interpret(nestedTestString01);
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
            final ModlObject modlObject = Interpreter.interpret(nestedTestString02);
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
            final ModlObject modlObject = Interpreter.interpret(nestedTestString03);
            Assert.assertNotNull(modlObject);
            Assert.fail("Expected a RuntimeException since the variable name doesn't match anything from the input string.");
        } catch (IOException e) {
            Assert.fail(e.getLocalizedMessage());
        } catch (RuntimeException r) {
            if (!r.getLocalizedMessage().startsWith("Invalid Object Reference:")) {
                Assert.fail("Wrong error message received, expected 'Invalid Object Reference'.");
            }
            // Ok, the exception is valid.
        }
    }

    @Test
    public void test_04() {
        try {
            final ModlObject modlObject = Interpreter.interpret(nestedTestString04);
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
            final ModlObject modlObject = Interpreter.interpret(nestedTestString05);
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


}
