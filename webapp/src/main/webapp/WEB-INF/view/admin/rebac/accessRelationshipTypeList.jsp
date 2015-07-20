<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Relationship Types" otherwise="/login.htm" redirect="/admin/rebac/accessRelationshipTypeList.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<script type="text/javascript">

	$j(document).ready(function() {
		$j('.toggleAddAccessRelationshipType').click(function(event) {
			$j('#addAccessRelationshipType').slideToggle('fast');
			event.preventDefault();
		});
	});
</script>



<h2><spring:message code="Rebac.accessRelationshipType.manage.title"/></h2>


<br />

<a class="toggleAddAccessRelationshipType" href="#"><spring:message code="Rebac.accessRelationshipType.add"/></a>

<div id="addAccessRelationshipType" style="border: 1px black solid; background-color: #e0e0e0; display: none">
	<form method="post" action="accessRelationshipTypeAdd.form">
		<table>
			<tr>
				<th><spring:message code="general.name"/></th>
				<td>
					<input type="text" name="name" id="name" />
				</td>
			</tr>
			<tr>
				<td><spring:message code="general.description"/></th>
				<td>
					<textarea cols="40" row="3" name="description" id="description" ></textarea>
				</td>
			</tr>
			<tr>
				<th></th>
				<td>
					<input type="submit" value="<spring:message code="general.save"/>" />
					<input type="button" value="<spring:message code="general.cancel"/>" class="toggleAddAccessRelationshipType" />
				</td>
			</tr>
		</table>
	</form>
</div>


<br /><br />

<b class="boxHeader"><spring:message code="Rebac.accessRelationshipType.list.title"/></b>
<form method="post" class="box">
	<table style="width: 100%;"  cellpadding="2" cellspacing="0">
		<tr>
			<th> <spring:message code="general.name"/> </th>
			<th> <spring:message code="general.description"/> </th>
			<th> <spring:message code="general.delete"/> </th>
		</tr>
		<c:forEach var="accessRelationshipType" items="${accessRelationshipTypes}">
			<tr>
				<td>${accessRelationshipType.name}</td>
				<td>${accessRelationshipType.description}</td>
				<td><a href="accessRelationshipTypePurge.form?id=${accessRelationshipType.accessRelationshipTypeId}"><img src="${pageContext.request.contextPath}/images/delete.gif"/></a>
				</td>
			</tr>
		</c:forEach>
	</table>
</form>

<%@ include file="/WEB-INF/template/footer.jsp" %>