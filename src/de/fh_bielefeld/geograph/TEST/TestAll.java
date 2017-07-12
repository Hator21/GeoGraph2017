package de.fh_bielefeld.geograph.TEST;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
// more classes to add to Testsuite
@SuiteClasses({OSMApiTest.class, GuiTestFx.class, OSMParserTest.class})
public class TestAll {
}