import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.catalina.User;

import com.google.gson.JsonObject;
import com.google.gson.Gson;



@WebServlet("/FavoritesServlet")
public class FavoritesServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	//for intercepting POST requests like adding favorites
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		PrintWriter pw = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		System.out.println("favorites servlet doPost");
		
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
		    buffer.append(line);
		}
		String data = buffer.toString();
		System.out.println("Received data: " + data);

		//chat gpt: how can i read in from an object inside of an object?" 5 lines
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(data, JsonObject.class);

		JsonObject resDetails = jsonObject.getAsJsonObject("resDetails");
		String resName = resDetails.get("name").getAsString();
		String address = resDetails.get("address").getAsString();
		String phone = resDetails.get("phone").getAsString();
		String imageURL = resDetails.get("imageURL").getAsString();
		String cuisine = resDetails.get("cuisine").getAsString();
		String price = resDetails.get("price").getAsString();
		float rating = Float.parseFloat(resDetails.get("rating").getAsString());
		String url = resDetails.get("url").getAsString();
		
		String username = jsonObject.get("username").getAsString();
		String buttonCheck = jsonObject.get("forButton").getAsString();
		//yes if for quick check for button, no if retrieving all data
		
		
		boolean addedToFav = false;
		
		try {
			
			boolean restaurantExists = JDBCConnector.doesRestaurantExist(resName, address);
			System.out.println("restaurantExists: " + restaurantExists);
			
			if("yes".equals(buttonCheck) && !restaurantExists) {
				pw.write(String.valueOf(restaurantExists)); //false for "not in favorites"
				return;
			} else if(!restaurantExists && !"yes".equals(buttonCheck)) {
				//add it to the restaurants table before adding it to favorites
				JDBCConnector.addRestaurant(resName, address, phone, price, rating, cuisine, imageURL, url);
				
			}
			
			
			//get resID for that restaurant
			Integer resID = JDBCConnector.getRestaurantID(resName, address);
			//get userID for the current user
			Integer userID = JDBCConnector.getUserID(username);
			
			boolean favoriteExists = JDBCConnector.checkFavorite(userID, resID);
			
			//restaurant is not favorited by user
			if(!favoriteExists) {
				
				if("yes".equals(buttonCheck)) { //if only checking one item for button
					pw.write("false"); //false for not in favorite
					
					System.out.println("should return here");
					return;
					
				}
				
				JDBCConnector.addFavorite(userID, resID);
				addedToFav = true;
				pw.write(String.valueOf(addedToFav)); //true for being in favorite
				
				System.out.println("Restaurant added to favorites successfully!");
				
			} else {
				if("yes".equals(buttonCheck)) { //if only checking one item for button
					pw.write("true"); //true for being in favorite
					
					System.out.println("should return here for checkUser");
					return;
					
				}
				System.out.println("Restaurant already in favorites");
				addedToFav = true;
				pw.write(String.valueOf(addedToFav)); //true for being in favorite
			}
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	//for intercepting GET requests like retrieving favorites and checking if already in favorites
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		System.out.println("favorites servlet doGet");
		
		String username = request.getParameter("username");
        String sort = request.getParameter("sort");


		try {
			//get userID for the current user
			Integer userID = JDBCConnector.getUserID(username);
			
			//get all of the resIDs for favorites
			List<Integer> allResID = JDBCConnector.getResFromFav(userID);
			
			//get all the restaurants ordered depending on sorting option
			List<Restaurant> favoriteRestaurants = JDBCConnector.getRestaurants(allResID, sort);
			
			// Convert restaurants to JSON
	        Gson gson = new Gson();
	        String restaurantsJson = gson.toJson(favoriteRestaurants);

	        // Send JSON data to the front end
	        PrintWriter out = response.getWriter();
	        out.write(restaurantsJson);
	       
	        // Send the JSON data back to the front end
	        out.flush();
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
	//for intercepting DELETE requests like removing favorites 
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		System.out.println("favorites servlet doDelete");
		
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
		    buffer.append(line);
		}
		String data = buffer.toString();
		System.out.println("Received data: " + data);

		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(data, JsonObject.class);

		JsonObject resDetails = jsonObject.getAsJsonObject("resDetails");
		String resName = resDetails.get("name").getAsString();
		String address = resDetails.get("address").getAsString();
		
		String username = jsonObject.get("username").getAsString();

	    boolean removedFromFav;

	    try {
	        // Check if the restaurant exists in restaurant database
	        boolean restaurantExists = JDBCConnector.doesRestaurantExist(resName, address);

	        // If the restaurant exists, get its ID
	        if (restaurantExists) {
	            Integer resID = JDBCConnector.getRestaurantID(resName, address);
	            Integer userID = JDBCConnector.getUserID(username);

	            boolean favoriteExists = JDBCConnector.checkFavorite(userID, resID);

	            if (favoriteExists) {
	                JDBCConnector.removeFavorite(userID, resID);
	                removedFromFav = true;
	                
	                System.out.println("Restaurant removed from favorites successfully!");
	                
	                //return true if successfully removed from favorites
	                pw.write(String.valueOf(removedFromFav));

	                
	            } else {
	                System.out.println("Restaurant not found in favorites");
	            }
	        } else {
	            System.out.println("Restaurant does not exist in the database");
	        }
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }
		
	}
	
        
}

