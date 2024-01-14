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

import com.google.gson.JsonObject;
import com.google.gson.Gson;



@WebServlet("/ReservationsServlet")
public class ReservationsServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	//for intercepting POST requests
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		
		System.out.println("in doPost for reservation servlet");
		
		PrintWriter pw = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
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
		String phone = resDetails.get("phone").getAsString();
		String imageURL = resDetails.get("imageURL").getAsString();
		String cuisine = resDetails.get("cuisine").getAsString();
		String price = resDetails.get("price").getAsString();
		float rating = Float.parseFloat(resDetails.get("rating").getAsString());
		String url = resDetails.get("url").getAsString();
		
		String username = jsonObject.get("username").getAsString();
		String date = jsonObject.get("date").getAsString();
		String time = jsonObject.get("time").getAsString();

		
		String notes = jsonObject.get("notes").getAsString();
		
		boolean addedToReservation = false;
		
		
		try {
			boolean restaurantExists = JDBCConnector.doesRestaurantExist(resName, address);
			System.out.println("restaurantExists: " + restaurantExists);
			
			if(!restaurantExists) {
				//add it to the restaurants table before adding it to favorites
				JDBCConnector.addRestaurant(resName, address, phone, price, rating, cuisine, imageURL, url);
			} 
			
			//get resID for that restaurant
			Integer resID = JDBCConnector.getRestaurantID(resName, address);
			//get userID for the current user
			Integer userID = JDBCConnector.getUserID(username);
			
			
			//dont need to check if the userID and resID exists in the database
			
			
			//add to reservation
			JDBCConnector.addNewReservation(userID, resID, date, time, notes);
			addedToReservation = true;
			pw.write(String.valueOf(addedToReservation)); //true for being in reservations
			
			System.out.println("successfully reserved: " + resName);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
	}
	
	//for intercepting GET requests
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		System.out.println("reservation servlet doGet");
		
		String username = request.getParameter("username");
        String sort = request.getParameter("sort");


		try {
			//get userID for the current user
			Integer userID = JDBCConnector.getUserID(username);
			
			
			//call function that combines reservation information and the restuarant information for the user
			List<ReservationWithRestaurant> allResRes = JDBCConnector.getReservationsWithRestaurant(userID, sort);
			
			// Convert restaurants to JSON
	        Gson gson = new Gson();
	        String restaurantsJson = gson.toJson(allResRes);

	        // Send JSON data to the front end
	        PrintWriter out = response.getWriter();
	        out.write(restaurantsJson);
	        
	        // Send the JSON data back to the front end
	        out.flush();
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}
	
        
        
}

