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
public interface ConfigExporter {

	void export(Configuration p_config, StringBuffer p_buffer);
}
