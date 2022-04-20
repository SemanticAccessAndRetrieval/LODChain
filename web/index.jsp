
<%		boolean folders = false;
    String pag = "main.jsp";
    if (request.getAttribute("page") != null) {
        pag = request.getAttribute("page").toString();

    }

%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LODChain</title>
</head>
<body>
    <jsp:include page="<%=pag%>"/>
</body>
</html>
