package in.gov.kpc.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;





import in.gov.kpc.exception.KPCWebException;
import in.gov.kpc.manager.KPCWebManager;
import in.gov.kpc.model.FileInfo;
import in.gov.kpc.model.GovernmentOrders;
import in.gov.kpc.model.PhotoGallery;
import in.gov.kpc.model.Services;
import in.gov.kpc.model.User;
import in.gov.kpc.validator.GosUploadValidator;
import in.gov.kpc.validator.LoginFormValidator;

@Controller
@SessionAttributes("user")
public class KPCAdminController {

	private org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private KPCWebManager kpcWebManager;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setAutoGrowCollectionLimit(999999);
	}

	Set<String> keys = new HashSet<String>();

	public Set<String> getKeys() {
		return keys;
	}

	public void setKeys(Set<String> keys) {
		this.keys = keys;
	}

	/*LOGIN LOGOUT SERVICE STARTS*/
	
	@RequestMapping(value = "/admin/login")
	@ResponseBody
	public ModelAndView userLogin() throws KPCWebException {
		ModelAndView mav = new ModelAndView();
		User user = null;
		List<Services> menus = null;
		menus = kpcWebManager.getMenuForHeader(user);
		mav.addObject("menus", menus);
		mav.setViewName("loginPage");
		return mav;
	}

	@RequestMapping(value = "/admin/logOut", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView userLogout(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		session.removeAttribute("user");
		User user = null;
		session.setAttribute("loggedIn", false);
		List<Services> menus = null;
		try {
			menus = kpcWebManager.getMenuForHeader(user);
			session.setAttribute("menus", menus);
			mav.addObject("menus", menus);
		} catch (KPCWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mav.setViewName("home");
		return mav;
	}

	
	@RequestMapping(value = "/admin/adminPage")
	@ResponseBody
	public ModelAndView goAdminPage(@ModelAttribute("loginForm") User user, BindingResult result, Model model,
			HttpSession session) throws KPCWebException {

		ModelAndView mav = new ModelAndView();
		boolean flag = false;
		System.out.println("users"+user);
		List<Services> menus = null;
		menus = kpcWebManager.getMenuForHeader(user);
		System.out.println("menus"+menus);
		new LoginFormValidator().validate(user, result);

		mav.addObject("user", user);
		session.setAttribute("loginFailed", "false");

		if (result.hasErrors()) {
			user.setErrorMessage("Please enter valid UserName and Password");
			session.setAttribute("loginFailed", "true");
			mav.setViewName("loginPage");
		} else {
			Boolean isUserLoggedIn = false;

			mav.setViewName("loginPage");
			if (user.getUserName().equals("kpcadmin") || user.getUserName().equals("egovadmin")) {
				isUserLoggedIn = true;
				session.setAttribute("user", user);
				mav.setViewName("adminPage");
				session.setAttribute("loggedIn", isUserLoggedIn);
				menus = kpcWebManager.getMenuForHeader(user);
				session.setAttribute("menus", menus);
				mav.addObject("menus", menus);
			}
		}
		return mav;
	}
	/*LOGIN LOGOUT SERVICE ENDS*/
	
	/*UPLOADS SERVICES STARTS HERE*/

	/*G.O UPLOADS*/
	@RequestMapping(value = "/admin/uploadSanctionedCopies")
	@ResponseBody
	public ModelAndView uploadSanctionedCopies(HttpSession session) throws KPCWebException {
		ModelAndView mav = new ModelAndView();
		User user = (User) session.getAttribute("user");
		List<Services> menus = null;
		menus = kpcWebManager.getMenuForHeader(user);
		mav.addObject("menus", menus);
		mav.setViewName("uploadSanctions");
		return mav;
	}

	@RequestMapping(value = "/admin/savefiles", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView save(@ModelAttribute("uploadForm") GovernmentOrders uploadForm, BindingResult result,
			HttpSession session, Model map) {
		ModelAndView mav = new ModelAndView();
		User user = (User) session.getAttribute("user");
		Boolean flag = false;
		new GosUploadValidator().validate(uploadForm, result);
		if (result.hasErrors()) {
			uploadForm.setErrorMessage("Please enter all the fields to upload Form");
			mav.setViewName("uploadSanctions");
		} else {
			MultipartFile files = uploadForm.getFiles();
			if (null != files && files.getSize() > 0) {
				String fileName = files.getOriginalFilename();
				try {

					GovernmentOrders go = new GovernmentOrders();

					String file_name = files.getOriginalFilename();
					String extension = file_name.substring(file_name.lastIndexOf("."));

					String[] temps = fileName.split(Pattern.quote("."));
					Date d = new Date();
					String saveFileName = "G.O" + d.getTime() + uploadForm.getGoNumber() + "." + extension;
					String rootPath = System.getProperty("catalina.home");
					File dir = new File(rootPath + File.separator + "webapps" + File.separator + "KPCWebApp"
							+ File.separator + "uploadedFiles" + File.separator + "sanctionedCopies");
					if (!dir.exists())
						dir.mkdirs();
					files.transferTo(new File(dir.getAbsolutePath() + File.separator + saveFileName));
					go.setGoNumber(uploadForm.getGoNumber());
					go.setGoDate(uploadForm.getGoDate());
					go.setGoDescription(uploadForm.getGoDescription());
					go.setGoFileName(saveFileName);
					go.setGoFilePath(dir.getAbsolutePath() + File.separator + saveFileName);
					flag = kpcWebManager.uploadSanctionedCopies(go, user);
					if (flag) {
						mav.setViewName("FileSave");
					} else {
						mav.setViewName("uploadSanctions");
					}

				} catch (KPCWebException e) {

				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				map.addAttribute("files", fileName);
			}
		}

		return mav;
	}
	/*G.O UPLOADS*/
	
	/*PHOTOS AND VIDEOS UPLOAD*/
	@RequestMapping(value="/admin/uploadMediaContent")
	@ResponseBody
	public ModelAndView uploadMediaContent(HttpSession session) throws KPCWebException{
		ModelAndView mav = new ModelAndView();
		User user = (User) session.getAttribute("user");
		List<Services> menus = null;
		menus = kpcWebManager.getMenuForHeader(user);
		mav.addObject("menus", menus);
		mav.setViewName("uploadMediaContent");
		return mav;	
	}

	@RequestMapping(value = "/admin/saveMediaContent", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView saveImages(@ModelAttribute("uploadForm") PhotoGallery uploadForm, BindingResult result,
			HttpSession session, Model map) {
		ModelAndView mav = new ModelAndView();
		User user = (User) session.getAttribute("user");
		Boolean flag = false;
		new GosUploadValidator().validate(uploadForm, result);
		if (result.hasErrors()) {
			uploadForm.setErrorMessage("Please enter all the fields to upload Form");
			mav.setViewName("uploadMediaContent");
		} else {
			MultipartFile files = uploadForm.getFiles();
			if (null != files && files.getSize() > 0) {
				String fileName = files.getOriginalFilename();
				try {
					PhotoGallery image = new PhotoGallery();
					String file_name = files.getOriginalFilename();
					String extension = file_name.substring(file_name.lastIndexOf("."));

					String[] temps = fileName.split(Pattern.quote("."));
					Date d = new Date();
					String saveFileName = "Image" + d.getTime() + uploadForm.getPhotoName() + extension;
					String rootPath = System.getProperty("catalina.home");
					File dir = new File(rootPath + File.separator + "webapps" + File.separator + "KPCWebApp"
							+ File.separator + "uploadedFiles" + File.separator + "galleryUploads");
					if (!dir.exists())
						dir.mkdirs();
					files.transferTo(new File(dir.getAbsolutePath() + File.separator + saveFileName));
					
					flag = kpcWebManager.uploadtoGallery(image, user);
					if (flag) {
						mav.setViewName("FileSave");
					} else {
						mav.setViewName("uploadMediaContent");						
					}
				} catch (KPCWebException e) {
					log.error(e.getMessage());
				} catch (IllegalStateException e) {
					log.error(e.getMessage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.error(e.getMessage());
				}
				/*map.addAttribute("files", fileName);*/
			}
		}
			return mav;
	}
	/*PHOTOS AND VIDEOS UPLOAD*/
	/*UPLOADS SERVICE ENDS HERE*/

	/*DB SERVICE METHODS STARTS HERE*/
	@RequestMapping(value = "/admin/dbService", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView executeQueries(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = (User) session.getAttribute("user");
		if (user.getUserName().equals("egovadmin")) {
			keys = null;
			String result = null;
			mav.addObject("keys", keys);
			mav.addObject("queryResult",result);
			mav.setViewName("queryPage");
		} else {
			mav.setViewName(""); // Redirect to unauthorized page
		}
		return mav;
	}

	@RequestMapping(value = "/admin/getResultForQuery", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView getResultForQuery(@ModelAttribute("query") String query, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = (User) session.getAttribute("user");
		Map<String, Object> map = new HashMap<String, Object>();

		Object[] keysarray = null;
		List<List<Object>> valuesList = new ArrayList<List<Object>>();
		List<Map<String, Object>> resultset = new ArrayList<Map<String, Object>>();
		if (user.getUserName().equals("egovadmin")) {
			mav.setViewName("queryPage");
			mav.addObject("query", query);
			// only egovernance admin users are allowed to view this page
			try {
				if (query.toLowerCase().contains("select")) {
					resultset = kpcWebManager.getResultSetForQuery(query);
					if (resultset.size() > 0) {
						for (int i = 0; i < resultset.size(); i++) {
							List<Object> vlist = new ArrayList<Object>();
							map.putAll(resultset.get(i));
							if (i == 0) {
								keys = map.keySet();
								keysarray = keys.toArray();
							} else {
							}
							for (int j = 0; j < keysarray.length; j++) {
								vlist.add((Object) resultset.get(i).get(keysarray[j].toString()));
							}
							valuesList.add(vlist);
						}
					}
					mav.addObject("keys", keys);
					mav.addObject("valuesList", valuesList);
					mav.addObject("resultset", resultset);
				} else if (query.toLowerCase().contains("update")||query.toLowerCase().contains("insert")) {
						Integer count = kpcWebManager.updateDatabaseRecords(query, user);
						String result = "Updated"+count+"records Successfully";
					mav.addObject("queryResult",result);			
				}
			} catch (KPCWebException e) {
				e.printStackTrace();
			}
		} else {
			mav.setViewName("");// redirect to unauthorized page
			log.error("invalid user" + user.getUserName());
		}
		return mav;
	}
	/*DB SERVICE METHODS ENDS HERE*/
	
	@RequestMapping(value = "/admin/FileReplace", method = RequestMethod.GET)
	public ModelAndView FileReplace() {

		ModelAndView mav = new ModelAndView();
		mav.setViewName("fileReplace");
		return mav;

	}
	
	
	@RequestMapping(value = "/admin/FileReplaceAdd", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView FileReplace(@ModelAttribute("fList") FileInfo fList, BindingResult result,
			HttpSession session, HttpServletRequest request) throws FileNotFoundException {
	
         // System.out.println(fList);
		
		//PrintWriter pw = new PrintWriter("C:/Users/eGov2/Desktop/ICadWebApp24Jul/src/main/webapp/"+fList.getFileName()+"");
      String    destPath = System.getProperty("catalina.home");
			File dir = new File(destPath + File.separator + "webapps"
					+ File.separator + "KPCWebApp");
			
		
					
					
          PrintWriter pw = new PrintWriter(dir.getAbsolutePath()+fList.getFileName());
          
          
          pw.write(fList.getData());
		
		pw.close();
		
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("fileReplace");
		return mav;

	}
	
	
	@RequestMapping(value = "/admin/uploadFilestoServer", method = RequestMethod.GET)
	public ModelAndView addFiletoServer() {

		ModelAndView mav = new ModelAndView();
		mav.setViewName("uploadFilestoServer");
		return mav;

	}
	
	@RequestMapping(value = "/admin/uploadFilestoServerAdd", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView addFiletoServer(@ModelAttribute("fList") FileInfo fList, BindingResult result,
			HttpSession session, HttpServletRequest request) throws IllegalStateException, IOException {
	
         // System.out.println(fList);
		
		//PrintWriter pw = new PrintWriter("C:/Users/eGov2/Desktop/ICadWebApp24Jul/src/main/webapp/"+fList.getFileName()+"");
  
		
		MultipartFile sdfile = fList.getFile();

		if (null != sdfile && sdfile.getSize() > 0) {

			String savesdFileName = fList.getFileName();

			String rootPath = System.getProperty("catalina.home");
			File dir = new File(rootPath + File.separator + "webapps" + File.separator + "KPCWebApp" + File.separator
					);
			
			if (!dir.exists())
				dir.mkdirs();

			System.out.println(dir.getAbsolutePath() +File.separator +savesdFileName);
			
			File checkdir= new File(dir.getAbsolutePath() +File.separator +savesdFileName);
			
			if (!checkdir.exists())
				checkdir.delete();
			
			sdfile.transferTo(new File(dir.getAbsolutePath() + File.separator + savesdFileName));

			
			
			//prjDetails.setSchematicDiagramPath(savesdFileName);

		}

		
		
		
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("uploadFilestoServer");
		return mav;

	}
	
}
