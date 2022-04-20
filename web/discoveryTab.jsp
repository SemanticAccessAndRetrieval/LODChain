<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<html>


    <body>


        <table class="alt"  border="1" id="discov" >


            <c:forEach var="disc" items="${discovery}" varStatus="statusdisc">
                <c:choose>
                    <c:when test="${statusdisc.index==0}">
                        <tr class="simple" style="font-weight: bold;">
                        </c:when>
                        <c:otherwise>
                        <tr class="blueTr" style="background-color:white">
                        </c:otherwise>
                    </c:choose>
                    <c:set var="count" value="0" scope="page" />
                    <c:forEach var="disc2" items="${discovery[statusdisc.index]}" varStatus="status3">
                        <c:if test = "${count == 0}">
                            <td width="25%" style="font-weight: bold;text-align: center; vertical-align: middle;">${disc2}</td>
                        </c:if>
                        <c:if test = "${count == 1}">
                            <td width="25%" style="text-align: center; vertical-align: middle;">${disc2}</td>
                        </c:if>
                            
                        <c:if test = "${count == 2}">
                            <td width="25%" style="text-align: center; vertical-align: middle;">${disc2}</td>
                        </c:if>
                            
                        <c:if test = "${count == 3}">
                            <td width="25%" style="text-align: center; vertical-align: middle;">${disc2}</td>
                        </c:if>

                        <c:set var="count" value="${count + 1}" scope="page"/>
                    </c:forEach>
                </tr>
            </c:forEach>
        </table>

    </body>
</html>