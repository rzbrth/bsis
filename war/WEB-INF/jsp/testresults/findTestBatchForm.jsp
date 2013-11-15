<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
  pageContext.setAttribute("newLineChar", "\n");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
    return System.nanoTime();
  }%>


<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tabContent-${unique_page_id}</c:set>
<c:set var="mainContentId">mainContent-${unique_page_id}</c:set>
<c:set var="childContentId">childContent-${unique_page_id}</c:set>

<c:set var="findAndAddTestBatchFormId">findAndAddTestBatchForm-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
	$("#${mainContentId}").find(".findTestBatchButton").button({
	    icons : {
	    	 primary : 'ui-icon-search'
	    }
	  }).click(function() {
	    var findCollectionFormData = $("#${findAndAddTestBatchFormId}").serialize();
	    var resultsDiv = $("#${mainContentId}").find(".findTestBatchResults");
	    //showLoadingImage(resultsDiv);
	    $.ajax({
	      type : "GET",
	      url : "findTestBatch.html",
	      data : findCollectionFormData,
	      success: function(data) {
	    	  		 animatedScrollTo(resultsDiv);
	                 resultsDiv.html(data);
	               },
	      error: function(data) {
	               showErrorMessage("Something went wrong. Please try again later.");        
	             }
	    });
	  });
  
    $("#${mainContentId}").find(".addTestBatchButton").button({
        icons : {
          primary : 'ui-icon-plusthick'
        }
      }).click(
          function() {
          	var addTestBatchDiv = $("#${findAndAddTestBatchFormId}").find(".addTestBatch");
          	var div = $(addTestBatchDiv[0]);
          	var firstDIN = div.find('input[name="firstDIN"]').val();
          	var lastDIN = div.find('input[name="lastDIN"]').val();
       	   
        	$.ajax({
       	      url: "addTestBatch.html",
       	      data: {firstDIN: firstDIN , lastDIN: lastDIN},
       	      type: "POST",
       	      success: function(response) {
       	                 $("#${tabContentId}").replaceWith(response);
       	                 showMessage("TestBatch Inserted Successfully!");
       	               },
       	      error:    function(response) {
       	                 showErrorMessage("Something went wrong. Please try again later");
       	                 console.log(response);
       	               },
       	    });
       	    return false;
          });

      $("#${mainContentId}").find(".clearFormButton")
                            .button()
                            .click(refetchForm);
      
      
      	  function getcreatedAfterDate() {
    	    return $("#${findCollectionFormId}").find(".createdAfterDate");  
    	  }

    	  function getcreatedBeforeDate() {
    	    return $("#${findCollectionFormId}").find(".createdBeforeDate");  
    	  }

    	  getcreatedAfterDate().datepicker({
    	    changeMonth : true,
    	    changeYear : true,
    	    minDate : -36500,
    	    maxDate : 365,
    	    dateFormat : "dd/mm/yy",
    	    yearRange : "c-100:c+1",
    	    onSelect : function(selectedDate) {
    	      getDateOfCollectionToInput().datepicker("option", "minDate", selectedDate);
    	    }
    	  });

    	  getcreatedBeforeDate().datepicker({
    	    changeMonth : true,
    	    changeYear : true,
    	    minDate : -36500,
    	    maxDate : 365,
    	    dateFormat : "dd/mm/yy",
    	    yearRange : "c-100:c+1",
    	    onSelect : function(selectedDate) {
    	      getDateOfCollectionFromInput().datepicker("option", "maxDate", selectedDate);
    	    }
    	  });

});
</script>

<div id="${tabContentId}" class="formDiv">
  <div id="${mainContentId}">
    <b>Open Test Batches</b>
    <form:form method="GET" commandName="findTestBatchForm" id="${findAndAddTestBatchFormId}"
      class="formFormatClass">
      <b>Find Test Batches</b>
      <br/>
      <br/>
  		<div>
        <form:label path="testBatchNumber">Test Batch Number</form:label>
        <form:input path="testBatchNumber" class="testBatchNumber" />
        </div>
  		<div>
        <form:label path="createdAfterDate">Created After Date</form:label>
        <form:input path="createdAfterDate" class="createdAfterDate" />
        </div>
        <div>
        <form:label path="createdBeforeDate">Created Before Date</form:label>
        <form:input path="createdBeforeDate" class="createdBeforeDate"/>
      </div>
      <div class="formFormatClass">
      <div>
        <label></label>
        <button type="button" class="findTestBatchButton">
          Find Test Batches
        </button>
        <button type="button" class="clearFormButton">
          Clear form
        </button>
      </div>
      </div>
  	  <div class="findTestBatchResults" ></div>
  	  <b>New Test Batches</b>
        <br/>
        <br/> 
      <div class="addTestBatch formFormatClass">
      <div>
        <form:label path="firstDIN" cssStyle="margin-right:3px !important;">First DIN</form:label>
        <form:input path="firstDIN" id="firstDIN" class="firstDIN"   />
      </div>
       <div>
        <form:label path="lastDIN" cssStyle="margin-right:3px !important;">Last DIN</form:label>
        <form:input path="lastDIN" id="lastDIN" class="lastDIN" />
        </div>
      </div>
    </form:form>
 	<div style="margin-left: 200px;">
      <label></label>
      <button type="button" class="addTestBatchButton autoWidthButton">
        Add Test Batch
      </button>
      <button type="button" class="clearFormButton autoWidthButton">
        Clear form
      </button>        
    </div> 
  <div id="${childContentId}"></div>
</div>  
</div>
