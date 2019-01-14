package com.example.demo;

 
import java.util.Hashtable;

public class ColorSwatchDTO {
	private String color;
	private String rgbColor;
	private String skuid;
	
	public String getColor() {
		return color;
	}
	public String getRgbColor() {
		return rgbColor;
	}
	public String getSkuid() {
		return skuid;
	}
	public ColorSwatchDTO(String color, String basicColor) {
		super();
		this.color = color;
		this.rgbColor = BasicColorToRGBConverter.convert(basicColor);
		this.skuid = "kg";
	}
}

class BasicColorToRGBConverter{
	private static Hashtable<String, String> colors = new Hashtable<>();
	static {
		colors.put("white", "FFFFFF");
	}
	
	public static String convert(String basicColor) {
		return colors.contains(basicColor) ? colors.get(basicColor):"UnMapped Color:"+basicColor;
	}
}