<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Actions,Get Relationship Types,View Relationship Types,Get Programs,View Programs" otherwise="/login.htm" redirect="/admin/rebac/actionList.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="Rebac.action.manage.title"/></h2>

<a href="actionForm.htm"><spring:message code="Rebac.action.add"/></a>
<br/><br/>

<b class="boxHeader"><spring:message code="Rebac.action.list.title"/></b>

<table cellpadding="2" cellspacing="0">
	<tr>
		<th><spring:message code="general.id"/></th>
		<th> <spring:message code="general.name"/> </th>
	</tr>
	<c:forEach var="record" items="${actions}" varStatus="rowStatus">
		<tr class='${rowStatus.index % 2 == 0 ? "evenRow" : "oddRow" }'>
			<th/>
			<td>${record.id}</td>
			<td><a href="actionViewForm.htm?id=${record.actionId}">${record.name}</a></td>
		</tr>
	</c:forEach>
</table>

<%@ include file="/WEB-INF/template/footer.jsp" %>