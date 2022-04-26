package com.emretopcu.shoppingwebservice.jpa;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.emretopcu.shoppingwebservice.entity.Customer;


@Repository
@Transactional
public class CustomerRepository {
	
	@PersistenceContext
	EntityManager entityManager;
	
	public List<Customer> retrieveCustomers(String name, String country){
		Query query = entityManager.createNamedQuery("retrieve_customers");
		if(name == null) {
			query.setParameter("name", null);
		}
		else {
			query.setParameter("name", "%" + name + "%");
		}
		query.setParameter("country", country);
		return (List<Customer>) query.getResultList();
	}
	
	public Customer retrieveCustomerById(String id) {
		return entityManager.find(Customer.class, id);
	}
	
	public Customer addCustomer(Customer customer) {
		if(retrieveCustomerById(customer.getId()) != null) {
			return null;
		}
		entityManager.merge(customer);
		return customer;
	}
	
	public Customer updateCustomer(Customer customer) {
		Customer existingCustomer = retrieveCustomerById(customer.getId());
		if(existingCustomer == null) {
			return null;
		}
		entityManager.merge(customer);
		return customer;
	}
	
	public Customer deleteCustomer(String id) {
		Customer customer = retrieveCustomerById(id);
		if(customer == null) {
			return null;
		}
		entityManager.remove(customer);
		return customer;
	}
	

}
