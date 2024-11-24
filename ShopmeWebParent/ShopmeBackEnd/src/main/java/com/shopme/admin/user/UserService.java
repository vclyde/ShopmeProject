package com.shopme.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<User> listAllUsers() {
		return (List<User>) userRepo.findAll();
	}

	public List<Role> listAllRoles() {
		return (List<Role>) roleRepo.findAll();
	}

	public void save(User user) {
		encodePassword(user);
		userRepo.save(user);
	}

	public void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}

	public boolean isEmailUnique(Integer id, String email) {
		User user = userRepo.getUserByEmail(email);
		
		if (user == null) { // null means no duplicate in db
			return true;
		}
		
		boolean isCreatingNew = (id == null);
		if (isCreatingNew) {
			if (user != null) {
				return false;
			}
		} else {
			if (user.getId() != null) {
				return false;
			}
		}
		
		// id != null 
		// user.getId().equals(id);

		return true; 
	}

	public User get(Integer id) throws UserNotFoundException {
		try {
			return userRepo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("User not found with ID: " + id);
		}
	}
}
