package uk.modl.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

public class Tree<T> {
  private Node<T> root;

  public Tree(final T rootData) {
    root = new Node<>(rootData, null, new ArrayList<>());
  }

  @AllArgsConstructor
  public static class Node<T> {
    private T data;
    private Node<T> parent;
    private List<Node<T>> children;

    public Node<T> getParent() {
      return parent;
    }

    public void addChildren(final List<T> newChildren) {
      final List<Node<T>> newNodes = newChildren.stream().map(t -> new Node<>(t, this, new ArrayList<>()))
          .collect(Collectors.toList());
      children.addAll(newNodes);
    }

    public T getNodeData() {
      return data;
    }

    public List<Node<T>> getChildren() {
      return children;
    }

    public void addChildData(final List<T> result) {
      if (data != null && data.toString().length() > 0) {
        result.add(data);
      }
      children.forEach(c -> c.addChildData(result));
    }

    public void addChild(final Node<T> node) {
      children.add(node);
    }
  }

  public Node<T> getRoot() {
    return root;
  }

  public List<T> getAllNodes() {
    final List<T> result = new ArrayList<>();
    root.addChildData(result);
    return result;
  }
}