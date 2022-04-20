<%-- 
    Document   : main
    Created on : 28-May-2014, 15:32:04
    Author     : micha_000
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
    "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<!DOCTYPE HTML>
<!--
        Released for free under the Creative Commons Attribution 3.0 license (templated.co/license)
-->
<html>
    <head>
        <title>LODChain</title>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="stylesheet" href="assets/css/main.css" />
        <link rel="stylesheet" href="assets/css/w3.css" />
        <link rel="stylesheet" href="assets/css/w3-theme-black.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <style>
            .footer-links,	.footer-links:focus, .footer-links:hover {
                text-decoration: none!important;
                color: white!important;
            }
        </style>
    </head>
    <body>

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

        <section id="One" class="wrapper style3">
            <div class="inner">
                <header class="align-center">
                    <p style="font-size:1.3rem">LODChain</p>
                    <h2>About</h2>
                </header>
            </div>
        </section>

        <!-- One -->
        <section id="one" class="wrapper style2">


            <div class="inner" id="services">
                    

                <div class="grid-style">

                    <div>
                        <div class="box">

                            <div class="content">
                                <header class="align-center">
                                    <p>LODChain</p>
                                    <h2>Connect your Dataset to the Rest Linked Open Data</h2>
                                </header>
                                
                            
                                
                             
                                <p align="justify">
                                    <a href="input.jsp">LODChain</a> is a research prototype that
                                    enables the connection of a new or an existing RDF dataset to hundreds of LOD datasets (through <a href="https://demos.isl.ics.forth.gr/lodsyndesis/">LODsyndesis</a>)
                                    for ensuring its connectivity, for fixing possible connectivity errors, 
                                    and for enriching its contents by discovering related datasets. <br><br>
                                    The steps of LODChain are the following:

                                <ol align="justify">
                                    <li> The user gives as input an RDF dataset in several formats, including  N-Triples, N-Quads, Turtle and RDF/XML Format. </li><br>
                                    <li> LODChain exploits 
                                        the results of the transitive and symmetric closure of owl:sameAs, owl:equivalentProperty and owl:equivalentClass relationships of LODsyndesis at real time
                                        for inferring new connections.</li><br>
                                    <li>LODChain offers several analytics, visualizations, enriched data and metadata for the input dataset.</li>
                                </ol>
                                </p>     
                                <br><br>
                                <footer class="align-center">
                                    <a href="input.jsp" class="button special">Try LODChain</a>
                                </footer>
                            </div>
                        </div>
                    </div>

                    <div>
                        <div class="box">
                            <div class="content">
                                <header class="align-center">
                                    <p>Zenodo</p>
                                    <h2>Results for Real and Synthetic Datasets and Code</h2>
                                </header>
                                <p align="justify">
                                    We have evaluated the following datasets:

                                <ul align="justify">
                                    <li>A subset of  the dataset <a href="http://data.bibliotheken.nl/doc/dataset/persons">
                                            Persons of National Library of Netherlands</a>
                                        with  over 1,500,000 triples.</li>
                                    <li>The dataset <a href="https://www.ldf.fi/dataset/ww1lod/index.html">World War I as LOD</a> from the Universities of Aalto and Helsinki, Finland with 47,176 triples, 11,339 entities and 547 owl:sameAs relationships.</li>
                                    <li>The dataset <a href="https://lod-cloud.net/dataset/greekchildrensartmuseum">Greek Children Art Museum dataset</a> from the Department of Cultural Technology and Communication of the University of the Aegean with 2,211 triples.</li>
                                    <li>A subset of the dataset <a href="https://lod-cloud.net/dataset/muziekweb">Muziek Web</a>  with over 500,000 triples for albums and artists.</li>
                                    <li>The dataset <a href="https://lod-cloud.net/dataset/GTS">Geological Timescale</a> with 15,421 triples.</li>
                                </ul>
                            </p><p align="justify">
                                In this Zenodo <a href="https://doi.org/10.5281/zenodo.6467419">link</a>, one can download the triples of these datasets, their metadata in CSV and VoID format, the provenance of entities
                            and  the inferred sameAs relationships. Moreover, it contains screenshots showing the produced visualizations for these datasets and the code of LODChain. </p> 
                        <footer class="align-center">
                            <a href="https://doi.org/10.5281/zenodo.6467419" class="button special">Browse the Results & Code</a>
                        </footer>
                    </div>
                </div>
            </div>



            <div>
                <div class="box">
                    <div class="content">
                        <header class="align-center">
                            <p>Youtube</p>
                            <h2>Tutorial Video</h2>
                        </header>
                        <p align="justify">
                            <iframe width="480" height="350" src="https://www.youtube.com/embed/Kh9751p32tM" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe>
                    </div>
                </div>
            </div>

            <div>
                <div class="box">
                    <div class="content">
                        <header class="align-center">
                            <p>LODChain</p>
                            <h2>Visit LODsyndesis</h2>
                        </header>
                        <p align="justify">
                            <a href="https://demos.isl.ics.forth.gr/lodsyndesis/">LODsyndesis</a> contains over 400 million entities
                            from 400 LOD datasets and two billion facts.
                            It has pre-computed the transitive and symmetric closure of 44 millions owl:sameAs relationships  among all the underlying datasets,
                            for storing  for each entity  in global entity-centric indexes all its URIs, its triples and their provenance.
                            <br><br>
                            It provides query services and measurements (by using special novel indexes &
                            algorithms) that are useful for several important tasks like (a) object co-reference, (b) dataset
                            discovery, (c) visualization, (d) connectivity assessment and monitoring, (e) machine-learning based tasks, (f)
                            information extraction tasks, and others.
                        </p>
                        <footer class="align-center">
                            <a href="https://demos.isl.ics.forth.gr/lodsyndesis/" class="button special">Visit LODsyndesis</a>
                        </footer>
                    </div>
                </div>
            </div>

        </div>


    </div>


</section>

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


<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/jquery.scrollex.min.js"></script>
<script src="assets/js/skel.min.js"></script>
<script src="assets/js/util.js"></script>
<script src="assets/js/main.js"></script>

</body>
</html>