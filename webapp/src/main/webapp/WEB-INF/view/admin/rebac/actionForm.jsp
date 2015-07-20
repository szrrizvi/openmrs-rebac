<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Actions,Get Relationship Types,View Relationship Types,Get Programs,View Programs" otherwise="/login.htm" redirect="/admin/rebac/actionForm.htm" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>
<script type="text/javascript">
	window.onload = function() {
		// On load, populate the precondition free variables		
		var sel = document.getElementById("precondition");
		updatePrecondition(sel);
	}
</script>


<script type="text/javascript">	

	var participantsCount = 0;
	
	function addParticipant(){
	
		var parName = "";
		
		do {
			parName = prompt("Enter Participant Name\n(NOTE: Name cannot be blank)", "");
			
			if (parName == null){
				return;
			}
		} while ($j.trim(parName) == "");
	
		participantsCount++;

		var table = document.getElementById("participantsTable");
		var row = table.insertRow(-1);
		var cell1 = row.insertCell(-1);
		cell1.innerHTML = 'Participant ' + participantsCount + '</br>';
		
		if (participantsCount % 2 == 0){
			row.className = "evenRow";
		} else {
			row.className= "oddRow";
		}
		
		var name = document.createElement("input");
		name.type = "text";
		name.name = "p" + participantsCount + "Name";
		name.id = "p" + participantsCount + "Name";
		name.readOnly = "readOnly";
		name.value = parName;
		
		cell1.appendChild(name);
				
		addParticipantSideEffect();
	}
	
	function deleteParticipant(){
		if (participantsCount > 0){
			var table =  document.getElementById("participantsTable");
			table.deleteRow(-1);
			
			deleteParticipantSideEffects();
			
			participantsCount--;
		} else {
			alert("No participants");
		}
	}

</script>

<script type="text/javascript">

	var freeVariableCount = 0;

	function updatePrecondition(select){
		
		// Get the table for free variables
		var table = document.getElementById("freeVariablesTable");
		// Reset previous settings
		$j(table).empty();
		freeVariableCount = 0;
		
		<c:forEach var="record" items="${hybridFormulas}"> 
			// Find the select Hybrid Formula
			if ("${record.id}" == select.value){				
				<c:forEach var="varName" items="${record.variableNames}">
					// Add a participant selector for each explicit free variable
					freeVariableCount++;
					
					var row = table.insertRow(-1);
					var cell1 = row.insertCell(-1);		
				
					
					//Create the label for the free variable
					var label = document.createElement("input");
					label.type = "text";
					label.name = "varName" + freeVariableCount;
					label.id = "varName" + freeVariableCount;
					label.value="${varName}";
					label.readOnly = "readOnly";
					
					
					//Create the select element
					var varSelect = document.createElement("select");
					varSelect.name= "freeVariable" + freeVariableCount;
					varSelect.id = "freeVariable" + freeVariableCount;
					varSelect.onclick = function() {selectBackgroundColor(this);};
				
					//Create the option for patient
					var patOpt = document.createElement("option");
					patOpt.value = "patient";
					patOpt.text = "Patient";		
					varSelect.add(patOpt,null);
				
					//Create the option for user
					var userOpt = document.createElement("option");
					userOpt.value = "user";
					userOpt.text = "User";
					varSelect.add(userOpt, null);
				
					//Create the options for each explicit participant
					for (var i = 1; i <= participantsCount; i++){
						
						var name = document.getElementById('p'+ i + 'Name');
						
						var opt = document.createElement("option");
						opt.value = i;
						opt.text = 'p' + i + ':' + name.value;
						varSelect.add(opt, null);
					}
					
					//Add the label and select element to the cell
					cell1.appendChild(label);
					cell1.appendChild(varSelect);
				</c:forEach>
				
				return;
			}
		</c:forEach>
	}

</script>

<script type="text/javascript">	
	var effectsCount = 0;
	
	function addEffect(){
		effectsCount++;
		
		var table = document.getElementById("effectsTable");
		var row = table.insertRow(-1);
		var cell1 = row.insertCell(-1);
		cell1.innerHTML = 'Effect ' + effectsCount;
		
		if (effectsCount % 2 == 0){
			row.className = "evenRow";
		} else {
			row.className = "oddRow";
		}
		
		// Create select elements for participants
		
		var ent1Select = document.createElement("select");
		ent1Select.name = 'effect' + effectsCount + 'e1';
		ent1Select.id = 'effect' + effectsCount + 'e1';
		ent1Select.onclick = function() {selectBackgroundColor(this);};
		
		var ent2Select = document.createElement("select");
		ent2Select.name = 'effect' + effectsCount + 'e2';
		ent2Select.id = 'effect' + effectsCount + 'e2';
		ent2Select.onclick = function() {selectBackgroundColor(this);};
		
		var patOpt = document.createElement("option");
		patOpt.value = "patient";
		patOpt.text = "Patient";		
		ent1Select.add(patOpt,null);
		ent2Select.add(patOpt.cloneNode(true), null);
		
		var userOpt = document.createElement("option");
		userOpt.value = "user";
		userOpt.text = "User";
		ent1Select.add(userOpt, null);
		ent2Select.add(userOpt.cloneNode(true), null);
		
		for (var i = 1; i <= participantsCount; i++){
			
			var name = document.getElementById('p'+ i + 'Name');
			
			var opt = document.createElement("option");
			opt.value = i;
			opt.text = 'p' + i + ':' + name.value;
			ent1Select.add(opt, null);
			ent2Select.add(opt.cloneNode(true), null);
		}
		
		var cell2 = row.insertCell(-1);
		
		var effectSel = document.createElement("select");
		effectSel.name = "effect" + effectsCount + "Type";

		var addOpt = document.createElement("option");
		addOpt.value = "add";
		addOpt.text = "Add";
		effectSel.add(addOpt, null);
		
		var removeOpt = document.createElement("option");
		removeOpt.value = "remove";
		removeOpt.text = "Remove";
		effectSel.add(removeOpt, null);
		
		var relId = document.createElement("select");
		relId.name = "effect" + effectsCount + "RelId";

		<c:forEach var="record" items="${relIds}">
			var opt = document.createElement("option");
			opt.text = "${record.name}";
			opt.value = "${record.accessRelationshipTypeId}";
			relId.add(opt, null);
		</c:forEach>
		
		
		var label1 = document.createTextNode(' <spring:message code="Rebac.action.form.effect.1"/>');
		var label2 = document.createTextNode(' <spring:message code="Rebac.action.form.effect.2"/>');
		var label3 = document.createTextNode(' <spring:message code="Rebac.action.form.effect.3"/>');
		
		cell2.appendChild(effectSel);
		cell2.appendChild(label1);
		cell2.appendChild(ent1Select);
		cell2.appendChild(label2);
		cell2.appendChild(ent2Select);
		cell2.appendChild(label3);
		cell2.appendChild(relId);
	}
	
	function deleteEffect(){
		if (effectsCount > 0){
			var table =  document.getElementById("effectsTable");
			table.deleteRow(-1);
			
			effectsCount--;
		} else {
			alert("No Effects");
		}
	}
</script>

<script type="text/javascript">

	// Adds the newly created participant to the existing participant selects
	function addParticipantSideEffect(){
		var ptcName = document.getElementById("p" + participantsCount + "Name");
		
		// Edit entity selects for free variables 
		for (i = 1; i <= freeVariableCount; i++){
			// Get the select element
			var sel = document.getElementById("freeVariable" + i);
			
			// Create the new option
			var opt = document.createElement("option");
			opt.value = participantsCount;
			opt.text = "p" + participantsCount + ":" + ptcName.value;
			
			// Add the new option to the select
			sel.add(opt,null);
		}
		
		// Edit entity selects for effects
		for (i = 1; i <= effectsCount; i++){
			// Get the select elements
			var e1 = document.getElementById("effect" + i + "e1");
			var e2 = document.getElementById("effect" + i + "e2");
			
			// Create the new option
			var opt = document.createElement("option");
			opt.value = participantsCount;
			opt.text = "p" + participantsCount + ":" + ptcName.value;
			
			// Add the new option to the selects 
			e1.add(opt,null);
			e2.add(opt.cloneNode(true),null);
		}
	}
	
	// Removed the late deleted participant from the existing participants selects
	function deleteParticipantSideEffects(){
	
		var ptcVal = participantsCount;	
		var ptcInd = participantsCount + 1;
	
		// Edit entity selects for free variables
		for (i = 1; i <= freeVariableCount; i++){
			// Get the select element
			var sel = document.getElementById("freeVariable" + i);
			
			// If the deleted participant was selected, then post an alert message
			// and change the colour of the select element to red
			if (sel.value == ptcVal){
				sel.selectedIndex = 0;
				sel.style.backgroundColor = "red";
				alert("Changed Effect " + i + " Entity 1");
			}
			
			// Remove the participant from the select
			sel.remove(ptcInd);
		}
		
		// Edit entity selects for effects
		for (i = 1; i <= effectsCount; i++){
			// Get the select elements
			var e1 = document.getElementById("effect" + i + "e1");
			var e2 = document.getElementById("effect" + i + "e2");
			
			// For e1: 
			// If the deleted participant was selected, then post an alert message
			// and change the colour of the select element to red
			if (e1.value == ptcVal){
				e1.selectedIndex = 0;
				e1.style.backgroundColor = "red";
				alert("Changed Effect " + i + " Entity 1");
			}

			// For e2: 
			// If the deleted participant was selected, then post an alert message
			// and change the colour of the select element to red
			if (e2.value == ptcVal){
				e2.selectedIndex = 0;
				e2.style.backgroundColor = "red";
				alert("Changed Effect " + i + " Entity 2");				
			}
			
			// Remove the participant from the selects
			e1.remove(ptcInd);
			e2.remove(ptcInd);
		}
	}
		
	function selectBackgroundColor(select){
		if (select.style.backgroundColor == "red"){
			select.style.backgroundColor = "white";
		}
	}
</script>

<h2>
	<spring:message code="Rebac.action.create"/>
</h2>
<br/><br/>

<form name="actionForm" method="post" action="actionAdd.form">
	<table>
		<tr align="left">
			<th><spring:message code="general.name"/></th>
			<td>
				<input type="text" name="actionName">
			</td>
		</tr>
		<tr align="left">
			<th valign="top"><spring:message code="Rebac.action.policy"/></th>
			<td>
				<select name="precondition0">
					<c:forEach var="record" items="${avHybridFormulas}">
						<option value="${record.id}">${record.hybridFormula}</option>				
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr align="left" valign="top">
			<th><spring:message code="Rebac.action.participants"/></th>
			<td>
				<table id="participantsTable" cellspacing="2" cellpadding="2">
				</table>
			</td>
			<td valign="top">
				<button type="button" onclick="addParticipant()"><spring:message code="Rebac.action.participants.add"/></button>
				<br />
				<button type="button" onclick="deleteParticipant()"><spring:message code="Rebac.action.participants.delete"/></button>
			</td>
		</tr>
		<tr align="left" valign="top">
			<th><spring:message code="Rebac.action.preconditions"/></th>
			<td valign="top">
				<select id="precondition" name="precondition" onchange="updatePrecondition(this)">
					<c:forEach var="record" items="${hybridFormulas}">
						<option value="${record.id}">${record.hybridFormula}</option>				
					</c:forEach>
				</select>
			</td>
			<td>
				<table id="freeVariablesTable" cellspacing="2" cellpadding="2">
				</table>
			</td>
		</tr>
		<tr align="left" valign="top">
			<th><spring:message code="Rebac.action.effects"/></th>
			<td>
				<table id="effectsTable" cellspacing="2" cellpadding="2">
				</table>
			</td>
			<td>
				<button type="button" onclick="addEffect()"><spring:message code="Rebac.action.effects.add"/></button>
				<br />
				<button type="button" onclick="deleteEffect()"><spring:message code="Rebac.action.effects.delete"/></button>
			</td>
		</tr>
	</table>
	<input type="submit" value="<spring:message code="general.save"/>" />
</form>


<%@ include file="/WEB-INF/template/footer.jsp" %>