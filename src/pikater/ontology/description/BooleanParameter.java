package pikater.ontology.description;

/**
 * Created by marti_000 on 27.12.13.
 */
public class BooleanParameter extends Parameter {

    boolean active;

    public BooleanParameter(String name, boolean active) {
        this.active = active;
        setName(name);
    }

    public BooleanParameter() {}

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean value) {
        this.active = value;
    }
}
