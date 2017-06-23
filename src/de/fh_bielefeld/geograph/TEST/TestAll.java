package de.fh_bielefeld.geograph.TEST;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
// more classes to add to Testsuite
@SuiteClasses({OSMApiTest.class, GuiTest.class, OSMParserTest.class, GuiTest.class})
public class TestAll {
}