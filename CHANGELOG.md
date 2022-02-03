1.0.0
===

- Rewrite to match the TypeScript version.

0.2.2
===

- Update to latest MODL grammar - removes `00`, `01`, and `000`, as `false`, `true`, and `null`.

0.2.0
===

- Update to latest MODL grammar - greatly simplified to be more like JSON

0.1.26
===
- Fix recursive load bug.

0.1.25
===
- Fix recursive load detection problem.

0.1.21 to 0.1.24
===
- Prevent logging of a `ThreadDeath` error
- Use a logger rather than `System.out`s in the command line version.
- Fix `StringEscapeReplacer`
- Add SonarQube and Jacoco
- Prevent recursive file loading with `*load`.

0.1.20
===
- Code declutter.
- Fix class expansion problem.

0.1.19
===
- Fix for relative `*load`.

0.1.18
===
- Fix for relative `*load`.

0.1.17
===
- Add a timeout for large and/or deeply nested MODL.

0.1.16
===
- Bug fix for relative `*load` instructions.

0.1.15
===
- Bug fix for class expansion to primitive values with wildcarded `*assign` instructions. See CH story 8206.

0.1.14
===
- Support for relative URIs in the `*load` instruction.

0.1.13
===
- Raise an error if a `*superclass` doesn't exist when encountered. I.e. there has to be a pre-existing `*class` with that name, unless its a built-in superclass such as map, arr, etc.
- Some support for relative URIs in `*load` instructions.
- Extra validation for `*class` and `*method` instructions.

0.1.12
===
- Join lines from files using `\n`

0.1.11
===
- Add a custom artifact for running the interpreter as an executable jar.

0.1.10
===
- Disallow top-level pairs with top=level arrays or maps.
- Trailing commas are not part of a reference.
- Fix nested refs with applied methods.
- Error when attempting to embed objects in strings via references.
- Allow embedding of primitives in strings via references.

0.1.9
===
- Fix *assign for values inside maps.

0.1.8
===
- Locally loaded files need lines joined with a newline separator.

0.1.7
===
- Detect immutable name clashes in parent scopes.

0.1.6
===
- Allow immutable names to be reused in different scopes.

0.1.5
===
- Fix caching issue.

0.1.4
===
- Factor out the scoped search into a class separate from the Ancestry class.
- Refactor the most complex sections for easier readability.

0.1.3
===
- Fix empty array and empty map results.
- Change scope-search to look in the left parent tree only.

0.1.2
===
- Class expansion fixes.

0.1.1
===
- README Updates
- Added CHANGELOG
- Added convenience methods for calling the interpreter inside a Java program

0.1.0
===
- Complete rewrite
