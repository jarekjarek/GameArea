package gryczko.jaroslaw.service;

import gryczko.jaroslaw.domain.Notices;

import java.util.List;


public interface NoticesManager {

	public boolean addAnn(Notices no);

	List<Notices> getbrowse();

	List<Notices> getmybrowse();

	List<Notices> getpending();

	boolean accept(int id);

	boolean drop(int id);

	List<Notices> getsearch(String ssearch);

	public boolean deleteAnn(Notices no);

	public boolean update(Notices no);

	public boolean rox(int id);

	public boolean sux(int id);

	public Notices showgetann(Long id);

	public boolean checkVotes(int id);

	public List<Notices> getbrowse(int id);

}
