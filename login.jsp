<%@include file="/jsp/taglib_includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head></head>

<body>
<br/>
<br/>
  <div class="container" style="width: 25%;border: 5px solid #dadee1;min-height:220px;">

<br/>
<br/>
    
      <div class="heading-border clearfix">
          <p style="color: blue;font-family: Gabriolla;font-size: 20px;">Sign in </p>
        </div>
		<!-- <font color="red"><s:actionerror /></font> -->
		<form:form method="post" action="../admin/adminPage" commandName="loginForm">
 
 <br/>
     
         
          <c:if test="${loginForm.errorMessage!=null}">
     	  <b><font color="red">${loginForm.errorMessage}</font></b>
   		</c:if> 
		
		
            <div>
            	<input type="text" name="userName" placeholder="UserName" style="font-size: 17px;font-family: gabriolla;"/>
            		<form:errors path="userName"
								cssStyle="color:red"></form:errors>
            	
            </div>
            <div>
            	<input type="password" name="password" placeholder="Password" size="35"/>  
            	<form:errors path="password"
								cssStyle="color:red"></form:errors>
           </div>
            <div style="text-align: center;">
            	<input type="submit" value="Login" class="btn btn-info">
            </div>
         <br/>
      <br/>
        </form:form>
      </div>
      <br/>
      <br/>
     



</body>
</html>
<!-- content ends
================================================== -->
