<%@ include file="/WEB-INF/template/include.jsp"%>
<%--
  ~ The contents of this file are subject to the OpenMRS Public License
  ~ Version 2.0 (the "License"); you may not use this file except in
  ~ compliance with the License. You may obtain a copy of the License at
  ~ http://license.openmrs.org
  ~
  ~ Software distributed under the License is distributed on an "AS IS"
  ~ basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
  ~ the License for the specific language governing rights and
  ~ limitations under the License.
  ~
  ~ Copyright (C) OpenHMIS.  All Rights Reserved.
  --%>
<c:if test="${!empty paymentMode.attributeTypes}">
<fieldset>
<ul>
	<c:forEach var="attrType" items="${paymentMode.attributeTypes}">
	<li class="bbf-field">
	<label for="${attrType.uuid}">${attrType.name}</label>
	<div class="bbf-editor">
		<c:choose>
			<c:when test='${attrType.format == "org.openmrs.Concept" }'>
				<openmrs_tag:conceptAnswerField
					formFieldName="${attrType.uuid}"
					concept="${conceptMap[attrType.foreignKey]}"
					optionHeader="&mdash;&mdash;" />
			</c:when>
			<c:otherwise>
				<openmrs:fieldGen 
					type="${attrType.format}" 
					formFieldName="${attrType.uuid}"
					val=""
					parameters="optionHeader=[blank]|showAnswers=${attrType.foreignKey}|isNullable=false" />
			</c:otherwise>
		</c:choose>
		<span id="${attrType.uuid}-meta" style="display: none;">{"required":${attrType.required}}</span>
	</div>
	</li>
	</c:forEach>
</ul>
</fieldset>
</c:if>
