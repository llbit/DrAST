package drast.api.filteredtree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class for cluster of clusters
 * Created by gda10jth on 10/21/15.
 */
public class TreeClusterParent extends GenericTreeCluster {

    private List<TreeCluster> clusters; //The clusters the cluster contains
    public TreeClusterParent(GenericTreeNode parent){
        super(parent);
        clusters = new ArrayList<>();
    }

    @Override
    public boolean isCluster() {
        return false;
    }

    @Override
    public boolean isClusterParent() {
        return true;
    }

    public void addCluster(TreeCluster cluster){
        clusters.add(cluster);
        cluster.clusterRef = this;
        nodeCount += cluster.getNodeCount();
        for(Map.Entry<String, Integer> e : cluster.typeList.entrySet())
            typeList.put(e.getKey(),typeList.containsKey(e.getKey()) ? typeList.get(e.getKey()) + e.getValue() : e.getValue());
    }

    public List<TreeCluster> getClusters(){
        return clusters;
    }


}