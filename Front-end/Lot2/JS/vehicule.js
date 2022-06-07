// document.getElementById("button")
// document.getElementById("variable").textContent="oui"
var variable = "hello";

async function mofidyVehicule() {
    // appel de la modification d'un véhicule
    var idVehicle = document.getElementById("idVehicle").value;
    var typeVehicle = document.getElementById("typeVehicle").value;
    var idFacility = document.getElementById("idFacility").value;
    // var lattitude = document.getElementById("lattitude").value;
    // var longitude = document.getElementById("longitude").value;
    var liquidType = document.getElementById("liquidType").value;
    var nbCrewMember = document.getElementById("nbCrewMember").value;
    var fuel = document.getElementById("fuel").value;
    var liquidQuantity = document.getElementById("liquidQuantity").value;

    var facility = await getFacility(idFacility);
    var parsedFacility = JSON.parse(facility);

    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
        "id": idVehicle,
        "facilityRefID": idFacility,
        "lat": parsedFacility.lat,
        "lon": parsedFacility.lon,
        "type": typeVehicle,
        "liquidType": liquidType,
        "crewMember": nbCrewMember,
        "fuel": fuel,
        "liquidQuantity": liquidQuantity
    });

    var requestOptions = {
    method: 'PUT',
    headers: myHeaders,
    body: raw,
    redirect: 'follow'
    };

    fetch("http://vps.cpe-sn.fr:8081/vehicle/c230e0e0-8de9-4c39-8dec-e246dc0c6334" + "/" + idVehicle, requestOptions)
    .then(response => response.text())
    .then(function(response){
        console.log(response);
    })
    .then(result => console.log(result))
    .catch(error => console.log('error', error));
    console.log("Modification d'un véhicule");
    backToIndex();
}

async function createVehicule() {
    // appel de la création d'un véhicule
    var typeVehicle = document.getElementById("typeVehicle").value;
    var idFacility = document.getElementById("idFacility").value;
    // var lattitude = document.getElementById("lattitude").value;
    // var longitude = document.getElementById("longitude").value;
    var liquidType = document.getElementById("liquidType").value;
    var nbCrewMember = document.getElementById("nbCrewMember").value;
    var fuel = document.getElementById("fuel").value;
    var liquidQuantity = document.getElementById("liquidQuantity").value;

    var facility = await getFacility(idFacility);
    var parsedFacility = JSON.parse(facility);

    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
        "facilityRefID": idFacility,
        "lat": parsedFacility.lat,
        "lon": parsedFacility.lon,
        "type": typeVehicle,
        "liquidType": liquidType,
        "crewMember": nbCrewMember,
        "fuel": fuel,
        "liquidQuantity": liquidQuantity
    });

    var requestOptions = {
    method: 'POST',
    headers: myHeaders,
    body: raw,
    redirect: 'follow'
    };

    fetch("http://vps.cpe-sn.fr:8081/vehicle/c230e0e0-8de9-4c39-8dec-e246dc0c6334", requestOptions)
    .then(response => response.text())
    .then(function(response){
        console.log(response);
    })
    .then(result => console.log(result))
    .catch(error => console.log('error', error));
    console.log("création d'un véhicule");
    backToIndex();
}

function getVehicule() {
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };
      
    fetch("http://vps.cpe-sn.fr:8081/vehicle/", requestOptions)
    .then(response => response.text())
    .then(function(response){
       
    })
}



function deleteVehicule() {
    var id = document.getElementById("idDeleteVehicle").value;

    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var requestOptions = {
    method: 'DELETE',
    headers: myHeaders,
    redirect: 'follow'
    };

    fetch("http://vps.cpe-sn.fr:8081/vehicle/c230e0e0-8de9-4c39-8dec-e246dc0c6334/"+id, requestOptions)
    .then(response => response.text())
    .then(result => console.log(result))
    .catch(error => console.log('error', error));
    console.log("supression d'un véhicule");
}

async function getFacility(id) {
    var facilityInfo;
    await fetch("http://vps.cpe-sn.fr:8081/facility/"+id)
    .then(response => response.text())
    .then(function(response) {
        facilityInfo = response;
    });
    return facilityInfo;
}

function backToIndex(){
    window.location.href='../../Lot1/Index.html';
}