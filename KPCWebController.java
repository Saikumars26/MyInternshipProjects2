package in.gov.kpc.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import in.gov.kpc.exception.KPCWebException;
import in.gov.kpc.manager.KPCWebManager;
import in.gov.kpc.model.GovernmentOrders;
import in.gov.kpc.model.Services;
import in.gov.kpc.model.User;


@Controller
@SessionAttributes("user")

public class KPCWebController {

	
	@Autowired
	private KPCWebManager kpcWebManager;
	private Logger log = LoggerFactory.getLogger(this.getClass());

	
	@RequestMapping(value = "/kpc/home")
	@ResponseBody
	public ModelAndView getAllMenus(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = null;
		mav.setViewName("home");
		List<Services> menus = null;
		try {
			menus = kpcWebManager.getMenuForHeader(user);

			mav.addObject("menus", menus);
			session.setAttribute("menus", menus);
			log.info(""+menus);
		} catch (KPCWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getLocalizedMessage());
		}
		
		return mav;
}
	@RequestMapping(value="/kpc/sanctionedOrders")
	@ResponseBody
	public ModelAndView getAllSanctions(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("sanctionedOrders");
		List<GovernmentOrders> sanctionedOrders = null;
		try {
			sanctionedOrders = kpcWebManager.getSanctionedOrders();
			mav.addObject("sanctionedOrders", sanctionedOrders);
		} catch (KPCWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getLocalizedMessage());
		}
		
		return mav;
	}
	
	
	@RequestMapping(value = "/kpc/about")
	@ResponseBody
	public ModelAndView showAboutUs() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("about");
		
		return mav;
	}
	
	
	@RequestMapping(value = "/kpc/kpcBoard")
	@ResponseBody
	public ModelAndView showBoard() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("kpcBoard");
		
		return mav;
	}
	
	
	@RequestMapping(value = "/kpc/clearances")
	@ResponseBody
	public ModelAndView showClearances() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("clearances");
		
		return mav;
	}
	
	
	@RequestMapping(value = "/kpc/overview")
	@ResponseBody
	public ModelAndView showOverview() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("overview");
		
		return mav;
	}
	
	@RequestMapping(value = "/kpc/benefits")
	@ResponseBody
	public ModelAndView showBenefits() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("benefits");
		
		return mav;
	}
	
	
	@RequestMapping(value = "/kpc/history")
	@ResponseBody
	public ModelAndView showHistory() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("history");
		
		return mav;
	}
	
	@RequestMapping(value = "/kpc/features")
	@ResponseBody
	public ModelAndView showFeatures() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("features");
		
		return mav;
	}
	

	@RequestMapping(value = "/kpc/pressRelease")
	@ResponseBody
	public ModelAndView pressRelease() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("underConstruction");
		
		return mav;
	}
	
	@RequestMapping(value = "/kpc/mediaCoverage")
	@ResponseBody
	public ModelAndView mediaCoverage() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("underConstruction");
		
		return mav;
	}
	
	@RequestMapping(value = "/kpc/videoReports")
	@ResponseBody
	public ModelAndView videoReports() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("underConstruction");
		
		return mav;
	}
	@RequestMapping(value = "/kpc/keyVisits")
	@ResponseBody
	public ModelAndView keyVisits() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("underConstruction");
		
		return mav;
	}
	
	@RequestMapping(value = "/kpc/kpcContacts")
	@ResponseBody
	public ModelAndView kpcContacts() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("underConstruction");
		
		return mav;
	}
	
	
}
