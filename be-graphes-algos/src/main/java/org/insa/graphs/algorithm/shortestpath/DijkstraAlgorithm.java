package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.*;
import org.insa.graphs.model.*;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
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
		
		// Tableau de Labels 
		Label labels_tab[] = new Label[graph.size()];
		
		// Initialisation 
		
		for (Node node : graph.getNodes()) {
			labels_tab[node.getId()] = new Label (node);
		}
		Label first = new Label(data.getOrigin());
		labels_tab[first.getNode().getId()] = first;
		tas.insert(first);
		first.setCost(0);
		notifyOriginProcessed(data.getOrigin());
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
	        for (Arc arc : labels_tab[x.getNode().getId()].getNode().getSuccessors()) {
	        	
	        	/*int nbr_succ = labels_tab[x.getNode().getId()].getNode().getNumberOfSuccessors();
	        	System.out.println(nbr_succ);*/
	        	//Vérifie si l'arc est accessible (dépend du mode de circulation)
	        	if (!data.isAllowed(arc)) {
					continue;
				}
	        	notifyNodeReached(arc.getDestination());
	        	//Si le sommet n'est pas marqué :
	        	if (!(labels_tab[arc.getDestination().getId()].isMarked())) {
	        		
	        		//Si on obtient un meilleur coût
	        		if (labels_tab[arc.getDestination().getId()].getCost() > labels_tab[x.getNode().getId()].getCost() + arc.getLength()) {
	        			//Si deja dans le tas 
	        			if (labels_tab[arc.getDestination().getId()].getCost() != Float.MAX_VALUE) {
	        				tas.remove(labels_tab[arc.getDestination().getId()]);
	        			}

	        		//Met à jour le coût et ajoute le label au tas
	        			labels_tab[arc.getDestination().getId()].setCost(labels_tab[x.getNode().getId()].getCost()+ arc.getLength());
	        			tas.insert(labels_tab[arc.getDestination().getId()]);
	        			labels_tab[arc.getDestination().getId()].setArcPrec(arc);
	        		}
	        	}
	        }
	        // Destination has no predecessor, the solution is infeasible...
	        if (labels_tab[data.getDestination().getId()].getArcPrec() == null) {
	            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
	        }
	        else {
	        	
	            // The destination has been found, notify the observers.
	            notifyDestinationReached(data.getDestination());
	            // Create the path from the array of predecessors...
	            ArrayList<Arc> arcs = new ArrayList<>();
	            Arc arc = labels_tab[data.getDestination().getId()].getArcPrec();
	            while (arc != null) {
	                arcs.add(arc);
	                arc = labels_tab[arc.getOrigin().getId()].getArcPrec();
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
