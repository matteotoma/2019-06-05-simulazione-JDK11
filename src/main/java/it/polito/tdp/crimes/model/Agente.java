package it.polito.tdp.crimes.model;

public class Agente {
	
	private Integer id;
	private Integer distretto;
	private boolean occupato;
	
	public Agente(Integer id, Integer distretto) {
		this.id = id;
		this.distretto = distretto;
		this.occupato = false;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDistretto() {
		return distretto;
	}

	public void setDistretto(Integer distretto) {
		this.distretto = distretto;
	}

	public boolean isOccupato() {
		return occupato;
	}

	public void setOccupato(boolean occupato) {
		this.occupato = occupato;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Agente other = (Agente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
