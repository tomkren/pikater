package pikater.ontology.description;

import java.util.ArrayList;

/**
 * Created by Martin Pilat on 27.12.13.
 */
public class IntegerParameter extends Parameter {

    int value;
    Interval range;
    ArrayList<Integer> set;

    public IntegerParameter(String name, int value) {
        this.value = value;
        setName(name);
    }

    public IntegerParameter() {}

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Interval getRange() {
        return range;
    }

    public void setRange(Interval range) {
        this.range = range;
    }

    public ArrayList<Integer> getSet() {
        return set;
    }

    public void setSet(ArrayList<Integer> set) {
        this.set = set;
    }
}
