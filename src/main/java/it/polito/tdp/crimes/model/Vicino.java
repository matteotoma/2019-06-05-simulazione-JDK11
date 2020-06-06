package it.polito.tdp.crimes.model;

public class Vicino implements Comparable<Vicino>{
	
	private Integer distretto;
	private Double distanzaMedia;
	
	public Vicino(Integer distretto, Double distanzaMedia) {
		super();
		this.distretto = distretto;
		this.distanzaMedia = distanzaMedia;
	}

	public Integer getDistretto() {
		return distretto;
	}

	public void setDistretto(Integer distretto) {
		this.distretto = distretto;
	}

	public Double getDistanzaMedia() {
		return distanzaMedia;
	}

	public void setDistanzaMedia(double distanzaMedia) {
		this.distanzaMedia = distanzaMedia;
	}

	public int compareTo(Vicino o) {
		return this.distanzaMedia.compareTo(o.getDistanzaMedia());
	}

}
