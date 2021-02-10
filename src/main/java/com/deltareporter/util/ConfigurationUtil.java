package com.deltareporter.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationUtil {

  public static Properties getConfigurationFile() throws IOException {
    Properties prop = new Properties();
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    InputStream stream = loader.getResourceAsStream("delta.properties");
    assert stream != null;
    prop.load(stream);
    return prop;
  }

}
