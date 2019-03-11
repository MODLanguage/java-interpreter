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


## Linking the grammar sources

Here's a quick reference regarding the import of the grammar files from the grammar repo into this project.

Please note that this is not expected to be done again. It is provided as a reference for other repos that want to use the
same pattern.

The procedure is exactly the same as for the update of the grammar but we will need to replace the following line:

```
git subtree merge --squash --prefix grammar removeme-antlr4-branch
```

with:

```
git subtree add --squash --prefix grammar removeme-antlr4-branch
```

## Updating the grammar sources

The following commands will update the content of the `grammar` director with the new commits done to the antlr4 directory 
of the grammar repository. Please keep them in sync as much as possible to avoid discrepancies.

```
#we create a branch that we will contain the changes and they we will use for a Pull Request
git checkout -b branch-for-pr

#we connect to the grammar repo that contains the files we want
git remote add grammar git@github.com:MODLanguage/grammar
git fetch grammar

#we create a tmp branch with a copy of the grammar on master
git checkout -b removeme-export-branch grammar/master

#we extract the files we're intrested in (we don't want the whole grammar repo)
git subtree split --prefix antlr4 -b removeme-antlr4-branch

#we go back to our main branch for our Pull Request
git checkout branch-for-pr

#we import the file we extracted in the interpreter project
git subtree merge --squash --prefix=grammar/ removeme-antlr4-branch

#we clean up the temporary branches and remote connection
git branch -D removeme-export-branch removeme-antlr4-branch
git remote rm grammar
```


Now we're all set to create a Pull Request on the `branch-for-pr` branch.

Please note that if the version of the files have diverged, the merge conflicts will need to be fixed before proceeding 
to the cleanup. This will only happen if the files were edited manually in the repo without doing a clean import.


## Updating the json test files

The following commands will update the content of the `src/test/json` directory with the new commits done to the test directory 
of the grammar repository. Please keep them in sync as much as possible to avoid discrepancies.

```
#we create a branch that we will contain the changes and thet we will use for a Pull Request
git checkout -b branch-for-pr

#we connect to the grammar repo that contains the files we want
git remote add grammar git@github.com:MODLanguage/grammar
git fetch grammar

#we create a tmp branch with a copy of the grammar repo on master
git checkout -b removeme-export-branch grammar/master

#we extract the files we're intrested in (we don't want the whole grammar repo)
git subtree split --prefix tests -b removeme-tests-branch

#we go back to our main branch for our Pull Request
git checkout branch-for-pr

#we import the file we extracted in the interpreter project
git subtree merge --squash --prefix src/test/json removeme-tests-branch

#we clean up the temporary branches and remote connection
git branch -D removeme-export-branch removeme-tests-branch
git remote rm grammar
```
