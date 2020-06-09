package it.polito.tdp.crimes.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.crimes.db.EventsDao;
import it.polito.tdp.crimes.model.Evento.EventType;

public class Simulator {
	
	// CODA DEGLI EVENTI
	private PriorityQueue<Evento> queue;
	
	// OUTPUT DA CALCOLARE
	private int malGestito;
	
	// PARAMETRI DELLA SIMULZIONE
	private List<Agente> agenti;
	private EventsDao dao;
	private Integer velocita;
	
	// MONDO
	private Graph<Integer, DefaultWeightedEdge> grafo;
	
	public void init(Integer anno, Integer mese, Integer giorno, Integer numAgenti, Graph<Integer, DefaultWeightedEdge> grafo) {
		this.dao = new EventsDao();
		this.velocita = 60;
		this.malGestito = 0;
		
		this.grafo = grafo;
		
		this.queue = new PriorityQueue<>();
		
		this.agenti = new ArrayList<>();
		setAgenti(dao.getDistrettoMin(anno), numAgenti);
		
		for(Event crimine: dao.listEvents(anno, mese, giorno)) {
			Evento e = new Evento(EventType.CRIMINE, crimine.getReported_date(), crimine, null);
			this.queue.add(e);
		}
	}

	private void setAgenti(Integer district_id, Integer n) {
		for(int i=1; i<=n; i++) {
			Agente a = new Agente(i, district_id);
			this.agenti.add(a);
		}
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Evento e = queue.poll();
			processEvento(e);
		}
	}

	private void processEvento(Evento e) {
		switch(e.getType()) {
		case CRIMINE:
			// trovo l'agente più vicino
			Agente a = cercaAgenteVicino(e.getCrimine().getDistrict_id());
			double distanza = 0.0;
			// vedo se è occupato
			if(a != null && a.isOccupato()==false) {
				a.setOccupato(true);
				// calcolo il tempo che impiega l'agente per arrivare
				if(!a.getDistretto().equals(e.getCrimine().getDistrict_id())) {
					distanza = this.grafo.getEdgeWeight(this.grafo.getEdge(a.getDistretto(), e.getCrimine().getDistrict_id()));
					a.setDistretto(e.getCrimine().getDistrict_id());
				}
				long tempo = (long) ((distanza / this.velocita) * 60);
				
				Evento nuovo = new Evento(EventType.ARRIVO_AGENTE, e.getTime().plus(tempo, ChronoUnit.MINUTES), e.getCrimine(), a);
				queue.add(nuovo);
			}
			else
				this.malGestito++;
			break;
		case ARRIVO_AGENTE:
			LocalDateTime tempo;
			// controllo se è arrivato entro 15 minuti
			if(e.getTime().isAfter(e.getCrimine().getReported_date().plus(15, ChronoUnit.MINUTES)))
				this.malGestito++;
			
			// gestisco l'evento
			if(e.getCrimine().getOffense_category_id().equals("all_other_crimes")) {
				Double r = Math.random();
				if(r > 0.5)
					tempo = e.getTime().plusHours(2);
				else
					tempo = e.getTime().plusHours(1);
			}
			else
				tempo = e.getTime().plusHours(2);
			
			Evento nuovo = new Evento(EventType.GESTITO, tempo, e.getCrimine(), e.getAgente());
			queue.add(nuovo);
			break;
		case GESTITO:
			// libero l'agente
			e.getAgente().setOccupato(false);
			break;
		}
	}

	private Agente cercaAgenteVicino(Integer d) {
		double distanzaMin = Double.MAX_VALUE;
		Agente agente = null;
		for(Agente a: this.agenti) {
			if(a.getDistretto().equals(d))
				return a;
			else {
				double dist = this.grafo.getEdgeWeight(this.grafo.getEdge(a.getDistretto(), d));
				if(dist < distanzaMin) {
					agente = a;
					distanzaMin = dist;
				}
			}
		}
		return agente;
	}
	
	public Integer getMalGestiti() {
		return this.malGestito;
	}

}
