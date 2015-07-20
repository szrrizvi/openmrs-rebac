<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Rebac Authorization Toggle" otherwise="/login.htm" redirect="/admin/rebac/rebacEnableDisable.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="Rebac.enableDisable.manage.title"/></h2>
<br/>

<c:choose>
	<c:when test="${rebacEnable == true}">
		<spring:message code="Rebac.enableDisable.isEnabled"/>
		
		<form method="post" action="disableRebac.form">
			<input type="submit" value="<spring:message code="Rebac.enableDisable.disable"/>" />
		</form>
		
	</c:when>
	<c:otherwise>
		<spring:message code="Rebac.enableDisable.isDisabled"/>
		
		<form method="post" action="enableRebac.form">
			<input type="submit" value="<spring:message code="Rebac.enableDisable.enable"/>" />
		</form>
		
	</c:otherwise>
</c:choose>

<br/><br/><br/>

	<spring:message code="Rebac.enableDisable.algorith.current" arguments="${currentAlgorithm}"/>
	
	<form method="post" action="setAlgorithm.form">
		<select name="algorithm" id="algorithm">
			<c:forEach var="algm" items='${algorithms}'>
				<option value="${fn:toLowerCase(algm)}">${algm}</option>
			</c:forEach>
		</select>
		<input type="submit" value="Change Algorithm">
	</form>

<%@ include file="/WEB-INF/template/footer.jsp" %>
