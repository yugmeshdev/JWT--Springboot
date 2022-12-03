package com.yugmeshdev.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.yugmeshdev.jwt.model.JWTRequest;
import com.yugmeshdev.jwt.model.JWTResponse;
import com.yugmeshdev.jwt.service.UserService;
import com.yugmeshdev.jwt.utility.JWTUtility;

@RestController
public class HomeController {
	@Autowired
	private JWTUtility jwtUtility;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserService userService;
	@GetMapping("/")
	public String home() {
		return "Welcome";
	}
	@PostMapping("/authenticate")
	public JWTResponse authenticate(@RequestBody JWTRequest jwtRequest) throws Exception {
		try {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						jwtRequest.getUsername(),
						jwtRequest.getPassword()
						)
				);
		}
		catch(Exception e) {
			throw new Exception(e);
		}
		final UserDetails userDetails=
				userService.loadUserByUsername(jwtRequest.getUsername());
		final String token=jwtUtility.generateToken(userDetails);
		return new JWTResponse(token);
	}
}
