package com.emretopcu.shoppingwebservice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.emretopcu.shoppingwebservice.controller.exception.ResourceNotFoundException;
import com.emretopcu.shoppingwebservice.controller.exception.ResourceWithDuplicateIdException;
import com.emretopcu.shoppingwebservice.entity.Customer;
import com.emretopcu.shoppingwebservice.jpa.CustomerRepository;

@RestController
public class CustomerController {
	
	@Autowired
	CustomerRepository customerRepository;
	
	@GetMapping("/customers")
	public List<EntityModel<Customer>> retrieveCustomers(@RequestParam(required = false) String name, @RequestParam(required = false) String country) {
		List<Customer> customerList = customerRepository.retrieveCustomers(name, country);
		List<EntityModel<Customer>> modelList = new ArrayList<>();
		for(Customer customer: customerList) {
			EntityModel<Customer> model = EntityModel.of(customer);
			WebMvcLinkBuilder linkToCustomers = linkTo(methodOn(this.getClass()).retrieveCustomerById(customer.getId()));
			model.add(linkToCustomers.withRel("customer-" + customer.getId()));
			modelList.add(model);
		}
		return modelList;
	}	
	
	@GetMapping("/customers/{id}")
	public EntityModel<Customer> retrieveCustomerById(@PathVariable String id) {
		Customer customer = customerRepository.retrieveCustomerById(id);
		if(customer == null) {
			throw new ResourceNotFoundException("Customer with id: " + id + " not found.");
		}
		EntityModel<Customer> model = EntityModel.of(customer);
		WebMvcLinkBuilder linkToCustomers = linkTo(methodOn(this.getClass()).retrieveCustomers(null, null));
		model.add(linkToCustomers.withRel("all-customers"));
		// TODO buraya ilgili customer'ın order'larının linki de eklenecek.
		return model;
	}
		
	@PostMapping("/customers")
	public ResponseEntity<Object> addCustomer(@Valid @RequestBody Customer customer) {
		Customer newCustomer = customerRepository.addCustomer(customer);
		if(newCustomer == null) {
			throw new ResourceWithDuplicateIdException("There already exists a customer with id: " + customer.getId());
		}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(customer.getId()).toUri();
		return ResponseEntity.created(location).build();	// .created will return 201 response status which means successful create.
	}
	
	@PutMapping("customers/{id}")
	public void updateCustomer(@Valid @RequestBody Customer customer) {
		Customer updatedCustomer = customerRepository.updateCustomer(customer);
		if(updatedCustomer == null) {
			throw new ResourceNotFoundException("Customer with id: " + customer.getId() + " not found.");
		}
		// nothing return means 200 response status which means successful update.
	}
	
	@DeleteMapping("/customers/{id}")
	public void deleteCustomerById(@PathVariable String id) {
		Customer customer = customerRepository.deleteCustomer(id);
		if(customer == null) {
			throw new ResourceNotFoundException("Customer with id: " + id + " not found.");
		}
		// nothing return means 200 response status which means successful delete.
	}
	
	
}
