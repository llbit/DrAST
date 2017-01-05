package drast;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.IllegalFormatConversionException;
import java.util.MissingFormatArgumentException;

public class LogTest {
  @Rule public ExpectedException thrown = ExpectedException.none();

  @Test public void testInfoFormat1() {
    Log.info("%s is not a format specifier");
  }

  @Test public void testInfoFormat2() {
    thrown.expect(MissingFormatArgumentException.class);
    Log.info("Can't format %s: missing argument", new Object[0]);
  }

  @Test public void testInfoFormat3() {
    thrown.expect(IllegalFormatConversionException.class);
    Log.info("Can't format %f: wrong type", 10);
  }

  @Test public void testWarningFormat1() {
    Log.warning("%s is not a format specifier");
  }

  @Test public void testWarningFormat2() {
    thrown.expect(MissingFormatArgumentException.class);
    Log.warning("Can't format %s: missing argument", new Object[0]);
  }

  @Test public void testWarningFormat3() {
    thrown.expect(IllegalFormatConversionException.class);
    Log.warning("Can't format %f: wrong type", 10);
  }

  @Test public void testErrorFormat1() {
    Log.error("%s is not a format specifier");
  }

  @Test public void testErrorFormat2() {
    thrown.expect(MissingFormatArgumentException.class);
    Log.error("Can't format %s: missing argument", new Object[0]);
  }

  @Test public void testErrorFormat3() {
    thrown.expect(IllegalFormatConversionException.class);
    Log.error("Can't format %f: wrong type", 10);
  }
}
