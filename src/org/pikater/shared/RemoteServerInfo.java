package  org.pikater.shared;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("server")
public class RemoteServerInfo
{
	public enum FieldType
	{
		HOSTNAME,
		FINGERPRINT,
		USERNAME,
		PASSWORD,
		DIRECTORY
	};
	
	private final String hostname;
	private final String fingerprint;
	private String username;
	private String password;
	private String directory;
	
	public RemoteServerInfo(String hostname, String fingerprint, String username, String password, String directory)
	{
		this.hostname = hostname;
		this.fingerprint = fingerprint;
		this.username = username;
		this.password = password;
		this.directory = directory;
	}
	
	public String getField(FieldType field)
	{
		switch (field)
		{
			case DIRECTORY:
				return directory;
			case HOSTNAME:
				return hostname;
			case FINGERPRINT:
				return fingerprint;
			case PASSWORD:
				return password;
			case USERNAME:
				return username;
			default:
				throw new IllegalArgumentException();
		}
	}
	
	public void setField(FieldType field, String value)
	{
		switch (field)
		{
			case HOSTNAME:
			case FINGERPRINT:
				throw new IllegalArgumentException(String.format("The value '%s' is immutable. Should you wish to change it, you have to "
						+ "edit the corresponding topology xml file and restart the application", field));
			case DIRECTORY:
				directory = value;
				break;
			case PASSWORD:
				password = value;
				break;
			case USERNAME:
				username = value;
				break;
			default:
				throw new IllegalArgumentException();
		}
	}
	
	public boolean isHostnameUsernameOrPasswordMissing()
	{
		return FieldVerifier.isStringNullOrEmpty(hostname) || FieldVerifier.isStringNullOrEmpty(username) || FieldVerifier.isStringNullOrEmpty(password);
	}
}