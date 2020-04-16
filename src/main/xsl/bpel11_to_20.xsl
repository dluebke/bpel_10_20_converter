<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:bpel11="http://schemas.xmlsoap.org/ws/2003/03/business-process/"
	xmlns:bpel="http://docs.oasis-open.org/wsbpel/2.0/process/executable"
	version="1.0" 
	>
	
	<xsl:variable name="bpel11ns" select="'http://schemas.xmlsoap.org/ws/2003/03/business-process/'" />
	<xsl:variable name="bpel20ns" select="'http://docs.oasis-open.org/wsbpel/2.0/process/executable'" />
	
	<xsl:template match="/bpel11:process">
		<bpel:process xmlns:bpel="http://docs.oasis-open.org/wsbpel/2.0/process/executable">
			<xsl:copy-of select="namespace::*" />
			<xsl:apply-templates select="* | @*" />
		</bpel:process>
	</xsl:template>
	
	<!-- Convert bpel11:terminate to bpel:exit -->
	<xsl:template match="bpel11:terminate">
		<bpel:exit>
			<xsl:copy-of select="namespace::*" />
			<xsl:apply-templates select="child::node()" />
		</bpel:exit>
	</xsl:template>
	
	<!-- Convert bpel11:switch to bpel:if -->
	<xsl:template match="bpel11:switch">
		<bpel:if>
			<xsl:copy-of select="namespace::*" />
			<bpel:condition><xsl:value-of select="bpel11:case[1]/@condition" /></bpel:condition>
			<xsl:apply-templates select="bpel11:case[1]/*" />
			<xsl:for-each select="bpel11:case[position() &gt; 1]">
				<bpel:elseIf>
					<xsl:copy-of select="namespace::*" />
					<bpel:condition><xsl:value-of select="@condition" /></bpel:condition>
					<xsl:apply-templates select="*" />
				</bpel:elseIf>
			</xsl:for-each>
			<xsl:if test="bpel11:otherwise">
				<bpel:else>
					<xsl:copy-of select="bpel11:otherwise/namespace::*" />
					<xsl:apply-templates select="bpel11:otherwise/@*" />
					<xsl:apply-templates select="bpel11:otherwise/*" />
				</bpel:else>
			</xsl:if>
		</bpel:if>
	</xsl:template>

	<!-- Convert all remaining bpel11 elements to bpel20 elements -->
	<xsl:template match="bpel11:*">
		<xsl:element namespace="{$bpel20ns}" name="{local-name(.)}">
			<xsl:copy-of select="namespace::*" />
			<xsl:apply-templates select="* | @*" />
		</xsl:element>
	</xsl:template>
	
	<!-- Copy everything else -->
	<xsl:template match="node()">
		<xsl:copy />
	</xsl:template>
	<xsl:template match="@*">
		<xsl:copy />
	</xsl:template>
</xsl:stylesheet>