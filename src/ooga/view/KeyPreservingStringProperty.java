package ooga.view;

import java.security.Key;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.StringProperty;
import ooga.view.menu.Dictionary;

public class KeyPreservingStringProperty {

  private String myKey;
  private ReadOnlyProperty<String> myStringProperty;

  public KeyPreservingStringProperty(String key){
    System.out.println("key is: " + key);
    myStringProperty = Dictionary.getInstance().get(key);
    System.out.println("value is: " + myStringProperty);
    myKey = key;
  }

  public ReadOnlyProperty<String> valueProperty(){
    return myStringProperty;
  }

  public String getKey(){
    return myKey;
  }
  @Override
  public boolean equals(Object o){
    return o instanceof KeyPreservingStringProperty && ((KeyPreservingStringProperty) o).getKey().equals(getKey());
  }

}
