# Minimal Object Description Language (MODL) Interpreter
This Java interpreter is based on the [MODL ANTLR4 Grammar](https://github.com/MODLanguage/grammar-antlr4) and the [MODL Specification](http://www.modl.uk).

There are several ways the interpreter can be used:

- Using the [MODL Playground](https://www.modl.uk/playground)
- Writing a Java program
- Building and running on the command line

## Command-line Usage

First clone the repository and build the project using `gradle clean customFatJar` from the project root directory.

Create a file containing a MODL object, e.g. `test.modl` with:
```
a=test;
b=123;
c=Hello World
```

Run the interpreter using the command:

```shell script
java -cp ./build/libs/interpreter-<version>.jar test.modl
```

The result should be:
```
Processing file: test.modl
{
  "a" : "test",
  "b" : 123,
  "c" : "Hello World"
}
Finished file: test.modl
```
Multiple file names can be provided if needed, and the value of `$?` is the number of files that errored, so `0` is success.
## Usage In a Java Program

The `Interpreter` class has several convenience methods, each returning a slightly different result depending on how the result is to be further processed.

### JSON String Result
Convert a MODL String to a JSON String:
```java
        final Interpreter interpreter = new Interpreter();

        final String json = interpreter.interpretToJsonString("a=b");
```
Use this method to generate a compact `JSON` String.
### Pretty JSON String Result
Convert a MODL String to a pretty-printed JSON String:
```java
        final Interpreter interpreter = new Interpreter();

        final String json = interpreter.interpretToPrettyJsonString("a=b");
```
Use this method to generate a `JSON` String for easy reading.
### Jackson Core JsonNode Result
Convert a MODL String to a `JsonNode`:
```java
        final Interpreter interpreter = new Interpreter();

        final JsonNode jsonNode = interpreter.interpretToJsonObject("a=b");
```
Use this method to generate non-proprietary object for further processing.
### Modl Object Result
Convert a MODL String to a `Modl` object:
```java
        final Interpreter interpreter = new Interpreter();

        final Modl modl = interpreter.interpret("a=b");
```
Use this method to generate an object using the core model within the library, i.e. those in the `uk.modl.model` package.

# Other Interpreters

We have a version of the interpreter in [Ruby](https://github.com/MODLanguage/ruby-interpreter), and we are planning a JavaScript version.
# Contributing

Bugs and New feature requests should be raised as GitHub issues, and the community is welcome to fork the repository and submit pull requests.