/* This file was generated with JastAdd2 (http://jastadd.org) version 2.1.8 */
package AST;

import java.util.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.io.File;
import java.util.Set;
import java.util.ArrayList;
import beaver.*;
import org.jastadd.util.*;
import java.util.zip.*;
import java.io.*;
import java.io.FileNotFoundException;
/**
 * @ast node
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/grammar/Java.ast:62
 * @production DoubleType : {@link FloatingPointType};

 */
public class DoubleType extends FloatingPointType implements Cloneable {
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:903
   */
  public void prettyPrint(StringBuffer sb) {
    sb.append("double");
  }
  /**
   * @declaredat ASTNode:1
   */
  public DoubleType() {
    super();
  }
  /**
   * Initializes the child array to the correct size.
   * Initializes List and Opt nta children.
   * @apilevel internal
   * @ast method
   * @declaredat ASTNode:10
   */
  public void init$Children() {
    children = new ASTNode[3];
    setChild(new Opt(), 1);
    setChild(new List(), 2);
  }
  /**
   * @declaredat ASTNode:15
   */
  public DoubleType(Modifiers p0, String p1, Opt<Access> p2, List<BodyDecl> p3) {
    setChild(p0, 0);
    setID(p1);
    setChild(p2, 1);
    setChild(p3, 2);
  }
  /**
   * @declaredat ASTNode:21
   */
  public DoubleType(Modifiers p0, beaver.Symbol p1, Opt<Access> p2, List<BodyDecl> p3) {
    setChild(p0, 0);
    setID(p1);
    setChild(p2, 1);
    setChild(p3, 2);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:30
   */
  protected int numChildren() {
    return 3;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:36
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:42
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    boxed_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:49
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:55
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:61
   */
  public DoubleType clone() throws CloneNotSupportedException {
    DoubleType node = (DoubleType) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:68
   */
  public DoubleType copy() {
    try {
      DoubleType node = (DoubleType) clone();
      node.parent = null;
      if(children != null) {
        node.children = (ASTNode[]) children.clone();
      }
      return node;
    } catch (CloneNotSupportedException e) {
      throw new Error("Error: clone not supported for " + getClass().getName());
    }
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @deprecated Please use emitTreeCopy or emitTreeCopyNoTransform instead
   * @declaredat ASTNode:87
   */
  public DoubleType fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:96
   */
  public DoubleType treeCopyNoTransform() {
    DoubleType tree = (DoubleType) copy();
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        ASTNode child = (ASTNode) children[i];
        if(child != null) {
          child = child.treeCopyNoTransform();
          tree.setChild(child, i);
        }
      }
    }
    return tree;
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The subtree of this node is traversed to trigger rewrites before copy.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:116
   */
  public DoubleType treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:123
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node)  && (tokenString_ID == ((DoubleType)node).tokenString_ID);    
  }
  /**
   * Replaces the Modifiers child.
   * @param node The new node to replace the Modifiers child.
   * @apilevel high-level
   */
  public void setModifiers(Modifiers node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the Modifiers child.
   * @return The current node used as the Modifiers child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Modifiers")
  public Modifiers getModifiers() {
    return (Modifiers) getChild(0);
  }
  /**
   * Retrieves the Modifiers child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Modifiers child.
   * @apilevel low-level
   */
  public Modifiers getModifiersNoTransform() {
    return (Modifiers) getChildNoTransform(0);
  }
  /**
   * Replaces the lexeme ID.
   * @param value The new value for the lexeme ID.
   * @apilevel high-level
   */
  public void setID(String value) {
    tokenString_ID = value;
  }
  /**
   * JastAdd-internal setter for lexeme ID using the Beaver parser.
   * @param symbol Symbol containing the new value for the lexeme ID
   * @apilevel internal
   */
  public void setID(beaver.Symbol symbol) {
    if(symbol.value != null && !(symbol.value instanceof String))
    throw new UnsupportedOperationException("setID is only valid for String lexemes");
    tokenString_ID = (String)symbol.value;
    IDstart = symbol.getStart();
    IDend = symbol.getEnd();
  }
  /**
   * Retrieves the value for the lexeme ID.
   * @return The value for the lexeme ID.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Token(name="ID")
  public String getID() {
    return tokenString_ID != null ? tokenString_ID : "";
  }
  /**
   * Replaces the optional node for the SuperClass child. This is the <code>Opt</code>
   * node containing the child SuperClass, not the actual child!
   * @param opt The new node to be used as the optional node for the SuperClass child.
   * @apilevel low-level
   */
  public void setSuperClassOpt(Opt<Access> opt) {
    setChild(opt, 1);
  }
  /**
   * Replaces the (optional) SuperClass child.
   * @param node The new node to be used as the SuperClass child.
   * @apilevel high-level
   */
  public void setSuperClass(Access node) {
    getSuperClassOpt().setChild(node, 0);
  }
  /**
   * Check whether the optional SuperClass child exists.
   * @return {@code true} if the optional SuperClass child exists, {@code false} if it does not.
   * @apilevel high-level
   */
  public boolean hasSuperClass() {
    return getSuperClassOpt().getNumChild() != 0;
  }
  /**
   * Retrieves the (optional) SuperClass child.
   * @return The SuperClass child, if it exists. Returns {@code null} otherwise.
   * @apilevel low-level
   */
  public Access getSuperClass() {
    return (Access) getSuperClassOpt().getChild(0);
  }
  /**
   * Retrieves the optional node for the SuperClass child. This is the <code>Opt</code> node containing the child SuperClass, not the actual child!
   * @return The optional node for child the SuperClass child.
   * @apilevel low-level
   */
  @ASTNodeAnnotation.OptChild(name="SuperClass")
  public Opt<Access> getSuperClassOpt() {
    return (Opt<Access>) getChild(1);
  }
  /**
   * Retrieves the optional node for child SuperClass. This is the <code>Opt</code> node containing the child SuperClass, not the actual child!
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The optional node for child SuperClass.
   * @apilevel low-level
   */
  public Opt<Access> getSuperClassOptNoTransform() {
    return (Opt<Access>) getChildNoTransform(1);
  }
  /**
   * Replaces the BodyDecl list.
   * @param list The new list node to be used as the BodyDecl list.
   * @apilevel high-level
   */
  public void setBodyDeclList(List<BodyDecl> list) {
    setChild(list, 2);
  }
  /**
   * Retrieves the number of children in the BodyDecl list.
   * @return Number of children in the BodyDecl list.
   * @apilevel high-level
   */
  public int getNumBodyDecl() {
    return getBodyDeclList().getNumChild();
  }
  /**
   * Retrieves the number of children in the BodyDecl list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the BodyDecl list.
   * @apilevel low-level
   */
  public int getNumBodyDeclNoTransform() {
    return getBodyDeclListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the BodyDecl list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the BodyDecl list.
   * @apilevel high-level
   */
  public BodyDecl getBodyDecl(int i) {
    return (BodyDecl) getBodyDeclList().getChild(i);
  }
  /**
   * Check whether the BodyDecl list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasBodyDecl() {
    return getBodyDeclList().getNumChild() != 0;
  }
  /**
   * Append an element to the BodyDecl list.
   * @param node The element to append to the BodyDecl list.
   * @apilevel high-level
   */
  public void addBodyDecl(BodyDecl node) {
    List<BodyDecl> list = (parent == null || state == null) ? getBodyDeclListNoTransform() : getBodyDeclList();
    list.addChild(node);
  }
  /**
   * @apilevel low-level
   */
  public void addBodyDeclNoTransform(BodyDecl node) {
    List<BodyDecl> list = getBodyDeclListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the BodyDecl list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setBodyDecl(BodyDecl node, int i) {
    List<BodyDecl> list = getBodyDeclList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the BodyDecl list.
   * @return The node representing the BodyDecl list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="BodyDecl")
  public List<BodyDecl> getBodyDeclList() {
    List<BodyDecl> list = (List<BodyDecl>) getChild(2);
    list.getNumChild();
    return list;
  }
  /**
   * Retrieves the BodyDecl list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the BodyDecl list.
   * @apilevel low-level
   */
  public List<BodyDecl> getBodyDeclListNoTransform() {
    return (List<BodyDecl>) getChildNoTransform(2);
  }
  /**
   * Retrieves the BodyDecl list.
   * @return The node representing the BodyDecl list.
   * @apilevel high-level
   */
  public List<BodyDecl> getBodyDecls() {
    return getBodyDeclList();
  }
  /**
   * Retrieves the BodyDecl list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the BodyDecl list.
   * @apilevel low-level
   */
  public List<BodyDecl> getBodyDeclsNoTransform() {
    return getBodyDeclListNoTransform();
  }
  @ASTNodeAnnotation.Attribute
  public Constant cast(Constant c) {
    ASTNode$State state = state();
    Constant cast_Constant_value = Constant.create(c.doubleValue());

    return cast_Constant_value;
  }
  @ASTNodeAnnotation.Attribute
  public Constant plus(Constant c) {
    ASTNode$State state = state();
    Constant plus_Constant_value = c;

    return plus_Constant_value;
  }
  @ASTNodeAnnotation.Attribute
  public Constant minus(Constant c) {
    ASTNode$State state = state();
    Constant minus_Constant_value = Constant.create(-c.doubleValue());

    return minus_Constant_value;
  }
  @ASTNodeAnnotation.Attribute
  public Constant mul(Constant c1, Constant c2) {
    ASTNode$State state = state();
    Constant mul_Constant_Constant_value = Constant.create(c1.doubleValue() * c2.doubleValue());

    return mul_Constant_Constant_value;
  }
  @ASTNodeAnnotation.Attribute
  public Constant div(Constant c1, Constant c2) {
    ASTNode$State state = state();
    Constant div_Constant_Constant_value = Constant.create(c1.doubleValue() / c2.doubleValue());

    return div_Constant_Constant_value;
  }
  @ASTNodeAnnotation.Attribute
  public Constant mod(Constant c1, Constant c2) {
    ASTNode$State state = state();
    Constant mod_Constant_Constant_value = Constant.create(c1.doubleValue() % c2.doubleValue());

    return mod_Constant_Constant_value;
  }
  @ASTNodeAnnotation.Attribute
  public Constant add(Constant c1, Constant c2) {
    ASTNode$State state = state();
    Constant add_Constant_Constant_value = Constant.create(c1.doubleValue() + c2.doubleValue());

    return add_Constant_Constant_value;
  }
  @ASTNodeAnnotation.Attribute
  public Constant sub(Constant c1, Constant c2) {
    ASTNode$State state = state();
    Constant sub_Constant_Constant_value = Constant.create(c1.doubleValue() - c2.doubleValue());

    return sub_Constant_Constant_value;
  }
  @ASTNodeAnnotation.Attribute
  public Constant questionColon(Constant cond, Constant c1, Constant c2) {
    ASTNode$State state = state();
    Constant questionColon_Constant_Constant_Constant_value = Constant.create(cond.booleanValue() ? c1.doubleValue() : c2.doubleValue());

    return questionColon_Constant_Constant_Constant_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean eqIsTrue(Expr left, Expr right) {
    ASTNode$State state = state();
    boolean eqIsTrue_Expr_Expr_value = left.constant().doubleValue() == right.constant().doubleValue();

    return eqIsTrue_Expr_Expr_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean ltIsTrue(Expr left, Expr right) {
    ASTNode$State state = state();
    boolean ltIsTrue_Expr_Expr_value = left.constant().doubleValue() < right.constant().doubleValue();

    return ltIsTrue_Expr_Expr_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean leIsTrue(Expr left, Expr right) {
    ASTNode$State state = state();
    boolean leIsTrue_Expr_Expr_value = left.constant().doubleValue() <= right.constant().doubleValue();

    return leIsTrue_Expr_Expr_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isDouble() {
    ASTNode$State state = state();
    boolean isDouble_value = true;

    return isDouble_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean boxed_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl boxed_value;
/**
 * @apilevel internal
 */
private void boxed_reset() {
  boxed_computed = false;
  boxed_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl boxed() {
    if(boxed_computed) {
      return boxed_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boxed_value = lookupType("java.lang", "Double");
    if (isFinal && num == state().boundariesCrossed) {
      boxed_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return boxed_value;
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
