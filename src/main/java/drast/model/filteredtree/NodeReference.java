package drast.model.filteredtree;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that keeps track of the references generated by a attribute method.
 * Contains the node the references are from and to which nodes they point to.
 */
public class NodeReference {
  private final String label;
  private final GenericTreeNode from;
  private List<GenericTreeNode> refList;
  private List<Object> futureRefList;

  /**
   * A NodeReference with a label and a list of eventual references.
   */
  public NodeReference(String label, GenericTreeNode from, List<Object> futureRefList) {
    this.label = label;
    this.from = from;
    this.futureRefList = futureRefList;
  }

  /**
   * A NodeReference with a label, will create a empty list of references
   */
  public NodeReference(String label, GenericTreeNode from) {
    this.label = label;
    this.from = from;
    this.refList = new ArrayList<>();
  }

  public void setReferences(ArrayList<GenericTreeNode> treeNodes) {
    this.refList = treeNodes;
  }

  public void addReference(GenericTreeNode treeNode) {
    refList.add(treeNode);
  }

  /**
   * Returns the list of "future" references, these needs to be controlled that they really are real node references.
   */
  public List<Object> getFutureReferences() {
    return futureRefList;
  }

  public List<GenericTreeNode> getReferences() {
    return refList;
  }

  public GenericTreeNode getReferenceFrom() {
    return from;
  }

  public String getLabel() {
    return label;
  }
}
