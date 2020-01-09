package uk.modl.parser;

import io.vavr.control.Option;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.model.Modl;

public class ParserTest {

    @Test
    public void parseOk() {
        final Option<Modl> modl = new Parser().apply("*c(*i=a;*n=alpha;*s=map;v=victor);*c(*i=b;*n=bravo;*s=a;w=whisky);*c(*i=c;*n=charlie;*s=b;x=xray);*c(*i=d;*n=delta;*s=c;y=yankee);d(test1=test2)");
        Assert.assertNotNull(modl);
        Assert.assertNotNull(modl.get());
    }

    @Test
    public void parseBad() {
        final Option<Modl> modl = new Parser().apply("xxx");
        Assert.assertNotNull(modl);
        Assert.assertTrue(modl.isEmpty());
    }
}