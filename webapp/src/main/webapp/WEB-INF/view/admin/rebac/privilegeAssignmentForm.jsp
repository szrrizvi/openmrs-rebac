<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Privilege Assignments" otherwise="/login.htm" redirect="/admin/rebac/privilegeAssignmentList.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<style>
 th { text-align: left; }
 .thisPrivilegeAssignment { font-style: italic; }
</style>

<h2><spring:message code="Rebac.privilegeAssignment.manage.title"/></h2>	


<form name="myForm" method="post" action="privilegeAssignmentEdit.form">
<table>
	<tr>
		<th><spring:message code="Rebac.privilegeAssignment"/></th>
		<td>
			<input type="text" name="apName" value="${privilegeAssignment.authorizationPrincipal.authorizationPrincipal}" readOnly="readOnly">
		</td>
	</tr>
	<tr>
		<th><spring:message code="Rebac.privilegeAssignment.privileges"/></th>
		<td/>
	</tr>
	<tr>
		<th></th>
		<td name="privileges">
			<br>
			<openmrs:listPicker name="paPrivileges" allItems="${privileges}" currentItems="${privilegeAssignment.privileges}"/>
		</td>
	</tr>
	<tr>
		<th valign="top"><spring:message code="Rebac.policy"/></th>
		<td align="left">
				<c:out value="${policyString}"/>
		</td>
	</tr>
</table>
	<input type="hidden" name="privilegeAssignment" value="${privilegeAssignment.privilegeAssignmentId}"/>
	<input type="submit" value="<spring:message code="general.save"/>" />
</form>

<%@ include file="/WEB-INF/template/footer.jsp" %>