package com.emretopcu.shoppingwebservice.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.emretopcu.shoppingwebservice.entity.Product;

@Repository
@Transactional
public class ProductRepository {

	@PersistenceContext
	EntityManager entityManager;
	
	public List<Product> retrieveProducts(String supplierId, String name){
		Query query = entityManager.createNamedQuery("retrieve_products");
		query.setParameter("supplierId", supplierId);
		if(name == null) {
			query.setParameter("name", null);
		}
		else {
			query.setParameter("name", "%" + name + "%");
		}
		return (List<Product>) query.getResultList();
	}
	
	public Product retrieveProductById(String id) {
		return entityManager.find(Product.class, id);
	}
	
	public Product addProduct(Product product) {
		if(retrieveProductById(product.getId()) != null) {
			return null;
		}
		entityManager.merge(product);
		return product;
	}
	
	public Product updateProduct(Product product) {
		Product existingProduct = retrieveProductById(product.getId());
		if(existingProduct == null) {
			return null;
		}
		entityManager.merge(product);
		return product;
	}
	
	public Product deleteProduct(String id) {
		Product product = retrieveProductById(id);
		if(product == null) {
			return null;
		}
		entityManager.remove(product);
		return product;
	}
	
}
