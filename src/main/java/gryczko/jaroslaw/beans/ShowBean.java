package gryczko.jaroslaw.beans;

import gryczko.jaroslaw.domain.Notices;
import gryczko.jaroslaw.service.NoticesManagerImpl;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.inject.Inject;


public class ShowBean {

	private Notices selectedNotice;

	public void setSelectedNotice(Notices selectedNotice) {
		this.selectedNotice = selectedNotice;     
	}

	@Inject   
	NoticesManagerImpl nm = new NoticesManagerImpl();

	public Notices getselectedNotice() {
		Map<String, String> reqParams = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		String eId = reqParams.get("notice");
		Long selectedNoticeId = Long.parseLong(eId);
		selectedNotice = nm.showgetann(selectedNoticeId);
		return selectedNotice;
	}

	public String vrox() {   
		nm.rox(selectedNotice.getId());
		return "show.jsf?faces-redirect=true&notice=" + selectedNotice.getId();

	}

	public String vsux() { 
		nm.sux(selectedNotice.getId());
		return "show.jsf?faces-redirect=true&notice=" + selectedNotice.getId();

	}


	public boolean ischeckVotes() {   
		boolean check = nm.checkVotes(selectedNotice.getId());
		if (check) {
			return false;
		}
		return true;

	}
}
