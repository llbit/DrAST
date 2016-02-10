package drast.views.gui.graph.jungcomponents;

import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;

import java.awt.geom.Point2D;

/**
 * Scaling class for the zoom, has a limit in how much it is allowed to scale down the view and layout
 */
public class ScalingControllerMinLimit extends CrossoverScalingControl {
    private final double scaleLimit;

    public ScalingControllerMinLimit(double scaleLimit){
        super();
        this.scaleLimit = scaleLimit;
    }
    public ScalingControllerMinLimit(){
        super();
        this.scaleLimit = 0.025;
    }

    @Override
    public void scale(VisualizationServer<?,?> vv, float amount, Point2D at) {
        MutableTransformer layoutTransformer = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
        MutableTransformer viewTransformer = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW);
        double modelScale = layoutTransformer.getScale();
        double modelScaleX = layoutTransformer.getScaleX();
        double modelScaleY = layoutTransformer.getScaleY();

        double viewScale = viewTransformer.getScale();
        double viewScaleX = viewTransformer.getScaleX();
        double viewScaleY = viewTransformer.getScaleY();
        System.out.println(viewScaleX + " " + viewScaleY + " " + viewScale);
        double scale = modelScale * viewScale;

        if(viewScale < scaleLimit && amount < 1)
            return;

        double inverseModelScale = Math.sqrt(crossover)/modelScale;
        double inverseViewScale = Math.sqrt(crossover)/viewScale;

        Point2D transformedAt = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(Layer.VIEW, at);
        if((scale*amount - crossover)*(scale*amount - crossover) < 0.001) {
            // close to the control point, return both transformers to a scale of sqrt crossover value
            layoutTransformer.scale(inverseModelScale, inverseModelScale, transformedAt);
            viewTransformer.scale(inverseViewScale, inverseViewScale, at);


        } else if(scale*amount < crossover) {
            // scale the viewTransformer, return the layoutTransformer to sqrt crossover value
            viewTransformer.scale(amount, amount, at);
            layoutTransformer.scale(inverseModelScale, inverseModelScale, transformedAt);

        } else {
            // scale the layoutTransformer, return the viewTransformer to crossover value
            layoutTransformer.scale(amount, amount, transformedAt);
            viewTransformer.scale(inverseViewScale, inverseViewScale, at);

        }
        double newModelScale = layoutTransformer.getScale();
        double newViewScale = viewTransformer.getScale();
        scale = newModelScale * newViewScale;
/*
        if(viewTransformer.getScale() < scaleLimit){
            viewTransformer.setScale(scaleLimit, scaleLimit, at);
        }
*/
        vv.repaint();
    }

    private double getScale(MutableTransformer layoutTransformer, MutableTransformer viewTransformer){
        double modelScale = layoutTransformer.getScale();
        double viewScale = viewTransformer.getScale();
        return modelScale * viewScale;
    }
}