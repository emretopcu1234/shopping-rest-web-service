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
import com.emretopcu.shoppingwebservice.entity.Product;
import com.emretopcu.shoppingwebservice.jpa.ProductRepository;

@RestController
public class ProductController {

	@Autowired
	ProductRepository productRepository;
	
	@GetMapping("/products")
	public List<EntityModel<Product>> retrieveProducts(@RequestParam(required = false) String supplierId,
				@RequestParam(required = false) String name){
		List<Product> productList = productRepository.retrieveProducts(supplierId, name);
		List<EntityModel<Product>> modelList = new ArrayList<>();
		for(Product product: productList) {
			EntityModel<Product> model = EntityModel.of(product);
			WebMvcLinkBuilder linkToSpecificProduct = linkTo(methodOn(this.getClass()).retrieveProductById(product.getId()));
			model.add(linkToSpecificProduct.withRel("product-" + product.getId()));
			modelList.add(model);
		}
		return modelList;
	}
	
	@GetMapping("/products/{id}")
	public EntityModel<Product> retrieveProductById(@PathVariable String id){
		Product product = productRepository.retrieveProductById(id);
		if(product == null) {
			throw new ResourceNotFoundException("Product with id: " + id + " not found.");
		}
		EntityModel<Product> model = EntityModel.of(product);
		WebMvcLinkBuilder linkToProducts = linkTo(methodOn(this.getClass()).retrieveProducts(null, null));
		model.add(linkToProducts.withRel("all-products"));
		// TODO ilgili product'a ait tüm purchase_item listesinin linki eklenecek.
		return model;
	}
	
	@PostMapping("/products")
	public ResponseEntity<Object> addProduct(@Valid @RequestBody Product product){
		Product newProduct = productRepository.addProduct(product);
		if(newProduct == null) {
			throw new ResourceWithDuplicateIdException("There already exists a product with id: " + product.getId());
		}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(product.getId()).toUri();
		return ResponseEntity.created(location).build();	// .created will return 201 response status which means successful create.
	}
	
	@PutMapping("/products/{id}")
	public void updateProduct(@Valid @RequestBody Product product) {
		Product updatedProduct = productRepository.updateProduct(product);
		if(updatedProduct == null) {
			throw new ResourceNotFoundException("Product with id: " + product.getId() + " not found.");
		}
		// nothing return means 200 response status which means successful update.
	}
	
	@DeleteMapping("/products/{id}")
	public void deleteProductById(@PathVariable String id) {
		Product product = productRepository.deleteProduct(id);
		if(product == null) {
			throw new ResourceNotFoundException("Product with id: " + id + " not found.");
		}
		// nothing return means 200 response status which means successful delete.
	}

}
