package gryczko.jaroslaw.beans;


import gryczko.jaroslaw.domain.User;
import gryczko.jaroslaw.service.SessionManagerImpl;
import gryczko.jaroslaw.service.UserManagerImpl;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;



public class UserBean {

	private User user = new User();   
	private ListDataModel<User> usersmodel = new ListDataModel<User>();  

	@NotEmpty(message = "Password confirm can't be empty")
	@Length(min = 6, max = 45, message = "Password confirm have to beetween 6-45 chars")
	private String passconfirm;   
	private boolean newsletter = false;

	public boolean getNewsletter() {
		return newsletter;
	}

	public void setNewsletter(boolean newsletter) {
		this.newsletter = newsletter;
	}

	public String getPassconfirm() {
		return passconfirm;
	}// ss

	public void setPassconfirm(String passconfirm) {
		this.passconfirm = passconfirm;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Inject
	UserManagerImpl um = new UserManagerImpl();   

	@Inject
	SessionManagerImpl sm = new SessionManagerImpl();   

	public String register() {   

		boolean registercheck = um.addUser(user, newsletter);   
		if (registercheck) {   
			return "/reports/registerdone";    
		} else {
			return "/reports/registerfail";  
		}

	}

	public String login() {   
		user.setDob(null);
		user.setEmail(null);
		user.setFname(null);       
		user.setLname(null);
		user.setTelephone(0);

		boolean log_in = sm.loginUser(user);
		if (log_in) {
			return "index.jsp";
		} else {
			return "login";
		}

	}

	public boolean isCheck() {   
		boolean check = sm.checkCookie();

		if (check) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isCheck1() {     
		boolean check = sm.checkCookie();

		if (!check) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isCheck2() {    
		boolean check = sm.checkAdmin();

		if (check) {
			return true;
		} else {
			return false;
		}
	}

	public boolean islogout() {           
		sm.removeCookie();
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext ec = context.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) ec.getRequest();
		request.getSession(false).invalidate();
		return true;
	}

	public boolean issettings() {  
		user = um.editUser();
		return true;

	}

	public String update() {      
		boolean check = um.update(user);
		if (check) {
			return "settings";
		} else {
			return "home";
		}

	}

	public String delete() {                
		boolean check = um.deleteUser();
		if (check) {
			return "home";
		} else {
			return "login";
		}

	}

	public DataModel<User> getAllUsers() {     
		usersmodel.setWrappedData(um.getbrowse());
		return usersmodel;

	}

	public String adelete() {       
		User u = usersmodel.getRowData();
		boolean check = um.adelete(u.getLogin());
		if (check) {
			return "users";
		}

		return "login";

	}

	public String aadmin() {     
		User u = usersmodel.getRowData();
		boolean check = um.aadmin(u.getLogin());
		if (check) {
			return "users";
		}

		return "login";

	}

	public String auser() {
		User u = usersmodel.getRowData();
		boolean check = um.auser(u.getLogin());
		if (check) {
			return "users";
		}

		return "login";

	}

	public boolean validateDOB(ComponentSystemEvent e) {  

		UIForm form = (UIForm) e.getComponent();

		UIInput dobInput = (UIInput) form.findComponent("dob");
		if (dobInput == null) {
			return true;
		}
		String dob = (String) dobInput.getValue();
		if (dob == null) {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(
					"Date of Birth have incorrect format");
			fc.addMessage(form.getClientId(), message);
			fc.renderResponse();
			return true;
		}
		if (dob.matches("[a-zA-Z]+")) {   
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(
					"Date of Birth have incorrect format");
			fc.addMessage(form.getClientId(), message);
			fc.renderResponse();
			return true;

		}

		if (!dob.matches("[a-zA-Z]+")) {      
			if (!(dob == null && !dob.matches("[a-zA-Z]+"))) {

				int rok = Integer.parseInt(dob.substring(0, 4));   
				int miesiac = Integer.parseInt(dob.substring(5, 7)); 
				int dzien = Integer.parseInt(dob.substring(8, 10));  
				String tab1 = dob.substring(4, 5); 
				String tab2 = dob.substring(7, 8);

			
				if (!(rok > 1910 && rok < 2005 && miesiac < 13 && miesiac > 0   
						&& dzien < 32 && dzien > 0 && tab1.equals("-") && tab2
						.equals("-"))) {

					FacesContext fc = FacesContext.getCurrentInstance();
					FacesMessage message = new FacesMessage(
							"Date of Birth have incorrect format");
					fc.addMessage(form.getClientId(), message);
					fc.renderResponse();
					return true;
				}
			}
		}
		return false;

	}

	public void validateTELEPHONE(ComponentSystemEvent e) {   

		UIForm form = (UIForm) e.getComponent();

		UIInput telInput = (UIInput) form.findComponent("telephone");
		String tel = Integer.toString((Integer) telInput.getValue());
		if (!(tel.equalsIgnoreCase("0"))) {

			if (tel.length() != 9) {    
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage message = new FacesMessage(
						"Telephone have incorrect format");
				fc.addMessage(form.getClientId(), message);
				fc.renderResponse();

			}
		}
	}

	public void validatePassConfirm(ComponentSystemEvent e) {   

		UIForm form = (UIForm) e.getComponent();
		UIInput passInput = (UIInput) form.findComponent("password");
		UIInput pass1Input = (UIInput) form.findComponent("passwordconfirm");
		String pass = (String) pass1Input.getValue();
		String pass1 = (String) passInput.getValue();

		if (pass != null && pass1 != null) {
			if (!pass.equals(pass1)) {

				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage message = new FacesMessage(
						"Passwords are not equals.");
				fc.addMessage(form.getClientId(), message);
				fc.renderResponse();

			}
		}
	}

}
