<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Relationships" otherwise="/login.htm" redirect="/admin/rebac/accessRelationshipList.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<script type="text/javascript">

	// jQuery function for toggling add access relationship section
	$j(document).ready(function() {
		$j('.toggleAddAccessRelationship').click(function(event) {
			$j('#addAccessRelationship').slideToggle('fast');
			event.preventDefault();
		});		
	});
	
</script>


<h2><spring:message code="Rebac.accessRelationship.manage.title"/></h2>
<br/>

<a class="toggleAddAccessRelationship" href="#"><spring:message code="Rebac.accessRelationship.add"/></a>

<div id="addAccessRelationship" style="border: 1px black solid; background-color: #e0e0e0; display: none">
	<form method="post" action="accessRelationshipAdd.form">
		<table>
				<th><spring:message code="Person.searchBox"/></th>
				<td>
					<openmrs_tag:personField formFieldName="personA" formFieldId="personA_id"/>
				</td>
			</tr>
			<tr>
				<th><spring:message code="Person.searchBox"/></th>
				<td>
					<openmrs_tag:personField formFieldName="personB"/>
				</td>
			</tr>
			<tr id=relationshipType>
				<th><spring:message code="Rebac.accessRelationshipType"/></th>
				<td>
					<select name="relationshipType">
						<option value=""></option>
						<openmrs:forEachRecord name="accessRelationshipType">
							<option value="${record.accessRelationshipTypeId}">${record}</option>
						</openmrs:forEachRecord>
					</select>
				</td>
			</tr>
			<tr>
				<th></th>
				<td>
					<input type="submit" value="<spring:message code="general.save"/>" />
					<input type="button" value="<spring:message code="general.cancel"/>" class="toggleAddAccessRelationship" />
				</td>
			</tr>
		</table>
	</form>
</div>

<br/>
<br/>


<spring:message code="Rebac.accessRelationship.find"/>
<br />
<form method="get">

<table>
	<tr>
		<td><spring:message code="Person.searchBox"/></td>
		<td><input type="text" name="name" value="<c:out value="${param.name}"/>" /></td>
	</tr>
	<tr>
		<td></td>
		<td><input type="submit" name="action" value="<spring:message code="general.search"/>"/></td>
	</tr>
</table>

</form>
<br/>

<b class="boxHeader"><spring:message code="Rebac.accessRelationship.list.title"/></b>
<form method="post" class="box">
	<table style="width: 100%;"  cellpadding="2" cellspacing="0">
		<tr>
			<th align="left"> <spring:message code="general.id"/> </th>
			<th align="left"> <spring:message code="Relationship.personA.id"/> </th>
			<th align="left"> <spring:message code="Relationship.personA.name"/> </th>
			<th align="left"> <spring:message code="Relationship.personB.id"/> </th>
			<th align="left"> <spring:message code="Relationship.personB.name"/> </th>
			<th align="left"> <spring:message code="Rebac.accessRelationshipType"/> </th>
			<th align="left"> <spring:message code="general.delete"/> </th>
		</tr>
		<c:forEach var="relationship" items="${relationships}">
			<tr>
				<td valign="top">${relationship.accessRelationshipId}</td>
				<td valign="top">${relationship.personA.personId}</td>
				<td valign="top">${relationship.personA.personName}</td>
				<td valign="top">${relationship.personB.personId}</td>
				<td valign="top">${relationship.personB.personName}</td>
				<td valign="top">${relationship.accessRelationshipType}</td>
				<td valign="top">
					<a href="accessRelationshipPurge.form?id=${relationship.accessRelationshipId}"><img src="${pageContext.request.contextPath}/images/delete.gif"/></a>
				</td>
			</tr>
		</c:forEach>
	</table>
</form>

<%@ include file="/WEB-INF/template/footer.jsp" %>
