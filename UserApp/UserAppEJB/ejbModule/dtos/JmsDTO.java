package dtos;

/**
 * @author Nikola
 *
 */
public class JmsDTO {

	private String type;
	private String username;
	private String status;
	private String info;
	private String host;

	public JmsDTO() {
		super();
	}

	public JmsDTO(String type, String username, String status, String info) {
		super();
		this.type = type;
		this.username = username;
		this.status = status;
		this.info = info;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
	
}
