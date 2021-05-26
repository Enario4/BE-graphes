package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.algorithm.utils.*;
import org.insa.graphs.model.*;


public class AStarAlgorithm extends DijkstraAlgorithm {
	
	public AStarAlgorithm(ShortestPathData data) {
		super(data);
	}
	
	@Override
	protected void insert(BinaryHeap<Label> BH, Node n, boolean marked, float Cout, Arc Father, ShortestPathData data) {
		Label lab;
		double cout_estim;
		
		if (data.getMode()==Mode.LENGTH) 
		{
			cout_estim= n.getPoint().distanceTo(data.getDestination().getPoint());
		}
		else 
		{
			float max_speed = (float) Math.max(data.getMaximumSpeed(), data.getGraph().getGraphInformation().getMaximumSpeed())  ;
			//cout_estim=(float)n.getPoint().distanceTo(data.getDestination().getPoint())/max_speed;
			cout_estim = (Point.distance(data.getDestination().getPoint(),n.getPoint())*3600.0) / ((double)(max_speed)*1000.0);
		}
		lab = new LabelStar(n,marked,Cout,Father,(float) cout_estim);
		BH.insert(lab);
		Label.label_tab[n.getId()]=lab;
		notifyNodeReached(lab.getNode());
	}
	
	
	/*Node destination;
    AbstractInputData.Mode mode;
    int VitesseMax;

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
        this.destination = data.getDestination(); 
        this.mode = data.getMode();
        this.VitesseMax = data.getMaximumSpeed();
        
    }
   
    
    @Override
    public LabelStar NewLabel(Node sommet_courant, boolean marque, float cout, Arc arc_precedent) {
    	
    	double estimate_value;
    	if (mode == AbstractInputData.Mode.LENGTH){
            estimate_value = Point.distance(this.destination.getPoint(),sommet_courant.getPoint());
        } else {
        	estimate_value = ((Point.distance(this.destination.getPoint(),sommet_courant.getPoint())*3600.0) / ((double)(VitesseMax)*1000.0));
        }
        
        return new LabelStar(sommet_courant, marque, cout, arc_precedent, estimate_value);
    	
    	
    }*/

}
