import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        BufferedReader reader = request.getReader();
        PrintWriter pw = response.getWriter();

        // Deserialize JSON to Java object
        User user = gson.fromJson(reader, User.class);

        String username = user.getUsername();
        String password = user.getPassword();

        // Perform login authentication (Check if username and password are valid)
        boolean loginSuccessful;
        int userID;
        
		try {
			//true if username & password matches
			loginSuccessful = JDBCConnector.authenticateUser(username, password);
			
			if (loginSuccessful) {
	            // Set session attributes or user authentication tokens
	            HttpSession session = request.getSession();
	            session.setAttribute("username", username);

	            // Send success response back to the frontend
	            JsonObject jsonResponse = new JsonObject();
	            jsonResponse.addProperty("loggedIn", true);
	            response.getWriter().write(jsonResponse.toString());
	        } else {
	            // Send failure response back to the frontend
	            JsonObject jsonResponse = new JsonObject();
	            System.out.println("login invalid credentials");
	            jsonResponse.addProperty("loggedIn", false);
	            jsonResponse.addProperty("error", "Invalid credentials");
	            userID = -1;
	            
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				String error = "Invalid username or password";
				pw.write(gson.toJson(error));
				pw.flush();
	        }
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Replace with your authentication logic

        
    }
}
