package uk.modl.interpreter;

import io.vavr.control.Either;
import org.junit.Assert;
import org.junit.Test;
import uk.modl.error.Error;
import uk.modl.model.Modl;

public class InterpreterTest {

    private static Interpreter interpreter = new Interpreter();

    @Test
    public void parseOk() {
        final Either<Error, Modl> modl = interpreter.apply("*c(*i=a;*n=alpha;*s=map;v=victor);*c(*i=b;*n=bravo;*s=a;w=whisky);*c(*i=c;*n=charlie;*s=b;x=xray);*c(*i=d;*n=delta;*s=c;y=yankee);d(test1=test2)");
        Assert.assertNotNull(modl);
        Assert.assertNotNull(modl.get());
    }

    @Test
    public void parseBad() {
        final Either<Error, Modl> modl = interpreter.apply("xxx");
        Assert.assertNotNull(modl);
        Assert.assertTrue(modl.isEmpty());
    }
}