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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/grammar/Java.ast:34
 * @production AmbiguousAccess : {@link Access} ::= <span class="component">&lt;ID:String&gt;</span>;

 */
public class AmbiguousAccess extends Access implements Cloneable {
  /**
   * @aspect NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:79
   */
  public void nameCheck() {
    error("ambiguous name " + name());
  }
  /**
   * @aspect NodeConstructors
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NodeConstructors.jrag:33
   */
  public AmbiguousAccess(String name, int start, int end) {
    this(name);
    this.start = this.IDstart = start;
    this.end = this.IDend = end;
  }
  /**
   * @declaredat ASTNode:1
   */
  public AmbiguousAccess() {
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
  }
  /**
   * @declaredat ASTNode:12
   */
  public AmbiguousAccess(String p0) {
    setID(p0);
  }
  /**
   * @declaredat ASTNode:15
   */
  public AmbiguousAccess(beaver.Symbol p0) {
    setID(p0);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:21
   */
  protected int numChildren() {
    return 0;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:27
   */
  public boolean mayHaveRewrite() {
    return true;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:33
   */
  public void flushAttrCache() {
    super.flushAttrCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:39
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:45
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:51
   */
  public AmbiguousAccess clone() throws CloneNotSupportedException {
    AmbiguousAccess node = (AmbiguousAccess) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:58
   */
  public AmbiguousAccess copy() {
    try {
      AmbiguousAccess node = (AmbiguousAccess) clone();
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
   * @declaredat ASTNode:77
   */
  public AmbiguousAccess fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:86
   */
  public AmbiguousAccess treeCopyNoTransform() {
    AmbiguousAccess tree = (AmbiguousAccess) copy();
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
   * @declaredat ASTNode:106
   */
  public AmbiguousAccess treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:113
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node)  && (tokenString_ID == ((AmbiguousAccess)node).tokenString_ID);    
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
   * @apilevel internal
   */
  protected String tokenString_ID;
  /**
   */
  public int IDstart;
  /**
   */
  public int IDend;
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
  @ASTNodeAnnotation.Attribute
  public String dumpString() {
    ASTNode$State state = state();
    String dumpString_value = getClass().getName() + " [" + getID() + "]";

    return dumpString_value;
  }
  @ASTNodeAnnotation.Attribute
  public SimpleSet qualifiedLookupType(String name) {
    ASTNode$State state = state();
    SimpleSet qualifiedLookupType_String_value = SimpleSet.emptySet;

    return qualifiedLookupType_String_value;
  }
  @ASTNodeAnnotation.Attribute
  public SimpleSet qualifiedLookupVariable(String name) {
    ASTNode$State state = state();
    SimpleSet qualifiedLookupVariable_String_value = SimpleSet.emptySet;

    return qualifiedLookupVariable_String_value;
  }
  @ASTNodeAnnotation.Attribute
  public String name() {
    ASTNode$State state = state();
    String name_value = getID();

    return name_value;
  }
  @ASTNodeAnnotation.Attribute
  public NameType predNameType() {
    ASTNode$State state = state();
    NameType predNameType_value = NameType.AMBIGUOUS_NAME;

    return predNameType_value;
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    // Declared at @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ResolveAmbiguousNames.jrag:194
    if (!duringSyntacticClassification()) {
      state().duringNameResolution++;
      ASTNode result = rewriteRule0();
      state().duringNameResolution--;
      return result;
    }    return super.rewriteTo();
  }  /**
   * @declaredat @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ResolveAmbiguousNames.jrag:194
   * @apilevel internal
   */  private Access rewriteRule0() {
{
      if(!lookupVariable(name()).isEmpty()) {
        return new VarAccess(name(), start(), end());
      }
      else if(!lookupType(name()).isEmpty()) {
        return new TypeAccess(name(), start(), end());
      }
      else {
        return new PackageAccess(name(), start(), end());
      }
    }  }
}
