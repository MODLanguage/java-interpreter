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

The grammar can be synched from its original repository. It's possible to update the project to get the latest or even
to get older version.

### Using Gradle

Gradle has a task to deal with automate this process. You can run the following command: `./gradlew updateGrammar 
-PgrammarTag=gammarVersion -Pbranch=branchName`. That will create a new branch called branchName. The branch will 
contain the version of the grammar with the tag gammarVersion. You can use the tag called `latest` to get the latest 
grammar (it's also the default value). You can use an older tag to revert to an older version.

If something goes wrong, you can cleanup the git temporary artifacts by running `./gradlew gitClean`. You may need to
also switch back to your initial branch and remove branchName if that one was created.

### Manually

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

The following commands will update the content of the `grammar_tests` directory with the new commits done to the test directory 
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
git subtree merge --squash --prefix grammar_tests removeme-tests-branch

#we clean up the temporary branches and remote connection
git branch -D removeme-export-branch removeme-tests-branch
git remote rm grammar
```
## Publishing to Maven Central

The gradle configuration supports publishing to maven. You need to set your credentials
and create a public/private key pair for signing the file. Gradle will take care of most
of the process for you but you will need to configure your credentials.

Install gpg to create a key (for signing the files). Generate your key with
`gpg --gen-key` and then export it with `gpg --export-secret-keys -o secring.gpg`.
You can name the file differently if you want. It's the path to that file that you will need
to provide in the gradle configuration file.

To set your credentials, please edit `~/.gradle/gradle.properties`. The file may not exist
yet. Add the following properties:

```
signing.keyId=<GPG short key (8 hex characters)>
signing.password=<passphrase for your key>
signing.secretKeyRingFile=<path to the key ring file>

sonatypeUsername=<Sonatype username provided by admin>
sonatypePassword=<Sonatype password>
```

You can get the short key with `gpg --list-keys --keyid-format short`. Please contact
an administrator of the project to get Sonatype credentials.

You will now need to upload your public key to allow maven to check the file signatures.
To achieve that, simply execute the following command: 
`gpg --keyserver hkp://pool.sks-keyservers.net --send-key <hex code of the gpg key>`

You're all set, you can now publish by running `gradle publish`. Once the operation is
successful, go to `https://oss.sonatype.org` and log in with your sonatype credentials
(the same as those used for the gradle config file). Go to the "Staging Repositories" page 
and locate your build (it should be at the very bottom of the page). Select it and click "Close".
After a while, you will get some feedback. If you get errors, fix the issues mentioned and try again.
Once you get a successful build, click the "Release" button and the build will find its way to
maven central after a few minutes/hours.
