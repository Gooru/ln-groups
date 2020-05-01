package org.gooru.groups.reports.dbhelpers.core.hierarchy;

/**
 * @author szgooru
 *
 */
public class Node<T> {
  private T data = null;

  private Node<T> child = null;

  private Node<T> parent = null;

  public Node(T data) {
    this.data = data;
  }

  public Node<T> addChild(Node<T> child) {
    child.setParent(this);
    this.child = child;
    return child;
  }

  public Node<T> getChild() {
    return child;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  private void setParent(Node<T> parent) {
    this.parent = parent;
  }

  public Node<T> getParent() {
    return parent;
  }
  
  public Node<T> getRoot() {
    if(parent == null){
     return this;
    }
    return parent.getRoot();
   }
  
  public boolean isRoot() {
    return (parent == null) ? true : false;
  }
  
  public Node<T> getLeaf() {
    Node<T> child = this.getChild();
    if (child == null) {
      return this;
    } 
    
    return child.getLeaf();
  }
  
  public boolean isLeaf() {
    return (child == null) ? true : false;
  }
}
