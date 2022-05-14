package com.emretopcu.shoppingwebservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name="product")
@NamedQuery(name="retrieve_products", query="select p from Product p where (:name is null or p.name like :name) "
		+ "and (:supplierId is null or p.supplierId = :supplierId)")
public class Product {
	
	@Id
	@Size(min=1, max=10, message="Id must have between 1-10 characters.")
	private String id;
	
	@Size(min=1, max=50, message="Name must have between 1-50 characters.")
	private String name;
	
	@Column(name="supplier_id")
	private String supplierId;
	
	public Product() {
		
	}

	public Product(@Size(min = 1, max = 10, message = "Id must have between 1-10 characters.") String id,
			@Size(min = 1, max = 50, message = "Name must have between 1-50 characters.") String name,
			String supplierId) {
		super();
		this.id = id;
		this.name = name;
		this.supplierId = supplierId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
	
}
