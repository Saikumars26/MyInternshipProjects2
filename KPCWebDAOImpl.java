package in.gov.kpc.dao.impl;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import in.gov.kpc.dao.KPCWebDAO;
import in.gov.kpc.exception.KPCWebException;
import in.gov.kpc.model.GovernmentOrders;
import in.gov.kpc.model.PhotoGallery;
import in.gov.kpc.model.Services;
import in.gov.kpc.model.User;
import in.gov.kpc.util.DateUtil;

@Repository
public class KPCWebDAOImpl implements KPCWebDAO {

	private JdbcTemplate jdbcTemplate;
	@SuppressWarnings("unused")
	private JdbcTemplate jdbcTemplateMstr;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "dataSource")
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Value("${GET_MENU_FOR_PUBLIC}")
	private String GET_MENU_FOR_PUBLIC;

	@Value("${GET_MENU_FOR_PROJECT}")
	private String GET_MENU_FOR_PROJECT;
		
	@Value("${GET_USER_ROLE}")
	private String GET_USER_ROLE;
	
	public List<Services> getMenuForHeader(User user) throws KPCWebException {
		List<Services> serviceList = new ArrayList<Services>();
		String sql,rolesql = null;		
	
		if (user==null) {
			sql = GET_MENU_FOR_PUBLIC;
		
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
			try {
				for (Map<String, Object> row : rows) {
					Services s = new Services();
					s.setResourceId(Integer.parseInt(String.valueOf(row.get("slno"))));
					s.setResourceName(String.valueOf(row.get("resource_name")));
					s.setHasChild(Boolean.parseBoolean(String.valueOf(row.get("has_child"))));
					s.setParentId(Integer.parseInt(String.valueOf(row.get("parent_id"))));
					s.setDisplayOrder(Integer.parseInt(String.valueOf(row.get("display_order"))));
					s.setResourceUrl(String.valueOf(row.get("url")));
					s.setResourceIcon(String.valueOf(row.get("resource_icon")));
					serviceList.add(s);
				}
				log.info("Successfully Added to Service List" + serviceList);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		} else {
			sql = GET_MENU_FOR_PROJECT;
			rolesql=GET_USER_ROLE;		
			Integer roleId=jdbcTemplate.queryForInt(rolesql,user.getUserName());

			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, roleId);
			try {
				for (Map<String, Object> row : rows) {
					Services s = new Services();
					s.setResourceId(Integer.parseInt(String.valueOf(row.get("slno"))));
					s.setResourceName(String.valueOf(row.get("resource_name")));
					s.setHasChild(Boolean.parseBoolean(String.valueOf(row.get("has_child"))));
					s.setParentId(Integer.parseInt(String.valueOf(row.get("parent_id"))));
					s.setDisplayOrder(Integer.parseInt(String.valueOf(row.get("display_order"))));
					s.setResourceUrl(String.valueOf(row.get("url")));
					s.setResourceIcon(String.valueOf(row.get("resource_icon")));
					serviceList.add(s);
				}
				log.info("Successfully Added to Service List" + serviceList);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			
			}			
		return serviceList;
	}


	@Value("${INSERT_GOVERNMENT_ORDERS}")
	private String INSERT_GOVERNMENT_ORDERS;
	
	public Boolean uploadSanctionedCopies(GovernmentOrders sanctions, User user) throws KPCWebException {
		// TODO Auto-generated method stub
		// Upload Sanctioned G.O to table government_orders
				String sql = INSERT_GOVERNMENT_ORDERS;


		Date goDate = null;
		try {
			goDate = (DateUtil.getSQLDate(sanctions.getGoDate()));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			jdbcTemplate.update(sql, new Object[] { sanctions.getGoNumber(), goDate, sanctions.getGoDescription(),
					sanctions.getGoFileName(), sanctions.getGoFilePath(), user.getUserName() });
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
	}

	
	@Value("${GET_ALL_GOVERNMENT_ORDERS}")
	private String GET_ALL_GOVERNMENT_ORDERS;
	public List<GovernmentOrders> getSanctionedOrders() throws KPCWebException {
		// TODO Auto-generated method stub
		String sql = GET_ALL_GOVERNMENT_ORDERS;
		List<GovernmentOrders> sanctionedOrders = new ArrayList<GovernmentOrders>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		try {
			for (Map<String, Object> row : rows) {
				GovernmentOrders go = new GovernmentOrders();
				go.setGoNumber(String.valueOf(row.get("go_number")));
				go.setGoDate(String.valueOf(row.get("go_date")));
				go.setGoDescription(String.valueOf(row.get("go_description")));
				go.setGoFileName(String.valueOf(row.get("go_file_name")));
				go.setGoFilePath(String.valueOf(row.get("go_file_path")));
				sanctionedOrders.add(go);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return sanctionedOrders;
	}

	@Value("${INSERT_GALLERY_IMAGES}")
	private String INSERT_GALLERY_IMAGES;

	@Value("${INSERT_GALLERY_VIDEOS}")
	private String INSERT_GALLERY_VIDEOS;

	
	public Boolean uploadtoGallery(PhotoGallery image, User user) throws KPCWebException {
		String sql;
	
	if(image.getMediaId()==1){
		 sql = INSERT_GALLERY_IMAGES;
	}else {
		 sql =INSERT_GALLERY_VIDEOS;
	}
			try {
				jdbcTemplate.update(sql, new Object[] { image.getPhotoName(), image.getFilePhotoPath(),
						image.getPhotoDesc(), user.getUserName(), image.getIpAddress() });
				return true;
			}
		
		 catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
	}
	
	public List<Map<String, Object>> getResultSetForQuery(String query) throws KPCWebException {
		// TODO Auto-generated method stub

		List<Map<String, Object>> resultSet = null;
		try {
			resultSet = jdbcTemplate.queryForList(query);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return resultSet;
	}

	public Integer updateDatabaseRecords(String query, User user) throws KPCWebException {
		// TODO Auto-generated method stub
		int count = 0;		
		try {
			count = jdbcTemplate.update(query);			
		} catch (Exception e) {			
			e.printStackTrace();
			log.error(e.getLocalizedMessage());
		}
		return count;
	}


}

