<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>
<%@ taglib uri="http://edia.nl/jsp/tags/edia-sakai-tags" prefix="est"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta http-equiv="Content-Style-Type" content="text/css" />
  <link href="css/createsite.css" type="text/css" rel="stylesheet" media="all" />
  <title><s:message code="page.editsite.title"/></title>
  <script type="text/javascript" src="/library/editor/FCKeditor/fckeditor.js"></script>
  <%=request.getAttribute("sakai.html.head")%>
</head>
<body onload="<%= request.getAttribute("sakai.html.body.onload") %>">
  <div class="portletBody">
  	
    <div class="navIntraTool">
    </div>
    
    <h3><s:message code="page.editsite.header"/></h3>
    <p class="instruction"><s:message code="page.editsite.instruction"/></p>
    
    <form method="post">
    
      <input type="hidden" name="templateSiteId" value="${templateSite.id}"/>
      
      <s:bind path="command.title">
        <p class="shorttext">
          <label for="${status.expression}" ${status.error ? "style=\"color:red;\"" : ""}>
            <s:message code="${status.expression}.label"/>
          </label>
          <input type="text" id="${status.expression}" name="${status.expression}" value="${status.value}"/>
        </p>
      </s:bind>
    
      <s:bind path="command.shortDescription">
        <p class="shorttext">
          <label for="${status.expression}" ${status.error ? "style=\"color:red;\"" : ""}>
            <s:message code="${status.expression}.label"/>
          </label>
          <textarea id="${status.expression}" name="${status.expression}" wrap="virtual" rows="1" cols="30">${status.value}</textarea>
        </p>
      </s:bind>
    
      <s:bind path="command.description">
        <p class="shorttext">
          <label for="${status.expression}" ${status.error ? "style=\"color:red;\"" : ""}>
            <s:message code="${status.expression}.label"/>
          </label>
          <textarea id="${status.expression}" name="${status.expression}" wrap="virtual" rows="30" cols="80"
            ><est:escapeHtmlFormattedText>${status.value}</est:escapeHtmlFormattedText></textarea>
          <h:fckeditor texteareaid="${status.expression}"/>
        </p>
      </s:bind>

      <br/>
      
      <s:bind path="command.published">
        <p class="shorttext">
          <label ${status.error ? "style=\"color:red;\"" : ""}>
            <s:message code="${status.expression}.label"/>
          </label>
          <input id="${status.expression}_false" name="${status.expression}" type="radio" value="false"/>
          <s:message code="${status.expression}_false.label"/>
          <input id="${status.expression}_true" name="${status.expression}" type="radio" value="true" checked="checked"/>
          <s:message code="${status.expression}_true.label"/>
        </p>
      </s:bind>

      <s:bind path="command.joinable">
        <p class="shorttext">
          <c:if test="${empty templateSite.joinerRole}">
            <label style="color:gray;" title="<s:message code="${status.expression}.nojoinerrole.alt"/>">
              <s:message code="${status.expression}.label"/>
            </label>
            <input id="${status.expression}_true" name="${status.expression}" type="radio" value="true" disabled="disabled"/>
            <span style="color:gray;"><s:message code="${status.expression}_true.label"/></span>
            <input id="${status.expression}_false" name="${status.expression}" type="radio" value="false" checked="checked" disabled="disabled"/>
            <span style="color:gray;"><s:message code="${status.expression}_false.label"/></span>
          </c:if>

          <c:if test="${not empty templateSite.joinerRole}">
            <label>
              <s:message code="${status.expression}.label"/>
            </label>
            <input id="${status.expression}_true" name="${status.expression}" type="radio" value="true" checked="checked"/>
            <s:message code="${status.expression}_true.label"/>
            <input id="${status.expression}_false" name="${status.expression}" type="radio" value="false"/>
            <s:message code="${status.expression}_false.label"/>
          </c:if>
        </p>
      </s:bind>
      
      <br/>
      
      <input type="submit" name="submit" value="<s:message code="page.editsite.submit.label"/>"/> &nbsp;&nbsp;
      <input type="button" name="cancel" value="<s:message code="page.editsite.cancel.label"/>"/>

    </form>
  
  </div>	
</body>
</html>