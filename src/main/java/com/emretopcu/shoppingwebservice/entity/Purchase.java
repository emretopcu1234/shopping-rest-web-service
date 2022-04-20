package com.emretopcu.shoppingwebservice.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

@Entity
@Table(name="purchase")
public class Purchase {
	
	@Id
	@Size(min=1, max=10, message="Id must have between 1-10 characters.")
	private String id;
	
	@Past(message="Timestamp must be from the past.")
	private Date timestamp;
		
	@Column(name="customer_id")
	@Size(min=1, max=10, message="Customer id must have between 1-10 characters.")
	private String customerId;
	
	public Purchase() {
		
	}

	public Purchase(@Size(min = 1, max = 10, message = "Id must have between 1-10 characters.") String id,
			@Past(message = "Timestamp must be from the past.") Date timestamp,
			@Size(min = 1, max = 10, message = "Customer id must have between 1-10 characters.") String customerId) {
		super();
		this.id = id;
		this.timestamp = timestamp;
		this.customerId = customerId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
}
