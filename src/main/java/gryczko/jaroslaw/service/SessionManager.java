package gryczko.jaroslaw.service;   

import gryczko.jaroslaw.domain.User;

public interface SessionManager {

	public boolean loginUser(User user);

	public boolean removeCookie();

	public boolean checkCookie();

	public boolean checkAdmin();

	public int getCurrentUserId();
}
