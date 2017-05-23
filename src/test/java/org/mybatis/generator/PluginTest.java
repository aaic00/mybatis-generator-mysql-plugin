package org.mybatis.generator;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

public class PluginTest {

  @SuppressWarnings("unused")
  private final static Logger log = Logger.getLogger(PluginTest.class);

  @Test
  public void test() throws Exception {
    final String configFile = "/scripts/test.xml";
    final List<String> warnings = new ArrayList<String>();
    final ConfigurationParser cp = new ConfigurationParser(warnings);
    final Configuration config =
        cp.parseConfiguration(JavaCodeGenerationTest.class.getResourceAsStream(configFile));

    final DefaultShellCallback shellCallback = new TestShellCallback(true);
    System.out.println(shellCallback.getDirectory(null, null));

    final MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
    myBatisGenerator.generate(null, null, null, true);



  }
}
