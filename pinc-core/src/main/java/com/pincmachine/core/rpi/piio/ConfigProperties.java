package com.pincmachine.core.rpi.piio;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

import static com.rjsdevelopment.javagcode.impl.pi.piio.PIIO.MyLog;

public class ConfigProperties {

	private String filename = null;
	private InputStream is = null;
	private Properties props = new Properties();

	public ConfigProperties(String fileName) throws Exception {
		this.filename = fileName;

		try {
			is = ConfigProperties.class.getClassLoader().getResourceAsStream(this.filename);
			if (is != null)
				this.props.load(is);
			else
				throw new Exception("Could not load properties");
		} catch (Exception e) {
			throw e;
		}
	}

	public String getProperty(String name) {
		return props.getProperty(name);
	}

	public void storeProperty(String name, String value, String comment) {
		this.props.setProperty(name, value);
		URL file = ConfigProperties.class.getClassLoader().getResource(this.filename);
		try {
			File propsFile = new File(URLDecoder.decode(file.getPath(), "UTF-8"));
			FileWriter writer = new FileWriter(propsFile);
			this.props.store(writer, comment);
		} catch (Exception e) {
			MyLog.error("Could not save parameter: " + name);
		}
	}
}
