<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="relationTypes" required="true" type="java.util.List" %>
<%@ attribute name="allowFreeVars" required="false" type="java.lang.Boolean" %>
<%@ attribute name="useOwner" required="false" type="java.lang.Boolean" %>

<c:choose>
	<c:when test="${useOwner}">
		<c:set var="variants">Requestor,Owner,True,False,Diamond,Box,And,Or,Not,At,Bind,Variable</c:set>
	</c:when>
	<c:otherwise>
		<c:set var="variants">Requestor,Resource,True,False,Diamond,Box,And,Or,Not,At,Bind,Variable</c:set>
	</c:otherwise>
</c:choose>


<script type="text/javascript">
	window.onload = function() {
		// On load, generate the policy string
		updateHidden();
	}
</script>


<script type="text/javascript">

//Node to maintain the ReBAC policy tree
function Node(parentNode, variant)
{
    this.parent=parentNode;
    this.variant=variant;

    //A variant can have 0, 1, or 2 subpolicies
    this.childA = null;
    this.childB = null;

    //The Box and Diamond variants have relation identifier and direction
    this.relationIdentifier = null;
    this.direction = null;
    
    //The At, Bind, and Variant variants have variables
    this.variable = null;
}

//toString Function
Node.prototype.toString = function(){
	
	// Create variables for each field
    var sChildA = "{null}";
    var sChildB = "{null}";
    
    // if childA is not null, get it's string version
	if (this.childA != null){
		sChildA = this.childA.toString();
	}
	
    // if childB is not null, get it's string version
	if (this.childB != null){
		sChildB = this.childB.toString();	
	}

    //Generate and return the string representing this policy
	var str = "{"+ this.variant + "," + this.relationIdentifier + "," + this.direction + "," + this.variable + "," + sChildA + "," + sChildB + "}";
	return str;
}

// Returns the variable names binded this far, as well as the freeVariables
Node.prototype.getAncestorVarNames = function(){
	var varNames = [];
	
	// If the varaint is bind, then obtain the binded variable name
	if (this.variant == "bind"){
		varNames.push(document.getElementById(this.variable).value);
	}
	
	// If this is not the root node, get the binded variables for the parent node
	if (this.parent != null){
		varNames.push.apply(varNames, this.parent.getAncestorVarNames());
	}
	else {
	// else if this is a root node, then add the free variables
		varNames.push.apply(varNames, freeVariables);
	}
	
	return varNames;
}

// Returns the variable names binded for the descendants
// Note: Does not include the freeVariables
Node.prototype.getDescendantVarNames = function(){
	var varNames = [];
	// If the variant is bind, then obtain the binded variable name
	if (this.variant == "bind"){
		varNames.push(document.getElementById(this.variable).value);
	}
	// If the node has childA, then recurse on childA
	if (this.childA != null){
		varNames.push.apply(varNames, this.childA.getDescendantVarNames());
	}
	
	// If the node has childB, then recurse on childB
	if (this.childB != null){
		varNames.push.apply(varNames, this.childB.getDescendantVarNames());
	}
	
	return varNames;
}

// Update the Select objects for variable names for this node and it's children
Node.prototype.updateVariableSelectors = function(varName){
	if (this.variant == "at" || this.variant == "variable"){
		// If the variant is either "At" or "Variable"
		// then update the select object but adding varName
		var selector = document.getElementById(this.variable);
		var opt = document.createElement("option");
		opt.text = varName;
		opt.value = varName;
		selector.add(opt, null);
	}
	
	// If the node has childA, then recurse on childA
	if (this.childA != null){
		this.childA.updateVariableSelectors(varName);
	}
	
	// If the node has childB, then recurse on childB
	if (this.childB != null){
		this.childB.updateVariableSelectors(varName);
	}
}
</script>


<script type="text/javascript">	
	var relIdCount = 0;			// Counter for relation identifiers, used for naming
	var variableCount = 0;		// Counter for variables, used for naming
	var rootNode = new Node(null, "requestor");	//The root node for the policy tree
	
	var freeVariables = ["req"];			// List of free varianbles

	// Add the free varaibles either own or res depending on the convention
	<c:choose>
		<c:when test="${useOwner}">
			freeVariables.push("own");
		</c:when>
		<c:otherwise>
			freeVariables.push("res");
		</c:otherwise>
	</c:choose>
	
	
	// Populate the given selector with variants
	function populateVariantSelector(selector){
	
		<c:forEach var="variant" items='${variants}'>
			var opt = document.createElement("option");
			opt.text = '${variant}';
			opt.value = '${fn:toLowerCase(variant)}';
			selector.add(opt,null);		
		</c:forEach>
	}
	
	// Creates a new subPolicy row in the given list.
	function newSubPolicy(newList, node){
	
		// Create the elements and set the attributes
		var listItem = document.createElement("li");
		var sel = document.createElement("select"); 
		sel.onchange=function() {updatePolicy(this, node);};

		populateVariantSelector(sel);
		
		jQuery(sel).attr("prevVal", "");	// Set previous value for the drop down menu
		// Add the element to the list
		jQuery(listItem).append(sel);
		jQuery(newList).append(listItem);

	}
	
	// Create a drop down menu for relation types, and adds it
	// to the given list
	function addRelationType(listItem){
		// Create the selector for relation identifier
		var sel = document.createElement("select");
		sel.id="relationIdentifier"+relIdCount;
		sel.name="relationIdentifier"+relIdCount;
		
		<c:forEach var="relationType" items='${relationTypes}'>
			var opt = document.createElement("option");
			opt.text = '${relationType.text}';
			opt.value = '${relationType.value}';
			sel.add(opt,null);		
		</c:forEach>

	
		// Add the element to the list
		jQuery(listItem).append(sel);
		
		// Create the selector for the direction
		var dir = document.createElement("select");
		dir.id="direction"+relIdCount;
		dir.name="direction"+relIdCount;
		
		// Populate the drop down menu
		var forward = document.createElement("option");
		forward.text = "Forward";
		forward.value = "forward";
		dir.add(forward, null);		

		var backward = document.createElement("option");
		backward.text = "Backward";
		backward.value = "backward";
		dir.add(backward, null);		

		var either = document.createElement("option");
		either.text = "Either";
		either.value = "either";
		dir.add(either, null);		


		// Add the element to the list
		jQuery(listItem).append(dir);
		
		relIdCount++;
	}
	
	// Clears the children of a policy when the variant is changed
	function clearPrevious(selector, node){
		var prevVal = jQuery(selector).attr("prevVal");
		if (prevVal=="" || prevVal=="requestor" || prevVal=="true" || prevVal=="false" || prevVal=="owner"){
			// do nothing. No sub policies, so nothing to clean up.
		}
		else {
			// remove the children
			var currLi = selector.parentNode;
			var nextLi = jQuery(currLi).next();
			jQuery(nextLi).remove();
			
			// remove the relation identifier and direction if the previous 
			// variant was box or diamond
			if (prevVal=="box" || prevVal=="diamond"){
				jQuery(currLi).children().eq(2).remove();
				jQuery(currLi).children().eq(1).remove();
				
			} 
			else if (prevVal=="bind" || prevVal=="at" || prevVal=="variable"){
				//Remove the variable fields from the variants bind, at, and variable		
				jQuery(currLi).children().eq(1).remove();
			}
		}
		
		//Reset the node
		node.variant = null;
		node.childA = null;
		node.childB = null;
		node.relationIdentifier = null;
		node.direction = null;
		node.variable = null;
	}
	
	// Convert the policy tree to a string
	function updateHidden(){
		document.getElementById("policyTree").value=rootNode.toString();
	}
	
	
	// Updates the policy form when a variant drop down menu bar changes
	function updatePolicy(selector, node){
		try {
			
			// get the list item, that the selector is part of
			var listItem = selector.parentNode;
			clearPrevious(selector, node);			// clear children
			
			node.variant = selector.value;
			
			if (selector.value=="box" || selector.value=="diamond"){
				// If the new variant is "box" or "diamond"
				
				// Create the new elements 
				var newListItem = document.createElement("li");
				var newList = document.createElement("ul");
				newList.type="none";
				
				//Set the node
				node.relationIdentifier = "relationIdentifier"+relIdCount;
				node.direction = "direction"+relIdCount;
				
				var nodeChild = new Node(node, "requestor");
				node.childA = nodeChild;
				
				// Add the items to newList
				addRelationType(listItem);
				newSubPolicy(newList, nodeChild);
				
				jQuery(newListItem).append(newList);	// Add newList to newListItem
				jQuery(listItem).after(newListItem);	// Attach newListItem after listItem
			}
			else if (selector.value=="and" || selector.value=="or"){
				// If the new variant is "and" or "or"
			
				// Create the new elements
				var newListItem = document.createElement("li");	
				var newList = document.createElement("ul");
				newList.type="none";

				//Set the node
				var nodeChildA = new Node(node, "requestor");
				node.childA = nodeChildA;
				
				var nodeChildB = new Node(node, "requestor");
				node.childB = nodeChildB;
				
				// Add the items to newList
				newSubPolicy(newList, nodeChildA);
				newSubPolicy(newList, nodeChildB);
				
				jQuery(newListItem).append(newList); // Add newList to newListItem
				jQuery(listItem).after(newListItem); // Attach newListItem after listItem
				
			}
			else if (selector.value=="not"){
				// If the new variant is "not"
				
				// Create the new elements			
				var newListItem = document.createElement("li");	
				var newList = document.createElement("ul");
				newList.type="none";

				//Set the node
				var nodeChildA = new Node(node, "requestor");
				node.childA = nodeChildA;		
				
				// Add the item to newList
				newSubPolicy(newList, nodeChildA);	
				
				jQuery(newListItem).append(newList); // Add newList to newListItem
				jQuery(listItem).after(newListItem); // Attach newListItem after listItem
			}
			else if (selector.value=="bind"){
				// If the new variant is "box" or "diamond"
				
				// Create the new elements 
				var newListItem = document.createElement("li");
				var newList = document.createElement("ul");
				newList.type="none";
				
				
				// Add variable name
				var vName = newVarName(node, true);
				
				var name = document.createElement("input");
				name.id = "variable"+variableCount;
				name.name = "variable"+variableCount;
				name.type = "text";
				name.readOnly = "readOnly";
				name.value = vName;
				
				jQuery(listItem).append(name);
				
				
				// Set the node
				node.variable = "variable"+variableCount;
				
				var nodeChildA = new Node(node, "requestor");
				node.childA = nodeChildA;
				
				variableCount++;		// Update variable count
				
				
				// Add Sub policy
				newSubPolicy(newList, nodeChildA);
				
				jQuery(newListItem).append(newList);	// Add newList to newListItem
				jQuery(listItem).after(newListItem);	// Attach newListItem after listItem
			}
			else if (selector.value=="at" || selector.value=="variable"){
				var newListItem = document.createElement("li");
				var newList = document.createElement("ul");
				newList.type="none";
				
				
				// Create the selector for variable names
				var sel = document.createElement("select");
				sel.id="variable"+variableCount;
				sel.name="variable"+variableCount;
				
				node.variable = "variable"+variableCount;
				variableCount++;		// Update variable count
				
				// populate the list of avaialble variable names
				var varNames = [];
				if (node.parent != null){
					varNames = node.parent.getAncestorVarNames();
				} else {
					varNames = freeVariables;
				}
				
				for (var i= 0; i < varNames.length; i++ ) {
					var opt = document.createElement("option");
					opt.text = varNames[i];
					opt.value = varNames[i];
					sel.add(opt,null);		
				}
				
				// Add the element to the list
				jQuery(listItem).append(sel);
				
				if (selector.value=="at"){
					// Add Sub policy
					
					var nodeChildA = new Node(node, "requestor");
					node.childA = nodeChildA;
					
					newSubPolicy(newList, nodeChildA);
				}

				jQuery(newListItem).append(newList);	// Add newList to newListItem
				jQuery(listItem).after(newListItem);	// Attach newListItem after listItem
			}
			
			// Update the value for the hidden input field "policyTree"
			updateHidden();
			// Update "prevVal" attribute for the selector
			jQuery(selector).attr("prevVal", selector.value);
			
		} catch(e){
			alert(e);
		}
	}
	
	// Checks if the given variable name is valid
	// If null is returned, then the name is valid
	// If a string is return, then thename is not valid
	function isVNameValid(node, vName, useAncestors){

		var varNames = [];
		if (useAncestors){
			//Obtain the variable names for the node's ancestors
			if (node.parent != null){
				varNames = node.parent.getAncestorVarNames();
			}
		} else {
			//Obtain the variable names for the node's descendants
			varNames = node.getDescendantVarNames();
			//Add the freeVariables to the list
			varNames.push.apply(varNames, freeVariables);
		}
		
		var errMsg = null;
		
		if (vName == null){
			//Check if vName is null
			errMsg = "Input is null";
			return errMsg;
		}
		else if (jQuery.trim(vName) == ""){
			//Check if vName is blank
			errMsg = "Input is blank";
			return errMsg;
		}
		else if (varNames.indexOf(vName) > -1){
			//Check if vName has already been used
			errMsg = "Input name already used";
			return errMsg;
		}
		
		//Check if vName contains any illegal strings
		var illegalStrings = ["{", "}", " ", "-", "variant", "relationIdentifier", "direction", "variable", "owner", "requestor"];
		for (var i = 0; i < illegalStrings.length; i++){
			if (vName.indexOf(illegalStrings[i]) > -1){
				errMsg = "Illegal String: \"" + illegalStrings[i] + "\"";
				return errMsg;
			}
		}
		
		// If all tests pass, return null
		return null;
	}
	
	// Function to prompt the user for a new variable name, and check if it is valid
	function newVarName(node, useAncestors){
		// Get variable name
		var vName = prompt("Enter Variable Name\n(NOTE: Name cannot be blank or duplicated)", "");;
		var errMsg = isVNameValid(node, vName, useAncestors);	// Check for validity
		
		// Loop until vName is valid
		while (errMsg != null){
			vName = prompt("Illegal Input: " + errMsg + "\nEnter Variable Name\n(NOTE: Name cannot be blank or duplicated)", "");
			errMsg = isVNameValid(node, vName, useAncestors);
		}
		
		return vName
	}
	
	function addFreeVar(){
		var vName = newVarName(rootNode, false);
		freeVariables.push(vName);
		rootNode.updateVariableSelectors(vName);
	}
</script>


<ul name="rebacPolicy" id="rebacPolicy" type=none>
	<c:if test="${allowFreeVars}">
		<li>
				<button type=button onClick="addFreeVar()">Add Free Variable</button>
		</li>	
	</c:if>
	<li>
		<select name="variant0" id="variant0" prevVal="" onChange="updatePolicy(this, rootNode)">
			<c:forEach var="variant" items='${variants}'>
				<option value="${fn:toLowerCase(variant)}">${variant}</option>
			</c:forEach>
		</select>
	</li>
</ul>

<input type="hidden" name="policyTree" id="policyTree" value=""/>
