package com.mobiquityinc.model;

public class PacketItem {

	private Integer indexNumber;

	private Double weight;

	private Double cost;

	public PacketItem() {
	}

	public PacketItem(Integer indexNumber, Double weight, Double cost) {
		super();
		this.indexNumber = indexNumber;
		this.weight = weight;
		this.cost = cost;
	}

	public Integer getIndexNumber() {
		return indexNumber;
	}

	public void setIndexNumber(Integer indexNumber) {
		this.indexNumber = indexNumber;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "PackageItem [indexNumber=" + indexNumber + ", weight=" + weight + ", cost=€" + cost + "]\n";
	}

	
}
