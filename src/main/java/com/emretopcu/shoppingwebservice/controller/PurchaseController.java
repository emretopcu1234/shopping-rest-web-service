package com.emretopcu.shoppingwebservice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.sql.Timestamp;
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
import com.emretopcu.shoppingwebservice.entity.Purchase;
import com.emretopcu.shoppingwebservice.jpa.PurchaseRepository;

@RestController
public class PurchaseController {
	
	@Autowired
	PurchaseRepository purchaseRepository;
	
	@GetMapping("/purchases")
	public List<EntityModel<Purchase>> retrievePurchases(@RequestParam(required = false) String customerId,
				@RequestParam(required = false) Timestamp from, @RequestParam(required = false) Timestamp to) {
		List<Purchase> purchaseList = purchaseRepository.retrievePurchases(customerId, from, to);
		List<EntityModel<Purchase>> modelList = new ArrayList<>();
		for(Purchase purchase: purchaseList) {
			EntityModel<Purchase> model = EntityModel.of(purchase);
			WebMvcLinkBuilder linkToSpecificPurchase = linkTo(methodOn(this.getClass()).retrievePurchaseById(purchase.getId()));
			model.add(linkToSpecificPurchase.withRel("purchase-" + purchase.getId()));
			modelList.add(model);
		}
		return modelList;
	}
	
	@GetMapping("/purchases/{id}")
	public EntityModel<Purchase> retrievePurchaseById(@PathVariable String id) {
		Purchase purchase = purchaseRepository.retrievePurchaseById(id);
		if(purchase == null) {
			throw new ResourceNotFoundException("Purchase with id: " + id + " not found.");
		}
		EntityModel<Purchase> model = EntityModel.of(purchase);
		WebMvcLinkBuilder linkToPurchases = linkTo(methodOn(this.getClass()).retrievePurchases(null, null, null));
		model.add(linkToPurchases.withRel("all-purchases"));
		// TODO ilgili purchase'a ait tüm purchase_item listesinin linki eklenecek.
		return model;
	}
	
	@PostMapping("/purchases")
	public ResponseEntity<Object> addPurchase(@Valid @RequestBody Purchase purchase) {
		Purchase newPurchase = purchaseRepository.addPurchase(purchase);
		if(newPurchase == null) {
			throw new ResourceWithDuplicateIdException("There already exists a purchase with id: " + purchase.getId());
		}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(purchase.getId()).toUri();
		return ResponseEntity.created(location).build();	// .created will return 201 response status which means successful create.
	}
	
	@PutMapping("purchases/{id}")
	public void updatePurchase(@Valid @RequestBody Purchase purchase) {
		Purchase updatedPurchase = purchaseRepository.updatePurchase(purchase);
		if(updatedPurchase == null) {
			throw new ResourceNotFoundException("Purchase with id: " + purchase.getId() + " not found.");
		}
		// nothing return means 200 response status which means successful update.
	}
	
	@DeleteMapping("/purchases/{id}")
	public void deletePurchaseById(@PathVariable String id) {
		Purchase purchase = purchaseRepository.deletePurchase(id);
		if(purchase == null) {
			throw new ResourceNotFoundException("Purchase with id: " + id + " not found.");
		}
		// nothing return means 200 response status which means successful delete.
	}
	
}
