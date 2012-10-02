<%@ include file="/WEB-INF/template/include.jsp"%>
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
	</div>
	</li>
	</c:forEach>
</ul>
</fieldset>
</c:if>