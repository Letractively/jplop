package tifauv.jplop.core;

import tifauv.jplop.core.auth.UserBase;
import tifauv.jplop.core.board.History;
import tifauv.jplop.core.config.Configuration;
import tifauv.jplop.core.config.ConfigurationMock;

public class BackendMock implements Backend {

	// FIELDS \\
	private Configuration m_config = new ConfigurationMock();
	
	@Override
	public void clean() {
		// TODO Auto-generated method stub
	}

	@Override
	public Configuration getConfig() {
		return m_config;
	}

	@Override
	public History getHistory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserBase getUserBase() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(String p_contextDir) {
		// TODO Auto-generated method stub

	}

}
