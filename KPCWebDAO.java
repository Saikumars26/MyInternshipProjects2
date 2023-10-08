package in.gov.kpc.dao;

import java.util.List;
import java.util.Map;

import in.gov.kpc.exception.KPCWebException;
import in.gov.kpc.model.GovernmentOrders;
import in.gov.kpc.model.PhotoGallery;
import in.gov.kpc.model.Services;
import in.gov.kpc.model.User;

public interface KPCWebDAO {
	
	public List<Services> getMenuForHeader(User user)  throws KPCWebException;
	
	// Upload G.O copies 
	public Boolean uploadSanctionedCopies(GovernmentOrders sanctions, User user) throws KPCWebException;
	
	public List<GovernmentOrders> getSanctionedOrders() throws KPCWebException;

	//Image Upload
	public Boolean uploadtoGallery(PhotoGallery image, User user) throws KPCWebException;
	
	//Queries Execution Methods
	public List<Map<String, Object>> getResultSetForQuery(String query) throws KPCWebException;

	public Integer updateDatabaseRecords(String query, User user) throws KPCWebException; 
}
