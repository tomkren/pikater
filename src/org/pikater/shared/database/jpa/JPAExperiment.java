package org.pikater.shared.database.jpa;

import java.util.List;

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
	// nejsem si jisty, jaky je ucel tohohle a jestli BINARY(1) v diagramu znamena boolean nebo byte
    @Column(nullable = false)
	private byte workflow;

	private String status;

	private JPABatch batch;
	private JPAModel model;
	private List<JPAResult> results;

	public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public byte getWorkflow() {
        return workflow;
    }
    public void setWorkflow(byte workflow) {
        this.workflow = workflow;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(ExperimentStatus finished) {
        this.status = finished.toString();
    }

	public JPABatch getBatch() {
		return batch;
	}
	public void setBatch(JPABatch batch) {
		this.batch = batch;
	}

	public JPAModel getModel() {
		return model;
	}
	public void setModel(JPAModel model) {
		this.model = model;
	}

	public List<JPAResult> getResults() {
		return results;
	}
	public void setResults(List<JPAResult> results) {
		this.results = results;
	}
	public void addResult(JPAResult result) {
		this.results.add(result);
	}

}
