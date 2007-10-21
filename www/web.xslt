<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output 
	method="html"
	encoding="UTF-8"
	doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
	doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1.dtd"
	cdata-section-elements="qnames"
	indent="yes" />
                
<xsl:template match="board">
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr">
		<head>
			<title><xsl:text>JBoard - La Tribune des décideurs pressés</xsl:text></title>
			<author>Olivier Serve</author>
			<link rel="stylesheet" type="text/css" href="board.css" />
			<script type="text/javascript" src="board.js"></script>
		</head>
		<body>
			<div id="header">
				<h1>JBoard</h1>
				<div id="site">
					<a href="{@site}" title="Accueil de la tribune"><xsl:apply-templates select="@site" /></a>
				</div>
			</div>
			
			<ul id="board">
				<xsl:for-each select="post">
					<xsl:sort select="@id" data-type="number" />
					<xsl:variable name="clock" select="concat(substring(@time, 9, 2), ':', substring(@time, 11, 2), ':', substring(@time, 13, 2))" />
					<xsl:variable name="login" select="login" />
					<li class="post">
						<span class="clock" onclick="javascript: addStrToMessage('{$clock} ');">[<xsl:value-of select="$clock" />]</span>
						<span class="login" onclick="javascript: addStrToMessage('{$login}&lt; ');" title="{info}"><xsl:value-of select="$login" />&gt;</span>
						<span class="message"><xsl:value-of select="message" /></span>
					</li>
				</xsl:for-each>
			</ul>
			
			<div id="postForm">
				<form method="post" action="post">
					<input type="button" onclick="javascript: clearMessage();" />
					<input id="message" name="message" type="text" maxlength="512" accesskey="m" />
				</form>
			</div>
			
			<hr />
		
			<div id="footer">Cette tribune est dédiée à la mémoire de Pierre Tramo.</div>
		</body>
	</html>
</xsl:template>

</xsl:stylesheet>