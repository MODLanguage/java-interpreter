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

package uk.modl;

/**
 * Created by alex on 20/09/2018.
 */
public interface ModlApi {
    /*
    Given this MODL object:

grandparent(
  val=g
  parent(
    val=p
    child(
      val=c
      letters[a;b;c;d;e]
    )
  )
)
Where “modl" is the name of the object read in by the interpreter, these are the API calls I’d want to be able to make:

modl.grandparent => full object
modl.grandparent.val => "g"
modl.grandparent.parent.val => "p"
modl.grandparent.parent.child.val => "c"
modl.grandparent.parent.child.letters => array of letters
modl.grandparent.parent.child.letters[0] => "a"
     */
}
