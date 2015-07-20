<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="" otherwise="/login.htm" redirect="index.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>

<h2>
	<spring:message code="Rebac.action.execute" arguments="${action.name}"/>
</h2>
<br/><br/>

<a href="${pageContext.request.contextPath}/patientDashboard.form?patientId=${patientId}"><openmrs:message code="patientDashboard.viewDashboard"/></a>
<br/><br/>

<div>
	<form method="post" action="actionExecuteSubmit.form">
		<table cellspacing="2" cellpadding="2">
			<caption>
				<h3><spring:message code="Rebac.action.participants"/></h3>
			</caption>
			<tr>
				<th>
					<spring:message code="general.name"/>
				</th>
				<th>
					<spring:message code="Rebac.action.execute.selectUser"/>
				</th>
			</tr>
			<c:forEach var="participant" items="${action.participants}" varStatus="rowStatus">
				<c:if test="${rowStatus.count > 2}">
					<tr>
						<th align="left">
							${participant.name}
						</th>
						<td>
							<openmrs_tag:userField formFieldName="participant_${rowStatus.count}"/>
						</td>
					</tr>
				</c:if>
			</c:forEach>
		</table>
		<br/><br/>
		
		<table>
			<caption><h3><spring:message code="Rebac.action.precondition"/></h3></caption>
			<tr>
				<th>
					<spring:message code="Rebac.action.precondition"/>
				</th>
				<td>
					<spring:message code="Rebac.action.precondition.text" arguments="${action.precondition.hybridFormula.hybridFormula}"/>
				</td>
			</tr>
		</table>
		<br/><br/>
		
		<table cellspacing="2" cellpadding="2">
			<caption><h3><spring:message code="Rebac.action.effects"/></h3></caption>
			<c:forEach var="effect" items="${action.effects}"  varStatus="rowStatus">
				<tr>
					<th>
						<spring:message code="Rebac.action.effect"/>${rowStatus.count}
					<th>
					<td>
						<spring:message code="Rebac.action.effect.text" arguments="${effect.participantA.name}, ${effect.participantB.name}, ${effect.effectType}, ${effect.accessRelationshipType.name}"/>
					</td>
				</tr>
			</c:forEach>
		</table>
		<input type="hidden" name="patientId" id="patientId" value="${patientId}"/>
		<input type="hidden" name="actionId" id="actionId" value="${action.actionId}"/>
		<input type="submit" value="<spring:message code="general.save"/>" />
	</form>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>
