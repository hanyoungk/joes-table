import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.http.*;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class YelpAPI {
	
	//my API key for yelp
	private static final String apiKey = "9jS0BygLr28lNQHVHSxjk7vZLIQ__bWK5f90uLTs6E2rkb68DYoOlz5OG6jfnFKDXiqwTr3JSLwbrGbfJLFLeyRQtxmQOzmd_63Q6ukMTsdNgM2JCWVFf9Sz9Tw0ZXYx";
	//api url for searching for businesses
	private static final String apiUrl = "https://api.yelp.com/v3/businesses/search";
	
	public List<Restaurant> fetchRestaurants(String term, String latitude, String longitude, String sort) {

		List<Restaurant> restaurantList = new ArrayList<>();
		
		try {
			String encodedTerm = URLEncoder.encode(term, "UTF-8");
			String urlString = apiUrl + "?term=" + encodedTerm + "&latitude=" + latitude + "&longitude=" + longitude + "&sort_by=" + sort + "&limit=10";
			
			System.out.println(urlString);
			//**CITATION**
			// ""how to use HttpRequest, HttpClient class to pull data from yelp API" prompt (5 lines). 
			//	Chat GPT 3.5 version, OpenAI, Oct 21 2023, chat.openai.com/chat
			//create a connection to the API
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(urlString))
					.header("accept", "application/json").header("Authorization", "Bearer " + apiKey).GET().build();
			
			HttpClient client = HttpClient.newHttpClient();
			
			//Read the API response
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			
			
			// Check the HTTP response status
            int statusCode = response.statusCode();
            if (statusCode != 200) {
                System.out.println("Error: Unexpected HTTP status code: " + statusCode);
                // Handle or log the error appropriately
                return restaurantList; // or return null, depending on your error handling strategy
            }
			
			//parsing JSON response

			//**CITATION**
			// "how can i parse the JSON response using Gson from the Yelp API call" prompt (9 lines). 
			//	Chat GPT 3.5 version, OpenAI, Oct 22 2023, chat.openai.com/chat
			Gson gson = new Gson();	
			
			JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
			JsonArray businesses = jsonResponse.getAsJsonArray("businesses");
			
			for(JsonElement element : businesses) {
				JsonObject business = element.getAsJsonObject();
				Restaurant restaurant = new Restaurant();
		
				// Extract required data and set it in Restaurant object
                restaurant.setName(business.get("name").getAsString());
                restaurant.setPhone(business.get("display_phone").getAsString());
                
                
                //**CITATION**
            	// "how to concatenate yelp displayAddress indexes" prompt (5 lines). 
            	//	Chat GPT 3.5 version, OpenAI, Nov 9 2023, chat.openai.com/chat
                JsonArray displayAddressArray = business.getAsJsonObject("location").getAsJsonArray("display_address");
                StringBuilder concatenatedAddress = new StringBuilder();
                for (JsonElement addressElement : displayAddressArray) {
                	concatenatedAddress.append(addressElement.getAsString()).append(" ");
                }

                // Remove the trailing comma and space at the end
                String finalAddress = concatenatedAddress.toString().trim();

                // Set the concatenated address to the restaurant object
                restaurant.setAddress(finalAddress);
                
                // Extract price if available, otherwise set to null
                JsonElement priceElement = business.get("price");
                String price = (priceElement != null && !priceElement.isJsonNull()) ? priceElement.getAsString() : null;
                restaurant.setPrice(price);

                restaurant.setCuisine(business.getAsJsonArray("categories").get(0).getAsJsonObject().get("title").toString());
                
                //**CITATION**
            	// how can i check if JsonElement is null and assign it null if it's null?" prompt (3 lines). 
            	//	Chat GPT 3.5 version, OpenAI, Nov 10 2023, chat.openai.com/chat
                // Check for the existence of the "rating" field and set it
                JsonElement ratingElement = business.get("rating");
                Double rating = (ratingElement != null && !ratingElement.isJsonNull()) ? ratingElement.getAsDouble() : null;
                restaurant.setRating(rating);

                restaurant.setImageURL(business.get("image_url").getAsString());
                
                
                //**CITATION**
            	// how can i shorten the url after the ?adjust part of the url?" prompt (3 lines). 
            	//	Chat GPT 3.5 version, OpenAI, Nov 10 2023, chat.openai.com/chat
                String originalUrl = business.get("url").getAsString();
                
                int adjustIndex = originalUrl.indexOf("?adjust"); // Find the index of the substring "?adjust"
                String modifiedUrl = originalUrl.substring(0, adjustIndex); // Extract the substring before "?adjust"
                restaurant.setUrl(modifiedUrl);

                restaurantList.add(restaurant);
                
                
			}
			
			
		} catch(IOException e) {
			e.printStackTrace();
		} catch(JsonSyntaxException e) {
			e.printStackTrace();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return restaurantList;
		
	}
}
