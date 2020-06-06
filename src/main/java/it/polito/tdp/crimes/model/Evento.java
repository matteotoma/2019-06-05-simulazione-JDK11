package it.polito.tdp.crimes.model;

import java.time.LocalDateTime;

public class Evento implements Comparable<Evento>{
	
	public enum EventType{
		CRIMINE, ARRIVO_AGENTE, GESTITO
	}
	
	private EventType type;
	private LocalDateTime time;
	private Event crimine;
	private Agente agente;
	
	public Evento(EventType type, LocalDateTime time, Event crimine, Agente agente) {
		super();
		this.type = type;
		this.time = time;
		this.crimine = crimine;
		this.agente = agente;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public Event getCrimine() {
		return crimine;
	}

	public void setCrimine(Event crimine) {
		this.crimine = crimine;
	}

	public Agente getAgente() {
		return agente;
	}

	public void setAgente(Agente agente) {
		this.agente = agente;
	}

	public int compareTo(Evento o) {
		return this.time.compareTo(o.getTime());
	}
	
}
