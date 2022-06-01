var map = L.map('map').setView([45.76,4.8], 15);

L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);
var Sport= L.layerGroup().addTo(map);
var Pizzeria=L.layerGroup().addTo(map);
var SportIcon = L.icon({iconUrl: 'fire-B_G.svg',iconSize: [100, 100] });
L.marker([45.732333858926715,4.8260937761478795],{icon: SportIcon }).addTo(Sport).bindPopup("<h3>Meriem Club</h3></br>Sale de sport pour Femme</br>Du Samedi au Jeudi</br>Tel 029999999");
L.marker([45.793118996773316,4.808583763718545],{icon: SportIcon }).addTo(Sport).bindPopup("<h3>FormeInshape</h3></br>Sale pour Homme et Femme</br>7/7jour</br>Tel 029000000");
var PizzaIcon = L.icon({iconUrl: 'v.svg',iconSize: [60, 60] });
L.marker([45.69,4.79],{icon: PizzaIcon }).addTo(Pizzeria).bindPopup("<h3>Hamou's Pizza</h3></br>De 11h a 21h</br>Sur place/Livraison</br>Tel 029777777");
L.marker([45.72,4.78],{icon: PizzaIcon }).addTo(Pizzeria).bindPopup("<h3>Pizza Family</h3></br>De 10h a 22h</br>7/7jour</br>Tel 029888888");

var sportCheckbox = document.querySelector('input[value="sport"]');

	sportCheckbox.onchange = function() {
		if(sportCheckbox.checked) {
						map.addLayer(Sport);
	    
	  				  } 
	 	else			  {
						map.removeLayer(Sport);
	    
	  				  }
	};

	var pizzeriaCheckbox = document.querySelector('input[value="pizzeria"]');

	pizzeriaCheckbox.onchange = function() {
	      if(pizzeriaCheckbox.checked) {
	                    			map.addLayer(Pizzeria);
	    
	  				   } 
	     else {
					       map.removeLayer(Pizzeria);
		    
	  				  }
	};

    function onMouseMove(e) {
        document.getElementById("xy").innerHTML =e.latlng;
        }

map.on('mousemove', onMouseMove);

function myFunction(){ 
    var t=[
           [33.799602,2.864200,"Meriem Club"],
           [33.802579,2.867955,"FormeInshape"],
                           [33.800778,2.860724,"SportPro"],
                           [33.804755,2.863398,"Hamou's Pizza"],
                           [33.797801,2.868462,"Pizza Family"]
                          ];
    var x = document.getElementById("maRecherche").value;
    var trouvee=0;
    for (var i=0;i<t.length;i++){	
                    if (t[i][2]==x){
                            map.setView([t[i][0],t[i][1]], 17);
                            trouvee=1;
                            }
                    }
    if (trouvee==0){alert("Untrouvable");}
}
var info = {
    method: 'GET',
    mode: 'cors',
    cache: 'default'};
fetch('http://vps.cpe-sn.fr:8081/fire',info)
.then(function(response){
return response.json();
}
)
vardump(info)