package com.example.demo;

import java.util.Hashtable;
import java.util.List;

public class ProductDTO {
	private String productId;
	private String title;
	private String nowPrice;
	private String priceLabel;
	private List<ColorSwatchDTO> colorSwatches;

	public String getProductId() {
		return productId;
	}

	public String getTitle() {
		return title;
	}

	public String getNowPrice() {
		return nowPrice;
	}

	public String getPriceLabel() {
		return priceLabel;
	}

	public List<ColorSwatchDTO> getColorSwatches() {
		return colorSwatches;
	}
	
	public ProductDTO(String productId, String title, double wasPrice, double nowPrice, String then1, String then2,
			String currency, LabelType labelType,List<ColorSwatchDTO> colorSwatches) {
		this.productId = productId;
		this.title = title;
		this.nowPrice =  PriceFormatter.format(nowPrice,currency);
		this.priceLabel = labelType.format(wasPrice, nowPrice, then1, then2, currency);
		this.colorSwatches = colorSwatches;

	}
}

class CurrencyToSymbolConverter{
	private static Hashtable<String, String> symbols = new Hashtable<>();
	static {
		symbols.put("GBP", "£");
	}
	
	public static String convert(String currency) {
		return symbols.containsKey(currency) ? symbols.get(currency):"UnMapped Currency:"+currency;
	}
}