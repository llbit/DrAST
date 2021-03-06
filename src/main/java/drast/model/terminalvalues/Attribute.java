package drast.model.terminalvalues;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class that holds the terminal Attributes
 * A Attribute holds their computed values.
 * Created by gda10jli on 10/20/15.
 */
public class Attribute extends TerminalValue {
  private boolean parametrized;
  private String aspect;
  private String declaredAt;
  private boolean isCircular;
  private boolean isNTA;
  private String kind;
  private HashMap<String, Object> computedValues;
  private final HashMap<String, Object[]> usedParameters;
  private String lastComputedKey;

  public Attribute(String name, Method m) {
    super(name, null, m, false);
    computedValues = new HashMap<>();
    usedParameters = new HashMap<>();
  }


  @Override protected void setChildInfo(ArrayList<AttributeInfo> al) {
    al.add(new AttributeInfo("Is circular", isCircular));
    al.add(new AttributeInfo("Is NTA", isNTA));
    al.add(new AttributeInfo("Aspect", aspect));
    al.add(new AttributeInfo("Declared at", declaredAt, true));
    al.add(new AttributeInfo("Kind", kind));
  }

  @Override public String getKind() {
    return kind;
  }

  public void setKind(String kind) {
    this.kind = kind;
  }

  @Override public String toString() {
    return getName(method, null);
  }

  @Override public boolean isParametrized() {
    return parametrized;
  }

  public void setParametrized(boolean parametrized) {
    this.parametrized = parametrized;
  }

  public void setCircular(boolean isCircular) {
    this.isCircular = isCircular;
  }

  /**
   * Check if the Attribute is a non-terminal attribute
   */
  @Override public boolean isNTA() {
    return isNTA;
  }

  public void setNTA(boolean NTA) {
    this.isNTA = NTA;
  }

  public void setAspect(String aspect) {
    this.aspect = aspect;
  }

  /**
   * Check if a attribute is parametrized
   */
  @Override public boolean isAttribute() {
    return true;
  }

  public void setDeclaredAt(String declaredAt) {
    this.declaredAt = declaredAt;
  }

  /**
   * Adds a computed value to its List of computed values.
   * If the attribute is non-parametrized it will write to the attributes main value
   */
  public void addComputedValue(Object[] params, Object value) {
    if (!isParametrized() && this.value == null) {
      this.value = value;
    }
    if (computedValues == null) {
      computedValues = new HashMap<>();
    }
    String key = getKey(params);
    lastComputedKey = key;
    computedValues.put(key, value);
    usedParameters.put(key, params);
  }

  public boolean containsValue(Object[] params) {
    return computedValues.containsKey(getKey(params));
  }

  public Object getComputedValue(Object[] params) {
    return computedValues.get(getKey(params));
  }

  public Set<Map.Entry<String, Object>> getComputedEntries() {
    return computedValues.entrySet();
  }

  public HashMap<String, Object[]> getUsedParameters() {
    return usedParameters;
  }

  public String getLastComputedKey() {
    return lastComputedKey;
  }

  private String getKey(Object[] params) {
    String key = "";
    for (Object obj : params) {
      key += (obj == null ? null : obj.hashCode()) + " : ";
    }
    return key;
  }

}
