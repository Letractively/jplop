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
			<title><xsl:text>JBoard - La Tribune des décideurs pressés</xsl:text></title>
			<author>Olivier Serve</author>
			<link rel="stylesheet" type="text/css" href="styles/board.css" />
			<script type="text/javascript" src="scripts/board.js"></script>
		</head>
		<body>
			<xsl:apply-templates select="board"/>
		</body>
	</html>
</xsl:template>

<!-- The Board display -->
<xsl:template match="board">
	<div id="header">
		<h1>JBoard</h1>
		<div id="site">
			<a href="{@site}" title="Accueil de la tribune">Accueil</a>
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
		<form method="post" action="post" onsubmit="javascript: return !sendMessage('{@site}/post');">
			<input type="button" onclick="javascript: addTagToMessage('b');"  value="Gras"     accessKey="g" />
			<input type="button" onclick="javascript: addTagToMessage('i');"  value="Italique" accessKey="i" />
			<input type="button" onclick="javascript: addTagToMessage('u');"  value="Souligné" accessKey="s" />
			<input type="button" onclick="javascript: addTagToMessage('s');"  value="Barré"    accessKey="b" />
			<input type="button" onclick="javascript: addTagToMessage('tt');" value="TeleType" accessKey="t" />
			<br/>
			<input type="reset" value="" />
			<input type="text"  id="message" name="message" maxlength="512" accesskey="m" />
			<input type="submit" />
		</form>
	</div>

	<hr />

	<div id="footer">Cette tribune est dédiée à la mémoire de Pierre Tramo.</div>
</xsl:template>

</xsl:stylesheet>