<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="View Actions," otherwise="/login.htm" redirect="/admin/rebac/actionList.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2>
	<spring:message code="Rebac.action.view"/>
</h2>
<br/><br/>

<table cellspacing="5" cellpadding="5">
	<tr align="left">
		<th><spring:message code="general.name"/></th>
		<td>
			${action.name}
		</td>
	</tr>

	<tr align="left">
		<th valign="top"><spring:message code="Rebac.policy"/></th>
		<td>
			<spring:message code="Rebac.hybridFormula"/>: &nbsp
			${action.hybridFormula.hybridFormula}
		</td>
	</tr>
	<tr align="left" valign="top">
		<th><spring:message code="Rebac.action.participants"/></th>
		<td>
			<table cellspacing="2" cellpadding="2">
				<tr>
					<th/>
					<th><spring:message code="general.name"/></th>
				</tr>
				<c:forEach var="record" items="${action.participants}" varStatus="rowStatus">
					<tr class='${rowStatus.index % 2 == 0 ? "evenRow" : "oddRow" }'>
						<th><spring:message code="Rebac.action.participant"/>${rowStatus.count}</th>
						<td>${record.name}</td>
					</tr>				
				</c:forEach>
			</table>
		</td>
	</tr>
	<tr align="left" valign="top">
		<th><spring:message code="Rebac.action.precondition"/></th>
		<td>
			<table cellspacing="2" cellpadding="2">
				<tr>
					<td><spring:message code="Rebac.hybridFormula"/>: &nbsp
					${action.precondition.hybridFormula.hybridFormula}</td>
				</tr>
				<tr>
					<td><spring:message code="Rebac.freeVariables"/>
				</tr>
				<c:forEach var="record" items="${action.participants}" varStatus="rowStatus">
					<tr class='${rowStatus.index % 2 == 0 ? "evenRow" : "oddRow" }'>
						<th>
							${record.name}: &nbsp
						</th>
						<c:forEach var="freeVar" items="${record.freeVariables}">
							<td>
								${freeVar}  
							</td>
						</c:forEach>
					</tr>				
				</c:forEach>
			</table>
		</td>
	</tr>
	<tr align="left" valign="top">
		<th><spring:message code="Rebac.action.effects"/></th>
		<td>
			<table cellspacing="2" cellpadding="2">
				<c:forEach var="record" items="${action.effects}" varStatus="rowStatus">
					<tr class='${rowStatus.index % 2 == 0 ? "evenRow" : "oddRow" }'>
						<th>
							<spring:message code="Rebac.action.effect"/>${rowStatus.count}: 
						</th>
						<td>
							${record.effectType} 
							<spring:message code="Rebac.action.form.effect.1"/> 
							${record.participantA.name}
							<spring:message code="Rebac.action.form.effect.2"/>
							${record.participantB.name}
							<spring:message code="Rebac.action.form.effect.3"/>
							${record.accessRelationshipType.name}
						</td>
					</tr>				
				</c:forEach>
			</table>
		</td>
	</tr>
</table>

<form method="post" action="actionPurge.form">
	<input type="hidden" name="actionId" value="${action.actionId}" />
	<input type="submit" value="<spring:message code="general.delete"/>" />
</form>

<%@ include file="/WEB-INF/template/footer.jsp" %>