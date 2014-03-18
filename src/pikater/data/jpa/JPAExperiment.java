package pikater.data.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class JPAExperiment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	private String name;
	private String note;
	// nejsem si jisty, jaky je ucel tohohle a jestli BINARY(1) v diagramu znamena boolean nebo byte
    @Column(nullable = false)
	private byte workflow;
	// TODO link to batch table
	private int batchId;
	// TODO link to model table
	private int modelId;
	private String status;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public byte getWorkflow() {
        return workflow;
    }
    public void setWorkflow(byte workflow) {
        this.workflow = workflow;
    }
    public int getBatchId() {
        return batchId;
    }
    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }
    public int getModelId() {
        return modelId;
    }
    public void setModelId(int modelId) {
        this.modelId = modelId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
