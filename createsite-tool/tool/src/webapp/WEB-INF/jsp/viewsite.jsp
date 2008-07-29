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
  <title><s:message code="page.viewsite.title"/></title>
  <%=request.getAttribute("sakai.html.head")%>
</head>
<body onload="<%= request.getAttribute("sakai.html.body.onload") %>">
  <div class="portletBody">
  	
    <div class="navIntraTool">
    </div>
    
    <h3><s:message code="page.viewsite.header"/></h3>
    <p class="instruction"><s:message code="page.viewsite.instruction"/></p>
    
    <p class="shorttext">
    
    <p class="shorttext">
      <label><s:message code="siteId.label"/></label>
      ${site.id}
    </p>
  
    <p class="shorttext">
      <label><s:message code="title.label"/></label>
      ${site.title}
    </p>
  
    <p class="shorttext">
      <label><s:message code="shortDescription.label"/></label>
      ${site.shortDescription}
    </p>
  
    <p class="shorttext">
      <label><s:message code="description.label"/></label>
      ${site.description}
    </p>
  
  </div>	
</body>
</html>