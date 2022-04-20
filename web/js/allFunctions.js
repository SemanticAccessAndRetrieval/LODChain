function downloadSameAs() {
    document.location.href = "GetSameAs";
    return 1;
}
function getErrors() {
    document.location.href = "GetSameAsErrors";
}

function downloadProvenance(type) {
    document.location.href = "GetProvenance?type=" + type;
}

function downloadFacts(type) {
    document.location.href = "GetCommon_Comp_Triples?type=" + type;
}


function downloadVoID() {
    document.location.href = "GetMetadata?type=VOID";
}
function downloadCSV() {
    document.location.href = "GetMetadata?type=CSV";
}

function downloadEfficiencyStatistics(){
    document.location.href = "GetMetadata?type=EFF";
}

function sendQuery() {
    var input = document.getElementById("query").value;
    if (input === "") {
        document.getElementById("query").style = "border: 5px solid red";
    } else if (document.getElementById("extra").style.display === "block") {
        var domain = document.getElementById("domain").value;
        var name = document.getElementById("name").value;
        var url = document.getElementById("url").value;
        var lines = document.getElementById("lines").value;
        document.location.href = "LODChain?URI=" + input + "&Name=" + name + "&URL=" + url + "&domain=" + domain + "&lines=" + lines;
    } else {
        document.location.href = "LODChain?URI=" + input;
    }

    ////var uri = input.replace("http://", "");
    // uri = uri.replace(/\//g, '$');
    // uri = uri.replace("#", "@");
    // uri=uri.replace("http://","");


}

function getColor(domain) {
    if (domain === "Publication") {
        return "#00008B";
    } else if (domain === "Cross-Domain") {
        return "#c8a788";
    } else if (domain === "Government") {
        return "#ae77db";
    } else if (domain === "Media") {
        return "#42f5ef";
    } else if (domain === "Social_Network") {
        return "#d84d8c";
    } else if (domain === "User-Generated_Content") {
        return "#94ff69";
    } else if (domain === "Life_Sciences") {
        return "#525150";
    } else if (domain === "Linguistics") {
        return "#36bc8d";
    } else if (domain === "Location") {
        return "#effacd";
    } else if (domain === "Unknown") {
        return "yellow";
    }
}


function changeTab(selection) {
    var x = document.getElementById("equiv");
    var x2 = document.getElementById("entities");
    var x7 = document.getElementById("graph");
    var x3 = document.getElementById("schema");
    var x4 = document.getElementById("triples");
    var x5 = document.getElementById("meta");
    var x6 = document.getElementById("discov");
    var x8 = document.getElementById("comp");
    var colorSelected = "#36486b";
    var colorNonSelected = "#92a8d1";
    if (selection === "equiv") {
        x.style.display = "block";
        document.getElementById("equivButton").style.background = colorSelected;
    } else {
        x.style.display = "none";
        document.getElementById("equivButton").style.background = colorNonSelected;
    }
    if (selection === "entities") {
        x2.style.display = "block";
        document.getElementById("entitiesButton").style.background = colorSelected;
    } else {
        x2.style.display = "none";
        document.getElementById("entitiesButton").style.background = colorNonSelected;
    }
    if (selection === "graph") {
        x7.style.display = "block";
        seeByConnections();

        document.getElementById("graphButton").style.background = colorSelected;
    } else {
        x7.style.display = "none";
        document.getElementById("graphButton").style.background = colorNonSelected;
    }
    if (selection === "schema") {

        x3.style.display = "block";
        document.getElementById("schemaButton").style.background = colorSelected;
    } else {
        x3.style.display = "none";
        document.getElementById("schemaButton").style.background = colorNonSelected;
    }
    if (selection === "triples") {

        x4.style.display = "block";
        document.getElementById("triplesButton").style.background = colorSelected;
    } else {
        x4.style.display = "none";
        document.getElementById("triplesButton").style.background = colorNonSelected;
    }
    if (selection === "meta") {

        x5.style.display = "block";
        document.getElementById("metaButton").style.background = colorSelected;
    } else {
        x5.style.display = "none";
        document.getElementById("metaButton").style.background = colorNonSelected;
    }
    if (selection === "comp") {

        x8.style.display = "block";
        document.getElementById("compButton").style.background = colorSelected;
    } else {
        x8.style.display = "none";
        document.getElementById("compButton").style.background = colorNonSelected;
    }
    if (selection === "discov") {

        x6.style.display = "block";
        document.getElementById("discoveryButton").style.background = colorSelected;
    } else {
        x6.style.display = "none";
        document.getElementById("discoveryButton").style.background = colorNonSelected;
    }

}



function init() {
    if (document.getElementById("extra") !== null)
        document.getElementById("extra").style.display = "none";
    else
        changeTab("equiv");

}

function showHideOptional() {
    if (document.getElementById('optional').checked) {
        document.getElementById("extra").style.display = "none";

    } else {
        document.getElementById("extra").style.display = "block";

    }
}

function setWW1() {
    document.getElementById("query").value = "http://users.ics.forth.gr/mountant/files/world_war_1_lod.nt";
    document.getElementById("name").value = "World War I LOD";
    document.getElementById("domain").value = "Publication";
    document.getElementById("url").value = "http://ldf.fi/ww1lod/";
    document.getElementById("lines").value = "All";
}

function setWW1synth() {
    document.getElementById("query").value = "http://users.ics.forth.gr/mountant/files/world_war_1_lod_synth.nt";
    document.getElementById("name").value = "World War I LOD Synthetic";
    document.getElementById("domain").value = "Publication";
    document.getElementById("url").value = "http://ldf.fi/ww1lod/";
    document.getElementById("lines").value = "All";
}



function setPNLN() {
    document.getElementById("query").value = "http://users.ics.forth.gr/mountant/files/netherlands_persons.nt";
    document.getElementById("name").value = "Persons of Netherlands National Library";
    document.getElementById("domain").value = "Publication";
    document.getElementById("url").value = "http://data.bibliotheken.nl/";
    document.getElementById("lines").value = "50000";
}

function setCervantes() {
    document.getElementById("query").value = "http://users.ics.forth.gr/mountant/files/cervantes.nq";
    document.getElementById("name").value = "Cervantes Museum Sample";
    document.getElementById("domain").value = "Publication";
    document.getElementById("url").value = "http://data.cervantesvirtual.com/";
    document.getElementById("lines").value = "All";
}

function setGR() {
    document.getElementById("query").value = "http://users.ics.forth.gr/mountant/files/children_artworks.ttl";
    document.getElementById("name").value = "Greek Children Art Museum";
    document.getElementById("domain").value = "User-Generated_Content";
    document.getElementById("url").value = "https://www.childrensartmuseum.gr/";
    document.getElementById("lines").value = "All";
}

function setMW() {
    document.getElementById("query").value = "http://users.ics.forth.gr/mountant/files/muziekweb.nt";
    document.getElementById("name").value = "Muziek Web";
    document.getElementById("domain").value = "Media";
    document.getElementById("url").value = "http://data.muziekweb.nl";
    document.getElementById("lines").value = "All";
}

function setGTS() {
    document.getElementById("query").value = "http://users.ics.forth.gr/mountant/files/isc2020.rdf";
    document.getElementById("name").value = "Geological TimeScale";
    document.getElementById("domain").value = "Location";
    document.getElementById("url").value = "http://resource.geosciml.org/";
    document.getElementById("lines").value = "All";
}