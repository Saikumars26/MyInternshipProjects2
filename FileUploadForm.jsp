<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/taglib_includes.jsp"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="//code.jquery.com/jquery-1.10.2.js"></script>
<script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
<script type="text/javascript">
$(function() {
	$(".datepicker").datepicker({
		showOn : "both",
		constrainInput : false,
		buttonImage : "img/calendar.gif",
		buttonImageOnly : true,
		buttonText : "Select date",
		dateFormat : "dd/mm/yy",
		showOtherMonths : true,
		selectOtherMonths : true,
		changeMonth : true,
		changeYear : true
	});
});
function validateDate(fieldName, fieldValue) {
	var matches = fieldValue.split("/");
	if (matches == null) {
		alert("Enter a valid date in DD/MM/YYYY format");
		fieldName.select();
		fieldName.focus();
		return false;
	}
	var d = matches[0];
	var m = matches[1] - 1;
	var y = matches[2];
	var composedDate = new Date(y, m, d);
	if (composedDate.getDate() == d && composedDate.getMonth() == m
			&& composedDate.getFullYear() == y) {
		return true;
	} else {
		alert("Enter a valid date in DD/MM/YYYY format");
		fieldName.select();
		fieldName.focus();
		return false;
	}
}

</script>
</head>
<body>
<div class="container clearfix" style="height: 440px">
<div class="spacer-40px"></div>
<div class="row">
 <div class="span1"></div> 
<div class="span10" align="center">

<c:if test="${uploadForm.errorMessage!=null}">
     	  <b><font color="red">${uploadForm.errorMessage}</font></b>
   		</c:if>
 
<form:form method="post" action="savefiles.html"
      modelAttribute="uploadForm"  enctype="multipart/form-data">
 
    <table id="fileTable">
          
          <tr><td><label>G.O Number</label></td>
            <td><input name="goNumber" type="text" /></td>
            <td><form:errors path="goNumber"
								cssStyle="color:red"></form:errors></td>
        </tr>
         <tr><td><label>G.O Date</label></td>
            <td><input name="goDate" type="text" class="datepicker"/></td>
            <td><form:errors path="goDate"
								cssStyle="color:red"></form:errors></td>
        </tr>
         <tr><td><label>G.O Description</label></td>
            <td><input name="goDescription" type="text" /></td>
            <td><form:errors path="goDescription"
								cssStyle="color:red"></form:errors></td>
        </tr>
   
        <tr><td><label>Select File</label></td>
            <td><input name="files" type="file" /></td>
            <td><form:errors path="files"
								cssStyle="color:red"></form:errors></td>
        </tr>
        
        
    </table>
    <br/><input type="submit" value="Upload" class="pngButton" style="width: 100px; color: white"/>
</form:form>
</div>
 <div class="span1"></div> 
 </div></div>

</body>
</html>