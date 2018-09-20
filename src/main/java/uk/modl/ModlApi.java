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
