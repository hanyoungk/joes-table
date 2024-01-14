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

//newly loaded results page
document.addEventListener('DOMContentLoaded', function() {
	userLogin();

	//default sorting option is most recent to least
	const defaultSort = 'mostRecent';
	const params = new URLSearchParams(window.location.search);
	const sort = params.get("sort");
	if(sort === null) {
		getReservations(defaultSort);
	} else {
		getReservations(sort);
	}
});

//initial getFavorites
function getReservations(sort) {
	const username = localStorage.getItem('username');
	
	fetch(`ReservationsServlet?username=${username}&sort=${sort}`, {
		method: 'GET'
	})
	.then(response => response.json())
	.then(data => {
		//update html with the JSON data
		console.log("processing JSON data in favorites");
		updateHTML(data);
	})
	.catch(error => {
		console.error('get request failed', error);
	})
}

// Function to parse query parameters from URL
function getQueryParams() {
    const params = new URLSearchParams(window.location.search);
    return {
        searchTerm: params.get('search-term'),
        latitude: params.get('latitudeInput'),
        longitude: params.get('longitudeInput'),
        sort: params.get('sort')
    };
}

// Function to update URL using history.pushState 
function updateURL(queryParams) {
    const url = `results.html?search-term=${queryParams.searchTerm}&submit=Submit&latitudeInput=${queryParams.latitude}&longitudeInput=${queryParams.longitude}&sort=${queryParams.sort}`;
    history.pushState({}, '', url);
    window.location.href = url; // Redirect to the new URL
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


function toggleDropdown() {
    document.getElementById("myDropdown").classList.toggle("show");
    console.log("should show content items in dropdown");
 }
 // Function to attach event listeners to the dropdown
function attachDropdownListeners() {
    const sorting = document.getElementById('myDropdown');

    sorting.addEventListener('change', function () {
        const newSort = sorting.value;
        getFavorites(newSort);
        window.location.reload();
    });
}

// Process JSON data and update HTML
function updateHTML(data) {
	const resultsTitle = document.getElementById('title');
	resultsTitle.innerHTML = localStorage.getItem('username')+ '\'s Reservations';
	
	const dropdownHTML = `
		<div class="dropdown" id="dropdownDiv">
		    <button onclick="myFunction()" class="btn">&#xf161; Sort By  &#xf0d7</button>
		    <div id="myDropdown" class="dropdown-item">
		      <a href="#" value="mostRecent">Most Recent</a>
		      <a href="#" value="leastRecent">Least Recent</a>
		    </div>
		    
		    <script>
			  function myFunction() {
			    document.getElementById("myDropdown").classList.toggle("show");
			    console.log("should show content items in dropdown");
			  }
			</script>
		</div>
		`;
	
	resultsTitle.innerHTML += dropdownHTML;

	
    let resultsList = document.getElementById('reservations-list');
    resultsList.innerHTML = ''; // Clear previous results

    // Loop through the received data (restaurants) and create HTML elements for each
    data.forEach(reservation => {
        //elements for restaurant details
        let restaurantDiv = document.createElement('div');
        restaurantDiv.classList.add('restaurant-item');
        
        let restaurant = reservation.restaurant;
        
        //redirects to the Yelp page
        let imageLink = document.createElement('a');
        imageLink.href = `details.html?name=${encodeURIComponent(restaurant.name)}&address=${encodeURIComponent(restaurant.address)}&phone=${encodeURIComponent(restaurant.phone)}&imageURL=${encodeURIComponent(restaurant.imageURL)}&cuisine=${encodeURIComponent(restaurant.cuisine)}&price=${encodeURIComponent(restaurant.price)}&rating=${encodeURIComponent(restaurant.rating)}&url=${encodeURIComponent(restaurant.url)}`;
        imageLink.classList.add('restaurant-image-link');
        
        let image = document.createElement('img');
        image.src = restaurant.imageURL;
	    image.alt = 'Restaurant Image';
	    image.classList.add('restaurant-image');
	    
	    // Add the image to the anchor tag
        imageLink.appendChild(image);
        
        
        let detailsDiv = document.createElement('div');
    	detailsDiv.classList.add('restaurant-details');

	    let name = document.createElement('p');
	    name.textContent = restaurant.name; // Restaurant name
	    name.style.fontSize = '16px';
        name.classList.add('name');
        name.style.marginBottom = '5px';
        

        let address = document.createElement('p');
        address.textContent = restaurant.address;
        address.classList.add('address');
        address.style.marginBottom = '5px';
        
        let date = document.createElement('p');
        date.innerHTML = `<strong>Date: </strong> ${reservation.date}`;
        date.classList.add('date');
        date.style.marginBottom = '5px';
        
        let time = document.createElement('p');
        time.innerHTML = `<strong>Time: </strong> ${reservation.time}`;
        time.classList.add('time');
        time.style.marginBottom = '5px';
        
        let notes = document.createElement('p');
        notes.innerHTML = `<strong>Notes: </strong> ${reservation.notes}`;
        notes.classList.add('notes');


		//**CITATION**
		// "how can i adjust the verticle space of an input" prompt (2 lines). 
		//	Chat GPT 3.5 version, OpenAI, Dec 1 2023, chat.openai.com/chat
		[name, address, date, time, notes].forEach(elem => {
            elem.style.marginBottom = '5px'; // Adjust this value as needed
        });

        // Append elements to the restaurantDiv
        detailsDiv.appendChild(name);
	    detailsDiv.appendChild(address);
	    detailsDiv.appendChild(date);
	    detailsDiv.appendChild(time);
	    detailsDiv.appendChild(notes);
	    
	    restaurantDiv.appendChild(imageLink);
    	restaurantDiv.appendChild(detailsDiv);

        // Append restaurantDiv to resultsList
        resultsList.appendChild(restaurantDiv);
        
        
    });
    const dropdownItems = document.querySelectorAll('.dropdown-item a');
    dropdownItems.forEach(item => {
        item.addEventListener('click', function(event) {
            event.preventDefault();
            const newSort = item.getAttribute('value');
            
            const url = `reservations.html?sort=${newSort}`;
		    history.pushState({}, '', url);
		    window.location.href = url; // Redirect to the new URL
        });
    });
}



function clearInputFields() {
    document.getElementById('search-term').value = '';
    document.getElementById('latitudeInput').value = '';
    document.getElementById('longitudeInput').value = '';
    // Clear the radio button selection if needed
    document.querySelector('input[name="sort"]:checked').checked = false;
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

function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
    console.log("should show content items in dropdown");
  }
