package gryczko.jaroslaw.domain;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;

@SessionScoped
@Named
public class Notices implements Serializable {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;

	private int vote;

	private int id;

	@NotEmpty(message = "Game Title can't be empty.")
	@Length(min = 6, message = "Game Title must have min. 6 chars")
	private String title;

	private String uname;

	private String date;

	@NotEmpty(message = "Content can't be empty.")
	@Length(min = 6, message = "Game Title must have min. 20 chars")
	private String content;

	@NotEmpty(message = "Email can't be empty.")
	@Email(message = "Correct email field")
	private String contact;

	@Min(1)
	private int telephone;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public int getTelephone() {
		return telephone;
	}

	public void setTelephone(int telephone) {
		this.telephone = telephone;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVote() {
		return vote;
	}

	public void setVote(int vote) {
		this.vote = vote;
	}
}
