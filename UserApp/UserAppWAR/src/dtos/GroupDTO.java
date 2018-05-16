package dtos;

import java.util.ArrayList;

/**
 * @author Nikola
 *
 */
public class GroupDTO {

	private String id;
	private String name;
	private ArrayList<String> members;

	public GroupDTO() {
		super();
	}

	public GroupDTO(String id, String name, ArrayList<String> members) {
		super();
		this.id = id;
		this.name = name;
		this.members = members;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getMembers() {
		return members;
	}

	public void setMembers(ArrayList<String> members) {
		this.members = members;
	}
}
