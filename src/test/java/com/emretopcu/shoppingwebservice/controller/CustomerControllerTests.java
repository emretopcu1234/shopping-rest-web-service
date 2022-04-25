package com.emretopcu.shoppingwebservice.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.emretopcu.shoppingwebservice.entity.Customer;
import com.emretopcu.shoppingwebservice.jpa.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SpringBootTest(properties = "spring.main.lazy-initialization=true", classes = CustomerController.class)
@AutoConfigureMockMvc
@ComponentScan("com.emretopcu.shoppingwebservice")
public class CustomerControllerTests {

	private static String BASE_URL = "http://localhost/";

	@InjectMocks
	CustomerController customerController;

	@MockBean
	CustomerRepository customerRepository;

	@Autowired
	MockMvc mvc;

	@Test
	public void testRetrieveAllCustomers_EmptyList() throws Exception {
		List<Customer> customerList = new ArrayList<>();

		when(customerRepository.retrieveCustomers(null, null)).thenReturn(customerList);

		mvc.perform(get("/customers"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	public void testRetrieveAllCustomers_NonEmptyList() throws Exception {
		Customer customer1 = new Customer("TC0001","Some Name1","Some Country1");
		Customer customer2 = new Customer("TC0002","Some Name2","Some Country2");
		List<Customer> customerList = new ArrayList<>();
		customerList.add(customer1);
		customerList.add(customer2);

		when(customerRepository.retrieveCustomers(null, null)).thenReturn(customerList);

		mvc.perform(get("/customers"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[0].id").value(customer1.getId()))
			.andExpect(jsonPath("$[0].name").value(customer1.getName()))
			.andExpect(jsonPath("$[0].country").value(customer1.getCountry()))
			.andExpect(jsonPath("$[0].links[0].rel").value("customer-" + customer1.getId()))
			.andExpect(jsonPath("$[0].links[0].href").value(BASE_URL + "customers/" + customer1.getId()))
			.andExpect(jsonPath("$[1].id").value(customer2.getId()))
			.andExpect(jsonPath("$[1].name").value(customer2.getName()))
			.andExpect(jsonPath("$[1].country").value(customer2.getCountry()))
			.andExpect(jsonPath("$[1].links[0].rel").value("customer-" + customer2.getId()))
			.andExpect(jsonPath("$[1].links[0].href").value(BASE_URL + "customers/" + customer2.getId()));
	}

	@Test
	public void testRetrieveCustomerById_CustomerExists() throws Exception {
		Customer customer = new Customer("TC0001","Some Name","Some Country");

		when(customerRepository.retrieveCustomerById("TC0001")).thenReturn(customer);

		mvc.perform(get("/customers/" + customer.getId()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(customer.getId()))
			.andExpect(jsonPath("$.name").value(customer.getName()))
			.andExpect(jsonPath("$.country").value(customer.getCountry()))
			.andExpect(jsonPath("$._links.all-customers.href").value(BASE_URL + "customers{?name,country}"));
	}

	@Test
	public void testRetrieveCustomerById_CustomerDoesNotExist() throws Exception {
		String id = "TC0000";
		when(customerRepository.retrieveCustomerById(id)).thenReturn(null);

		mvc.perform(get("/customers/" + id))
			.andExpect(status().isNotFound());
	}

	@Test
	public void testAddCustomer_CustomerDoesNotExist() throws Exception {
		Customer customer = new Customer("TC0001", "Some Name", "Some Country");

		when(customerRepository.addCustomer(any())).thenReturn(customer);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(customer);

		mvc.perform(post("/customers")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestJson))
			.andExpect(status().isCreated());
	}

	@Test
	public void testAddCustomer_CustomerAlreadyExists() throws Exception {
		Customer customer = new Customer("TC0001", "Some Name", "Some Country");

		when(customerRepository.addCustomer(any())).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(customer);

		mvc.perform(post("/customers")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestJson))
			.andExpect(status().isNotAcceptable());
	}


}
