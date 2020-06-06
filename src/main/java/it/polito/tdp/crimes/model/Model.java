package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private Graph<Integer, DefaultWeightedEdge> grafo;
	private Simulator sim;
	
	public Model() {
		dao = new EventsDao();
	}
	
	public List<Integer> getYears(){
		return dao.getYears();
	}
	
	public List<Integer> getMonths(){
		return dao.getMonths();
	}
	
	public List<Integer> getDays(){
		return dao.getDays();
	}
	
	public void creaGrafo(Integer anno) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		// aggiungo i vertici
		Graphs.addAllVertices(grafo, dao.getDistricts());
		
		// aggiungo gli archi
		for(Integer v1: grafo.vertexSet()) {
			for(Integer v2: grafo.vertexSet()) {
				if(!v1.equals(v2) && grafo.getEdge(v1, v2) == null) {
					double latMedia1 = dao.getLatMedia(anno, v1);
					double latMedia2 = dao.getLatMedia(anno, v2);
					double lonMedia1 = dao.getLonMedia(anno, v1);
					double lonMedia2 = dao.getLatMedia(anno, v2);
					
					double distanzaMedia = LatLngTool.distance(new LatLng(latMedia1, lonMedia1), new LatLng(latMedia2, lonMedia2), LengthUnit.KILOMETER);
					
					Graphs.addEdge(grafo, v1, v2, distanzaMedia);
				}
			}
		}
	}
	
	public List<Vicino> getVicini(Integer d){
		List<Vicino> list = new ArrayList<>();
		for(Integer vicino: Graphs.neighborListOf(grafo, d)) {
			Vicino v = new Vicino(vicino, grafo.getEdgeWeight(grafo.getEdge(d, vicino)));
			list.add(v);
		}
		Collections.sort(list);
		return list;
	}

	public Set<Integer> getVertici() {
		return grafo.vertexSet();
	}

	public Integer getNArchi() {
		return grafo.edgeSet().size();
	}
	
	public void simula(Integer anno, Integer mese, Integer giorno, Integer numAgenti) {
		this.sim = new Simulator();
		sim.init(anno, mese, giorno, numAgenti, this.grafo);
		sim.run();
	}

	public Integer getMalGestiti() {
		return sim.getMalGestiti();
	}
}
