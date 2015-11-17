package jastaddad.filteredtree;

import configAST.Color;
import configAST.Str;
import configAST.Value;
import jastaddad.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * The GenericTreeNode is the class that represents the nodes in the filteredTree, i.e. the tree after
 * it has been filtered by the configurations.
 * The node itself contains references to its parent and children and which cluster it is part of, if any.
 * Created by gda10jli on 11/2/15.
 */
public abstract class GenericTreeNode {
    protected List<GenericTreeNode> children;
    protected boolean referenceHighlight;
    protected GenericTreeNode clusterRef;
    protected GenericTreeNode parent;
    protected HashMap<String, Value> styles;
    private boolean isExpandable;

    public GenericTreeNode(GenericTreeNode parent){
        this.parent = parent;
        children = new ArrayList();
        styles = new HashMap<>();
        isExpandable = false;
        styles.put("node-color", new Color("\"\""));
        styles.put("node-shape", new Str("\"\""));
        styles.put("border-style", new Str("\"\""));
    }

    public GenericTreeNode getParent(){return parent;}

    /**
     * Set so the node can be manipulated by someone else than the configurations, and added to a cluster.
     * @param expandable
     */
    public void setExpandable(boolean expandable){ isExpandable = expandable;}

    /**
     * Check if the node can be manipulated by someone else than the configurations, and added to a cluster.
     */
    public boolean isExpandable(){return isExpandable;}

    public List<GenericTreeNode> getChildren(){ return children; }

    public void addChild(GenericTreeNode child) {
        children.add(child);
    }

    /**
     * Sets a reference flag, someone has a reference to This node.
     * @param highlight
     * @return
     */
    public GenericTreeNode setReferenceHighlight(boolean highlight){
        referenceHighlight = highlight;
        if(clusterRef != null){
            return clusterRef.setReferenceHighlight(highlight);
        }
        return this;
    }

    /**
     * Check if the a reference flag is set
     */
    public boolean isReferenceHighlight(){ return referenceHighlight; }

    /**
     * Check if the node is a "real" node
     * @return
     */
    public abstract boolean isNode();

    /**
     * Check if the node is a cluster of nodes.
     * @return
     */
    public abstract boolean isCluster();

    /**
     * Check if the node is a cluster of clusters
     * @return
     */
    public abstract boolean isClusterParent();

    /**
     * Checks if the child is a "real" node.
     * @param child
     * @return
     */
    public abstract boolean isRealChild(GenericTreeNode child);

    /**
     * Creates the a string containing the nodes name and the displayed attributes
     * @return
     */
    public abstract String toGraphString();

    /**
     * Sets the style of the nodes
     * @return
     */
    public abstract void setStyles(Config filter);

    /**
     * Returns a list of the node references pointing inwards
     * @return
     */
    public abstract HashMap<NodeReference, NodeReference> getInwardNodeReferences();

    /**
     * Returns a list of the node references pointing outwards
     * @return
     */
    public abstract HashSet<NodeReference> getOutwardNodeReferences();

    /**
     * Returns a list containing all node references, outwards and inwards.
     * @return
     */
    public abstract HashSet<NodeReference> getAllNodeReferences();

    public abstract boolean addInWardNodeReference(NodeReference ref);

    public HashMap<String, Value> getStyles(){ return styles; }

    /**
     * Returns the top cluster the node is a part of, if it a "real" node it will return itself
     * @return
     */
    public GenericTreeNode getClusterNode(){
        if(clusterRef == null)
            return this;
        return clusterRef != null ? clusterRef.getClusterNode() : clusterRef;
    }

    /**
     * Sets a reference to which cluster the node is gonna be a part of.
     * @param clusterRef
     */
    public void setClusterReference(GenericTreeNode clusterRef){ this.clusterRef = clusterRef; }

}