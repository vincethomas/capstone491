package com.example.allergyapp;

public class getResults {

	public String result (String ingredients, String allergies) {
		
		String flaggedIngredients = "";
		
		String[] ing = ingredients.split(",");
		String[] all = allergies.split(",");
		
		for (String i : ing){
			for (String a : all){
				if (a.equals(i)){
					flaggedIngredients = flaggedIngredients + a + ",";
				}
			}
		}
		return flaggedIngredients;
	}
}
