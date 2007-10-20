<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output 
	method="html"
	encoding="UTF-8"
	doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
	doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1.dtd"
	indent="yes" />
  
<xsl:template match="board">
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr">
		<head>
			<meta http-equiv="content-type"  content="text/html; charset=iso-8859-1" />
			<title>JBoard - La Tribune des décideurs pressés</title>
			<author>Olivier Serve</author>
			<link rel="stylesheet" type="text/css" href="board.css" />
		</head>
		<body>
			<h1>JBoard - <xsl:apply-templates select="@site" /></h1>
			
			<ul id="board">
				<xsl:apply-templates select="post" />
			</ul>
			
			<div id="postForm">
				<form method="post" action="post">
					<input id="message" name="message" type="text" />
					<input type="submit" />
				</form>
			</div>
			
			<hr />
		
			<div id="footer">Cette page devrait être en XHTML 1 strict.</div>
		</body>
	</html>
</xsl:template>

<xsl:template match="post">
	<li class="post">
		<span class="clock">[<xsl:value-of select="@time" />]</span>
		<span class="login" title="{info}"><xsl:value-of select="login" />&gt;</span>
		<span class="message"><xsl:value-of select="message" /></span>
	</li>
</xsl:template>

</xsl:stylesheet>