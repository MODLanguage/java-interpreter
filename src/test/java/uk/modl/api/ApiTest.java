package uk.modl.api;

import junit.framework.TestCase;
import org.junit.Test;
import uk.modl.interpreter.Interpreter;
import uk.modl.modlObject.ModlObject;
import uk.modl.modlObject.ModlValue;

import java.io.IOException;
import java.util.List;

/**
 * Created by alex on 05/11/2018.
 */
public class ApiTest extends TestCase {

    String inputString = "grandparent(\n" +
            "  val=g\n" +
            "  parent(\n" +
            "    val=p\n" +
            "    child(\n" +
            "      val=c\n" +
            "      letters[a;b;c;d;e]\n" +
            "    )\n" +
            "  )\n" +
            ")";

    @Test
    public void testApi() throws Exception {
        ModlObject modlObject = getModlObject();
        /*
        modl.grandparent => full object
        modl.grandparent.val => "g"
        modl.grandparent.parent.val => "p"
        modl.grandparent.parent.child.val => "c"
        modl.grandparent.parent.child.letters => array of letters
        modl.grandparent.parent.child.letters[0] => "a"
         */
        assertTrue(modlObject.isModlObject());
        assertFalse(modlObject.isMap());

        List<String> keys = modlObject.getKeys();
        assertEquals(1, keys.size());
        assertEquals("grandparent", keys.get(0));

        ModlValue grandparentValue = modlObject.get(0);
        assertTrue(grandparentValue.isPair());
        assertTrue(((ModlObject.Pair)grandparentValue).getModlValue().isMap());
        assertFalse(grandparentValue.isModlObject());

        grandparentValue = modlObject.get("grandparent");
        assertTrue(grandparentValue.isMap());
        assertFalse(grandparentValue.isModlObject());

        ModlValue val = grandparentValue.get(0);
        assertTrue(val.isPair());
        assertEquals("g", ((ModlObject.String)((ModlObject.Pair)val).getModlValue()).string);
        assertEquals("g", ((ModlObject.Pair) val).getModlValue().getValue());

        val = grandparentValue.get("val");
        assertTrue(val.isString());
        assertEquals("g", ((ModlObject.String)val).string);
        assertEquals("g", val.getValue());

        ModlValue parent = grandparentValue.get(1);
        assertTrue(parent.isPair());
        assertTrue(((ModlObject.Pair)parent).getModlValue().isMap());

        parent = grandparentValue.get("parent");
        assertTrue(parent.isMap());
        assertFalse(parent.isModlObject());

        val = parent.get("val");
        assertTrue(val.isString());
        assertEquals("p", ((ModlObject.String)val).string);
        assertEquals("p", val.getValue());

        ModlValue child = parent.get("child");
        assertTrue(child.isMap());
        assertFalse(child.isModlObject());

        val = child.get("val");
        assertTrue(val.isString());
        assertEquals("c", ((ModlObject.String)val).string);
        assertEquals("c", val.getValue());

        ModlValue letters = child.get("letters");
        assertTrue(letters.isArray());
        assertEquals("a", letters.get(0).getValue());
        assertEquals("e", letters.get(4).getValue());

        assertEquals("e", modlObject.get("grandparent").get("parent").get("child").get("letters").get(4).getValue());

    }

    @Test
    public void testUnknown() throws Exception {
        ModlObject modlObject = getModlObject();

        // Now iterate through the object without knowing what is there, printing everything as we go
        // We'd like to use a generic method here, so that we don't have to inspect each bloody thing

        String traversal = traverseModlValue(modlObject, false);
        System.out.println(traversal);

        assertEquals(inputString.replace(" ", "").replace("\n", "").replace("=",""),
                traversal.replace(" ", "").replace("\n", "").replace("=",""));
    }

    private String traverseModlValue(ModlValue modlValue, boolean isLast) {
        String ret = "";
        if (!modlValue.isTerminal()) {
            int count = 0;
            for (ModlValue mv : modlValue.getModlValues()) {
                if (mv.isArray()) {
                    ret += "[";
                }
                if (mv.isMap()) {
                    ret += "(\n";
                }
                if (mv.isPair()) {
                    ret += mv.getKeys().get(0) + "=";
                }

                ret += traverseModlValue(mv, ++count == modlValue.getModlValues().size() ? true : false);

                if (mv.isArray()) {
                    ret += "]";
                }
                if (mv.isMap()) {
                    ret += ")";
                }
                if (mv.isPair()) {
                    ret += "\n";
                }
            }
        } else {
            ret = modlValue.getValue().toString();
            if (!isLast) {
                ret += ";";
            }
        }
        return ret;
    }

    private ModlObject getModlObject() throws IOException {
        // Make a new ModlObject from the parser
        return Interpreter.interpret(inputString);
    }
}
