package gryczko.jaroslaw.beans;

import gryczko.jaroslaw.domain.Notices;
import gryczko.jaroslaw.domain.User;
import gryczko.jaroslaw.service.NoticesManagerImpl;
import gryczko.jaroslaw.service.UserManagerImpl;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;


public class ShowUserBean {

	private User selectedUser;

	private ListDataModel<Notices> noticesmodel = new ListDataModel<Notices>();

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

	@Inject
	UserManagerImpl um = new UserManagerImpl();
	@Inject
	NoticesManagerImpl nm = new NoticesManagerImpl();

	public DataModel<Notices> getAllNotices() {
		noticesmodel.setWrappedData(nm.getbrowse(selectedUser.getId()));
		return noticesmodel;
	}

	public User getselectedUser() {
		Map<String, String> reqParams = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		String eId = reqParams.get("login");
		selectedUser = um.showgetuser(eId);
		return selectedUser;
	}
}
