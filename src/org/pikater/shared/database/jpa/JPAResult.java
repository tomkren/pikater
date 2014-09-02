package org.pikater.shared.database.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(
		name="Result",
		indexes={
				@Index(columnList="serializedFileName"),
				@Index(columnList="errorRate"),
				@Index(columnList="experiment"),
				@Index(columnList="createdModel"),
				@Index(columnList="experiment,createdModel"),
				@Index(columnList="experiment,agentName,createdModel")
				}
		)
@NamedQueries({
	@NamedQuery(name="Result.getAll",query="select res from JPAResult res"),
	@NamedQuery(name="Result.getByAgentName",query="select res from JPAResult res where res.agentName=:agentName"),
	@NamedQuery(name="Result.getByExperiment",query="select res from JPAResult res where res.experiment=:experiment"),
	@NamedQuery(name="Result.getByExperimentWithModel",query="select res from JPAResult res where res.experiment=:experiment and res.createdModel is not null"),
	@NamedQuery(name="Result.getByExperimentWithModel.count",query="select count(res) from JPAResult res where res.experiment=:experiment and res.createdModel is not null"),
	@NamedQuery(name="Result.getByExperimentAndAgentNameWithModel",query="select res from JPAResult res where res.experiment=:experiment and res.agentName=:agentName and res.createdModel is not null"),
	@NamedQuery(name="Result.getByExperimentAndAgentNameWithModel.count",query="select count(res) from JPAResult res where res.experiment=:experiment and res.agentName=:agentName and res.createdModel is not null"),
	@NamedQuery(name="Result.getByDataSetHash",query="select res from JPAResult res where res.serializedFileName=:hash"),
	@NamedQuery(name="Result.getByDataSetHashErrorAscending",query="select res from JPAResult res where res.serializedFileName=:hash order by res.errorRate asc"),
	@NamedQuery(name="Result.getByExperimentErrorAscending",query="select res from JPAResult res where res.experiment=:experiment order by res.errorRate asc")
})
public class JPAResult extends JPAAbstractEntity{
	
    @Column(nullable = false)
	private int agentTypeId;
    @Column(nullable = false)
	private String agentName;
    @Lob
	private String options;
    @Column(nullable = false)
	private double errorRate;
    @Column(nullable = false)
	private double kappaStatistic;
    @Column(nullable = false)
	private double meanAbsoluteError;
    @Column(nullable = false)
	private double rootMeanSquaredError;
    @Column(nullable = false)
	private double relativeAbsoluteError;
    @Column(nullable = false)
	private double rootRelativeSquaredError;
    @Column(nullable=false)
    private int duration;
    @Column(nullable=false)
    private double durationLR;
    @Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date start;
	@Temporal(TemporalType.TIMESTAMP)
	private Date finish;
	private String serializedFileName;
	private String note;
	@OneToMany
	@JoinTable(name="result_dataset_inputs")
	private List<JPADataSetLO> inputs;
	@OneToMany
	@JoinTable(name="result_dataset_outputs")
	private List<JPADataSetLO> outputs;
    @ManyToOne
    private JPAExperiment experiment;
    @OneToOne
    private JPAModel createdModel;
    
    public JPAResult(){
    	this.inputs=new ArrayList<JPADataSetLO>();
    	this.outputs=new ArrayList<JPADataSetLO>();
    }
    
    public JPAExperiment getExperiment() {
        return experiment;
    }
    
    public int getAgentTypeId() {
        return agentTypeId;
    }

    public void setAgentTypeId(int agentTypeId) {
        this.agentTypeId = agentTypeId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public double getErrorRate() {
        return errorRate;
    }

    public void setErrorRate(double errorRate) {
        this.errorRate = errorRate;
    }

    public double getKappaStatistic() {
        return kappaStatistic;
    }

    public void setKappaStatistic(double kappaStatistic) {
        this.kappaStatistic = kappaStatistic;
    }

    public double getMeanAbsoluteError() {
        return meanAbsoluteError;
    }

    public void setMeanAbsoluteError(double meanAbsoluteError) {
        this.meanAbsoluteError = meanAbsoluteError;
    }

    public double getRootMeanSquaredError() {
        return rootMeanSquaredError;
    }

    public void setRootMeanSquaredError(double rootMeanSquaredError) {
        this.rootMeanSquaredError = rootMeanSquaredError;
    }

    public double getRelativeAbsoluteError() {
        return relativeAbsoluteError;
    }

    public void setRelativeAbsoluteError(double relativeAbsoluteError) {
        this.relativeAbsoluteError = relativeAbsoluteError;
    }

    public double getRootRelativeSquaredError() {
        return rootRelativeSquaredError;
    }

    public void setRootRelativeSquaredError(double rootRelativeSquaredError) {
        this.rootRelativeSquaredError = rootRelativeSquaredError;
    }

    public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public double getDurationLR() {
		return durationLR;
	}
	public void setDurationLR(double durationLR) {
		this.durationLR = durationLR;
	}
	public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getFinish() {
        return finish;
    }

    public void setFinish(Date finish) {
        this.finish = finish;
    }

    public String getSerializedFileName() {
        return serializedFileName;
    }

    public void setSerializedFileName(String serializedFileName) {
        this.serializedFileName = serializedFileName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setExperiment(JPAExperiment experiment) {
        this.experiment = experiment;
    }
    
    public boolean hasAnOutput()
    {
    	return (outputs != null) && !outputs.isEmpty();
    }

	public JPAModel getCreatedModel() {
		return createdModel;
	}
	public void setCreatedModel(JPAModel createdModel) {
		this.createdModel = createdModel;
	}
	public List<JPADataSetLO> getInputs() {
		return inputs;
	}

	public List<JPADataSetLO> getOutputs() {
		return outputs;
	}
	@Transient
	public static final String EntityName = "Result";

	@Override
	public void updateValues(JPAAbstractEntity newValues) {
		JPAResult updateValues=(JPAResult)newValues;
		this.agentName=updateValues.getAgentName();
    	this.agentTypeId=updateValues.getAgentTypeId();
    	this.errorRate=updateValues.getErrorRate();
    	this.experiment=updateValues.getExperiment();
    	this.finish=updateValues.getFinish();
    	this.kappaStatistic=updateValues.getKappaStatistic();
    	this.meanAbsoluteError=updateValues.getMeanAbsoluteError();
    	this.note=updateValues.getNote();
    	this.options=updateValues.getOptions();
    	this.relativeAbsoluteError=updateValues.getRelativeAbsoluteError();
    	this.rootMeanSquaredError=updateValues.getRootMeanSquaredError();
    	this.rootRelativeSquaredError=updateValues.getRootRelativeSquaredError();
    	this.duration=updateValues.getDuration();
    	this.durationLR=updateValues.getDurationLR();
    	this.serializedFileName=updateValues.getSerializedFileName();
    	this.start=updateValues.getStart();
    	this.createdModel=updateValues.getCreatedModel();
    	System.out.println("JPAResult to be updated: "+updateValues.getId());
	}
}