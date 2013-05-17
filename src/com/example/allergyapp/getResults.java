package com.example.allergyapp;
/* Returns a string of results, comma separated, of the ingredients contained in "ingredients" that also exist
 * in the user's "allergies".  */
public class getResults {

	public String result (String ingredients, String allergies) {
		
		String flaggedIngredients = "";
		
		String[] ing = ingredients.split(",");
		String[] all = allergies.split(",");
		
		for (String i : ing){
			for (String a : all){
				if (a.toLowerCase().equals(i.toLowerCase())){
					flaggedIngredients = flaggedIngredients + a.trim() + ",";
				}
			}
		}
		return flaggedIngredients;
	}
}
