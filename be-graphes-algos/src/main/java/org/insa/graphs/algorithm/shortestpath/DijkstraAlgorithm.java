package org.insa.graphs.algorithm.shortestpath;

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
		
		// Tableau des prédecesseurs 
		Arc[] arc_prec = new Arc[graph.size()];
		
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
	        x.setMarque();
	        notifyNodeMarked(x.getNode());
	        
	        //Si c'est la destination, on s'arrête
	        if (x.getNode().compareTo(dest) == 0 {
	        	fini = true;
	        	
	        }
  		}
        
        //tant que tous les sommets sont pas marqués : parcourt les successeurs 
        //Tant qu'il y a des successeurs : récupère ou crée le label 
        //Marque le successeur si pas deja fait. Si cout plus faible : met a jour et ajoute le noeud dans le tas
        
        
        return solution;
    }

}
