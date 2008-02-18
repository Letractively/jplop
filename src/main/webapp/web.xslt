<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output 
	method="html"
	encoding="UTF-8"
	doctype-public="-//W3C//DTD XHTML 1.1//EN"
	doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"
	indent="yes" />

<!-- The HTML structure -->
<xsl:template match="/">
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr">
		<head>
			<link rel="stylesheet" type="text/css" href="styles/backend.css" />
			<script type="text/javascript" src="scripts/utils.js"></script>
			<script type="text/javascript" src="scripts/backend.js"></script>
		</head>
		<body>
			<xsl:apply-templates select="board"/>
		</body>
	</html>
</xsl:template>

<!-- board -->
<xsl:template match="board">
	<ul id="backend">
		<xsl:for-each select="post">
			<xsl:sort select="@id" data-type="number" />
			<xsl:variable name="clock" select="concat(substring(@time, 9, 2), ':', substring(@time, 11, 2), ':', substring(@time, 13, 2))" />
			<xsl:variable name="login" select="login" />
			<xsl:variable name="aquot">'</xsl:variable>
			<xsl:variable name="elogin" select="translate(login, $aquot, '')" />
			<li class="post">
				<span class="clock" onclick="javascript: addStrToMessage('{$clock} ');">[<xsl:value-of select="$clock" />]</span>
				<xsl:choose>
					<xsl:when test="string-length($login) != 0">
						<span class="login" onclick="javascript: addStrToMessage('{$elogin}&lt; ');" title="{info}"><xsl:value-of select="$login" />&gt;</span>
					</xsl:when>
					<xsl:otherwise>
						<span class="anonymous login" title="{info}"><xsl:value-of select="concat(substring(info, 0, 12), '...')" /></span>
					</xsl:otherwise>
				</xsl:choose>
				<span class="message"><xsl:value-of select="message" /></span>
			</li>
		</xsl:for-each>
	</ul>
</xsl:template>
<!-- /board -->

</xsl:stylesheet>