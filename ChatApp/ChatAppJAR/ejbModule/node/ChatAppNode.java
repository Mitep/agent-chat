package node;

import javax.ejb.Remote;

@Remote
public interface ChatAppNode {

	public boolean amIMaster();
	
}
