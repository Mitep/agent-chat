package service.interfaces;

import java.util.Collection;

import javax.ejb.Local;

import model.Friendship;

@Local
public interface FriendshipServiceLocal {

	public boolean createFriendship(String str);
	public Friendship getFriendship(String id);
	public Collection<Friendship> readAll();
	public boolean deleteFriendship(String id);
}
