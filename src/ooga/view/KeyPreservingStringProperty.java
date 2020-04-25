package ooga.view;

import javafx.beans.property.ReadOnlyProperty;
import ooga.view.menu.Dictionary;

public class KeyPreservingStringProperty {

  private String myKey;
  private ReadOnlyProperty<String> myStringProperty;

  public KeyPreservingStringProperty(String key) {
    myStringProperty = Dictionary.getInstance().get(key);
    myKey = key;
  }

  public ReadOnlyProperty<String> valueProperty() {
    return myStringProperty;
  }

  public String getKey() {
    return myKey;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof KeyPreservingStringProperty && ((KeyPreservingStringProperty) o).getKey()
        .equals(getKey());
  }

}
