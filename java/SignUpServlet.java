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



@WebServlet("/SignupServlet")
public class SignUpServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	//for intercepting POST requests
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		PrintWriter pw = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		System.out.println("signup servlet");
		
		Gson gson = new Gson();
		
		BufferedReader reader = request.getReader();
	
	    // Deserialize JSON to Java object
	    User user = gson.fromJson(reader, User.class);

	    String username = user.getUsername();
	    String password = user.getPassword();
	    String email = user.getEmail();

		if(username == null || username.isBlank() 
				|| password == null || password.isBlank()
				|| email == null || email.isBlank()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String error = "User info is missing";
			pw.write(gson.toJson(error));
			pw.flush();
		}
	        
		
		int userID;
		
		try {
			userID = registerUser(username, password, email);
			
			if(userID == -1) {
				System.out.println("username already in use");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				String error = "Username is already in use";
				pw.write(gson.toJson(error));
				pw.flush();
			} else if(userID == -2) {
				System.out.println("email already use");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				String error = "Email is already in use";
				pw.write(gson.toJson(error));
				pw.flush();
			} else {
				System.out.println("no problem");
				response.setStatus(HttpServletResponse.SC_OK);
				pw.write(gson.toJson(userID));
				pw.flush();
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	//for intercepting GET requests
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
	}
	
	public static int registerUser(String username, String password, String email) throws ClassNotFoundException {
		int userID = 0;

		//call JDBC connector that calls the sql
		if(JDBCConnector.isExistingEmail(email)) {
			//if username is already in use
			return -2;
		} else if(JDBCConnector.isExistingUsername(username)) {
			//if email is already in use
			return -1;
		}
		
		//after authenticating the user, insert a new user into the table (sign up feature)
		JDBCConnector.insertNewUser(username, password, email);
		
		userID = JDBCConnector.getUserID(username);

		return userID;
	}
        
        
}

