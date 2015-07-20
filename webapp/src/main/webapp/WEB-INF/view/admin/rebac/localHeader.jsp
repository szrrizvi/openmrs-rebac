<ul id="menu">
	<li class="first">
		<a href="${pageContext.request.contextPath}/admin"><spring:message code="admin.title.short"/></a>
	</li>
	
	<openmrs:hasPrivilege privilege="Manage Authorization Principals,Get Authorization Principals,Get Relationship Types,View Relationship Types,View Patient Programs">
		<li <c:if test='<%= request.getRequestURI().contains("authorizationPrincipalList") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/admin/rebac/authorizationPrincipalList.htm">
				<spring:message code="Rebac.authorizationPrincipal.manage"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
	
	<openmrs:hasPrivilege privilege="Manage Privilege Assignments">
		<li <c:if test='<%= request.getRequestURI().contains("privilegeAssignmentList") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/admin/rebac/privilegeAssignmentList.htm">
				<spring:message code="Rebac.privilegeAssignment.manage"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
	
	<openmrs:hasPrivilege privilege="Manage Relationship Types">
		<li <c:if test='<%= request.getRequestURI().contains("accessRelationshipTypeList") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/admin/rebac/accessRelationshipTypeList.htm">
				<spring:message code="Rebac.accessRelationshipType.manage"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
	
	<openmrs:hasPrivilege privilege="Manage Relationships">
		<li <c:if test='<%= request.getRequestURI().contains("accessRelationshipList") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/admin/rebac/accessRelationshipList.htm">
				<spring:message code="Rebac.accessRelationship.manage"/>
			</a>
		</li>
	</openmrs:hasPrivilege>

	<openmrs:hasPrivilege privilege="Manage Rebac Authorization Toggle">
		<li <c:if test='<%= request.getRequestURI().contains("actionList") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/admin/rebac/actionList.htm">
				<spring:message code="Rebac.action.manage"/>
			</a>
		</li>
	</openmrs:hasPrivilege>

	<openmrs:hasPrivilege privilege="Manage Hybrid Formulas">
		<li <c:if test='<%= request.getRequestURI().contains("hybridFormulaList") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/admin/rebac/hybridFormulaList.htm">
				<spring:message code="Rebac.hybridFormula.manage"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
	
	<openmrs:hasPrivilege privilege="Manage Actions,Get Relationship Types,View Relationship Types,Get Programs,View Programs">
		<li <c:if test='<%= request.getRequestURI().contains("rebacEnableDisable") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/admin/rebac/rebacEnableDisable.htm">
				<spring:message code="Rebac.enableDisable.manage"/>
			</a>
		</li>
	</openmrs:hasPrivilege>

	<openmrs:extensionPoint pointId="org.openmrs.admin.rebac.localHeader" type="html">
			<c:forEach items="${extension.links}" var="link">
				<li <c:if test="${fn:endsWith(pageContext.request.requestURI, link.key)}">class="active"</c:if> >
					<a href="${pageContext.request.contextPath}/${link.key}"><spring:message code="${link.value}"/></a>
				</li>
			</c:forEach>
	</openmrs:extensionPoint>
</ul>