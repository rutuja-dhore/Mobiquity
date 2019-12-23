package com.mobiquityinc.model;

import java.util.List;

public class Packet {

	private Integer weightLimit;

	private List<PacketItem> items;

	
	public Packet(Integer weightLimit, List<PacketItem> items) {
		super();
		this.weightLimit = weightLimit;
		this.items = items;
	}

	public Integer getWeightLimit() {
		return weightLimit;
	}

	public void setWeightLimit(Integer weightLimit) {
		this.weightLimit = weightLimit;
	}

	public List<PacketItem> getItems() {
		return items;
	}

	public void setItems(List<PacketItem> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "Package [weightLimit=" + weightLimit + ", items=" + items + "]";
	}
	
	

}
