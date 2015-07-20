<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Hybrid Formulas,Get Relationship Types,View Relationship Types" otherwise="/login.htm" redirect="/admin/rebac/hybridFormulaList.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<style>
 th { text-align: left; }
 .thisAuthorizationPrincipal { font-style: italic; }
</style>

<h2><spring:message code="Rebac.hybridFormula.manage.title"/></h2>	


<form name="myForm" method="post" action="hybridFormulaAdd.form">
<table cellpadding="4" cellspacing="4">
	<tr>
		<th><spring:message code="Rebac.hybridFormula"/></th>
		<td>
			<input type="text" name="hfName">
		</td>
	</tr>
	<tr>
		<th valign="top"><spring:message code="Rebac.policy"/></th>
		<td align="left">
				<openmrs_tag:rebacPolicyForm relationTypes="${relationIdentifiers}" allowFreeVars="true" useOwner="true"/>
		</td>
	</tr>
</table>
		<input type="submit" value="<spring:message code="general.save"/>" />		
</form>

<%@ include file="/WEB-INF/template/footer.jsp" %>