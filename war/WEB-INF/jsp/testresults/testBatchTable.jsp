<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>

<%!public long getCurrentTime() {
    return System.nanoTime();
  }%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tabContent-${unique_page_id}</c:set>
<c:set var="mainContentId">mainContent-${unique_page_id}</c:set>
<c:set var="childContentId">childContent-${unique_page_id}</c:set>
<c:set var="table_id">testBatchTable-${unique_page_id}</c:set>
<c:set var="noResultsFoundDivId">noResultsFoundDiv-${unique_page_id}</c:set>

<script>
$(document).ready(
    function() {
alert("HI");
      var testBatchTable = $("#${table_id}").dataTable({
        "bJQueryUI" : true,
        "sDom" : '<"H"lrT>t<"F"ip>',
        "bServerSide" : true,
        "sAjaxSource" : "${model.nextPageUrl}",
        "sPaginationType" : "full_numbers",
        "aoColumnDefs" : [{ "sClass" : "hide_class", "aTargets": [0]}
                         ],
        "fnServerData" : function (sSource, aoData, fnCallback, oSettings) {
                           oSettings.jqXHR = $.ajax({
                             "datatype": "json",
                             "type": "GET",
                             "url": sSource,
                             "data": aoData,
                             "success": function(jsonResponse) {
                                           if (jsonResponse.iTotalRecords == 0) {
                                             $("#${mainContentId}").html($("#${noResultsFoundDivId}").html());
                                           }
                                           fnCallback(jsonResponse);
                                         }
                             });
                           },
        "oTableTools" : {
          "sRowSelect" : "single",
          "aButtons" : [ "print" ],
          "fnRowSelected" : function(node) {
                              $("#${mainContentId}").parent().trigger("collectionSummaryView");
                              var elements = $(node).children();
                              if (elements[0].getAttribute("class") === "dataTables_empty") {
                                return;
                              }
                              var selectedRowId = elements[0].innerHTML;
                              createCollectionSummary("collectionSummary.html",
                                  {collectionId: selectedRowId});
                             },
        "fnRowDeselected" : function(node) {
                            },
        },
        "oColVis" : {
           "aiExclude": [0,1],
        }
      });
      
      function createCollectionSummary(url, data) {
        $.ajax({
          url: url,
          data: data,
          type: "GET",
          success: function(response) {
                     $("#${mainContentId}").trigger("collectionSummaryView", response);
                   }
        });
      }

      function refreshResults() {
        showLoadingImage($("#${mainContentId}"));
        $.ajax({url: "${model.refreshUrl}",
                type: "GET",
                success: function(response) {
                           $("#${mainContentId}").html(response);
                         }
        });
      }

      $("#${mainContentId}").find(".testBatchTable").bind("refreshResults", refreshResults);

      $("#${table_id}_filter").find("label").find("input").keyup(function() {
        var searchBox = $("#${table_id}_filter").find("label").find("input");
        $("#${table_id}").removeHighlight();
        if (searchBox.val() != "")
          $("#${table_id}").find("td").highlight(searchBox.val());
      });


    });
</script>

<div id="${tabContentId}">

  <div id="${mainContentId}">
*************** ${fn:length(model.allCollections)}
    <c:choose>

      <c:when test="${fn:length(model.allCollections) eq -1}">
        <span style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
          Sorry no results found matching your search request
        </span>
      </c:when>
  
      <c:otherwise>
  
        <br />
          <table id="${table_id}" class="dataTable testBatchTable">
          <thead>
            <tr>
              <th style="display: none"></th>
              <c:if test="${model.collectedSampleFields.collectionNumber.hidden != true}">
                <th>${model.collectedSampleFields.collectionNumber.displayName}</th>
              </c:if>
              <c:if test="${model.collectedSampleFields.ttiStatus.hidden != true}">
                <th>${model.collectedSampleFields.ttiStatus.displayName}</th>
              </c:if>
              <c:if test="${model.collectedSampleFields.bloodTypingStatus.hidden != true}">
                <th>${model.collectedSampleFields.bloodTypingStatus.displayName}</th>
              </c:if>
              <c:if test="${model.collectedSampleFields.bloodAbo.hidden != true}">
                <th>${model.collectedSampleFields.bloodAbo.displayName}</th>
              </c:if>
              <c:if test="${model.collectedSampleFields.bloodRh.hidden != true}">
                <th>${model.collectedSampleFields.bloodRh.displayName}</th>
              </c:if>
              <c:if test="${model.collectedSampleFields.donor.hidden != true}">
                <th>${model.collectedSampleFields.donor.donorStatus.displayName}</th>
              </c:if>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="collection" items="${model.allCollections}">
              <tr>
                <td style="display: none">${collection.id}</td>
                <c:if test="${model.collectedSampleFields.collectionNumber.hidden != true}">
                  <td>${collection.collectionNumber}</td>
                </c:if>
                <c:if test="${model.collectedSampleFields.ttiStatus.hidden != true}">
                  <td>${collection.ttiStatus}</td>
                </c:if>
                <c:if test="${model.collectedSampleFields.bloodTypingStatus.hidden != true}">
                  <td>${collection.bloodTypingStatus}</td>
                </c:if>
                <c:if test="${model.collectedSampleFields.bloodAbo.hidden != true}">
                  <td>${collection.bloodAbo}</td>
                </c:if>
                <c:if test="${model.collectedSampleFields.bloodRh.hidden != true}">
                  <td>${collection.bloodRh}</td>
                </c:if>
                 <c:if test="${model.collectedSampleFields.donor.hidden != true}">
                  <td>${collection.donor.donorStatus}</td>
                </c:if>
              </tr>
            </c:forEach>
          </tbody>
        </table>
  
      </c:otherwise>
    </c:choose>
  </div>

  <div id="${childContentId}">
  </div>

</div>

<div id="${noResultsFoundDivId}" style="display: none;">
  <span
    style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
    Sorry no results found matching your search request </span>
</div>
