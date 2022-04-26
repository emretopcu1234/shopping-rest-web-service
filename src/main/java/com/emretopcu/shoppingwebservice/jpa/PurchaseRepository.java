package com.emretopcu.shoppingwebservice.jpa;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.emretopcu.shoppingwebservice.entity.Customer;
import com.emretopcu.shoppingwebservice.entity.Purchase;

@Repository
@Transactional
public class PurchaseRepository {
	
	@PersistenceContext
	EntityManager entityManager;
	
	public List<Purchase> retrievePurchases(String customerId, Timestamp from, Timestamp to) {
		Query query = entityManager.createNamedQuery("retrieve_purchases");
		query.setParameter("customerId", customerId);
		query.setParameter("from", from);
		query.setParameter("to", to);
		return (List<Purchase>) query.getResultList();
	}
	
	public Purchase retrievePurchaseById(String id) {
		return entityManager.find(Purchase.class, id);
	}
	
	public Purchase addPurchase(Purchase purchase) {
		if(retrievePurchaseById(purchase.getId()) != null) {
			return null;
		}
		entityManager.merge(purchase);
		return purchase;
	}
	
	public Purchase updatePurchase(Purchase purchase) {
		Purchase existingPurchase = retrievePurchaseById(purchase.getId());
		if(existingPurchase == null) {
			return null;
		}
		entityManager.merge(purchase);
		return purchase;
	}
	
	public Purchase deletePurchase(String id) {
		Purchase purchase = retrievePurchaseById(id);
		if(purchase == null) {
			return null;
		}
		entityManager.remove(purchase);
		return purchase;
	}

}
