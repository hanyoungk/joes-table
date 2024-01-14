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


@WebServlet("/YelpServlet")
public class YelpServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	protected void service(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		if("Submit".equals(request.getParameter("submit"))) {
			// Retrieve parameters from the request (if needed)
	        String searchTerm = request.getParameter("search-term");
	        String latitude = request.getParameter("latitudeInput");
	        String longitude = request.getParameter("longitudeInput");
	        String sort = request.getParameter("sort");
	        
	        System.out.println(searchTerm);
	        
	        //set content type and get PrintWriter object
	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        
	        //best_match, rating, review_count or distance.
			if("1".equals(sort)) {
			  sort = "best_match";
			} else if("2".equals(sort)) {
			  sort = "rating";
			} else if("3".equals(sort)) {
			  sort = "review_count";
			} else if("4".equals(sort)) {
			  sort = "distance";
			}
	        
	        // Call YelpAPI to get list of 10 restaurants
	        YelpAPI yelpAPI = new YelpAPI();
	        List<Restaurant> restaurants = yelpAPI.fetchRestaurants(searchTerm, latitude, longitude, sort);

	        // Convert restaurants to JSON
	        Gson gson = new Gson();
	        String restaurantsJson = gson.toJson(restaurants);

	        // Send JSON data to the front end
	        PrintWriter out = response.getWriter();
	        out.write(restaurantsJson);

	        // Send the JSON data back to the front end
	        out.flush();
	        
	        
		}
        
        
    }
}
