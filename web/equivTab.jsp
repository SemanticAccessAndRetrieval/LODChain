<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<html>
    <body>
            <table class="alt" border="1" id="equiv">
             <c:forEach var="sam" items="${sameAs}" varStatus="same">
                    <c:choose>
                        <c:when test="${same.index==0}">
                            <tr class="simple" style="font-weight: bold;">
                            </c:when>
                            <c:otherwise>
                            <tr class="blueTr" style="background-color:white">
                            </c:otherwise>
                        </c:choose>
                        <c:set var="count" value="0" scope="page" />
                        <c:forEach var="sam2" items="${sameAs[same.index]}" varStatus="same2">
                            <c:if test = "${count == 0}">
                                <td width="70%" style="font-weight: bold;text-align: center; vertical-align: middle;">${sam2}</td>
                            </c:if>
                            <c:if test = "${count == 1}">
                                <td width="100%" style="text-align: center; vertical-align: middle;">${sam2}</td>
                            </c:if>

                            <c:set var="count" value="${count + 1}" scope="page"/>
                        </c:forEach>
                    </tr>
                </c:forEach>
            </table>
   
    </body>
</html>