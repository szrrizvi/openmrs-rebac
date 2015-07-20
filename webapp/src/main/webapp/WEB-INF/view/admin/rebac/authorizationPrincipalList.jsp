<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Authorization Principals,Get Authorization Principals,Get Relationship Types,View Relationship Types,View Patient Programs" otherwise="/login.htm" redirect="/admin/rebac/authorizationPrincipalList.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="Rebac.authorizationPrincipal.manage.title"/></h2>

<a href="authorizationPrincipalForm.htm"><spring:message code="Rebac.authorizationPrincipal.add"/></a>
<br/><br/>

<b class="boxHeader"><spring:message code="Rebac.authorizationPrincipal.list.title"/></b>

<table cellpadding="2" cellspacing="0">
	<tr>
		<th/>
		<th> <spring:message code="general.name"/> </th>
	</tr>
	<c:forEach var="record" items="${authorizationPrincipals}" varStatus="rowStatus">
		<tr class='${rowStatus.index % 2 == 0 ? "evenRow" : "oddRow" }'>
			<th/>
			<td><a href="authorizationPrincipalForm.htm?apName=${record.authorizationPrincipal}">${record.authorizationPrincipal}</a></td>
		</tr>
	</c:forEach>
</table>

<%@ include file="/WEB-INF/template/footer.jsp" %>