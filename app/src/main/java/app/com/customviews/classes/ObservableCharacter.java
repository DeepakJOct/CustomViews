package app.com.customviews.classes;


import java.io.Serializable;
import java.util.Observable;

public class ObservableCharacter extends Observable implements Serializable {

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        this.setChanged();
        this.notifyObservers(value);
    }
}
