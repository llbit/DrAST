package jastaddad.ui;

import jastaddad.api.filteredtree.GenericTreeNode;
import jastaddad.api.filteredtree.TreeNode;
import jastaddad.api.nodeinfo.NodeInfo;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Temporary class for the inputdialog, used when specifying the values for method that will be computed.
 * ATM we can only use primitive types, and they will be cast to string.
 * The values will be caught by the AttributeTabController
 */
public class AttributeInputDialog extends UIDialog { //Todo redesign this dialog
    private NodeInfo info;
    private TreeNode node;
    private Method m;
    int[] gridNodePosition;
    private nodeParameter focusedNodeParameter;
    private ArrayList<nodeParameter> nodeParameters;
    ArrayList<Object> params;

    public AttributeInputDialog(NodeInfo attribute, TreeNode node, UIMonitor mon){
        super(mon);
        initModality(Modality.NONE);
        this.info = attribute;
        this.node = node;
        nodeParameters = new ArrayList<>();

        if(!attribute.isParametrized())
            return;

        setTitle("Choose Inputs");
        //setHeaderText("BEWARE: At the moment it only works with primitive types and String"); //TODO change so that more complex inputs can be added

        m = attribute.getMethod();
        params = new ArrayList();
        for(int i=0; i< m.getParameterCount();i++)
            params.add(null);

    }

    protected Parent buildDialogContent(){
        GridPane grid = new GridPane();
        gridNodePosition = new int[m.getParameterCount()];
        int fieldCounter;
        boolean firstNodeParamFound = false;
        for(int i = 0; i < m.getParameterCount(); i++){
            fieldCounter = 0;
            Class type = m.getParameterTypes()[i];
            grid.add(new Label("Input: " + type + " "), 1, i+1);
            if(type == int.class){
                TextField field = new TextField() {
                    @Override public void replaceText(int start, int end, String text) {
                        if (text.matches("[0-9]*")) {
                            super.replaceText(start, end, text);
                        }
                    }

                    @Override public void replaceSelection(String text) {
                        if (text.matches("[0-9]*")) {
                            super.replaceSelection(text);
                        }
                    }
                };
                fieldCounter = 1;
                grid.add(field, 2, i+1);
            }else if(type == boolean.class){
                final ToggleGroup group = new ToggleGroup();

                RadioButton rb1 = new RadioButton("True");
                rb1.setToggleGroup(group);
                rb1.setSelected(true);

                RadioButton rb2 = new RadioButton("False");
                rb2.setToggleGroup(group);
                fieldCounter = 2;
                grid.add(rb1, 2, i+1  );
                grid.add(rb2, 3, i+1  );

            }else if(isNodeParam(type)) {
                TextField nodeRefField = new TextField();
                nodeRefField.setEditable(false);
                final int paramPos = i;
                nodeParameter param = new nodeParameter(paramPos, type, nodeRefField);
                nodeParameters.add(param);
                nodeRefField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if(newValue){
                        paramTextFieldSelected(param);
                    }
                });
                if(!firstNodeParamFound){
                    firstNodeParamFound = true;
                    paramTextFieldSelected(param);
                }

                fieldCounter = 1;
                grid.add(nodeRefField, 2, i + 1);
            }else {
                fieldCounter = 1;
                grid.add(new TextField(), 2, i + 1);
            }

            if(i != 0)
                gridNodePosition[i] += gridNodePosition[i-1] + 1 + fieldCounter;
            else
                gridNodePosition[i] = fieldCounter;
        }

        buttonTypeOk.setText("Invoke");
        grid.add(buttonTypeOk, 1, gridNodePosition.length+1);
        return grid;
    }

    protected void yesButtonClicked(){

    }

    @Override
    protected void dialogClose() {
        mon.clearDialogSelectedNodes();
    }

    private void paramTextFieldSelected(nodeParameter param){
        if(focusedNodeParameter != null){
            focusedNodeParameter.field.setStyle("-fx-control-inner-background: #ffffff");
        }
        focusedNodeParameter = param;
        focusedNodeParameter.field.setStyle("-fx-control-inner-background: #789456");
    }

    private boolean isNodeParam(Class type){
        return mon.getApi().getTypeHash().containsKey(type.getSimpleName());
    }

    public ArrayList<Object> getResult(){
        if(!invokeButtonPressed)
            return null;
        GridPane grid = (GridPane)getScene().getRoot();
        for (int i = 0; i < m.getParameterCount(); i++) {
            Class type = m.getParameterTypes()[i];
            if(type == boolean.class || type == Boolean.class){
                //System.out.println("row: " + i + " pos: " + gridNodePosition[i]);
                RadioButton rb = (RadioButton)grid.getChildren().get(gridNodePosition[i]);
                rb = (RadioButton)rb.getToggleGroup().getSelectedToggle();
                params.set(i, rb.getText().equals("True"));
            }else if(isNodeParam(type)) {
                // this kind of parameter is set in function nodeSelectedChild(...);
            }else if(type == int.class || type == Integer.class) {
                TextField field = (TextField) grid.getChildren().get(gridNodePosition[i]);
                params.set(i, Integer.parseInt(field.getText()));
            }else {
                //System.out.println("row: " + i + " pos: " + gridNodePosition[i]);
                TextField field = (TextField) grid.getChildren().get(gridNodePosition[i]);
                params.set(i, field.getText());
            }
        }
        
        return params;
    }

    public NodeInfo getInfo(){
        return info;
    }

    public TreeNode getTreeNode(){return node;}
    public boolean invokeButtonPressed(){
        return invokeButtonPressed;
    }

    public void nodeSelectedChild(GenericTreeNode node){
        if(node.isNode() && focusedNodeParameter != null) {
            TreeNode fNode = (TreeNode) node;
            if (focusedNodeParameter.type.getSimpleName().equals(fNode.getNode().simpleNameClass)){
                int index = nodeParameters.indexOf(focusedNodeParameter);
                mon.getController().addMessage("index: " + index);
                if(index >= 0){
                    mon.removeDialogSelectedNodes(nodeParameters.get(index).getNode());
                }
                focusedNodeParameter.setNode(node);
                mon.addDialogSelectedNodes(focusedNodeParameter.getNode());
                params.set(focusedNodeParameter.pos, fNode.getNode().node);
                focusedNodeParameter.field.setText(focusedNodeParameter.type.getCanonicalName());
                //System.out.println("asdasd: " + focusedNodeParameter.toString());
            }
        }
    }


    private class nodeParameter{
        final int pos;
        final Class type;
        final TextField field;
        private GenericTreeNode node;

        public nodeParameter(int pos, Class type, TextField field){
            this.pos = pos;
            this.type = type;
            this.field = field;
            node = null;
        }
        void setNode(GenericTreeNode node){
            this.node = node;
        }
        GenericTreeNode getNode(){
            return node;
        }

    }
}
