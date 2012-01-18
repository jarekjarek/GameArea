package gryczko.jaroslaw.service;

import gryczko.jaroslaw.domain.Notices;
import gryczko.jaroslaw.domain.User;

import java.util.List;

import javax.mail.MessagingException;


public interface UserManager {

	public boolean addUser(User user, boolean newsletter);

	public User editUser();

	public boolean update(User user);

	public boolean deleteUser();

	public List<User> getbrowse();

	public boolean aadmin(String login);

	public boolean adelete(String login);

	public boolean auser(String login);

	public User showgetuser(String login);

}
