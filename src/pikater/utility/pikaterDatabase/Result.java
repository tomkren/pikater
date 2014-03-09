package pikater.utility.pikaterDatabase;

import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Calendar;

public class Result {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Long agentTypeId;
	private String agentName;
	private String options;
	private Float errorRate;
	private Float kappaStatistic;
	private Float meanAbsoluteError;
	private Float rootMeanSquaredError;
	private Float relativeAbsoluteError;
	private Float rootRelativeSquaredError;
	private Calendar start;
	private Calendar finish;
	private String serializedFileName;
	private String note;
	private Long experimentId;
	
	@Transient
	private static final String PERSISTENCE_UNIT_NAME = "results";
	@Transient
	private static EntityManagerFactory factory;
	
	
	public void setAgentTypeID(Long agentTypeID){
		this.agentTypeId=agentTypeID;
	}
	
	public Long getAgentTypeID(){
		return this.agentTypeId;
	}
	
	public void setAgentName(String agentName){
		this.agentName=agentName;
	}
	
	public String getAgentName(){
		return this.agentName;
	}
	
	public void setOptions(String options){
		this.options=options;
	}
	
	public String getOptions(){
		return this.options;
	}
	
	public void setErrorRate(float errorRate){
		this.errorRate=errorRate;
	}
	
	public float getErrorRate(){
		return this.errorRate;
	}
	
	public void setKappaStatistic(float kappaStatistic){
		this.kappaStatistic=kappaStatistic;
	}
	
	public float getKappaStatistic(){
		return this.kappaStatistic;
	}
	
	public void setMeanAbsoluteError(float meanAbsoluteError){
		this.meanAbsoluteError=meanAbsoluteError;
	}
	
	public float getMeanAbsoluteError(){
		return this.meanAbsoluteError;
	}
	
	public void setRootMeanSquaredError(float rootMeanSquaredError){
		this.rootMeanSquaredError=rootMeanSquaredError;
	}
	
	public float getRootMeanSquaredError(){
		return this.rootMeanSquaredError;
	}
	
	public void setRelativeAbsoluteError(float relativeAbsoluteError){
		this.relativeAbsoluteError=relativeAbsoluteError;
	}
	
	public float getRelativeAbsoluteError(){
		return this.relativeAbsoluteError;
	}
	
	public void setRootRelativeSquaredError(float rootRelativeSquaredError){
		this.rootRelativeSquaredError=rootMeanSquaredError;
	}
	
	public float getRootRelativeSquaredError(){
		return this.rootRelativeSquaredError;
	}
	
	public void setStart(Calendar start){
		this.start=start;
	}
	
	public Calendar getStart(){
		return this.start;
	}
	
	public void setFinish(Calendar finish){
		this.finish=finish;
	}
	
	public Calendar getFinish(){
		return this.finish;
	}
	
	public void setSerializedFilaName(String serializedFileName){
		this.serializedFileName=serializedFileName;
	}
	
	public String getSerializedFileName(){
		return this.serializedFileName;
	}
	
	public void setNote(String note){
		this.note=note;
	}
	
	public String getNote(){
		return this.note;
	}
	
	public void setExperimentID(Long experimentID){
		this.experimentId=experimentID;
	}
	
	public Long getExperimentID(){
		return this.experimentId;
	}
}
