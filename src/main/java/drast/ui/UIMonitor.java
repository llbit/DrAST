package drast.ui;

import drast.api.ASTAPI;
import drast.api.DrASTAPI;
import drast.api.filteredtree.GenericTreeNode;
import drast.ui.controllers.Controller;
import drast.ui.dialogs.UIDialog;
import drast.ui.graph.GraphView;
import drast.ui.graph.UIEdge;
import drast.ui.uicomponent.nodeinfotreetableview.NodeInfoView;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Monitor class for the UI.
 * It keeps track of the controllers, the GraphView and the ASTAPI etc.
 */
public class UIMonitor {
    private GenericTreeNode lastRealNode;
    private GenericTreeNode selectedNode;
    private NodeInfoView selectedInfo;
    private ArrayList<UIEdge> refEdges;
    private HashMap<GenericTreeNode,ArrayList<UIEdge>> displayedRefEdges;
    private Controller controller;
    private GraphView graphView;
    private Stage parentStage;
    private ArrayList<UIDialog> subWindows;
    private ArrayList<GenericTreeNode> dialogSelectedNodes;
    private ArrayList<GenericTreeNode> selectedParameterNodes;
    private boolean functionRunning;
    private ArrayList<String> highlightedSimpleClassNames;
    private DrASTAPI jaaAPI;
    private DrASTUI jaaUI;
    private Stage stage;
    private String defaultDirectory;
    private Config config;
    private boolean rerunable;

    public UIMonitor(){
        clean(null);
    }

    public UIMonitor(DrASTAPI jaaAPI){
        clean(jaaAPI);
    }

    public void clean(DrASTAPI jaaAPI) {
        this.jaaAPI = jaaAPI;
        subWindows = new ArrayList<>();
        dialogSelectedNodes = new ArrayList<>();
        selectedParameterNodes = new ArrayList<>();
        highlightedSimpleClassNames = new ArrayList<>();
        functionRunning = false;
        selectedNode = null;
        lastRealNode = null;
        selectedInfo = null;
        String tmp = new File(".").getAbsolutePath();
        defaultDirectory = tmp.substring(0,tmp.length()-1);
        config = new Config(defaultDirectory);
        rerunable = false;
    }

    public void setDrASTUI(DrASTUI jaaUI){ this.jaaUI = jaaUI;}
    public DrASTUI getDrASTUI(){return jaaUI;}

    public void setStage(Stage stage){ this.stage = stage;}
    public Stage getStage(){return stage;}

    public void functionStart(){functionRunning = true;}
    public void functionDone(){functionRunning = false;}
    public boolean isFunctionRunning(){return functionRunning;}

    public ArrayList<UIDialog> getSubWindows() {
        return subWindows;
    }
    public void addSubWindow(UIDialog window){ subWindows.add(window); }
    public void removeSubWindow(UIDialog window){ subWindows.remove(window); }

    public ArrayList<String> gethighlightedSimpleClassNames() {
        return highlightedSimpleClassNames;
    }
    public void addhighlightedSimpleClassName(String className){ highlightedSimpleClassNames.add(className); }
    public void removehighlightedSimpleClassName(String className){ highlightedSimpleClassNames.remove(className); }

    public ArrayList<GenericTreeNode> getSelectedParameterNodes() {
        return selectedParameterNodes;
    }
    public void addSelectedParameterNodes(GenericTreeNode node){
        selectedParameterNodes.add(node);
    }

    public void clearSelectedParameterNodes(){ selectedParameterNodes.clear();}

    public ArrayList<GenericTreeNode> getDialogSelectedNodes() {
        return dialogSelectedNodes;
    }
    public void addDialogSelectedNodes(GenericTreeNode node){
        dialogSelectedNodes.add(node);
    }

    public void removeDialogSelectedNodes(GenericTreeNode node){
        if(node != null)
            dialogSelectedNodes.remove(node);
    }

    public void clearDialogSelectedNodes(){ dialogSelectedNodes.clear();}

    public ASTAPI getApi(){ return jaaAPI.api(); }
    public DrASTAPI getDrASTAPI(){ return jaaAPI; }

    public GenericTreeNode getRootNode(){ return jaaAPI.getFilteredTree(); }
    public Controller getController(){return controller;}

    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Sets the node to last selected node.
     * If it is a "real" node it will also set the realnode. Otherwise that will be null, or keep its value
     * @param node
     */
    public void setSelectedNode(GenericTreeNode node){
        selectedNode = node;
        if(node!= null && node.isNode())
            lastRealNode = node;
        if(node == null)
            lastRealNode = null;
    }

    public Stage getParentStage(){return parentStage;}
    public void setParentStage(Stage stage){parentStage = stage;}

    public NodeInfoView getSelectedInfo(){ return selectedInfo;}

    public void setSelectedInfo(NodeInfoView info){ this.selectedInfo = info; }

    public GenericTreeNode getSelectedNode(){ return selectedNode;}

    /**
     * The last non cluster node that was selected
     * @return
     */
    public GenericTreeNode getLastRealNode(){ return lastRealNode;}

    public void setReferenceEdges(ArrayList<UIEdge> edges){ this.refEdges = edges; }

    public ArrayList<UIEdge> getReferenceEdges(){ return refEdges; }

    public void setDisplayedReferenceEdges(HashMap<GenericTreeNode,ArrayList<UIEdge>>  edges){ this.displayedRefEdges = edges; }

    public HashMap<GenericTreeNode,ArrayList<UIEdge>> getDisplayedReferenceEdges(){ return displayedRefEdges; }

    public void setGraphView(GraphView graphView) {
        this.graphView = graphView;
    }
    public GraphView getGraphView(){return graphView;}

    public String getDefaultDirectory() { return defaultDirectory; }

    public void setDefaultDirectory(String defaultDirectory) { this.defaultDirectory = defaultDirectory; }

    public Config getConfig() {
        return config;
    }

    public boolean isRerunable() {
        return rerunable;
    }

    public void setRerunable(boolean rerunable) {
        this.rerunable = rerunable;
    }
}