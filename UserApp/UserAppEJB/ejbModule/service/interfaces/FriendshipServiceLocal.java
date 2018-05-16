package service.interfaces;

import java.util.List;

import javax.ejb.Local;

import model.Friendship;

@Local
public interface FriendshipServiceLocal {

	public boolean createFriendship(String str);
	public Friendship getFriendship(String id);
	public List<Friendship> readAll();
	public boolean deleteFriendship(String id);
	public boolean updateFriendship(Friendship fr);
	public boolean setFriends(String id);
	public boolean setPending(String id);
}
