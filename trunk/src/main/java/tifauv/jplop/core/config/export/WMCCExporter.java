/**
 * 10 avr. 2010
 */
package tifauv.jplop.core.config.export;

import tifauv.jplop.core.config.Configuration;

/**
 * This is...
 *
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public final class WMCCExporter implements ConfigExporter {

	/**
	 * @see tifauv.jplop.core.config.export.ConfigExporter#export(tifauv.jplop.core.config.Configuration, java.lang.StringBuffer)
	 */
	@Override
	public void export(Configuration p_config, StringBuffer p_buffer) {
		p_buffer.append("board_site:                ").append(p_config.getName()).append("\n")
		.append(".backend_flavour:          2\n");
		if (false) {
			// Find a way to get the current user
			//p_buffer.append(".palmipede.userlogin:      ").append(login).append("\n");
		}
		p_buffer.append(".backend.url:              ").append(p_config.getURL()).append("/backend\n")
		.append(".post.url:                 ").append(p_config.getURL()).append("/post\n")
		.append(".tribune.delay:            60\n")
		.append(".palmipede.msg_max_length: ").append(p_config.getMaxPostLength()).append("\n");
	}
}
