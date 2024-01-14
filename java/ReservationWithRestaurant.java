//**CITATION**
// "how can i combine the reservation information with restaurant information" prompt (5 lines). 
//	Chat GPT 3.5 version, OpenAI, Nov 29 2023, chat.openai.com/chat
public class ReservationWithRestaurant {
	private String date;
	private String time;
	private String notes;
	
	private Restaurant restaurant; // Embedded Restaurant object
	

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
}
