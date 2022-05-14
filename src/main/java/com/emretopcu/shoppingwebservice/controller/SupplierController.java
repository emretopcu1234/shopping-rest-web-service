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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.emretopcu.shoppingwebservice.controller.exception.ResourceNotFoundException;
import com.emretopcu.shoppingwebservice.controller.exception.ResourceWithDuplicateIdException;
import com.emretopcu.shoppingwebservice.entity.Supplier;
import com.emretopcu.shoppingwebservice.jpa.SupplierRepository;

@RestController
public class SupplierController {

	@Autowired
	SupplierRepository supplierRepository;
	
	@Autowired
	ProductController productController;
	
	@GetMapping("/suppliers")
	public List<EntityModel<Supplier>> retrieveSuppliers(@RequestParam(required = false) String name,
				@RequestParam(required = false) String country) {
		List<Supplier> supplierList = supplierRepository.retrieveSuppliers(name, country);
		List<EntityModel<Supplier>> modelList = new ArrayList<>();
		for(Supplier supplier: supplierList) {
			EntityModel<Supplier> model = EntityModel.of(supplier);
			WebMvcLinkBuilder linkToSpecificSupplier = linkTo(methodOn(this.getClass()).retrieveSupplierById(supplier.getId()));
			model.add(linkToSpecificSupplier.withRel("supplier-" + supplier.getId()));
			modelList.add(model);
		}
		return modelList;
	}
	
	@GetMapping("/suppliers/{id}")
	public EntityModel<Supplier> retrieveSupplierById(@PathVariable String id){
		Supplier supplier = supplierRepository.retrieveSupplierById(id);
		if(supplier == null) {
			throw new ResourceNotFoundException("Supplier with id: " + id + " not found.");
		}
		EntityModel<Supplier> model = EntityModel.of(supplier);
		WebMvcLinkBuilder linkToSuppliers = linkTo(methodOn(this.getClass()).retrieveSuppliers(null, null));
		model.add(linkToSuppliers.withRel("all-suppliers"));
		WebMvcLinkBuilder linkToSupplierProducts = linkTo(methodOn(productController.getClass()).retrieveProducts(supplier.getId(), null));
		model.add(linkToSupplierProducts.withRel("supplier-products"));
		return model;
	}
	
	@PostMapping("/suppliers")
	public ResponseEntity<Object> addSupplier(@Valid @RequestBody Supplier supplier){
		Supplier newSupplier = supplierRepository.addSupplier(supplier);
		if(newSupplier == null) {
			throw new ResourceWithDuplicateIdException("There already exists a supplier with id: " + supplier.getId());
		}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(supplier.getId()).toUri();
		return ResponseEntity.created(location).build();	// .created will return 201 response status which means successful create.
	}
	
	@PutMapping("/suppliers/{id}")
	public void updateSupplier(@Valid @RequestBody Supplier supplier) {
		Supplier updatedSupplier = supplierRepository.updateSupplier(supplier);
		if(updatedSupplier == null) {
			throw new ResourceNotFoundException("Supplier with id: " + supplier.getId() + " not found.");
		}
		// nothing return means 200 response status which means successful update.
	}
	
	@DeleteMapping("/suppliers/{id}")
	public void deleteSupplier(@PathVariable String id) {
		Supplier supplier = supplierRepository.deleteSupplier(id);
		if(supplier == null) {
			throw new ResourceNotFoundException("Supplier with id: " + id + " not found.");
		}
		// nothing return means 200 response status which means successful delete.
	}
	
}
