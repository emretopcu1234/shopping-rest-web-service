package com.emretopcu.shoppingwebservice.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.emretopcu.shoppingwebservice.entity.Customer;


@Repository
@Transactional
public class CustomerRepository {
	
	@PersistenceContext
	EntityManager entityManager;
	
	public List<Customer> retrieveAllCustomers(){
		return entityManager.createNamedQuery("find_all_customers", Customer.class).getResultList();
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
