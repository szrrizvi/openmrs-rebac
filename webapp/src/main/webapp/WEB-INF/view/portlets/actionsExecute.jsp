<%@ include file="/WEB-INF/template/include.jsp" %>

<script type="text/javascript">
	$j(document).ready(function() {
		$j('#executeAction').dialog({
			autoOpen: false,
			modal: true,
			title: '<openmrs:message code="Relationship.action.execute" javaScriptEscape="true"/>',
			width: '50%',
			zIndex: 100,
			buttons: { 
				'<openmrs:message code="general.save"/>': function() { alert("dummy send");},
				'<openmrs:message code="general.cancel"/>': function() { $j(this).dialog("close"); }
			}
		});
		
	});
</script>

<script type="text/javascript">

	function openExecuteAction(actionId){
	}
</script>

<div class="boxHeader${model.patientVariation}"><openmrs:message code="Rebac.action.active"/></div>

<table>
	<tr>
		<th/>
		<th><openmrs:message code="general.name"/></th>
	</tr>
	<c:forEach var="action" items="${model.actions}">
		<tr>
			<th/>
			<td>
				<a href="admin/rebac/actionExecuteForm.htm?patientId=${model.patientId}&actionId=${action.actionId}"> ${action.name} </a>
			</td>
		</tr>
	</c:forEach>
</table>

<div id="executeAction">
	<table id="executeActionTable">
	</table>
</div>