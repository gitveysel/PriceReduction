package com.example.demo;

import java.text.MessageFormat;

public class PriceFormatter {
	static String format(double price,String currency) {
		return CurrencyToSymbolConverter.convert(currency) +( price >= 10 ? MessageFormat.format("{0,number,#}", price)
				: MessageFormat.format("{0,number,#.##}", price));
	}

}