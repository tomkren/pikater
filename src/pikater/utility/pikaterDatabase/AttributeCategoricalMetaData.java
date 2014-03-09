package pikater.utility.pikaterDatabase;

import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

public class AttributeCategoricalMetaData {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private Long numberOfCategories;
	
	@Transient
	private static final String PERSISTENCE_UNIT_NAME = "attributecategoricalmetadata";
	@Transient
	private static EntityManagerFactory factory;
	
	
	public void setNumberOfCategories(Long numberOfCategories){
		this.numberOfCategories=numberOfCategories;
	}
	
	public Long getNumberOfCategories(){
		return this.numberOfCategories;
	}
	
}
