package uicomponent.graph;

import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import jastaddad.ASTAPI;
import jastaddad.FilteredTreeNode;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import org.apache.commons.collections15.Transformer;
import uicomponent.UIMonitor;
import uicomponent.controllers.Controller;

import javax.swing.text.Position;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.RoundRectangle2D;

/**
 * Created by gda10jli on 10/15/15.
 */
public class GraphView extends SwingNode {

    private int id;
    private UIMonitor mon;
    private Controller con;
    private VisualizationViewer vs;

    public GraphView(UIMonitor mon, Controller con){
        this.id = 0;
        this.mon = mon;
        this.con = con;
        Forest<FilteredTreeNode, String> g = new DelegateForest<>();
        createTree(g, mon.getRootNode());
        createLayout(g);
        setListeners();
        setContent(vs);
    }

    private void createTree(Forest<FilteredTreeNode, String> g, FilteredTreeNode parent){
        for (FilteredTreeNode child : parent.getChildren()) {
            g.addEdge("jup" + id, parent, child);
            id++;
            createTree(g, child);
        }
    }

    public void createLayout(Forest<FilteredTreeNode, String> g ){//Creates UI specific stuff

        TreeLayout<FilteredTreeNode, String> layout = new TreeLayout<>(g, 150, 100);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Transformer <FilteredTreeNode, Shape> vertexShape = fNode -> {
            //CompositeShape shape = new CompositeShape();
            if(fNode.node.isList())
                return new RoundRectangle2D.Double(-50, -20, 130, 40,10,10);
            return new RoundRectangle2D.Double(-50, -20, 130, 40,40,40);
        };

        ScalingControl visualizationViewerScalingControl = new CrossoverScalingControl();

        vs = new VisualizationViewer<>(layout, screenSize);
        vs.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        vs.scaleToLayout(visualizationViewerScalingControl);

        vs.getRenderContext().setVertexFillPaintTransformer(new VertexPaintTransformer(vs.getPickedVertexState()));
        vs.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<FilteredTreeNode, String>());
        vs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vs.getRenderContext().setVertexShapeTransformer(vertexShape);
    }
    public void setListeners(){//Sets UI listeners of the graph

        final PickedState<String> pickedState = vs.getPickedVertexState();
        pickedState.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        con.itemStateChanged(e);
                    }
                });
            }
        });

        DefaultModalGraphMouse<FilteredTreeNode, String> picker = new DefaultModalGraphMouse<>();
        picker.setMode(ModalGraphMouse.Mode.PICKING);
        vs.setGraphMouse(picker);
    }

    private static class VertexPaintTransformer implements Transformer<FilteredTreeNode,Paint> {
        private final PickedInfo<FilteredTreeNode> pi;
        VertexPaintTransformer ( PickedInfo<FilteredTreeNode> pi ) {
            super();
            if (pi == null)
                throw new IllegalArgumentException("PickedInfo instance must be non-null");
            this.pi = pi;
        }
        @Override
        public Paint transform(FilteredTreeNode fNode) {
            if(pi.isPicked(fNode))
                return new Color(240, 240, 240);
            if(fNode.node.isList()) return new Color(200, 240, 230);
            return new Color(200, 200, 200);
        }
    }

}
