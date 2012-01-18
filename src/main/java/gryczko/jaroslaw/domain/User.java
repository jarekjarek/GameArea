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
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "First Name can't be empty.")
	@Length(min = 3, max = 45, message = "Pole IMI\u0118 nie mo\u017ce by\u0107 dlu\u017csze niz 45 znak\u00f3w oraz kr\u00f3tsze niz 3 znak\u00f3w.")
	private String fname;
	
	private int id;

	@NotEmpty(message = "Nie mo\u017cesz zostawi\u0107 pola NAZWISKO pustego.")
	@Length(min = 3, max = 45, message = "Pole NAZWISKO nie mo\u017ce by\u0107 dlu\u017csze niz 45 znak\u00f3w oraz kr\u00f3tsze niz 3 znak\u00f3w.")
	private String lname;
	//
	@Min(1)
	private int telephone;

	@NotEmpty(message = "Nie mo\u017cesz zostawi\u0107 pola DATA URODZENIA pustego.")
	@Length(min = 10, max = 10, message = "Pole DATA URODZENIA musi mie\u0107 dok\u0142adnie 10 znak\u00f3w.")
	private String dob;

	@NotEmpty(message = "Nie mo\u017cesz zostawi\u0107 pola EMAIL pustego.")
	@Email(message = "Wpisz poprawny adres email.")
	private String email;

	@NotEmpty(message = "Nie mo\u017cesz zostawi\u0107 pola LOGIN pustego.")
	@Length(min = 3, max = 45, message = "Pole LOGIN nie mo\u017ce by\u0107 dlu\u017csze niz 45 znak\u00f3w oraz kr\u00f3tsze niz 3 znak\u00f3w.")
	private String login;

	@NotEmpty(message = "Nie mo\u017cesz zostawi\u0107 pola HAS\u0141O pustego.")
	@Length(min = 6, max = 45, message = "Pole HAS\u0141O nie mo\u017ce by\u0107 dlu\u017csze niz 45 znak\u00f3w oraz kr\u00f3tsze niz 6 znak\u00f3w.")
	private String password;

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public int getTelephone() {
		return telephone;
	}

	public void setTelephone(int telephone) {
		this.telephone = telephone;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
