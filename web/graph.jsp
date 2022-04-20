<%-- 
    Document   : graph.jsp
    Created on : Nov 30, 2021, 3:59:25 PM
    Author     : mountant
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div id=graph>
     <div id="legend" style='border:2px solid black'>
     
    </div> <br>
    <div style="float:right">
    <button onclick="seeByConnections()" class="button special">Old/New Connections Mode</button>
    <button onclick="seeByDomain()"  class="button special">Domain Mode</button>
    </div>
    <div id='mynetwork' style='width:100%;'></div>  <br>
   
</div>
