package drast.views.gui.graph.jungextensions;

import drast.views.gui.graph.GraphView;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;

import java.awt.geom.Point2D;

/**
 * Scaling class for the zoom, has a limit in how much it is allowed to scale down the view and layout
 */
public class ScalingControllerMinLimit extends CrossoverScalingControl {
  private double scrollZoomAmountThreshold;
  private final double scaleLimit;
  private double scaleLimitZoom;
  private boolean maxedOutZoom;
  private GraphView graphView;


  public ScalingControllerMinLimit(GraphView graphView) {
    super();
    this.scaleLimit = 0.025;
    init(graphView);
  }

  private void init(GraphView graphView) {
    this.graphView = graphView;
    this.scaleLimitZoom = scaleLimit + 0.003;
    maxedOutZoom = false;
    scrollZoomAmountThreshold = 0.5;
  }

  public void resetScaler() {
    maxedOutZoom = false;
  }

  @Override public void scale(VisualizationServer<?, ?> vv, float amount, Point2D at) {
    MutableTransformer layoutTransformer =
        vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
    MutableTransformer viewTransformer =
        vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW);

    double modelScale = layoutTransformer.getScale();
    double viewScale = viewTransformer.getScale();
    double scale = modelScale * viewScale;

    if (amount < 1 && (maxedOutZoom || (amount > scrollZoomAmountThreshold
        && viewScale < scaleLimitZoom))) {
      return;
    }

    maxedOutZoom = false;

    double inverseModelScale = Math.sqrt(crossover) / modelScale;
    double inverseViewScale = Math.sqrt(crossover) / viewScale;

    Point2D transformedAt =
        vv.getRenderContext().getMultiLayerTransformer().inverseTransform(Layer.VIEW, at);
    if ((scale * amount - crossover) * (scale * amount - crossover) < 0.001) {
      // close to the control point, return both transformers to a scale of sqrt crossover value
      layoutTransformer.scale(inverseModelScale, inverseModelScale, transformedAt);
      viewTransformer.scale(inverseViewScale, inverseViewScale, at);


    } else if (scale * amount < crossover) {
      // scale the viewTransformer, return the layoutTransformer to sqrt crossover value
      viewTransformer.scale(amount, amount, at);
      layoutTransformer.scale(inverseModelScale, inverseModelScale, transformedAt);

    } else {
      // scale the layoutTransformer, return the viewTransformer to crossover value
      layoutTransformer.scale(amount, amount, transformedAt);
      viewTransformer.scale(inverseViewScale, inverseViewScale, at);

    }

    if (viewTransformer.getScale() < scaleLimit) {
      maxedOutZoom = true;
      viewTransformer.setScale(scaleLimit, scaleLimit, at);
      if (amount < scrollZoomAmountThreshold) {
        graphView.setHugeGraph();
        graphView.showWholeGraphOnScreen();
      }
    }

    vv.repaint();
  }
}
