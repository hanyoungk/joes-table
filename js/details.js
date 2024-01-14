console.log("local storage: " + localStorage.getItem('username'));
//call getQueryParams
const queryParams = getQueryParams();
//call populateRestaurantDetails
populateRestaurantDetails(queryParams);

const resDetails = getRestaurantDetails();

let isFavorite;



//newly loaded page
document.addEventListener('DOMContentLoaded', function() {
	userLogin();
    
    checkIfFavorite(resDetails, function() {
        console.log("isFavorite after calling function: " + isFavorite);
        displayUserButtons(); // Call displayUserButtons after isFavorite is updated
    });
        
     
});

function userLogin() {
	console.log('hi');
	console.log("local storage: " + localStorage.getItem('username'));
    if(localStorage.getItem('loggedIn') === 'true') {
		//select the navbar links ul
		const navbarLinks = document.getElementById('navbar-links');
		
		navbarLinks.innerHTML = '';
		
		//create new links for logged-in users
		const loggedInLinks = `
			<li><a href="./home.html">Home</a></li>
	        <li><a href="./favorites.html">Favorites</a></li>
	        <li><a href="./reservations.html">Reservations</a></li>
	        <li><button id="logoutButton" class="nav-button">Logout</button></li>
		`;
		
		 navbarLinks.innerHTML = loggedInLinks;
		
		const logoutBtn = document.getElementById('logoutButton');
	    logoutBtn.addEventListener('click', function() {
	        localStorage.clear();
	        // Reload the page to show the logged-out version of the navbar
	        window.location.reload();
	    });
	
	}
}




function getRestaurantDetails() {
	return queryParams;
}



//check if restaurant is in favorites
function checkIfFavorite(resDetails, callback) {
	console.log('in check if favorite');
    const combinedData = {
		resDetails : resDetails, 
		username : localStorage.getItem('username'),
		forButton : 'yes',
	};
	
	fetch('FavoritesServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(combinedData)
    })
    .then(response => response.json())
    .then(data => {
		if(data && data.toString() === 'true') {
			console.log('is in favorite. should display remove button');
			isFavorite = true;
			console.log("isFavorite in check: " + isFavorite);
		} else {
			console.log('not in favorite. should display add button');
			isFavorite = false;
		}
		callback(); // Invoke the callback function once checkIfFavorite finishes
    })
    .catch(error => {
        console.error('Error:', error);
        throw error; // Rethrow the error for handling
    });
    
}

//add restaurant to favorites
function addToFavorites(resDetails) {
	const combinedData = {
		resDetails : resDetails, 
		username : localStorage.getItem('username'),
		forButton : 'no',
	};
	
	fetch('FavoritesServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(combinedData)
    })
    .then(response => {
	    if (!response.ok) {
	        return response.json().then(error => {
	            // Display error message to the user
	            alert(error); 
	            throw new Error(error);
	        });
	    }
	    return response.json();
	})
    .then(data => {
		if(data && data.toString() === 'false') {
			console.log('failed adding to favorites bc it already exists');
			isFavorite = true;
		} else {
			console.log('successfully inserted into favorites table');
			isFavorite = true; //set it to true in order to display remove button
		}
    })
    .catch(error => {
        console.error('Error:', error);
    });
	
	
}

//remove restaurant from favorites
function removeFavorites(resDetails) {
	const combinedData = {
		resDetails : resDetails, 
		username : localStorage.getItem('username'),
	};
	
	fetch('FavoritesServlet', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(combinedData)
    })
    .then(response => {
	    if (!response.ok) {
	        return response.json().then(error => {
	            // Display error message to the user
	            alert(error);
	            throw new Error(error);
	        });
	    }
	    return response.json();
	})
    .then(data => {
		if(data.toString() === 'false') {
			console.log('failed removing from favorites');
		} else {
			console.log('successfully removed from favorites');
			isFavorite = false;
		}
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

function addToReservations(reservationDetails) {
	
	fetch('ReservationsServlet', {
		method: 'POST',
		headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(reservationDetails)
	})
	.then(response => {
		if (!response.ok) {
                throw new Error('Failed to add reservation');
            }
            return response.json();
	})
	.then(data => {
        if(data.toString() === 'false') {
			console.log('failed to add reservation');
		} else {
			console.log('Reservation added successfully:', data);
		}
        
    })
    .catch(error => {
        console.error('Error adding reservation:', error);
    });
	
	
}

function displayUserButtons() {
    const userButtonsDiv = document.getElementById('userButtons');

    // Check if the user is logged in
    if (localStorage.getItem('loggedIn') === 'true') {
		
		console.log('inside if loggedin');
        const addToFavoritesButton = document.createElement('button');
        addToFavoritesButton.setAttribute('id', 'addToFavorites');
        
        console.log("isFavorite value: " + isFavorite);
        
        if (!isFavorite) {
			addToFavoritesButton.innerHTML = '&#xf005; Add to Favorites';
		} else {
            addToFavoritesButton.innerHTML = '&#xf005; Remove from Favorites';
        }

        // Add event listener for the Add to Favorites button
        addToFavoritesButton.addEventListener('click', function () {
			console.log('when button is clicked');
        	
        	//user is adding it in favorites
        	if(!isFavorite) {
				addToFavoritesButton.innerHTML = '&#xf005; Remove from Favorites';
				addToFavorites(resDetails);
			}
        	else {
				addToFavoritesButton.innerHTML = '&#xf005; Add to Favorites';
				removeFavorites(resDetails);
			}
        });
		
        const addReservationButton = document.createElement('button');
        addReservationButton.setAttribute('id', 'addReservation');
        addReservationButton.textContent = 'Add Reservation';

		
		//**CITATION**
		// how can i display input fields when button is clicked?" prompt (9 lines). 
		//	Chat GPT 3.5 version, OpenAI, Nov 30 2023, chat.openai.com/chat
        // Add event listener for the Add Reservation button
        addReservationButton.addEventListener('click', function () {
			//display user input fields
			const reservationContainer = document.createElement('div');
			reservationContainer.setAttribute('id', 'reservationContainer');
			
			const dateInput = document.createElement('input');
			dateInput.setAttribute('type', 'text');
	        dateInput.setAttribute('id', 'reservationDate');
	        dateInput.setAttribute('placeholder', 'Date');
	        dateInput.setAttribute('required', '');
	        reservationContainer.appendChild(dateInput);
	        
	        const timeInput = document.createElement('input');
	        timeInput.setAttribute('type', 'text');
	        timeInput.setAttribute('id', 'reservationTime');
	        timeInput.setAttribute('placeholder', 'Time');
	        timeInput.setAttribute('required', '');
	        reservationContainer.appendChild(timeInput);
	
	        const notesInput = document.createElement('textarea');
	        notesInput.setAttribute('rows', '2');
	        notesInput.setAttribute('cols', '30');
	        notesInput.setAttribute('type', 'text');
	        notesInput.setAttribute('id', 'reservationNotes');
	        notesInput.setAttribute('placeholder', 'Reservation Notes');
	        notesInput.setAttribute('required', '');
	        notesInput.style.resize = 'vertical';
	        reservationContainer.appendChild(notesInput);
	
	        const submitButton = document.createElement('button');
	        submitButton.setAttribute('id', 'reservationSubmit');
	        submitButton.textContent = 'Submit Reservation';
	        reservationContainer.appendChild(submitButton);
	        
	        userButtonsDiv.appendChild(reservationContainer);
	        
	        // Event listeners for input focus to remove placeholder on click
		    dateInput.addEventListener('focus', function () {
				dateInput.setAttribute('type', 'date');
		        dateInput.setAttribute('placeholder', '');
		    });
		
		    timeInput.addEventListener('focus', function () {
				timeInput.setAttribute('type', 'time');
		        timeInput.setAttribute('placeholder', '');
		    });
	        
	        //listen to the submit button
	        submitButton.addEventListener('click', function () {
	            const selectedDate = dateInput.value;
	            const selectedTime = timeInput.value;
	            const notes = notesInput.value;
	
	            // Validate the inputs
	            if (!selectedDate || !selectedTime) {
	                alert('Please select date and time.');
	                return; 
	            }
	
	             //details to send over to servlet
	             const reservationDetails = {
					 resDetails: resDetails,
					 username : localStorage.getItem('username'),
					 date : selectedDate,
					 time : selectedTime,
					 notes : notes
				 };
				 
				 //call function to handle sending info through fetch api
				 addToReservations(reservationDetails);
				 
				 //hide the input fields
				 reservationContainer.style.display = 'none';
	             
	        });
        
        });

		console.log('before appending');
        // Append buttons to the userButtonsDiv
        userButtonsDiv.appendChild(addToFavoritesButton);
        userButtonsDiv.appendChild(addReservationButton);
    }
}

function getQueryParams() {
    const params = new URLSearchParams(window.location.search);
    return {
        name: params.get('name'),
        address: params.get('address'),
        phone: params.get('phone'),
        imageURL: params.get('imageURL'),
        cuisine: params.get('cuisine'),
        price: params.get('price'),
        rating: params.get('rating'),
        url: params.get('url')
    };
}

function populateRestaurantDetails(restaurant) {
    document.getElementById('title').textContent = restaurant.name;
    
    document.getElementById('restaurant-address').textContent = 'Address: ' + restaurant.address;
    document.getElementById('restaurant-phone').textContent = 'Phone No. ' + restaurant.phone;
    
    const cuisineElement = document.getElementById('restaurant-cuisine');

    //**CITATION**
	// "how to remove double quotes from a string" prompt (1 line). 
	//	Chat GPT 3.5 version, OpenAI, Nov 26 2023, chat.openai.com/chat
    const cuisineWithoutQuotes = restaurant.cuisine.replace(/"/g, ''); // Removes all double quotes
    // Set the cuisine text content without quotation marks
    cuisineElement.textContent = 'Cuisine: ' + cuisineWithoutQuotes;

    document.getElementById('restaurant-price').textContent = 'Price: ' + restaurant.price;
    
    const ratingElement = document.getElementById('restaurant-rating');
    ratingElement.innerHTML = 'Rating: ';

    const rating = parseFloat(restaurant.rating);

	//**CITATION**
	// "how to add star icons according to the number. if has decimals, put half star icon" prompt (6 lines). 
	//	Chat GPT 3.5 version, OpenAI, Nov 12 2023, chat.openai.com/chat
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating % 1 !== 0;

    for (let i = 0; i < fullStars; i++) {
		ratingElement.innerHTML += '&#xf005';
    }

    if (hasHalfStar) {
		ratingElement.innerHTML += '&#xf089';
    }


	const restaurantImage = document.querySelector('.restaurant-image');
    restaurantImage.src = restaurant.imageURL;
    
    
    // Set image link (Yelp URL)
    const restaurantImageLink = document.querySelector('.restaurant-image-link');
    restaurantImageLink.href = restaurant.url;

}

//submission from the results html page
document.getElementById('searchForm').addEventListener('submit', function (event) {
    event.preventDefault(); // Prevent default form submission
    
    // Get form values
    let searchTerm = document.getElementById('search-term').value;
    let latitude = document.getElementById('latitudeInput').value;
    let longitude = document.getElementById('longitudeInput').value;
    let sort = document.querySelector('input[name="sort"]:checked').value;
    
    const queryParams = {
        searchTerm: searchTerm,
        latitude: latitude,
        longitude: longitude,
        sort: sort
    };

    
    updateURL(queryParams);
});

// Function to update URL using history.pushState 
function updateURL(queryParams) {
    const url = `results.html?search-term=${queryParams.searchTerm}&submit=Submit&latitudeInput=${queryParams.latitude}&longitudeInput=${queryParams.longitude}&sort=${queryParams.sort}`;
    history.pushState({}, '', url);
    window.location.href = url; // Redirect to the new URL
}


// Initialize and add the map
let map;
let initialPosition = { lat: 34.02116, lng: -118.287132 };
let initialZoom = 4;

async function initMap() {
    console.log("initMap function called!");
  const position = { lat: 34.02116, lng: -118.287132 };
  const { Map } = await google.maps.importLibrary("maps");
  const { AdvancedMarkerElement } = await google.maps.importLibrary("marker");

  // The map, centered at initial position
  map = new Map(document.getElementById("map"), {
    zoom: initialZoom,
    center: initialPosition,
    mapId: "DEMO_MAP_ID",
  });

  // The marker, positioned at initial position
  const marker = new AdvancedMarkerElement({
    map: map,
    position: initialPosition,
    title: "USC",
  });

  //**CITATION**
	// how can i use google maps api marker to make it close when clicked" prompt (12 lines). 
	//	Chat GPT 3.5 version, OpenAI, Nov 12 2023, chat.openai.com/chat
  // This event listener calls addMarker() when the map is clicked.
  google.maps.event.addListener(map, "click", (event) => {
    var myMarker = new google.maps.Marker({
      position: event.latLng,
      map: map,
    });
    document.getElementById("latitudeInput").value = myMarker.getPosition().lat();
    document.getElementById("longitudeInput").value = myMarker.getPosition().lng();

    myMarker.setMap(null);
    map.setCenter(initialPosition);
    map.setCenter(initialZoom);
    closeMapModal();
    initMap();
  });
  // Add a marker at the center of the map.
  addMarker(position, map);
}

initMap();

document.getElementById("openMapButton").addEventListener("click", function () {
    openMapModal();
});

function openMapModal() {
    document.getElementById("mapModal").style.display = "block";

    initMap();
}

function closeMapModal() {
    document.getElementById("mapModal").style.display = "none";

    if (map) {
      map.dispose();
    }
    
    
} //END closeMapModal
