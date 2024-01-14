import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mysql.cj.jdbc.Driver;

public class JDBCConnector {
	
	//gets the userID corresponding to username
	public static int getUserID(String username) {
		Connection conn = null;
		PreparedStatement st = null;
		int userID = -1; //default value
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=zheldtnlqek0.");
			
			st = conn.prepareStatement("SELECT userID FROM Users WHERE username = ?");
			
			st.setString(1, username);
			
			ResultSet resultSet = st.executeQuery();
			
			//if there is a result
			if(resultSet.next()) {
				userID = resultSet.getInt("userID");
			}
			
		} catch(SQLException sqle) {
			System.out.println(sqle.getMessage());
			
		} catch(ClassNotFoundException ex) {
			System.out.println("MySQL Driver not found!");
		} finally {
			try {
				if(st != null) {
					st.close();
				} if(conn != null) {
					conn.close();
				}
			} catch(SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
		return userID;
	}
	
	//for SignUpServlet
	// Check if the username exists in the database
	public static boolean isExistingUsername(String username) throws ClassNotFoundException{
		Connection conn = null;
		PreparedStatement st = null;
		
        try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=zheldtnlqek0.");
			
			st = conn.prepareStatement("SELECT * FROM Users WHERE username = ?");
            
			
			st.setString(1, username);
            ResultSet resultSet = st.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Return true in case of error for safety
        }
    }
	
	//for SignUpServlet
	// Check if the email exists in the database
	public static boolean isExistingEmail(String email) throws ClassNotFoundException {
		Connection conn = null;
		PreparedStatement st = null;
		try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=zheldtnlqek0.");
			
			st = conn.prepareStatement("SELECT * FROM Users WHERE email = ?");
            
			
			st.setString(1, email);
            ResultSet resultSet = st.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Return true in case of error for safety
        }
    }
	
	//@POST for LoginServlet
	// Method to authenticate user login
    public static boolean authenticateUser(String username, String password) throws ClassNotFoundException {
    	Connection conn = null;
		PreparedStatement st = null;
    	
    	try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=zheldtnlqek0.");
            
            st = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            
            st.setString(1, username);
            st.setString(2, password);

            ResultSet resultSet = st.executeQuery();
            return resultSet.next(); // If result exists, user is authenticated
            
        } catch (SQLException sqle) {
        	System.out.println(sqle.getMessage());
            return false;
        }
    }
    
	/*
	 * @POST for SignUpServlet
	 * adding new user into database after successful sign up(valid email, unique username, etc.)
	 */
    public static void insertNewUser(String username, String password, String email) throws ClassNotFoundException {
		Connection conn = null;
		PreparedStatement st = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=zheldtnlqek0.");
			
			st = conn.prepareStatement("INSERT INTO Users (username, password, email) VALUES (?, ?, ?)");
			
			st.setString(1, username);
			st.setString(2, password);
			st.setString(3, email);
			
			
			st.executeUpdate();
			
			System.out.println("finished insertNewUser");
		
			
		} catch(SQLException sqle) {
			System.out.println(sqle.getMessage());
		} catch(ClassNotFoundException ex) {
			System.out.println("MySQL Driver not found!");
		} finally {
			try {
				if(st != null) {
					st.close();
				} if(conn != null) {
					conn.close();
				}
			} catch(SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
	}
	
    //@POST
	//TODO: make a function to add reservations
    public static void addNewReservation(Integer userID, Integer resID, String date, String time, String note) throws ClassNotFoundException{
		Connection conn = null;
		PreparedStatement st = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=zheldtnlqek0.");
			
			st = conn.prepareStatement("INSERT INTO Reservations (userID, resID, date, time, note) VALUES (?, ?, ?, ?, ?)");
			
			
			st.setInt(1, userID);
			st.setInt(2, resID);
			st.setString(3, date);
			st.setString(4, time);
			st.setString(5, note);
			st.executeUpdate();
			
		} catch(SQLException sqle) {
			System.out.println(sqle.getMessage());
		} catch(ClassNotFoundException ex) {
			System.out.println("MySQL Driver not found!");
		} finally {
			try {
				if(st != null) {
					st.close();
				} if(conn != null) {
					conn.close();
				}
			} catch(SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
		
	}
	
    //@GET
	//TODO: make a function to retrieve reservations
    public static ResultSet getReservation(Integer userID) throws ClassNotFoundException{
		Connection conn = null;
		PreparedStatement st = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=zheldtnlqek0.");
			
			st = conn.prepareStatement("SELECT * FROM Reservations WHERE userID = ?");
			
			//set the userID to current user
			st.setInt(1, userID);
			
			//return the ResultSet of all of the reservations for user
			return st.executeQuery();
			
		} catch(SQLException sqle) {
			System.out.println(sqle.getMessage());
		} catch(ClassNotFoundException ex) {
			System.out.println("MySQL Driver not found!");
		} finally {
			try {
				if(st != null) {
					st.close();
				} if(conn != null) {
					conn.close();
				}
			} catch(SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
		return null;
		
	}
    
    //for Favoties Servlet
    //Check if a restaurant (resID) is in a user's favorites (userID)
    public static boolean checkFavorite(int userID, int resID) {
        Connection conn = null;
        PreparedStatement st = null;
        boolean isFavorite = false;

        try {
        	
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=zheldtnlqek0.");

            st = conn.prepareStatement("SELECT * FROM Favorites WHERE userID = ? AND resID = ?");
            st.setInt(1, userID);
            st.setInt(2, resID);

            ResultSet resultSet = st.executeQuery();
            isFavorite = resultSet.next(); // If the query returns a result, it means it's a favorite
        } catch(SQLException sqle) {
			System.out.println(sqle.getMessage());
		} catch(ClassNotFoundException ex) {
			System.out.println("MySQL Driver not found!");
		} finally {
			try {
				if(st != null) {
					st.close();
				} if(conn != null) {
					conn.close();
				}
			} catch(SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
        return isFavorite;
    }
	
    //@POST for favorites servlet
	//TODO: make a function to add favorites
    public static void addFavorite(Integer userID, Integer resID) throws ClassNotFoundException{
		Connection conn = null;
		PreparedStatement st = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=zheldtnlqek0.");
			
			st = conn.prepareStatement("INSERT INTO Favorites (userID, resID) VALUES (?, ?)");
			
			
			st.setInt(1, userID);
			st.setInt(2, resID);
			st.executeUpdate();
			
		} catch(SQLException sqle) {
			System.out.println(sqle.getMessage());
		} catch(ClassNotFoundException ex) {
			System.out.println("MySQL Driver not found!");
		} finally {
			try {
				if(st != null) {
					st.close();
				} if(conn != null) {
					conn.close();
				}
			} catch(SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
		
	}
	
	//@POST for favorites servlet
	//TODO: make a function to remove favorites
    public static void removeFavorite(Integer userID, Integer resID) throws ClassNotFoundException{
		Connection conn = null;
		PreparedStatement st = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=zheldtnlqek0.");
			
			st = conn.prepareStatement("DELETE FROM Favorites WHERE userID = ? AND resID = ?");
			
			
			st.setInt(1, userID);
			st.setInt(2, resID);
			st.executeUpdate();
			
		} catch(SQLException sqle) {
			System.out.println(sqle.getMessage());
		} catch(ClassNotFoundException ex) {
			System.out.println("MySQL Driver not found!");
		} finally {
			try {
				if(st != null) {
					st.close();
				} if(conn != null) {
					conn.close();
				}
			} catch(SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
		
	}
	
    //@GET for favorites servlet
	//TODO: make a function to retrieve favorites
    public static ResultSet getFavorites(Integer userID) throws ClassNotFoundException{
		Connection conn = null;
		PreparedStatement st = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=zheldtnlqek0.");
			
			st = conn.prepareStatement("SELECT * FROM Favorites WHERE userID = ?");
			
			//set the userID to current user
			st.setInt(1, userID);
			
			//return the ResultSet of all of the favorites for user
			return st.executeQuery();
			
		} catch(SQLException sqle) {
			System.out.println(sqle.getMessage());
		} catch(ClassNotFoundException ex) {
			System.out.println("MySQL Driver not found!");
		} finally {
			try {
				if(st != null) {
					st.close();
				} if(conn != null) {
					conn.close();
				}
			} catch(SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
		return null;
		
	}
    
    //for favorites servlet
    //gets all the resIDs associated with the userID from Favorites
    public static List<Integer> getResFromFav(Integer userID) throws ClassNotFoundException {
        Connection conn = null;
        PreparedStatement st = null;
        List<Integer> allResID = new ArrayList<>();
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=zheldtnlqek0.");
            
            st = conn.prepareStatement("SELECT resID FROM Favorites WHERE userID = ?");
            st.setInt(1, userID);
            
            ResultSet resultSet = st.executeQuery();
            
            while (resultSet.next()) {
            	Integer resID = resultSet.getInt("resID");
                allResID.add(resID);
            }
            
        } catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        } finally {
            try {
                if(st != null) {
                    st.close();
                } if(conn != null) {
                    conn.close();
                }
            } catch(SQLException sqle) {
                System.out.println(sqle.getMessage());
            }
        }
        return allResID;
    }
    
    //for reservations servlet. prob didnt use
    //gets all the resIDs associated with the userID from Favorites
    public static List<Integer> getResFromReservations(Integer userID) throws ClassNotFoundException {
        Connection conn = null;
        PreparedStatement st = null;
        List<Integer> allResID = new ArrayList<>();
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=zheldtnlqek0.");
            
            st = conn.prepareStatement("SELECT resID FROM Reservations WHERE userID = ?");
            st.setInt(1, userID);
            
            ResultSet resultSet = st.executeQuery();
            
            while (resultSet.next()) {
            	Integer resID = resultSet.getInt("resID");
                allResID.add(resID);
            }
            
        } catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        } finally {
            try {
                if(st != null) {
                    st.close();
                } if(conn != null) {
                    conn.close();
                }
            } catch(SQLException sqle) {
                System.out.println(sqle.getMessage());
            }
        }
        return allResID;
    }
    
    //@POST for reservations or favorites servlet
    //adds restaurant info into the restaurant table if it isnt already exsisting
    public static void addRestaurant(String name, String address, String phone, String price, float rating, String cuisine, String imageURL, String url) throws ClassNotFoundException {
        Connection conn = null;
        PreparedStatement st = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=zheldtnlqek0.");
            
            st = conn.prepareStatement("INSERT INTO Restaurants (name, address, phone, price, rating, cuisine, imageURL, url) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            
            st.setString(1, name);
            st.setString(2, address);
            st.setString(3, phone);
            st.setString(4, price);
            st.setFloat(5, rating);
            st.setString(6, cuisine);
            st.setString(7, imageURL);
            st.setString(8, url);
            
            st.executeUpdate();
            
            System.out.println("finished inserting restaurant into Restaurants");
            
        } catch(SQLException sqle) {
			System.out.println(sqle.getMessage());
		} catch(ClassNotFoundException ex) {
			System.out.println("MySQL Driver not found!");
		} finally {
			try {
				if(st != null) {
					st.close();
				} if(conn != null) {
					conn.close();
				}
			} catch(SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
    }
    
    
    //for favorites and reservations servlet
    // Check if a restaurant with given name and address exists
    public static boolean doesRestaurantExist(String name, String address) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        boolean exists = false;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=zheldtnlqek0.");
            
            st = conn.prepareStatement("SELECT * FROM Restaurants WHERE name = ? AND address = ?");
            st.setString(1, name);
            st.setString(2, address);
            
            rs = st.executeQuery();
            
            exists = rs.next(); // Restaurant exists if there's at least one row
            
        } catch(SQLException sqle) {
			System.out.println(sqle.getMessage());
			
		} catch(ClassNotFoundException ex) {
			System.out.println("MySQL Driver not found!");
		} finally {
			try {
				if(st != null) {
					st.close();
				} if(conn != null) {
					conn.close();
				}
			} catch(SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
		return exists;
    }
    
    
    //gets the resID from restaurants using name address
    public static Integer getRestaurantID(String name, String address) throws ClassNotFoundException {
        Connection conn = null;
        PreparedStatement st = null;
        int resID = -1; // default value
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=zheldtnlqek0.");
            
            st = conn.prepareStatement("SELECT resID FROM Restaurants WHERE name = ? AND address = ?");
            st.setString(1, name);
            st.setString(2, address);
            
            ResultSet resultSet = st.executeQuery();
            
            if (resultSet.next()) {
                resID = resultSet.getInt("resID");
            }
            
        } catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        } finally {
            try {
                if(st != null) {
                    st.close();
                } if(conn != null) {
                    conn.close();
                }
            } catch(SQLException sqle) {
                System.out.println(sqle.getMessage());
            }
        }
        return resID;
    }
    
    //gets restaurants by sorting option
    //used for getting a user's favorites
    public static List<Restaurant> getRestaurants(List<Integer> allResID, String sort) throws ClassNotFoundException {
        Connection conn = null;
        PreparedStatement st = null;
        List<Restaurant> restaurants = new ArrayList<>();
        ResultSet resultSet = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=zheldtnlqek0.");
            
            
            //**CITATION**
    	    // "how can get all restaurants in the list of resID's in the sorting options" prompt (10 lines). 
    	    //Chat GPT 3.5 version, OpenAI, Nov 30 2023, chat.openai.com/chat
            String query = "SELECT * FROM Restaurants WHERE resID IN (";
            for (int i = 0; i < allResID.size(); i++) {
                query += "?";
                if (i < allResID.size() - 1) {
                    query += ",";
                }
            }
            query += ")";
            
            //sorting option based on sort
        	switch(sort) {
        	case "aToZ":
        		query += " ORDER BY name ASC";
        		break;
        	case "zToA":
        		query += " ORDER BY name DESC";
        		break;
        	case "highestRating":
        		query += " ORDER BY rating DESC";
        		break;
        	case "lowestRating":
        		query += " ORDER BY rating ASC";
        		break;
        	case "mostRecent":
        		query += " ORDER BY resID DESC";
        	default:
        		break;
        	}
        	
        	st = conn.prepareStatement(query);
        	
        	// Set resIDs as parameters
            for (int i = 0; i < allResID.size(); i++) {
            	st.setInt(i + 1, allResID.get(i));
            }

            resultSet = st.executeQuery();
            
        	while(resultSet.next()) {
        		//populate Restaurant object and add it to the list
        		Restaurant restaurant = new Restaurant();
                restaurant.setName(resultSet.getString("name"));
                restaurant.setPhone(resultSet.getString("phone"));
                restaurant.setAddress(resultSet.getString("address"));
                restaurant.setPrice(resultSet.getString("price"));
                restaurant.setCuisine(resultSet.getString("cuisine"));
                restaurant.setRating(resultSet.getDouble("rating"));
                restaurant.setImageURL(resultSet.getString("imageURL"));
                restaurant.setUrl(resultSet.getString("url"));

                restaurants.add(restaurant);
        	}
            
            
            
        } catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        } finally {
            try {
                if(st != null) {
                    st.close();
                } if(conn != null) {
                    conn.close();
                }
            } catch(SQLException sqle) {
                System.out.println(sqle.getMessage());
            }
        }
        return restaurants;
    }
    
   
    
    public static List<ReservationWithRestaurant> getReservationsWithRestaurant(int userID, String sort) throws ClassNotFoundException {
        Connection conn = null;
        PreparedStatement st = null;
        List<ReservationWithRestaurant> reservationsWithRestaurants = new ArrayList<>();
        ResultSet resultSet = null;
        
        try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/assignment4?user=root&password=zheldtnlqek0.");
            
            //**CITATION**
    	    // "how can i combine reservation information with restaurant information" prompt (10 lines). 
    	    //Chat GPT 3.5 version, OpenAI, Nov 30 2023, chat.openai.com/chat
            String query = "SELECT r.reserveID, r.userID, r.resID, DATE(r.date) as date, TIME_FORMAT(r.time, '%H:%i') as time, r.note, res.name AS resName, res.phone, res.address, res.price, res.cuisine, res.rating, res.imageURL, res.url " +
                            "FROM Reservations r " +
                            "JOIN Restaurants res ON r.resID = res.resID " +
                            "WHERE r.userID = ?";

            // Adjust sorting based on sorting option
            if ("mostRecent".equals(sort)) {
                query += " ORDER BY date ASC, time ASC";
            } else if ("leastRecent".equals(sort)) {
                query += " ORDER BY date DESC, time DESC";
            }

            st = conn.prepareStatement(query);
            st.setInt(1, userID);

            resultSet = st.executeQuery();

            while (resultSet.next()) {
                ReservationWithRestaurant reservationWithRestaurant = new ReservationWithRestaurant();
                reservationWithRestaurant.setDate(resultSet.getString("date"));
                reservationWithRestaurant.setTime(resultSet.getString("time"));
                reservationWithRestaurant.setNotes(resultSet.getString("note"));

                // Create a Restaurant object and set its details
                Restaurant restaurant = new Restaurant();
                restaurant.setName(resultSet.getString("resName"));
                restaurant.setPhone(resultSet.getString("phone"));
                restaurant.setAddress(resultSet.getString("address"));
                restaurant.setPrice(resultSet.getString("price"));
                restaurant.setCuisine(resultSet.getString("cuisine"));
                restaurant.setRating(resultSet.getDouble("rating"));
                restaurant.setImageURL(resultSet.getString("imageURL"));
                restaurant.setUrl(resultSet.getString("url"));

                // Set the Restaurant object within ReservationWithRestaurant
                reservationWithRestaurant.setRestaurant(restaurant);
                
                reservationsWithRestaurants.add(reservationWithRestaurant);
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        } finally {
        	try {
                if(st != null) {
                    st.close();
                } if(conn != null) {
                    conn.close();
                }
            } catch(SQLException sqle) {
                System.out.println(sqle.getMessage());
            }
        }
        return reservationsWithRestaurants;
    }
    
}


