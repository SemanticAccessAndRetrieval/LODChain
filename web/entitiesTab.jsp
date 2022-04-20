<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<html>
	

	<body>
            
		
                    <table class="alt" border="1" id="entities">
		
                    
		<c:forEach var="weather" items="${results}" varStatus="status">
			<c:choose>
				<c:when test="${status.index==0}">
					<tr class="simple" style="font-weight: bold;">
				</c:when>
				<c:otherwise>
					<tr class="blueTr" style="background-color:white">
				</c:otherwise>
			</c:choose>
                                            <c:set var="count" value="0" scope="page" />
				<c:forEach var="weather2" items="${results[status.index]}" varStatus="status3">
                                    <c:if test = "${count == 0}">
                                    <td width="35%" style="font-weight: bold;text-align: center; vertical-align: middle;">${weather2}</td>
                                    </c:if>
                                    <c:if test = "${count == 1}">
                                    <td width="65%" style="text-align: center; vertical-align: middle;">${weather2}</td>
                                    </c:if>
                                    
                                    <c:set var="count" value="${count + 1}" scope="page"/>
			</c:forEach>
		</tr>
    </c:forEach>
</table>
           
</body>
</html>