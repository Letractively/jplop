/**
 * 10 avr. 2010
 */
package tifauv.jplop.core.config.export;

import tifauv.jplop.core.CommonConstants;
import tifauv.jplop.core.config.Configuration;

/**
 * This is...
 *
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public final class BoardConfigExporter implements ConfigExporter {

	/**
	 * @see tifauv.jplop.core.config.export.ConfigExporter#export(tifauv.jplop.core.config.Configuration, java.lang.StringBuffer)
	 */
	@Override
	public void export(Configuration p_config, StringBuffer p_buffer) {
		p_buffer.ensureCapacity(473
				+ p_config.getName().length()
				+ p_config.getFullName().length()
				+ p_config.getURL().length()
				+ CommonConstants.LOGIN_PARAM.length()
				+ CommonConstants.PASSWORD_PARAM.length()
				+ CommonConstants.MESSAGE_PARAM.length());
		p_buffer.append("<?xml version=\"1.0\"?>")
		.append("<site name=\"").append(p_config.getName())
		.append("\" title=\"").append(p_config.getFullName())
		.append("\" baseurl=\"").append(p_config.getURL())
		.append("\" version=\"1.1\">")
		.append("<account>")
		.append("<login method=\"post\" path=\"/logon\">")
		.append("<field name=\"").append(CommonConstants.LOGIN_PARAM).append("\">$l</field>")
		.append("<field name=\"").append(CommonConstants.PASSWORD_PARAM).append("\">$p</field>")
		.append("</login>")
		.append("<logout method=\"get\" path=\"/logout\"/>")
		.append("</account>")
		.append("<module name=\"board\" title=\"Tribune\" type=\"application/board+xml\">")
		.append("<backend path=\"/backend\" public=\"true\" tags_encoded=\"false\" refresh=\"30\"/>")
		.append("<post method=\"post\" path=\"/post\" anonymous=\"true\" max_length=\"")
		.append(p_config.getMaxPostLength()).append("\">")
		.append("<field name=\"").append(CommonConstants.MESSAGE_PARAM).append("\">$m</field>")
		.append("</post>")
		.append("</module>")
		.append("</site>");
	}
}
