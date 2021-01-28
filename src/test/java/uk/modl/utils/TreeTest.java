package uk.modl.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TreeTest {

  private Tree<String> tree = null;

  @Before
  public void before() {
    tree = new Tree<>(null);
  }

  @Test
  public void addTreeNodes() {
    final List<String> data = new ArrayList<>();
    data.add("child_1");
    data.add("child_2");
    data.add("child_3");
    data.add("child_4");
    tree.getRoot().addChildren(data);

    Assert.assertEquals(4, tree.getRoot().getChildren().size());
    Assert.assertEquals(4, tree.getAllNodes().size());
  }
}
