package edu.wgu.d387_sample_code.model.response;

import edu.wgu.d387_sample_code.model.Links;

;

public class ReservableRoomResponse {

	private Long id;
	private Integer roomNumber;
	private Integer price;
	private Integer caPrice;
	private Integer euPrice;
	private Links links;
	
	public ReservableRoomResponse() {
		super();
	}
	
	public ReservableRoomResponse(Integer roomNumber, Integer price, Integer caPrice, Integer euPrice) {
		super();
		this.roomNumber = roomNumber;
		this.price = price;
		this.caPrice = caPrice;
		this.euPrice = euPrice;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(Integer roomNumber) {
		this.roomNumber = roomNumber;
	}
	public Integer getPrice() {
		return price;
	}
	public Integer getCaPrice() {
		return caPrice;
	}
	public Integer getEuPrice() {
		return euPrice;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public void setCaPrice(Integer caPrice) {
		this.caPrice = caPrice;
	}
	public void setEuPrice(Integer euPrice) {
		this.euPrice = euPrice;
	}
	public Links getLinks() {
		return links;
	}
	public void setLinks(Links links) {
		this.links = links;
	}
	
	
}
