# Minimal Object Description Language (MODL) Interpreter
This Java interpreter is based on the [MODL ANTLR4 Grammar](https://github.com/MODLanguage/grammar-antlr4) and the [MODL Specification](http://www.modl.uk).

It doesn't include an API for accessing the MODL object yet (coming soon) but does provide the option of converting the MODL object to JSON – which can then be accessed using the Java JSON API. To convert MODL to a JSON string: 

```java
String input = "test(message=Hello World)"; 
String json = Interpreter.parseToJson(input);
```

## HTTP Wrapper
This library includes a useful HTTP wrapper service which converts MODL to JSON. You'll need [Gradle](https://gradle.org/). Once you've downloaded the repository you can run:

    ./gradlew run
    
Then you can access the server on localhost at:

http://0.0.0.0:8080/?modl=test(message=this%20is%20a%20test%20message)

There's a very basic client in the repo at *client/index.html*
