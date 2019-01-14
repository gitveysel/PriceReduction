package com.example.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

@RestController
public class PriceCalculatorController {

	@GetMapping("/products")
	public Object listProducts(@RequestParam(required = false, defaultValue = "ShowWasNow") LabelType labelType)
			throws JsonParseException, JsonMappingException, IOException {

		List<ProductDTO> hasPrice = Arrays.stream(deserializeProducts().getProducts())
				.filter(p -> p.getPrice().getDiscount() > 0).map(p -> createProductDTO(p, labelType))
				.collect(Collectors.toList());

		return hasPrice;
	}

	private ProductDTO createProductDTO(Product product, LabelType labelType) {

		List<ColorSwatchDTO> colorSwatchesDTO = Arrays.stream(product.getColorSwatches())
				.map(c -> createColorSwatchDTO(c)).collect(Collectors.toList());

		ProductDTO dto = new ProductDTO(product.getProductId(), product.getTitle(), product.getPrice().getWas(), product.getPrice().nowPrice(),
				product.getPrice().getThen1(), product.getPrice().getThen2(), product.getPrice().getCurrency(), labelType,
				colorSwatchesDTO);
		return dto;
	}

	private ColorSwatchDTO createColorSwatchDTO(ColorSwatch p) {
		return new ColorSwatchDTO(p.getColor(),p.getBasicColor());
	}

	private ProductList deserializeProducts() throws IOException {
		/*
		 * NOTE: JSON HAS SOME ERRORS, in some item "now" is has properties which is not
		 * supposed to e
		 **/

		// RestTemplate template = new RestTemplate();
		/*
		 * Rootobject rootObject = res.
		 * ("https://jl-nonprod-syst.apigee.net/v1/categories/600001506/products?key=2ALHCAAs6ikGRBoy6eTHA58RaG097Fma",
		 * Rootobject.class);
		 */

		
		byte[] encoded = Files.readAllBytes(Paths.get("products.json")); //couldnt reach last night sometimes so put in a file
		String jsonAsString = new String(encoded);

		//correct errors in json ...for now couldn't find other another solution
		jsonAsString = jsonAsString.replaceAll("\\{\"from\":\"55.00\",\"to\":\"100.00\"\\}", "\"55\"");
		jsonAsString = jsonAsString.replaceAll("\\{\"from\":\"59.00\",\"to\":\"68.00\"\\}", "\"59\"");

	 	
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	  	return mapper.readValue(jsonAsString, ProductList.class);
	
	}

}

class ProductList {
	private Product[] products;

	public Product[] getProducts() {
		return products;
	}

	public void setProducts(Product[] products) {
		this.products = products;
	}
}

class Product {
	private String productId;
	private String title;

	private ColorSwatch[] colorSwatches;
	private Price price;

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public ColorSwatch[] getColorSwatches() {
		return colorSwatches;
	}

	public void setColorSwatches(ColorSwatch[] colorSwatches) {
		this.colorSwatches = colorSwatches;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}

class ColorSwatch {
	private String color;
	private String basicColor;

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getBasicColor() {
		return basicColor;
	}

	public void setBasicColor(String basicColor) {
		this.basicColor = basicColor;
	}

}

class Price {
	private double was;
	private double now;
	private String then1;
	private String then2;
	private String currency;

	public double getWas() {
		return was;
	}

	public double nowPrice() {
		// TODO Auto-generated method stub
		return was - now;
	}

	public void setWas(double was) {
		this.was = was;
	}

	public double getNow() {
		return now;
	}

	public void setNow(double now) {
		this.now = now;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getThen1() {
		return then1;
	}

	public void setThen1(String then1) {
		this.then1 = then1;
	}

	public String getThen2() {
		return then2;
	}

	public void setThen2(String then2) {
		this.then2 = then2;
	}

	public double getDiscount() {
		return getWas() - getNow();
	}

}

 
