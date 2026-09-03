package edu.wgu.d387_sample_code.entity;

import com.sun.istack.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "Room")
public class RoomEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	private Integer roomNumber;

	@NotNull
	private String price;
	private String caPrice;
	private String euPrice;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private List<ReservationEntity> reservationEntityList;

	public RoomEntity() {
	}

	public RoomEntity(Integer roomNumber, String price, String caPrice, String euPrice) {
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

	public String getPrice() {
		return price;
	}

	public String getCaPrice() {
		return caPrice;
	}

	public String getEuPrice() {
		return euPrice;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public void setCaPrice(String caPrice) {
		this.caPrice = caPrice;
	}

	public void setEuPrice(String euPrice) {
		this.euPrice = euPrice;
	}

	public List<ReservationEntity> getReservationEntityList() {
		return reservationEntityList;
	}

	public void setReservationEntityList(List<ReservationEntity> reservationEntityList) {
		this.reservationEntityList = reservationEntityList;
	}

	public void addReservationEntity(ReservationEntity reservationEntity) {
		if (null == reservationEntityList)
			reservationEntityList = new ArrayList<>();

		reservationEntityList.add(reservationEntity);
	}

}