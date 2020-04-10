package ooga.view;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.StringProperty;

public class Dictionary {
  private static final Dictionary INSTANCE = new Dictionary();
  private static final String DEFAULT_LANGUAGE = "English";

  private Map<String, ReadOnlyObjectWrapper<String>> myDictionary;
  private Collection<String> myResourcePaths;
  private String myLanguage;

  public static Dictionary getInstance(){
    return INSTANCE;
  }

  private Dictionary(){
    myLanguage = DEFAULT_LANGUAGE;
    myResourcePaths = new HashSet<>();
    myDictionary = new HashMap<>();
  }

  public void addReference(String path){
    myResourcePaths.add(path);
    ResourceBundle newBundle = ResourceBundle.getBundle(path+"."+myLanguage);
    updateWithResources(newBundle);
  }

  private void updateWithResources(ResourceBundle bundle){
    for(String key : bundle.keySet()){
      myDictionary.putIfAbsent(key,new ReadOnlyObjectWrapper<>());
      myDictionary.get(key).set(bundle.getString(key));
    }
  }

  public void setLanguage(String language){
    myLanguage = language;
    for(String path : myResourcePaths){
      ResourceBundle bundle = ResourceBundle.getBundle(path+ "." + myLanguage);
      updateWithResources(bundle);
    }
  }

  public ReadOnlyProperty<String> get(String key){
    return myDictionary.getOrDefault(key,null).getReadOnlyProperty();
  }
}
