package org.pikater.shared.database.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.pikater.shared.database.exceptions.AttributeActionNotApplicableException;
import org.pikater.shared.database.exceptions.AttributeNameNotFoundException;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.jpa.daos.DataSetDAO;


@Entity	
@Table(name="DataSetLO")
@NamedQueries({
	@NamedQuery(name="DataSetLO.getAll",query="select dslo from JPADataSetLO dslo"),
	@NamedQuery(name="DataSetLO.getAll.count",query="select count(dslo) from JPADataSetLO dslo"),
	@NamedQuery(name="DataSetLO.getAllVisible.count",query="select count(dslo) from JPADataSetLO dslo where dslo.visible=true"),
	@NamedQuery(name="DataSetLO.getAllWithResults",query="select dslo from JPADataSetLO dslo where exists (select r from JPAResult r where r.serializedFileName = dslo.hash) "),
	@NamedQuery(name="DataSetLO.getByID",query="select dslo from JPADataSetLO dslo where dslo.id=:id"),
	@NamedQuery(name="DataSetLO.getByOwner",query="select dslo from JPADataSetLO dslo where dslo.owner=:owner"),
	@NamedQuery(name="DataSetLO.getByOwner.count",query="select count(dslo) from JPADataSetLO dslo where dslo.owner=:owner"),
	@NamedQuery(name="DataSetLO.getByOwnerVisible.count",query="select count(dslo) from JPADataSetLO dslo where dslo.owner=:owner and dslo.visible=true"),
	@NamedQuery(name="DataSetLO.getByOID",query="select dslo from JPADataSetLO dslo where dslo.OID=:oid"),
	@NamedQuery(name="DataSetLO.getByHash",query="select dslo from JPADataSetLO dslo where dslo.hash=:hash"),
	@NamedQuery(name="DataSetLO.getByDescription",query="select dslo from JPADataSetLO dslo where dslo.description=:description")
})
public class JPADataSetLO extends JPAAbstractEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	
	public int getId() {
        return id;
    }
	//ID of the LargeObject stored in the Postgre DB, that contains
	//Use method in pikater.utility.pikaterDatabase.Database to retrieve the data based on OID
	private long OID;
	private JPAUser owner;
	private String description;
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(nullable=true)
	private JPAGlobalMetaData globalMetaData=null;
	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(nullable=true)
	private List<JPAAttributeMetaData> attributeMetaData=null;
	private String hash;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	private long size;
	private boolean approved;
	private boolean visible;
	
	/**Constructor for JPA compatibility**/
	public JPADataSetLO(){
		this.visible=true;
	}
	
	/**
	 * Cretes a new JPA Entity insance of dataset.
	 * To store the entity please use {@link DataSetDAO}.storeNewDataSet(File dataset,JPADataSetLO initialData) function
	 * which will automatically compute the Hash for the given DataSet and if necessary stores the File to Database.
	 * @param owner JPAUser owner of the DataSet
	 */
	public JPADataSetLO(JPAUser owner){
		this();
		this.setOwner(owner);
		this.setCreated(new Date());
	}
	/**
	 * Cretes a new JPA Entity insance of dataset.
	 * To store the entity please use {@link DataSetDAO}.storeNewDataSet(File dataset,JPADataSetLO initialData) function
	 * which will automatically compute the Hash for the given DataSet and if necessary stores the File to Database
	 * @param owner JPAUser owner of the DataSet
	 * @param description Some Description of the Dataset
	 */
	public JPADataSetLO(JPAUser owner,String description){
		this(owner);
		this.setDescription(description);
	}
	
	
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public void setOID(long OID){
		this.OID=OID;
	}	
	public long getOID(){
		return this.OID;
	}
	public JPAGlobalMetaData getGlobalMetaData() {
		return globalMetaData;
	}
	public void setGlobalMetaData(JPAGlobalMetaData globalMetaData) {
		this.globalMetaData = globalMetaData;
	}
	public List<JPAAttributeMetaData> getAttributeMetaData() {
		return attributeMetaData;
	}
	public void setAttributeMetaData(List<JPAAttributeMetaData> attributeMetaData) {
		this.attributeMetaData = attributeMetaData;
	}
	public JPAUser getOwner() {
		return owner;
	}
	public void setOwner(JPAUser owner) {
		this.owner = owner;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	@Transient
	public static final String EntityName = "DataSetLO";
	@Override
	public void updateValues(JPAAbstractEntity newValues) {
		JPADataSetLO updateValues=(JPADataSetLO)newValues;
		this.OID=updateValues.getOID();
		this.attributeMetaData=updateValues.getAttributeMetaData();
		this.created=updateValues.getCreated();
		this.description=updateValues.getDescription();
		this.globalMetaData=updateValues.getGlobalMetaData();
		this.hash=updateValues.getHash();
		this.owner=updateValues.getOwner();	
		this.approved=updateValues.isApproved();
	}
	
	
	public double getAttributeMinValue(String attributeName) throws AttributeNameNotFoundException, AttributeActionNotApplicableException{ 
		for(JPAAttributeMetaData md:getAttributeMetaData()){ 
			if(md.getName().equals(attributeName)){ 
				if(md instanceof JPAAttributeNumericalMetaData){ 
					return ((JPAAttributeNumericalMetaData)md).getMin(); 
				}else{ 
					throw new AttributeActionNotApplicableException(); 
				} 
			} 
		} 
		throw new AttributeNameNotFoundException(); 
	} 

	public double getAttributeMaxValue(String attributeName) throws AttributeNameNotFoundException, AttributeActionNotApplicableException{ 
		for(JPAAttributeMetaData md:getAttributeMetaData()){ 
			if(md.getName().equals(attributeName)){ 
				if(md instanceof JPAAttributeNumericalMetaData){ 
					return ((JPAAttributeNumericalMetaData)md).getMax(); 
				}else{ 
					throw new AttributeActionNotApplicableException(); 
				} 
			} 
		} 
		throw new AttributeNameNotFoundException(); 
	} 

	public int getNumberOfAttributes(){ 
		return this.getAttributeMetaData().size(); 
	}
	
	public List<JPAAttributeMetaData> getTargetAttributes()
	{
		List<JPAAttributeMetaData> result = new ArrayList<JPAAttributeMetaData>();
		for(JPAAttributeMetaData attr : getAttributeMetaData())
		{
			if(attr.isTarget())
			{
				result.add(attr);
			}
		}
		return result;
	}
	
	public String getFileName()
	{
		return DAOs.filemappingDAO.getSingleExternalFilename(this);
	}
}
