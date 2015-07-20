<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Privilege Assignments" otherwise="/login.htm" redirect="/admin/rebac/privilegeAssignmentList.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="Rebac.privilegeAssignment.manage.title"/></h2>

<br/><br/>

<b class="boxHeader"><spring:message code="Rebac.privilegeAssignment.list.title"/></b>

<table cellpadding="2" cellspacing="0">
	<tr>
		<th/>
		<th> <spring:message code="general.name"/> </th>
	</tr>
	<c:forEach var="record" items="${privilegeAssignments}" varStatus="rowStatus">
		<tr class='${rowStatus.index % 2 == 0 ? "evenRow" : "oddRow" }'>
			<th/>
			<td><a href="privilegeAssignmentForm.htm?paId=${record.privilegeAssignmentId}">${record.authorizationPrincipal.authorizationPrincipal}</a></td>
		</tr>
	</c:forEach>
</table>

<%@ include file="/WEB-INF/template/footer.jsp" %>