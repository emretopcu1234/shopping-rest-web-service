package com.emretopcu.shoppingwebservice.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.emretopcu.shoppingwebservice.entity.Supplier;

@Repository
@Transactional
public class SupplierRepository {
	
	@PersistenceContext
	EntityManager entityManager;
	
	public List<Supplier> retrieveSuppliers(String name, String country){
		Query query = entityManager.createNamedQuery("retrieve_suppliers");
		if(name == null) {
			query.setParameter("name", null);
		}
		else {
			query.setParameter("name", "%" + name + "%");
		}
		query.setParameter("country", country);
		return (List<Supplier>) query.getResultList();
	}
	
	public Supplier retrieveSupplierById(String id) {
		return entityManager.find(Supplier.class, id);
	}
	
	public Supplier addSupplier(Supplier supplier) {
		if(retrieveSupplierById(supplier.getId()) != null) {
			return null;
		}
		entityManager.merge(supplier);
		return supplier;
	}
	
	public Supplier updateSupplier(Supplier supplier) {
		Supplier existingSupplier = retrieveSupplierById(supplier.getId());
		if(existingSupplier == null) {
			return null;
		}
		entityManager.merge(supplier);
		return supplier;
	}
	
	public Supplier deleteSupplier(String id) {
		Supplier supplier = retrieveSupplierById(id);
		if(supplier == null) {
			return null;
		}
		entityManager.remove(supplier);
		return supplier;
	}


}
