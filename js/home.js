// Initialize and add the map
let map;
let initialPosition = { lat: 34.02116, lng: -118.287132 };
let initialZoom = 4;
console.log('hi');
console.log("local storage: " + localStorage.getItem('username'));


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



document.addEventListener('DOMContentLoaded', function() {
	userLogin();
});



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
    
}



document.getElementById('searchForm').addEventListener('submit', function (event) {
    event.preventDefault(); // Prevent default form submission
    
    // Get form values
    let searchTerm = document.getElementById('search-term').value;
    let latitude = document.getElementById('latitudeInput').value;
    let longitude = document.getElementById('longitudeInput').value;
    let sort = document.querySelector('input[name="sort"]:checked').value;

    // Construct the URL dynamically
    let url = `YelpServlet?search-term=${searchTerm}&submit=Submit&latitudeInput=${latitude}&longitudeInput=${longitude}&sort=${sort}`;

    // Fetch data based on user input
    fetch(url, {
        method: 'GET'
    })
    .then(response => response.json())
    .then(data => {
        // Process JSON data and update HTML
        console.log("processing JSON data");
        updateHTML(data);
    })
    .catch(error => {
        console.error('Request failed', error);
    });
    
    // Clear input fields after fetching data
    clearInputFields();
});

// Process JSON data and update HTML
function updateHTML(data) {
    let resultsList = document.getElementById('results-list');
    resultsList.innerHTML = ''; // Clear previous results

    // Loop through the received data (restaurants) and create HTML elements for each
    data.forEach(restaurant => {
        // Create elements for restaurant details
        let restaurantDiv = document.createElement('div');
        restaurantDiv.classList.add('restaurant-item');

        let name = document.createElement('h2');
        name.textContent = restaurant.name;

        let cuisine = document.createElement('p');
        cuisine.textContent = `Cuisine: ${restaurant.cuisine}`;

        let rating = document.createElement('p');
        rating.textContent = `Rating: ${restaurant.rating}`;

        // Append elements to the restaurantDiv
        restaurantDiv.appendChild(name);
        restaurantDiv.appendChild(cuisine);
        restaurantDiv.appendChild(rating);

        // Append restaurantDiv to resultsList
        resultsList.appendChild(restaurantDiv);
    });
}

function clearInputFields() {
    document.getElementById('search-term').value = '';
    document.getElementById('latitudeInput').value = '';
    document.getElementById('longitudeInput').value = '';
    // Clear the radio button selection if needed
    document.querySelector('input[name="sort"]:checked').checked = false;
}









