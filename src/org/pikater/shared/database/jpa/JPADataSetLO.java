package org.pikater.shared.database.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity	
@Table(name="DataSetLO")
@NamedQueries({
	@NamedQuery(name="DataSetLO.getAll",query="select dslo from JPADataSetLO dslo"),
	@NamedQuery(name="DataSetLO.getByID",query="select dslo from JPADataSetLO dslo where dslo.id=:id"),
	@NamedQuery(name="DataSetLO.getByOwner",query="select dslo from JPADataSetLO dslo where dslo.owner=:owner"),
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
	@OneToOne(cascade=CascadeType.PERSIST)
	private JPAGlobalMetaData globalMetaData;
	@OneToMany(cascade=CascadeType.PERSIST)
	private List<JPAAttributeMetaData> attributeMetaData;
	private String hash;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	private long size;
	
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
	@Override
	public String getEntityName() {
		return "DataSetLO";
	}
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
	}
	
}
