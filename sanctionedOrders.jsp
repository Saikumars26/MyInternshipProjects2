<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

<style type="text/css">
#clearance{
font-family: Book Antiqua;
font-size: 17px;
}

#tblData tr>th{
background-color: #035BB6;
text-align: center;
color: white;
}

p{
line-height: 1.5;

}
</style>

<script>
$(document).ready(
		function() {

			var rowcount = $('#tblData tr').length;
			for (var i = 1; i < rowcount; i++) {

				if (i % 2 == 0) {
					$('#tblData').find("tr:eq(" + i + ")").css("background",
							"#d4d4f4");
					
				} else {
					$('#tblData').find("tr:eq(" + i + ")").css("background",
							"#ffffff");
				}

			}

		});
</script>
</head>
<body>
<div class="container" id="clearance" style="min-height:440px;">
<div>
<h3 style="font-family: Book Antiqua; font-size: 17px; color: #AC1313;">
<strong>LIST OF SANCTIONS</strong>
</h3>
</div>
<div>
	<table class="table table-bordered" id="tblData" style="width: 70%;" align="center">
		<thead>
			<tr>
				<th>Sl.No</th>
				<th>G.O Number</th>
				<th>G.O Date</th>
				<th>G.O Description</th>
				<th>Download</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${sanctionedOrders!=null}">
				<c:forEach var="p" items="${sanctionedOrders}" varStatus="count">
					<tr>
						<td><c:out value="${count.count}"></c:out></td>
						<td><c:out value="${p.goNumber}"></c:out></td>
						<td><c:out value="${p.goDate}"></c:out></td>
						<td><c:out value="${p.goDescription}"></c:out></td>
						<td><a href="${p.goFilePath}">Download File</a></td>

					</tr>
				</c:forEach>
			</c:if>
		</tbody>
	</table>
	</div>	
<div class="spacer-40px"></div>
</div>
</body>
</html>