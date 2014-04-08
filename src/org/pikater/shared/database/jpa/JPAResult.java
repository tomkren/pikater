package org.pikater.shared.database.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class JPAResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(nullable = false)
	private int agentTypeId;
    @Column(nullable = false)
	private String agentName;
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
    @Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date start;
	@Temporal(TemporalType.TIMESTAMP)
	private Date finish;
	private String serializedFileName;
	private String note;
    @ManyToOne
    @JoinColumn(name = "experimentId")
    private JPAExperiment experiment;

    public JPAExperiment getExperiment() {
        return experiment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}