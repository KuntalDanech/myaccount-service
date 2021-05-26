package com.fujifilm.controller;

import java.security.Principal;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("myaccountapp")
public class MyAccountAppRedirectionController {
	
	private static final Logger LOGGER = Logger.getLogger(MyAccountAppRedirectionController.class.getName());
	
	@Secured("ROLE_CUSTOMER")
	@PostMapping("redirect")
	public ResponseEntity<String> redirect(
			Principal principal, 
			HttpServletRequest httpRequest,
			HttpServletResponse httpResponse){
		LOGGER.info(principal.getName()+" requested");
		return ResponseEntity.ok("Yes We are redirecting to \"My Account Apps\" legacy aplication, request created bu user : "+principal.getName());
		/*try {
			httpResponse.sendRedirect("https://myaccount-fbau.fujifilm.com/myaccount/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}