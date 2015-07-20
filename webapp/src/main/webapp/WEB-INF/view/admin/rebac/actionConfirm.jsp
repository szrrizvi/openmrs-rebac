<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>

<openmrs:require privilege="" otherwise="/login.htm" redirect="index.htm" />

<a href="${pageContext.request.contextPath}/patientDashboard.form?patientId=${patientId}"><openmrs:message code="patientDashboard.viewDashboard"/></a>

<%@ include file="/WEB-INF/template/footer.jsp" %>
