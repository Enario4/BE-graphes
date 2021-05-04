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
	        
	        //Si c'est la destination, on s'arrête
	        if (x.getNode().compareTo(dest) == 0) {
	        	fini = true;
	        }
	        //Sinon on parcourt les successeurs
	        for (Arc arc : labels_tab[x.getNode().getId()].getNode().getSuccessors()) {
	        	
	        	//Vérifie si l'arc est accessible (dépend du mode de circulation)
	        	if (!data.isAllowed(arc)) {
					continue;
				}
	        	
	        	//Si le sommet n'est pas marqué :
	        	if (!(labels_tab[arc.getDestination().getId()].isMarked())) {
	        		
	        		//Si on obtient un meilleur coût
	        		if (labels_tab[arc.getDestination().getId()].getCost() > labels_tab[x.getNode().getId()].getCost() + arc.getLength()) {
	        			//Si deja dans le tas 
	        			if (labels_tab[arc.getDestination().getId()].getCost() != Float.MAX_VALUE) {
	        				tas.remove(labels_tab[arc.getDestination().getId()]);
	        			}

	          			System.out.print("mise à jour du coût\n");
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
	            System.out.print("ligne 1\n");
	            // Create the path from the array of predecessors...
	            ArrayList<Arc> arcs = new ArrayList<>();
	            System.out.print("ligne 2\n");
	            Arc arc = labels_tab[data.getDestination().getId()].getArcPrec();
	            System.out.print("ligne 3\n");
	            while (arc != null) {
	            	System.out.print("ligne 4 dans le while \n");
	                arcs.add(arc);
	                System.out.print("ligne 5\n");
	                System.out.print(arc.getDestination().getId());
	                System.out.print(arc.getOrigin().getId());
	                arc = labels_tab[arc.getOrigin().getId()].getArcPrec();
	                System.out.print("ligne 6\n");
	            }

	            // Reverse the path...
	            System.out.print("ligne 7 après le while\n");
	            Collections.reverse(arcs);

	            // Create the final solution.
	            System.out.print("ligne 8\n");
	            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
	        }
  		}
        
        
        
        return solution;
    }

}
