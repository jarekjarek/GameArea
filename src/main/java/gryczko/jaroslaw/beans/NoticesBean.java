package gryczko.jaroslaw.beans;

import gryczko.jaroslaw.domain.Notices;
import gryczko.jaroslaw.service.NoticesManagerImpl;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;


public class NoticesBean {

	private Notices notices = new Notices();
	private Notices notices1 = new Notices(); 
	private String ssearch = null;    

	public String getSsearch() {   
		return ssearch;
	}

	public void setSsearch(String ssearch) {
		this.ssearch = ssearch;
	}

	public Notices getNotices1() {
		return notices1;
	}

	public void setNotices1(Notices notices1) {
		this.notices1 = notices1; // 
	}

	private ListDataModel<Notices> noticesmodel = new ListDataModel<Notices>();   

	@Inject     
	NoticesManagerImpl nm = new NoticesManagerImpl();

	public Notices getNotices() {
		return notices;
	}

	public void setNotices(Notices notices) {
		this.notices = notices;
	}

	public String add() {   
		notices1.setUname(null);
		notices1.setDate(null);    
		notices1.setId(0);
		boolean check = nm.addAnn(notices1);  
		if (check) {
			notices1.setContact(null);
			notices1.setContent(null);
			notices1.setDate(null);
			notices1.setId(0);
			notices1.setTelephone(0);
			notices1.setTitle(null);
			notices1.setUname(null);
			notices1.setVote(0);
			return "/reports/added";   
		} else {
			return "/reports/notadded";
		}
	}

	public String update() {         
		boolean check = nm.update(notices);
		if (check) {
			return "editlist";
		}
		return "home";

	}

	public String acceptNotice() {
		Notices n1 = noticesmodel.getRowData();   
		boolean check = nm.accept(n1.getId());  
		if (!check) {
			return "home";
		}
		return "pending.jsf";
	}

	public String dropNotice() {
		Notices n1 = noticesmodel.getRowData();   
		boolean check = nm.drop(n1.getId());
		if (!check) {
			return "home";
		}
		return "pending.jsf";
	}

	public String search() {
		getSearchNotices();
		return "/result.jsf";        

	}

	public DataModel<Notices> getAllNotices() {      
		noticesmodel.setWrappedData(nm.getbrowse());
		return noticesmodel;
	}

	public DataModel<Notices> getPendingNotices() {     
		noticesmodel.setWrappedData(nm.getpending());
		return noticesmodel;
	}

	public DataModel<Notices> getSearchNotices() {     
		noticesmodel.setWrappedData(nm.getsearch(ssearch));
		return noticesmodel;
	}

	public DataModel<Notices> getMyNotices() {   
		noticesmodel.setWrappedData(nm.getmybrowse());
		return noticesmodel;
	}

	public String editNotice() {    
		Notices notice1 = noticesmodel.getRowData();
		notices.setId(notice1.getId());
		notices.setContact(notice1.getContact());
		notices.setContent(notice1.getContent());
		notices.setDate(notice1.getDate());
		notices.setTelephone(notice1.getTelephone());
		notices.setTitle(notice1.getTitle());
		notices.setUname(notice1.getUname());

		return "editann";
	}

	public String deleteNotice() {
		Notices notice1 = noticesmodel.getRowData();
		notices.setId(notice1.getId());
		notices.setContact(notice1.getContact());
		notices.setContent(notice1.getContent());                                          
		notices.setDate(notice1.getDate());
		notices.setTelephone(notice1.getTelephone());
		notices.setTitle(notice1.getTitle());
		notices.setUname(notice1.getUname());
		notices.setVote(0);
		boolean check = nm.deleteAnn(notices);  
		if (check) {
			return "deletelist";   
		} else {
			return "home";
		}

	}

	public String show() {   
		Notices notice2 = noticesmodel.getRowData();
		notices.setId(notice2.getId());
		notices.setContact(notice2.getContact());
		notices.setContent(notice2.getContent());
		notices.setDate(notice2.getDate());
		notices.setTelephone(notice2.getTelephone());
		notices.setTitle(notice2.getTitle());
		notices.setUname(notice2.getUname());
		notices.setVote(notice2.getVote());
		return "show";
	}

}
