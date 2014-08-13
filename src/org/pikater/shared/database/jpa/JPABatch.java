package org.pikater.shared.database.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.pikater.shared.database.jpa.status.JPABatchStatus;


@Entity
@Table(name="Batch")
@NamedQueries({
	@NamedQuery(name="Batch.getAll",query="select b from JPABatch b"),
	@NamedQuery(name="Batch.getAll.count",query="select count(b) from JPABatch b"),
	@NamedQuery(name="Batch.getAllNotStatus.count",query="select count(b) from JPABatch b where b.status <> :status"),
	@NamedQuery(name="Batch.getByID",query="select b from JPABatch b where b.id=:id"),
	@NamedQuery(name="Batch.getByStatus",query="select b from JPABatch b where b.status=:status"),
	@NamedQuery(name="Batch.getByOwner",query="select b from JPABatch b where b.owner=:owner"),
	@NamedQuery(name="Batch.getByOwner.count",query="select count(b) from JPABatch b where b.owner=:owner"),
	@NamedQuery(name="Batch.getByOwnerAndStatus.count",query="select count(b) from JPABatch b where b.owner=:owner and b.status=:status"),
	@NamedQuery(name="Batch.getByOwnerAndNotStatus.count",query="select count(b) from JPABatch b where b.owner=:owner and b.status <> :status")
})
public class JPABatch extends JPAAbstractEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	
	public int getId() {
        return id;
    }
	private String name;
	private String note;
	@Lob
	private String XML;
	@ManyToOne
	private JPAUser owner;
	private int priority; // TODO: rename this to avoid confusion (e.g. userAssignedPriority)
	private int totalPriority; // TODO: this is currently not used (propagate to core)
	private int computationEstimateInHours; // TODO: this is obsolete now - delete
	private boolean sendEmailAfterFinish;
	@Enumerated(EnumType.STRING)
	private JPABatchStatus status;
	
	@OneToMany(cascade=CascadeType.PERSIST)
	private List<JPAExperiment> experiments = new ArrayList<JPAExperiment>();
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@Temporal(TemporalType.TIMESTAMP)
	private Date started;
	@Temporal(TemporalType.TIMESTAMP)
	private Date finished;
	
	/**
	 * Constructor for JPA compatibility
	 */
	public JPABatch(){}
	
	/**
	 * Creates an experiment to be saved (not executed).
	 * @param name
	 * @param note
	 * @param xml
	 * @param owner
	 */
	public JPABatch(String name, String note, String xml, JPAUser owner)
	{
		this.name=name;
		this.note = note;
		this.XML = xml;
		this.owner=owner;
		this.priority = 0;
		this.totalPriority=0;
		this.created=new Date();
		this.status=JPABatchStatus.CREATED;
	}
	
	/**
	 * Creates an experiment to be saved for execution.
	 * @param name
	 * @param note
	 * @param xml
	 * @param owner
	 * @param userAssignedPriority
	 * @param computationEstimateInHours
	 * @param sendEmailAfterFinish
	 */
	public JPABatch(String name, String note, String xml, JPAUser owner, int userAssignedPriority, boolean sendEmailAfterFinish)
	{
		this.name=name;
		this.note = note;
		this.XML = xml;
		this.owner=owner;
		this.priority = userAssignedPriority;
		this.sendEmailAfterFinish=sendEmailAfterFinish;
		this.totalPriority=99; // TODO: this should be refreshed when setting user assigned priority => a separate method
		this.created=new Date();
		this.status=JPABatchStatus.WAITING;
	}

	public void setName(String name){
		this.name=name;
	}
	public String getName(){
		return this.name;
	}
	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

	public String getXML() {
		return XML;
	}
	public void setXML(String xML) {
		XML = xML;
	}

	public JPAUser getOwner() {
		return owner;
	}
	public void setOwner(JPAUser owner) {
		this.owner = owner;
	}

	public void setPriority(int priority){
		if((priority >= 0) && priority < 10)
		{
			this.priority=priority; // TODO: total priority needs to be refreshed when user assigned priority changes and the batch had been scheduled
		}
		else
		{
			throw new IllegalArgumentException("Only values from 0 to 9 are allowed. Received: " + priority);
		}
	}
	public int getPriority(){ 
		return this.priority;
	}
	
	public void setTotalPriority(int totalPriority){
		// TODO: concatenation of user priority (JPAUser) and user assigned priority
		this.totalPriority=totalPriority;
	}
	public int getTotalPriority(){
		return this.totalPriority;
	}
	
	public int getComputationEstimateInHours() {
		return computationEstimateInHours;
	}

	public void setComputationEstimateInHours(int computationEstimateInHours) {
		this.computationEstimateInHours = computationEstimateInHours;
	}

	public boolean isSendEmailAfterFinish() {
		return sendEmailAfterFinish;
	}

	public void setSendEmailAfterFinish(boolean sendEmailAfterFinish) {
		this.sendEmailAfterFinish = sendEmailAfterFinish;
	}

	public List<JPAExperiment> getExperiments() {
		return experiments;
	}
	public void setExperiments(List<JPAExperiment> experiments) {
		this.experiments = experiments;
	}
	public void addExperiment(JPAExperiment experiment) {
		this.experiments.add(experiment);
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getStarted() {
		return started;
	}

	public void setStarted(Date started) {
		this.started = started;
	}

	public Date getFinished() {
		return finished;
	}
	public void setFinished(Date finished) {
		this.finished = finished;
	}
	public JPABatchStatus getStatus() {
		return status;
	}
	public void setStatus(JPABatchStatus status) {
		this.status = status;
	}
	/**
	 * Sets the status of the batch to the desired value
	 * @param status String representation of JPABatchStatus object
	 */
	public void setStatus(String status) {
		this.setStatus(JPABatchStatus.valueOf(status));
	}
	
	public boolean isScheduled()
	{
		return getStatus() != JPABatchStatus.CREATED;
	}

	@Transient
	public static final String EntityName = "Batch";
	
	@Override
	public void updateValues(JPAAbstractEntity newValues) throws Exception {
		JPABatch updateValues=(JPABatch)newValues;
		this.created=updateValues.getCreated();
		this.experiments=updateValues.getExperiments();
		this.started=updateValues.getStarted();
		this.finished=updateValues.getFinished();
		this.name=updateValues.getName();
		this.note=updateValues.getNote();
		this.owner=updateValues.getOwner();
		this.priority=updateValues.getPriority();
		this.status=updateValues.getStatus();
		this.totalPriority=updateValues.getTotalPriority();
		this.XML=updateValues.getXML();
	}
}
