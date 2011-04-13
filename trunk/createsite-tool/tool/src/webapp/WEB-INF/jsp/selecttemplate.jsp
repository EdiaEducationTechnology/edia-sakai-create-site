<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta http-equiv="Content-Style-Type" content="text/css" />
  <link href="css/createsite.css" type="text/css" rel="stylesheet" media="all" />
  <title><s:message code="page.selecttemplate.title"/></title>
  <script type="text/javascript" language="JavaScript" src="javascript/createsite.js"></script>
  <%=request.getAttribute("sakai.html.head")%>
</head>
<body onload="<%= request.getAttribute("sakai.html.body.onload") %>">
  <div class="portletBody">
  	
    <div class="navIntraTool">
    </div>
    
    <h3><s:message code="page.selecttemplate.header"/></h3>
    <p class="instruction"><s:message code="page.selecttemplate.instruction"/></p>
    
    <form method="post">
    
      <c:if test="${ empty templateSites }">
        <p class="shorttext">
          <label for="templateSiteId" ${status.error ? "style=\"color:red;\"" : ""}><s:message code="page.selecttemplate.templateSiteId.label"/></label>
          <select id="templateSiteId" name="templateSiteId" disabled="disabled">
            <option><s:message code="page.selecttemplate.notemplates"/></option>
          </select>
          &nbsp;&nbsp;
          <input type="submit" name="submit" value="<s:message code="page.selecttemplate.create"/>" disabled="disabled"/>
        </p>
      </c:if>
      
      <c:if test="${not empty templateSites}">
        <s:bind path="command.templateSiteId">
          <p class="shorttext">
            <label for="${status.expression}" ${status.error ? "style=\"color:red;\"" : ""}><s:message code="page.selecttemplate.${status.expression}.label"/></label>
            <select id="${status.expression}" name="${status.expression}"
                 onchange="edia.createsite.toggleTemplateDetailsVisibility()">
              <option value=""><s:message code="page.selecttemplate.selecttemplate"/></option>
              <c:forEach items="${templateSites}" var="templateSite">
                <option value="${templateSite.id}">${templateSite.title}</option>
              </c:forEach>
            </select>
            &nbsp;&nbsp;
            <input type="submit" id="submitBtn" name="submit" value="<s:message code="page.selecttemplate.create"/>" 
                disabled="disabled"/>
          </p>
        </s:bind>
        
        <h4><s:message code="page.selecttemplate.templatedetails"/></h4>
        
        <div id="siteDetailWrapper">
          <div id="noneSelected">
            <p class="shorttext"> 
              <label><s:message code="title.label"/></label>
              <span style="color:silver;">No template selected</span>
            </p>
            <p class="shorttext"> 
              <label><s:message code="shortDescription.label"/></label>
              <span style="color:silver;">No template selected</span>
            </p>
            <p class="shorttext"> 
              <label><s:message code="description.label"/></label>
              <span style="color:silver;">No template selected</span>
            </p>
          </div>
          
          <c:forEach items="${templateSites}" var="templateSite">
            <div id="detail_${templateSite.id}" style="display:none;">
              <p class="shorttext"> 
                <label><s:message code="title.label"/></label>
                ${templateSite.title}
              </p>
              <p class="shorttext"> 
                <label><s:message code="shortDescription.label"/></label>
                ${templateSite.shortDescription}
              </p>
              <p class="shorttext"> 
                <label><s:message code="description.label"/></label>
                ${templateSite.description}
              </p>
            </div>
          </c:forEach>
          
           
        </div>
        
      </c:if>
    
    </form>
  
  
  </div>	
</body>
</html>