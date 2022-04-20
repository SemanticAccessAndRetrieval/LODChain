<%-- 
    Document   : main
    Created on : 28-May-2014, 15:32:04
    Author     : micha_000
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
    "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
    <head>
        <script type="text/javascript" src="js/loader.js"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <!--<script type="text/javascript" src="https://ww3.arb.ca.gov/ei/tools/lib/vis/dist/vis.js"></script>
        <link href="https://ww3.arb.ca.gov/ei/tools/lib/vis/dist/vis.css" rel="stylesheet" type="text/css" />-->
        <link href="https://netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css" rel="stylesheet">
        <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
        <style type="text/css">
            #mynetwork {
                width: 800px;
                height: 800px;
                border: 1px solid lightgray;
            }
        </style>
        <style>
            /* Popup container - can be anything you want */
            .popup {
                position: relative;
                display: inline-block;
                cursor: pointer;
                -webkit-user-select: none;
                -moz-user-select: none;
                -ms-user-select: none;
                user-select: none;
            }

            /* The actual popup */
            .popup .popuptext {
                visibility: hidden;
                width: 500px;
                background-color: white;
                color: #fff;
                text-align: center;
                border-radius: 6px;
                padding: 8px 0;
                position: absolute;
                z-index: 1;
                bottom: 125%;
                left: 50%;
                margin-left: -80px;
            }

            /* Popup arrow */
            .popup .popuptext::after {
                content: "";
                position: absolute;
                top: 100%;
                left: 50%;
                margin-left: -5px;
                border-width: 5px;
                border-style: solid;
                border-color: #555 transparent transparent transparent;
            }

            /* Toggle this class - hide and show the popup */
            .popup .show {
                visibility: visible;
                -webkit-animation: fadeIn 1s;
                animation: fadeIn 1s;
            }

            /* Add animation (fade in the popup) */
            @-webkit-keyframes fadeIn {
                from {opacity: 0;} 
                to {opacity: 1;}
            }

            @keyframes fadeIn {
                from {opacity: 0;}
                to {opacity:1 ;}
            }
        </style>
        <title>LODChain</title>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="stylesheet" href="assets/css/main.css" />
        <link rel="stylesheet" href="assets/css/w3.css" />
        <link rel="stylesheet" href="assets/css/w3-theme-black.css" />
        <link rel="stylesheet" href="assets/css/vis.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <meta name="Keywords" content="warehouse, linked data, semantic, sparql" />
        <%		boolean folders = false;

            String pag2 = "", pag3 = "", pag4 = "", pag5 = "", pag6 = "", pag7 = "", pag8 = "", pag9 = "", name = "", classesNumber = "", propertiesNumber = "", commonProperties = " ", commonClasses = " ";
            String URI = "", msg = "", sameAsBeforeClosure = "", sameAsAfterClosure = "", sameAsInferred = "", jsonConn = "";
            String visualJ = "", datasetsConnections = "", sameAsErrors = "", compFacts = "", entitiesNumber = "", triplesNumber = "", beforeClosure = "", afterClosure = "", commonFacts = "";
            String rankingBeforeClosure = "", rankingBeforeClosureDomain = "", rankingAfterClosure = "", rankingAfterClosureDomain = "";

            if (request.getAttribute("page2") != null) {
                pag2 = request.getAttribute("page2").toString();
            }
            if (request.getAttribute("page3") != null) {
                pag3 = request.getAttribute("page3").toString();
            }
            if (request.getAttribute("page4") != null) {
                pag4 = request.getAttribute("page4").toString();
            }
            if (request.getAttribute("page5") != null) {
                pag5 = request.getAttribute("page5").toString();
            }
            if (request.getAttribute("page6") != null) {
                pag6 = request.getAttribute("page6").toString();
            }
            if (request.getAttribute("page7") != null) {
                pag7 = request.getAttribute("page7").toString();
            }
            if (request.getAttribute("page8") != null) {
                pag8 = request.getAttribute("page8").toString();
            }
            if (request.getAttribute("page9") != null) {
                pag9 = request.getAttribute("page9").toString();
            }
            if (request.getAttribute("jsonConn") != null) {
                jsonConn = request.getAttribute("jsonConn").toString();
            }
            if (request.getAttribute("URI") != null) {
                URI = "http://" + request.getAttribute("URI").toString().replace("$", "/");
            } else {
                URI = "http://dbpedia.org";
            }

            if (request.getAttribute("name") != null) {
                name = request.getAttribute("name").toString();
            }
            if (request.getAttribute("sameAsErrors") != null) {
                sameAsErrors = request.getAttribute("sameAsErrors").toString();
            }
            if (request.getAttribute("msg") != null) {
                msg = request.getAttribute("msg").toString();
            }

            if (request.getAttribute("domainVisual") != null) {
                visualJ = request.getAttribute("domainVisual").toString();
            }

            if (request.getAttribute("datasetsConnections") != null) {
                datasetsConnections = request.getAttribute("datasetsConnections").toString();
            }
            if (request.getAttribute("entitiesNumber") != null) {
                entitiesNumber = request.getAttribute("entitiesNumber").toString();
            }
            if (request.getAttribute("propertiesNumber") != null) {
                propertiesNumber = request.getAttribute("propertiesNumber").toString();
            }
            if (request.getAttribute("classesNumber") != null) {
                classesNumber = request.getAttribute("classesNumber").toString();
            }
            if (request.getAttribute("triplesNumber") != null) {
                triplesNumber = request.getAttribute("triplesNumber").toString();
            }
            if (request.getAttribute("beforeClosure") != null) {
                beforeClosure = request.getAttribute("beforeClosure").toString();
            }
            if (request.getAttribute("afterClosure") != null) {
                afterClosure = request.getAttribute("afterClosure").toString();
            }
            if (request.getAttribute("commonFacts") != null) {
                commonFacts = request.getAttribute("commonFacts").toString();
            }
            if (request.getAttribute("commonClasses") != null) {
                commonClasses = request.getAttribute("commonClasses").toString();
            }
            if (request.getAttribute("commonProperties") != null) {
                commonProperties = request.getAttribute("commonProperties").toString();
            }
            if (request.getAttribute("compFacts") != null) {
                compFacts = request.getAttribute("compFacts").toString();
            }
            if (request.getAttribute("sameAsBeforeClosure") != null) {
                sameAsBeforeClosure = request.getAttribute("sameAsBeforeClosure").toString();
            }
            if (request.getAttribute("sameAsAfterClosure") != null) {
                sameAsAfterClosure = request.getAttribute("sameAsAfterClosure").toString();
            }
            if (request.getAttribute("sameAsInferred") != null) {
                sameAsInferred = request.getAttribute("sameAsInferred").toString();
            }
            if (request.getAttribute("rankingBeforeClosure") != null) {
                rankingBeforeClosure = request.getAttribute("rankingBeforeClosure").toString();
            }
            if (request.getAttribute("rankingBeforeClosureDomain") != null) {
                rankingBeforeClosureDomain = request.getAttribute("rankingBeforeClosureDomain").toString();
            }
            if (request.getAttribute("rankingAfterClosure") != null) {
                rankingAfterClosure = request.getAttribute("rankingAfterClosure").toString();
            }
            if (request.getAttribute("rankingAfterClosureDomain") != null) {
                rankingAfterClosureDomain = request.getAttribute("rankingAfterClosureDomain").toString();
            }


        %>


        <script type="text/javascript" src="js/allFunctions.js">
        </script>
         <script type="text/javascript" src="js/vis.js">
        </script>
        <script type="text/javascript">

            google.charts.load('current', {packages: ['corechart', 'bar']});
            google.charts.setOnLoadCallback(drawAxisTickColors);
            function drawAxisTickColors() {
                var datasetConn = '<%=datasetsConnections%>';
                var datasets = [];
                datasets.push(['Dataset', 'Common Entities']);
                var cntDat = 0;
                var str_array = datasetConn.split(',');
                for (var i = 0; i < str_array.length; i++) {
                    // Trim the excess whitespace.
                    var split = str_array[i].split(' ');
                    cntDat++;
                    var NUM = parseInt(split[1]);
                    datasets.push([split[0], NUM]);
                    // Add additional code here, such as:

                }
                cntDat--;

                var name = '<%=name%>';

                var data = google.visualization.arrayToDataTable(datasets);
                var options = {
                    title: 'The top-' + cntDat + ' Most Connected Datasets to Dataset ' + name,
                    'width': 800, 'height': 500,
                    hAxis: {
                        title: 'Number of Common Entities',
                        minValue: 0,
                        textStyle: {
                            bold: true,
                            fontSize: 12,
                            color: '#000000'
                        },
                        titleTextStyle: {
                            bold: true,
                            fontSize: 18,
                            color: '#000000'
                        }
                    },
                    vAxis: {
                        title: 'Dataset',
                        textStyle: {
                            fontSize: 12,
                            bold: false,
                            color: '#000000'
                        },
                        titleTextStyle: {
                            fontSize: 18,
                            bold: true,
                            color: '#000000'
                        }
                    },

                    isStacked: 'true',
                    bar: {groupWidth: '35%'}
                };
                var chart = new google.visualization.BarChart(document.getElementById('chart_div'));
                chart.draw(data, options);
            }

            google.charts.setOnLoadCallback(drawAxisTickFacts);
            function drawAxisTickFacts() {
                var commonFacts = '<%=commonFacts%>';
                var datasets = [];
                datasets.push(['Dataset', 'Common Facts']);
                var cntDat = 0;
                var str_array = commonFacts.split(',');
                for (var i = 0; i < str_array.length; i++) {
                    // Trim the excess whitespace.
                    var split = str_array[i].split(' ');
                    cntDat++;
                    var NUM = parseInt(split[1]);
                    datasets.push([split[0], NUM]);
                    // Add additional code here, such as:

                }
                cntDat--;

                var name = '<%=name%>';

                var data = google.visualization.arrayToDataTable(datasets);
                var options = {
                    title: 'The top-' + cntDat + ' Datasets having the most common facts with dataset ' + name,
                    'width': 800, 'height': 500,
                    hAxis: {
                        title: 'Common Facts',
                        minValue: 0,
                        textStyle: {
                            bold: true,
                            fontSize: 12,
                            color: '#000000'
                        },
                        titleTextStyle: {
                            bold: true,
                            fontSize: 18,
                            color: '#000000'
                        }
                    },
                    vAxis: {
                        title: 'Dataset',
                        textStyle: {
                            fontSize: 12,
                            bold: false,
                            color: '#000000'
                        },
                        titleTextStyle: {
                            fontSize: 18,
                            bold: true,
                            color: '#000000'
                        }
                    },
                    isStacked: 'true',
                    bar: {groupWidth: '35%'}
                };
                var chart = new google.visualization.BarChart(document.getElementById('chart_commonFacts'));
                chart.draw(data, options);
            }



            google.charts.setOnLoadCallback(drawAxisTickPropertiess);
            function drawAxisTickPropertiess() {
                var commonProperties = '<%=commonProperties%>';
                var datasets = [];
                datasets.push(['Dataset', 'Common Properties']);
                var cntDat = 0;
                var str_array = commonProperties.split(',');
                for (var i = 0; i < str_array.length; i++) {
                    // Trim the excess whitespace.
                    var split = str_array[i].split(' ');
                    cntDat++;
                    var NUM = parseInt(split[1]);
                    datasets.push([split[0], NUM]);
                    // Add additional code here, such as:

                }
                cntDat--;

                var name = '<%=name%>';

                var data = google.visualization.arrayToDataTable(datasets);
                var options = {
                    title: 'The top-' + cntDat + ' Datasets having the most common properties with dataset ' + name,
                    'width': 800, 'height': 500,
                    hAxis: {
                        title: 'Common Properties',
                        minValue: 0,
                        textStyle: {
                            bold: true,
                            fontSize: 12,
                            color: '#000000'
                        },
                        titleTextStyle: {
                            bold: true,
                            fontSize: 18,
                            color: '#000000'
                        }
                    },
                    vAxis: {
                        title: 'Dataset',
                        textStyle: {
                            fontSize: 12,
                            bold: false,
                            color: '#000000'
                        },
                        titleTextStyle: {
                            fontSize: 18,
                            bold: true,
                            color: '#000000'
                        }
                    },
                    isStacked: 'true',
                    bar: {groupWidth: '35%'}
                };
                var chart = new google.visualization.BarChart(document.getElementById('chart_commonProperties'));
                chart.draw(data, options);
            }





            google.charts.setOnLoadCallback(drawAxisTickClassess);
            function drawAxisTickClassess() {
                var commonClasses = '<%=commonClasses%>';
                var datasets = [];
                datasets.push(['Dataset', 'Common Classes']);
                var cntDat = 0;
                var str_array = commonClasses.split(',');
                for (var i = 0; i < str_array.length; i++) {
                    // Trim the excess whitespace.
                    var split = str_array[i].split(' ');
                    cntDat++;
                    var NUM = parseInt(split[1]);
                    datasets.push([split[0], NUM]);
                    // Add additional code here, such as:

                }
                cntDat--;

                var name = '<%=name%>';

                var data = google.visualization.arrayToDataTable(datasets);
                var options = {
                    title: 'The top-' + cntDat + ' Datasets having the most common Classes with dataset ' + name,
                    'width': 800, 'height': 500,
                    hAxis: {
                        title: 'Common Classes',
                        minValue: 0,
                        textStyle: {
                            bold: true,
                            fontSize: 12,
                            color: '#000000'
                        },
                        titleTextStyle: {
                            bold: true,
                            fontSize: 18,
                            color: '#000000'
                        }
                    },
                    vAxis: {
                        title: 'Dataset',
                        textStyle: {
                            fontSize: 12,
                            bold: false,
                            color: '#000000'
                        },
                        titleTextStyle: {
                            fontSize: 18,
                            bold: true,
                            color: '#000000'
                        }
                    },
                    isStacked: 'true',
                    bar: {groupWidth: '35%'}
                };
                var chart = new google.visualization.BarChart(document.getElementById('chart_commonClasses'));
                chart.draw(data, options);
            }






            google.charts.setOnLoadCallback(drawAxisTickCompFacts);
            function drawAxisTickCompFacts() {
                var compFacts = '<%=compFacts%>';
                var name = '<%=name%>';
                var datasets = [];
                datasets.push(['Dataset', 'Comp. Facts']);
                var cntDat = 0;
                var str_array = compFacts.split(',');
                for (var i = 0; i < str_array.length; i++) {
                    // Trim the excess whitespace.
                    var split = str_array[i].split(' ');
                    cntDat++;
                    var NUM = parseInt(split[1]);
                    datasets.push([split[0], NUM]);
                    // Add additional code here, such as:

                }
                cntDat--;



                var data = google.visualization.arrayToDataTable(datasets);
                var options = {
                    title: 'The top-' + cntDat + ' Datasets having the most complementary facts for the entities of dataset ' + name,
                    'width': 750, 'height': 500,
                    hAxis: {
                        title: 'Compl. Facts',
                        minValue: 0,
                        textStyle: {
                            bold: true,
                            fontSize: 12,
                            color: '#000000'
                        },
                        titleTextStyle: {
                            bold: true,
                            fontSize: 18,
                            color: '#000000'
                        }
                    },
                    vAxis: {
                        title: 'Dataset',
                        textStyle: {
                            fontSize: 12,
                            bold: false,
                            color: '#000000'
                        },
                        titleTextStyle: {
                            fontSize: 18,
                            bold: true,
                            color: '#000000'
                        }
                    },
                    isStacked: 'true',
                    bar: {groupWidth: '35%'}
                };
                var chart = new google.visualization.BarChart(document.getElementById('chart_compFacts'));
                chart.draw(data, options);
            }


            function setBorderColor() {
                var input = document.getElementById("query").value;
                if (input === "") {
                    document.getElementById("query").style = "border: 5px solid red";
                } else {
                    document.getElementById("query").style = "border: 0px";
                }
            }




            function seeByDomain() {

                var nodeArr = [];
                var edgeArr = [];
                var dataset = "";
                var dtsets = JSON.parse('<%=jsonConn%>');
                var domains = [];


                for (let i = 0; i < dtsets.Datasets.length; i++) {
                    if (i == 0) {
                        dataset = dtsets.Datasets[i].Name;
                        nodeArr.push({id: i, label: dtsets.Datasets[i].Name, borderWidth: 3, color: {border: getColor(dtsets.Datasets[i].domain)}});

                    } else if (dtsets.Datasets[i].new === "yes") {
                        nodeArr.push({id: i, label: dtsets.Datasets[i].Name, borderWidth: 3, color: {border: getColor(dtsets.Datasets[i].domain)}});
                        edgeArr.push({from: i, to: 0, label: dtsets.Datasets[i].links, length: 250, arrows: 'from'});
                    } else {
                        nodeArr.push({id: i, label: dtsets.Datasets[i].Name, borderWidth: 3, color: {border: getColor(dtsets.Datasets[i].domain)}});
                        edgeArr.push({from: i, to: 0, label: dtsets.Datasets[i].links, length: 250, arrows: 'from'});
                    }
                    if (!domains.includes(dtsets.Datasets[i].domain)) {
                        domains.push(dtsets.Datasets[i].domain);
                    }
                }

                var nodes = new vis.DataSet(nodeArr);
                var edges = new vis.DataSet(edgeArr);


                // create a network
                var container = document.getElementById('mynetwork');
                var data = {
                    nodes: nodes,
                    edges: edges
                };
                var options = {
                    nodes: {
                        color: {
                            border: 'green',
                            background: 'white',
                            highlight: {
                                border: 'green',
                                background: 'white'
                            },
                            hover: {
                                border: 'black',
                                background: 'black'
                            }
                        },
                        font: {
                            color: 'black',
                            size: 20, // px
                            face: 'arial'
                        },
                        shape: 'boxc'
                    }
                };


                var network = new vis.Network(container, data, options);


                document.getElementById("legend").innerHTML = "<h5>Info</h5><b>Node</b>: A dataset <br>"
                        + "<b>Edge and Label</b>: Number of Common entities between a dataset and " + dataset + " <br>";


                if (domains.includes("Publication"))
                    document.getElementById("legend").innerHTML += "<b>Nodes with <span style='color:#00008B'>Border Color</span></b>: Publications Domain <br>";
                if (domains.includes("Cross-Domain"))
                    document.getElementById("legend").innerHTML += "<b>Nodes with <span style='color:#c8a788'>Border Color</span></b>: Cross-Domain<br>";

                if (domains.includes("Government"))
                    document.getElementById("legend").innerHTML += "<b>Nodes with <span style='color:#ae77db'>Border Color</span></b>: Government Domain<br>";
                if (domains.includes("Media"))
                    document.getElementById("legend").innerHTML += "<b>Nodes with <span style='color:#42f5ef'>Border Color</span></b>: Media Domain<br>";
                if (domains.includes("Social_Network"))
                    document.getElementById("legend").innerHTML += "<b>Nodes with <span style='color:#d84d8c'>Border Color</span></b>: Social Network Domain<br>";

                if (domains.includes("User-Generated_Content"))
                    document.getElementById("legend").innerHTML += "<b>Nodes with <span style='color:#94ff69'>Border Color</span></b>: User-Generated Content Domain<br>";
                if (domains.includes("Life_Sciences"))
                    document.getElementById("legend").innerHTML += "<b>Nodes with <span style='color:#525150'>Border Color</span></b>: Life_Sciences Domain<br>";

                if (domains.includes("Linguistics"))
                    document.getElementById("legend").innerHTML += "<b>Nodes with <span style='color:#36bc8d'>Border Color</span></b>: Linguistics Domain<br>";
                if (domains.includes("Location"))
                    document.getElementById("legend").innerHTML += "<b>Nodes with <span style='color:#effacd'>Border Color</span></b>: Location Domain<br>";

                if (domains.includes("Unknown")) {
                    document.getElementById("legend").innerHTML += "<b>Nodes with <span style='color:yellow'>Border Color</span></b>: Unknown Domain<br>";
                }

            }
            function myFunction(k) {
                var popup = document.getElementById("myPopup" + k);
                popup.classList.toggle("show");
            }

            google.charts.load('current', {'packages': ['corechart']});
            google.charts.setOnLoadCallback(drawChart);
            // Draw the chart and set the chart values
            function drawChart() {
                var visual = '<%=visualJ%>';
                var domains = [];
                domains.push(['Domain', 'Number']);
                var cntDomains = 0;
                var str_array = visual.split(',');
                for (var i = 0; i < str_array.length; i++) {
                    // Trim the excess whitespace.
                    var split = str_array[i].split(' ');
                    cntDomains++;
                    var NUM = parseInt(split[1]);
                    domains.push([split[0], NUM]);
                    // Add additional code here, such as:

                }
                cntDomains--;


                var data = google.visualization.arrayToDataTable(domains);
                // Optional; add a title and set the width and height of the chart
                var options = {'title': 'Connections with ' + cntDomains + ' Domains', 'width': 700, 'height': 400};
                // Display the chart inside the <div> element with id="piechart"
                var chart = new google.visualization.PieChart(document.getElementById('piechartDomains'));
                chart.draw(data, options);
            }


            google.charts.setOnLoadCallback(drawChartEntities);
            // Draw the chart and set the chart values
            function drawChartEntities() {
                var entitiesNumber = '<%=entitiesNumber%>';
                var ents = [];
                ents.push(['Category', 'Value']);
                var cntEntities = 0;
                var str_array = entitiesNumber.split(',');
                for (var i = 0; i < str_array.length; i++) {
                    // Trim the excess whitespace.
                    var split = str_array[i].split(':');
                    var NUM = parseInt(split[1]);
                    ents.push([split[0], NUM]);
                    cntEntities += NUM;
                    // Add additional code here, such as:

                }



                var data = google.visualization.arrayToDataTable(ents);
                // Optional; add a title and set the width and height of the chart
                var options = {'title': 'Unique and Common Entities (In Total ' + cntEntities + ' Entities)', 'width': 700, 'height': 400};
                // Display the chart inside the <div> element with id="piechart"
                var chart = new google.visualization.PieChart(document.getElementById('piechartEntities'));
                chart.draw(data, options);
            }

            google.charts.setOnLoadCallback(drawChartProperties);
            // Draw the chart and set the chart values
            function drawChartProperties() {
                var propertiesNumber = '<%=propertiesNumber%>';
                var props = [];
                props.push(['Category', 'Value']);
                var cntProperties = 0;
                var str_array = propertiesNumber.split(',');
                for (var i = 0; i < str_array.length; i++) {
                    // Trim the excess whitespace.
                    var split = str_array[i].split(':');
                    var NUM = parseInt(split[1]);
                    props.push([split[0], NUM]);
                    cntProperties += NUM;
                    // Add additional code here, such as:

                }
                var data = google.visualization.arrayToDataTable(props);
                // Optional; add a title and set the width and height of the chart
                var options = {'title': 'Unique and Common Properties (In Total ' + cntProperties + ' Properties)', 'width': 700, 'height': 400};
                // Display the chart inside the <div> element with id="piechart"
                var chart = new google.visualization.PieChart(document.getElementById('piechartProperties'));
                chart.draw(data, options);
            }
            google.charts.setOnLoadCallback(drawChartClasses);
            // Draw the chart and set the chart values
            function drawChartClasses() {
                var classesNumber = '<%=classesNumber%>';
                var clses = [];
                clses.push(['Category', 'Value']);
                var cntClasses = 0;
                var str_array = classesNumber.split(',');
                for (var i = 0; i < str_array.length; i++) {
                    // Trim the excess whitespace.
                    var split = str_array[i].split(':');
                    var NUM = parseInt(split[1]);
                    clses.push([split[0], NUM]);
                    cntClasses += NUM;
                    // Add additional code here, such as:

                }



                var data = google.visualization.arrayToDataTable(clses);
                // Optional; add a title and set the width and height of the chart
                var options = {'title': 'Unique and Common Classes (In Total ' + cntClasses + ' Classes)', 'width': 700, 'height': 400};
                // Display the chart inside the <div> element with id="piechart"
                var chart = new google.visualization.PieChart(document.getElementById('piechartClasses'));
                chart.draw(data, options);
            }
            google.charts.setOnLoadCallback(drawChartTriples);
            // Draw the chart and set the chart values
            function drawChartTriples() {
                var triplesNumber = '<%=triplesNumber%>';
                var trps = [];
                trps.push(['Category', 'Value']);
                var cmnTriples = 0;
                var str_array = triplesNumber.split(',');
                for (var i = 0; i < str_array.length; i++) {
                    // Trim the excess whitespace.
                    var split = str_array[i].split(':');
                    var NUM = parseInt(split[1]);
                    trps.push([split[0], NUM]);
                    cmnTriples += NUM;
                    // Add additional code here, such as:

                }
                var data = google.visualization.arrayToDataTable(trps);
                // Optional; add a title and set the width and height of the chart
                var options = {'title': 'Unique and Common Facts (In Total ' + cmnTriples + ' Facts)', 'width': 700, 'height': 400};
                // Display the chart inside the <div> element with id="piechart"
                var chart = new google.visualization.PieChart(document.getElementById('piechartTriples'));
                chart.draw(data, options);
            }




            google.charts.load("current", {packages: ['corechart']});
            google.charts.setOnLoadCallback(drawChartConnections);
            function drawChartConnections() {
                var beforeC = '<%=beforeClosure%>';

                var afterC = '<%=afterClosure%>';
                var data = google.visualization.arrayToDataTable([
                    ["Element", "Connections", {role: "style"}],
                    ["Before Closure", parseInt(beforeC), "red"],
                    ["Inferred Connections", parseInt(afterC - beforeC), "yellow"],
                    ["After Closure", parseInt(afterC), "green"]

                ]);

                var view = new google.visualization.DataView(data);
                view.setColumns([0, 1,
                    {calc: "stringify",
                        sourceColumn: 1,
                        type: "string",
                        role: "annotation"},
                    2]);

                var options = {
                    title: "Connections with other Datasets Before And After Closure",
                    width: 600,
                    height: 400,
                    bar: {groupWidth: "60%"},
                    legend: {position: "none"}
                };
                var chart = new google.visualization.ColumnChart(document.getElementById("connectionsBar"));
                chart.draw(view, options);
            }

            google.charts.load("current", {packages: ['corechart']});
            google.charts.setOnLoadCallback(drawChartSameAs);
            function drawChartSameAs() {
                var beforeC = '<%=sameAsBeforeClosure%>';
                var sameAsErrors = '<%=sameAsErrors%>';
                var afterC = '<%=sameAsAfterClosure%>';
                var inferred = '<%=sameAsInferred%>';
                var data = google.visualization.arrayToDataTable([
                    ["Element", "SameAs relationships", {role: "style"}],
                    ["SameAs Possible Errors", parseInt(sameAsErrors), "red"],
                    ["SameAs Before Closure", parseInt(beforeC), "blue"],
                    ["SameAs Inferred", parseInt(inferred), "yellow"],
                    ["SameAs After Closure", parseInt(afterC), "green"]
                ]);

                var view = new google.visualization.DataView(data);
                view.setColumns([0, 1,
                    {calc: "stringify",
                        sourceColumn: 1,
                        type: "string",
                        role: "annotation"},
                    2]);

                var options = {
                    title: "SameAs Relationships Before And After Closure",
                    width: 600,
                    height: 400,
                    bar: {groupWidth: "50%"},
                    legend: {position: "none"}
                };
                var chart = new google.visualization.ColumnChart(document.getElementById("sameAsBar"));
                chart.draw(view, options);
            }


            google.charts.load("current", {packages: ['corechart']});
            google.charts.setOnLoadCallback(drawChartRankingAll);
            function drawChartRankingAll() {
                var beforeC = '<%=rankingBeforeClosure%>';

                var afterC = '<%=rankingAfterClosure%>';
                var beforeDomainC = '<%=rankingBeforeClosureDomain%>';

                var afterDomainC = '<%=rankingAfterClosureDomain%>';

                var data = google.visualization.arrayToDataTable([
                    ["Element", "Dataset Ranking in All Domains", {role: "style"}],
                    ["Positions Gained in Global Ranking", parseInt(beforeC - afterC), "blue"],
                    ["Positions Gained in Domain Ranking", parseInt(beforeDomainC - afterDomainC), "green"]

                ]);

                var view = new google.visualization.DataView(data);
                view.setColumns([0, 1,
                    {calc: "stringify",
                        sourceColumn: 1,
                        type: "string",
                        role: "annotation"},
                    2]);

                var options = {
                    title: "Dataset Ranking in All Domains Before And After Closure",
                    width: 800,
                    height: 400,
                    vAxis: {
                        viewWindowMode: 'explicit',
                        viewWindow: {
                            min: 0
                        }
                    },
                    bar: {groupWidth: "50%"},
                    legend: {position: "none"}
                };
                var chart = new google.visualization.ColumnChart(document.getElementById("rankingBar"));
                chart.draw(view, options);
            }

            // create an array with nodes
            function seeByConnections() {

                var dataset = "";
                var nodeArr = [];
                var edgeArr = [];

                var dtsets = JSON.parse('<%=jsonConn%>');


                for (let i = 0; i < dtsets.Datasets.length; i++) {
                    if (i == 0) {
                        dataset = dtsets.Datasets[i].Name;
                        nodeArr.push({id: i, label: dtsets.Datasets[i].Name, borderWidth: 3, color: {border: 'blue'}});
                    } else if (dtsets.Datasets[i].new === "yes") {
                        nodeArr.push({id: i, label: dtsets.Datasets[i].Name, borderWidth: 3, color: {border: 'red'}});
                        edgeArr.push({from: i, to: 0, label: dtsets.Datasets[i].links, length: 250, arrows: 'from'});
                    } else {
                        nodeArr.push({id: i, label: dtsets.Datasets[i].Name, borderWidth: 3, color: {border: 'green'}});
                        edgeArr.push({from: i, to: 0, label: dtsets.Datasets[i].links, length: 250, arrows: 'from'});

                    }

                }
                document.getElementById("legend").innerHTML = "<h5>Info</h5><b>Node</b>: A dataset <br>"
                        + "<b>Edge and Label</b>: Number of Common entities between a dataset and " + dataset + " <br>"
                        + "<b>Nodes in <span style='color:red'>Red Border</span></b>: Old Connections of " + dataset + " <br>"
                        + "<b>Nodes in <span style='color:green'>Green Border</span></b>: New Connections of " + dataset;

                var nodes = new vis.DataSet(nodeArr);
                var edges = new vis.DataSet(edgeArr);
                // create a network
                var container = document.getElementById('mynetwork');
                var data = {
                    nodes: nodes,
                    edges: edges
                };
                var options = {
                    nodes: {
                        color: {
                            border: 'green',
                            background: 'white',
                            highlight: {
                                border: 'green',
                                background: 'white'
                            },
                            hover: {
                                border: 'black',
                                background: 'black'
                            }
                        },
                        font: {
                            color: 'black',
                            size: 15, // px
                            face: 'arial'
                        },
                        shape: 'boxc'
                    }
                };
                var network = new vis.Network(container, data, options);
            }
        </script>

    </head>
    <body onload="init();" class="subpage">

        <!-- Header -->
        <header id="header">
            <div class="logo"><a href="index.jsp">LODChain <span>SAR group ISL-FORTH</span></a></div>
            <a href="#menu">Menu</a>
        </header>

        <nav id="menu">
            <ul class="links">
                <li><a href="index.jsp">Home</a></li>
                <li><a href="input.jsp">LODChain</a></li>
                <li><a href="https://doi.org/10.5281/zenodo.6467419">Results for Real and Synthetic Datasets and Code</a></li>
                <li><a href="https://youtu.be/Kh9751p32tM">Tutorial Video</a></li>
                <li><a href="moreInformation.jsp">More Information</a></li>
                <li><a href="https://demos.isl.ics.forth.gr/lodsyndesis/">Go to LODsyndesis</a></li>
            </ul>
        </nav>

        <!-- One -->
        <section id="One" class="wrapper style3">
            <div class="inner">
                <header class="align-center">
                    <p style="font-size: 1.3rem">LODChain</p>
                    <h2>Connect Your Dataset to the Rest LOD Cloud</h2>
                </header>
            </div>
        </section>

        <!-- Two -->
        <section id="two" class="wrapper style2">
            <div class="inner">
                <div class="box">
                    <div class="content">
                        <header class="align-center">
                            <p>LODChain</p>
                            <%if (pag2.equals("")) {%>
                            <h2></h2>
                            <%}%>

                            <%if (!pag2.equals("")) {%>

                            <h2>Connectivity Analytics for <b><i><%=name%></i></b></h2>
                            <%}%>
                        </header>

                        <%if (pag2.equals("")) {%>
                        <div class="row uniform">

                            <div class="7u 14u(xsmall)">

                                <br>                
                                Give the Download URL of your dataset (<a href="https://www.w3.org/TR/n-triples/">N-triples</a>, <a href="https://www.w3.org/TR/rdf-syntax-grammar/">RDF/XML</a>, <a href="https://www.w3.org/TR/turtle/">TTL</a>, or <a href="https://www.w3.org/TR/n-quads/">NQ</a> format)
                                <img src="images/info-circle-solid.svg" style="width:3%"  
                                     title="For downloading the triples of your dataset"></img>
                                <input type="text" id="query" onchange="setBorderColor()" placeholder="URL of your dataset"  value=""/>




                                <br>
                                <div id="extra">
                                    Give the title of your Dataset <img src="images/info-circle-solid.svg" style="width:3%"   
                                                                        title="For producing visualizations containing the title of your dataset"></img>
                                    <input type="text" id="name" placeholder="Title of your dataset" value="" /><br>


                                    Give the Website of  your Dataset
                                    <img src="images/info-circle-solid.svg" style="width:3%"  
                                         title="For producing measurements in Ntriples format including the website of your dataset"></img>
                                    <input type="text" id="url"  value="" placeholder="Website URL of your dataset" /><br>
                                    Give the Domain of  your Dataset <img src="images/info-circle-solid.svg" style="width:3%"  
                                                                          title="For offering more advanced measurements including only the datasets of your domain"></img>
                                    <select id="domain">
                                        <option value="Cross-Domain" selected>Cross-Domain</option>
                                        <option value="Government">Government</option>
                                        <option value="Life_Sciences">Life Sciences</option>
                                        <option value="Linguistics">Linguistics</option>
                                        <option value="Location">Location</option>
                                        <option value="Media">Media</option>
                                        <option value="Publication">Publications</option>
                                        <option value="Social_Network">Social Networks</option>
                                        <option value="User-Generated_Content">User-Generated_Content</option>
                                    </select>
                                    <br>
                                    <!--Connect my Data and..
                                    <select id="whatToDo">
                                        <option value="MetricsOnly">Run Connectivity Metrics</option>
                                        <option value="MetricsIndex">Run Connectivity Metrics & Index the Data</option>
                                    </select>
                                    <br>-->




                                    Give the Number of Triples to Read
                                    <img src="images/info-circle-solid.svg" style="width:3%"  
                                         title="For having a very fast overview for the connectivity of your dataset"></img>
                                    <input type="text" id="lines" placeholder="10000 (Default: All)" value="" />

                                    <br>

                                    <br>
                                </div>
                            </div>
                        </div>
                        <input type="checkbox" id="optional" name="optional">
                        <label for="optional" onclick="showHideOptional()">Show optional fields</label> <br>
                        <a class="button special" onclick="sendQuery();">Submit</a>


                        <br><br><br>
                        <b>Sample Datasets:</b> &nbsp; &nbsp; <a onclick="setWW1()">WW1LOD</a>&nbsp; &nbsp; </b> &nbsp; <a onclick="setWW1synth()">WW1LODsynth</a>&nbsp; &nbsp;    <a onclick="setPNLN()">Persons of National Library of Netherlands</a> 
                    &nbsp; &nbsp; <br> <a onclick="setGR()">Greek Children Art Museum</a>   &nbsp; &nbsp;
                        <a onclick="setGTS()">Geological Timescale</a>  &nbsp; &nbsp;  <a onclick="setMW()">MuziekWeb</a>&nbsp; &nbsp;  <a onclick="setCervantes()">Cervantes Museum Sample</a><br>
                        <%}%>	


                        <div id="message">
                            <div class="subContent">

                                <%if (!pag2.equals("")) {%>

                                <header class="align-center">
                                   <!-- <p style="font-size:1.5rem;color:black"><%=msg%> <b><i><%=name%></i></b></i><br><br></p>-->

                                </header>


                                <%}%>	


                                <%if (!pag2.equals("")) {%>
                                <div class="align-center">
                                    <b>Analytics</b>: <a id="equivButton" class="button special" style="background:#92a8d1;" onclick="changeTab('equiv');">owl:sameAs Links</a>
                                    <a id="entitiesButton" class="button special" style="background:#92a8d1;" onclick="changeTab('entities');">Entities</a>
                                    <a id="graphButton" class="button special" style="background:#92a8d1;" onclick="changeTab('graph');">Connections Graph</a>
                                    <a id="schemaButton" class="button special" style="background:#92a8d1;" onclick="changeTab('schema');">Schema Elements</a>
                                    <a  id="triplesButton" class="button special" style="background:#92a8d1;" onclick="changeTab('triples');">Triples</a>

                                    <a  id="discoveryButton" class="button special" style="background:#92a8d1;" onclick="changeTab('discov');">Dataset Discovery and Selection</a>
                                    <br> <br>
                                    <b>Data Enrichment</b>: <a  id="compButton" class="button special" style="background:#92a8d1;" onclick="changeTab('comp');">Complementary and Verified Data</a>
                                    <a  id="metaButton" class="button special" style="background:#92a8d1;" onclick="changeTab('meta');">Results of Connectivity Measurements</a>
                                </div> <br>


                                <jsp:include page="<%=pag2%>" />
                                <jsp:include page="<%=pag3%>" />
                                <jsp:include page="<%=pag4%>" />
                                <jsp:include page="<%=pag5%>" />
                                <jsp:include page="<%=pag6%>" />
                                <jsp:include page="<%=pag7%>" />
                                <jsp:include page="<%=pag8%>" />
                                <jsp:include page="<%=pag9%>" />
                                <%}%>	


                            </div>
                        </div>



                    </div>
                </div>
            </div>
        </section>

        <!-- Footer -->

        <!-- Footer -->

        <div class="copyright" style="height:40px;font-size:12px; background-color: black;color:white;text-align:center;padding-top:10px">
            <a href="http://www.ics.forth.gr/isl/sar/privacy/TermsOfUse-ISL_EN.pdf" class="footer-links" target="_blank">Terms of Use</a>
            |
            <a href="http://www.ics.forth.gr/isl/sar/privacy/PrivacyPolicy-ISL_EN.pdf" style="padding-left:0px!important;" class="footer-links" target="_blank">Privacy Policy</a>
            | &copy; Copyright 2022 FOUNDATION FOR RESEARCH & TECHNOLOGY - HELLAS, All rights reserved.

        </div>



        <footer id="footer" style="background-color:#5B5B5B;padding-top:1rem">
            <img style="float:right;margin-right:20px" src="images/islLogo_En_Main_web_700x237px.png" height="35" >
            <img  style="float:right;margin-right:25px;" src="images/ics-diskin-en-transparent-white.png" height="35" >
            <br><br> <p style="float:right;margin-right:20px;color:#D1D1D1;font-size: 12pt;font-family:Myriad Pro"> Designed and developed by <a href="https://www.ics.forth.gr/isl"
                                                                                                                                                 style="color:white">ISL Team</a></p>
        </footer>

        <!-- Scripts -->
        <script src="assets/js/jquery.min.js"></script>
        <script src="assets/js/jquery.scrollex.min.js"></script>
        <script src="assets/js/skel.min.js"></script>
        <script src="assets/js/util.js"></script>
        <script src="assets/js/main.js"></script>

    </body>




</html>