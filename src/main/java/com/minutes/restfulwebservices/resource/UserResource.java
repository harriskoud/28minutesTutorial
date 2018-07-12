package com.minutes.restfulwebservices.resource;



import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.minutes.restfulwebservices.domain.User;
import com.minutes.restfulwebservices.exception.ListOfUsersExceptionNotFound;
import com.minutes.restfulwebservices.exception.UserNotFoundException;
import com.minutes.restfulwebservices.service.UserDaoService;

@RestController
public class UserResource {
	
	@Autowired
	UserDaoService userDaoService;
	
	@GetMapping("/users")
	public List<User> getListOfUser(){
		
		List<User> users = userDaoService.findAll();
		if (users.isEmpty())
			throw new ListOfUsersExceptionNotFound("No users are stored yet");
		return users ;
	}
	
	@GetMapping("/users/{id}")
	public Resource<User> getListOfUser(@PathVariable int id){
		User user = userDaoService.findOne(id);
		if (user == null) 
			throw  new UserNotFoundException("id-" +id);
		
		Resource<User> resource = new Resource<User>(user);
		ControllerLinkBuilder linkTo=  ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(this.getClass()).getListOfUser());
		resource.add(linkTo.withRel("all-users"));
		
		return resource;
	}
	
	@PostMapping("/users")
	public ResponseEntity<?> createuser(@Valid @RequestBody User user) {
		User savedUser = userDaoService.save(user);
		
		URI location = 
		ServletUriComponentsBuilder.
		fromCurrentRequest()
			.path("/id")
			.buildAndExpand(savedUser.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	
	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable int id){
		
		User user = userDaoService.deleteById(id);
		if (user == null) {
			throw  new UserNotFoundException("id-" +id);
		}
	}
	

}
