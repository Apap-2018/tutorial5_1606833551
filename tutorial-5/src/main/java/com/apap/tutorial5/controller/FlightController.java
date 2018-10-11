package com.apap.tutorial5.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.apap.tutorial5.model.*;
import com.apap.tutorial5.service.*;

@Controller
public class FlightController {
	@Autowired
	private FlightService flightService;

	@Autowired
	private PilotService pilotService;

	@RequestMapping(value = "/flight/add/{licenseNumber}", method = RequestMethod.GET)
	private String add(@PathVariable(value = "licenseNumber") String licenseNumber, Model model) {
		PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
		List<FlightModel> flightList = new ArrayList<>();
		pilot.setPilotFlight(flightList);
		flightList.add(new FlightModel());
		model.addAttribute("pilot", pilot);
		model.addAttribute("title", "APAP-Add Flight");
		return "addFlight";
	}

	@RequestMapping(value = "/flight/add/{licenseNumber}", method = RequestMethod.POST, params={"save"})
	private String addFligthSubmit(@ModelAttribute PilotModel pilot, Model model) {
		PilotModel archive = pilotService.getPilotDetailByLicenseNumber(pilot.getLicenseNumber());
		for(FlightModel flight : pilot.getPilotFlight()) {
			flight.setPilot(archive);
			flightService.addFlight(flight);
		}
		model.addAttribute("title", "APAP");
		return "add";
	}
	//untuk remove di tabel viewall flight
	@RequestMapping(value = "/flight/remove/{id}", method = RequestMethod.GET)
	private String deleteFlight(@PathVariable(value = "id") Long id, Model model) {
		flightService.deleteFlightById(id);
		model.addAttribute("title", "APAP");
		return "removeFlight";
	}
	//untuk remove dengan checkbox
	@RequestMapping(value = "/flight/delete", method = RequestMethod.POST)
	private String deleteFlight(@ModelAttribute PilotModel pilot, Model model) {
		for(FlightModel flight :pilot.getPilotFlight()) {
			flightService.deleteFlightById(flight.getId());	
		}
		model.addAttribute("title", "APAP");
		return "removeFlight";
	}
	@RequestMapping(value="/flight/update/{licenseNumber}/{id}", method = RequestMethod.GET)
	private String updateFlight(@PathVariable(value = "id") Long id, @PathVariable("licenseNumber") String licenseNumber, Model model) {
		FlightModel flight = new FlightModel();
		PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
		flight.setPilot(pilot);
		flight.setId(id);
		model.addAttribute("flight",flight);
		model.addAttribute("pilot",pilot);
		model.addAttribute("title", "APAP-Update Flight");
		return "updateFlight";
	}
	
	@RequestMapping(value="/flight/update/{id}",method=RequestMethod.POST)
	private String updateFlightSubmit(@ModelAttribute FlightModel flight,@PathVariable(value= "id") Long id, Model model) {
		flightService.updateFlight(id, flight.getFlightNumber(), flight.getOrigin(), flight.getDestination(), flight.getTime());
		model.addAttribute("title", "APAP");
		return "updateSuccess";
	}
	
	@RequestMapping(value ="/flight/view/{id}")
	private String viewFlight(@PathVariable(value = "id") long id, Model model) {
		FlightModel flight = flightService.getFlightDetailById(id);
		PilotModel pilotFlight = flight.getPilot();
		model.addAttribute("flight", flight);
		model.addAttribute("pilotFlight", pilotFlight);
		model.addAttribute("title", "APAP-View Flight");
		return "view-flight";
	}
	@RequestMapping(value ="/flight/view/")
	private String viewAll(Model model) {
		List<FlightModel> list = flightService.allFlight();
		model.addAttribute("list", list);
		model.addAttribute("title", "APAP-View All Flights");
		return "viewAll";		
	}
	@RequestMapping(value="/flight/add/{licenseNumber}", method = RequestMethod.POST, params= {"addRow"})
	public String addRow(@ModelAttribute PilotModel pilot, BindingResult bindingResult, Model model) {
		if (pilot.getPilotFlight() == null) {
			pilot.setPilotFlight(new ArrayList<FlightModel>());
            
        }
		pilot.getPilotFlight().add(new FlightModel());
		
		model.addAttribute("pilot", pilot);
		return "addFlight";
	}
	@RequestMapping(value="/flight/add/{licenseNumber}", params={"removeRow"}, method = RequestMethod.POST)
	public String removeRow(PilotModel pilot, BindingResult bindingResult, HttpServletRequest req, Model model) {
	   Integer rowId = Integer.valueOf(req.getParameter("removeRow"));
	   pilot.getPilotFlight().remove(rowId.intValue());
	   model.addAttribute("pilot", pilot);
	   return "addFlight";
	}
	
	
}
