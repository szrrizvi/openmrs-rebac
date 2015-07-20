<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Hybrid Formulas,Get Relationship Types,View Relationship Types" otherwise="/login.htm" redirect="/admin/rebac/hybridFormulaList.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="Rebac.hybridFormula.manage.title"/></h2>

<a href="hybridFormulaForm.htm"><spring:message code="Rebac.hybridFormula.add"/></a>
<br/><br/>

<b class="boxHeader"><spring:message code="Rebac.hybridFormula.list.title"/></b>

<table cellpadding="2" cellspacing="0">
	<tr>
		<th><spring:message code="general.id"/></th>
		<th><spring:message code="general.name"/></th>
		<th><spring:message code="Rebac.hybridFormula.formula"/></th>
		<th><spring:message code="general.delete"/></th>
	</tr>
	<c:forEach var="record" items="${hybridFormulas}" varStatus="rowStatus">
		<tr class='${rowStatus.index % 2 == 0 ? "evenRow" : "oddRow" }'>
			<td>${record.hybridFormulaId}</td>
			<td>${record.hybridFormula}</td>
			<td>${record.policyImpl}</td>
			<td><a href="hybridFormulaPurge.form?id=${record.hybridFormulaId}"><img src="${pageContext.request.contextPath}/images/delete.gif"/></a></td>
		</tr>
	</c:forEach>
</table>

<%@ include file="/WEB-INF/template/footer.jsp" %>