<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Authorization Principals,Get Authorization Principals,Get Relationship Types,View Relationship Types" otherwise="/login.htm" redirect="/admin/rebac/authorizationPrincipalList.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<style>
 th { text-align: left; }
 .thisAuthorizationPrincipal { font-style: italic; }
</style>

<h2><spring:message code="Rebac.authorizationPrincipal.manage.title"/></h2>	


<form name="myForm" method="post" action="authorizationPrincipalAdd.form">
<table cellpadding="4" cellspacing="4">
	<tr>
		<th><spring:message code="Rebac.authorizationPrincipal"/></th>
		<td>
			<c:if test="${authorizationPrincipal == null}"><input type="text" name="apName"></c:if>
			<c:if test="${!(authorizationPrincipal == null)}">
				<input type="text" name="apName" value="${authorizationPrincipal.authorizationPrincipal}" readOnly="readOnly">
			</c:if>
		</td>
	</tr>
	<c:if test="${privileges != null}">
		<tr>
			<th valign="top"><spring:message code="Rebac.privilegeAssignment.privileges"/></th>
			<td>
				<c:forEach var="record" items="${privileges}">
					<c:out value="${record.name}"/>
					<br/>
				</c:forEach>
			</td>
		</tr>
	</c:if>
	<tr>
		<th valign="top"><spring:message code="Rebac.policy"/></th>
		<td align="left">
			<c:if test="${policyString == null}">
				<openmrs_tag:rebacPolicyForm relationTypes="${relationIdentifiers}"/>
			</c:if>
			<c:if test="${!(policyString == null)}">
				<c:out value="${policyString}"/>
			</c:if>
		</td>
	</tr>
</table>
	<c:if test="${authorizationPrincipal == null}">
		<input type="submit" value="<spring:message code="general.save"/>" />		
	</c:if>
</form>

<c:if test="${!(authorizationPrincipal == null)}">
	<form method="post" action="authorizationPrincipalPurge.form">
		<input type="hidden" name="authorizationPrincipal" value="${authorizationPrincipal.authorizationPrincipal}" />
		<input type="submit" value="<spring:message code="general.delete"/>" />
	</form>
</c:if>

<%@ include file="/WEB-INF/template/footer.jsp" %>