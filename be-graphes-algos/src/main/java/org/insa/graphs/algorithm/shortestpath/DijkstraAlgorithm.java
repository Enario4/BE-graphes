package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.algorithm.utils.*;
import org.insa.graphs.model.*;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }
   
    
    protected void insert(BinaryHeap<Label> tas, Node sommet_courant, boolean marque, float cout, Arc arc_precedent, ShortestPathData data) {
    	Label label;
		label = new Label(sommet_courant, marque, cout, arc_precedent);
		tas.insert(label);
		Label.label_tab[sommet_courant.getId()] = label;
		notifyNodeReached(label.getNode());
    }
    
    

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        boolean fini = false;
        Graph graph = data.getGraph();
        Node dest = data.getDestination();
        
        // Tas de Labels
		BinaryHeap<Label> tas = new BinaryHeap<Label>();
		
				
		// Initialisation 
		
		/*for (Node node : graph.getNodes()) {
			labels_tab[node.getId()] = new Label (node);
		}
		Label first = new Label(data.getOrigin());
		labels_tab[first.getNode().getId()] = first;
		tas.insert(first);
		first.setCost(0);
		notifyOriginProcessed(data.getOrigin());*/
		
		Label.label_tab = new Label[graph.getNodes().size()];
		Node origine = data.getOrigin();
		insert (tas, origine, true, (float)0.0, null, data);
		
		// Itérations 
		
  		while (!fini && !tas.isEmpty()) {
	        Label x = tas.deleteMin();
	        x.setMark();
	        notifyNodeMarked(x.getNode());
	        /*float cost = x.getCost();
	        System.out.println(cost);*/
	        
	        //Si c'est la destination, on s'arrête
	        if (x.getNode().compareTo(dest) == 0) {
	        	fini = true;
	        }
	        
	        //Sinon on parcourt les successeurs
	        for (Arc arc : x.getNode().getSuccessors()) {
	        	
	        	/*int nbr_succ = labels_tab[x.getNode().getId()].getNode().getNumberOfSuccessors();
	        	System.out.println(nbr_succ);*/
	        	
	        	
	        	//Vérifie si l'arc est accessible (dépend du mode de circulation)
	        	if (!data.isAllowed(arc)) {
					continue;
				}
	        	

        		float c =0;
        		if (data.getMode() == Mode.TIME) {
        			c = (float)arc.getMinimumTravelTime();
        		} else {
        			c = arc.getLength();
        		}
        		
	        	//notifyNodeReached(arc.getDestination());
	        	if (Label.label_tab[arc.getDestination().getId()] == null) {
	        		insert(tas, arc.getDestination(), false, (float)(x.getCost() + c), arc, data);
	        	} else {
	        	//Si le sommet n'est pas marqué :
		        	if (!(Label.label_tab[arc.getDestination().getId()].isMarked())) {
		        		
			        	
		        		//Si on obtient un meilleur coût
			        	if (Label.label_tab[arc.getDestination().getId()].getCost() > Label.label_tab[x.getNode().getId()].getCost() + c) {
			        		//Si deja dans le tas 
			       			if (Label.label_tab[arc.getDestination().getId()].getCost() != Float.MAX_VALUE) {
			       				tas.remove(Label.label_tab[arc.getDestination().getId()]);
			       			}
	
			        	//Met à jour le coût et ajoute le label au tas
			        		Label.label_tab[arc.getDestination().getId()].setCost(Label.label_tab[x.getNode().getId()].getCost()+ c);
			       			tas.insert(Label.label_tab[arc.getDestination().getId()]);
			       			Label.label_tab[arc.getDestination().getId()].setArcPrec(arc);
			       		}
		       		}
	        	}
	        }
	        // Destination has no predecessor, the solution is infeasible...
	        if (Label.label_tab[data.getDestination().getId()] == null) {
	            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
	        }
	        else {
	        	
	            // The destination has been found, notify the observers.
	            notifyDestinationReached(data.getDestination());
	            // Create the path from the array of predecessors...
	            ArrayList<Arc> arcs = new ArrayList<>();
	            Arc arc = Label.label_tab[data.getDestination().getId()].getArcPrec();
	            while (arc != null) {
	                arcs.add(arc);
	                arc = Label.label_tab[arc.getOrigin().getId()].getArcPrec();
	            }

	            // Reverse the path...
	            Collections.reverse(arcs);

	            // Create the final solution.
	            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
	        }
  		}
        
        
        
        return solution;
    }

}
