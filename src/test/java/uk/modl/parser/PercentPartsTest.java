/*
MIT License

Copyright (c) 2018 NUM Technology Ltd

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of
the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package uk.modl.parser;

import org.junit.Assert;
import org.junit.Test;
import uk.modl.interpreter.StringTransformer;

import java.util.List;

/**
 * @author twalmsley
 */
public class PercentPartsTest {
    /**
     *
     */
    @Test
    public void test_01() {
        final List<String> parts = StringTransformer.getPercentPartsFromString("just %person.name.speak.u%_this=Hello %person.name.first.d!");
        Assert.assertNotNull(parts);
        Assert.assertTrue(parts.size() == 2);
        Assert.assertEquals("%person.name.speak.u%", parts.get(0));
        Assert.assertEquals("%person.name.first.d", parts.get(1));
    }
    @Test
    public void test_02() {
        final List<String> parts = StringTransformer.getPercentPartsFromString("A meta-%1 %2, used to create %1 %2%s such as DocBook.");
        Assert.assertNotNull(parts);
        Assert.assertTrue(parts.size() == 4);
        Assert.assertEquals("%1", parts.get(0));
        Assert.assertEquals("%2", parts.get(1));
        Assert.assertEquals("%1", parts.get(2));
        Assert.assertEquals("%2%", parts.get(3));
    }
    @Test
    public void test_03() {
        final List<String> parts = StringTransformer.getPercentPartsFromString("Blah %0.r<o,huzzah> %1.t<w>");
        Assert.assertNotNull(parts);
        Assert.assertTrue(parts.size() == 2);
        Assert.assertEquals("%0.r<o,huzzah>", parts.get(0));
        Assert.assertEquals("%1.t<w>", parts.get(1));
    }
}