package com.skilldistillery.todoapp.services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.skilldistillery.todoapp.entities.User;
import com.skilldistillery.todoapp.repositories.UserRepository;

//@Transactional
//@Repository
@Service
public class AuthServiceImpl implements AuthService {
	
	  @PersistenceContext
	  private EntityManager em;

	  @Autowired
	  private PasswordEncoder encoder;

	  @Autowired
	  private UserRepository userRepo;

	@Override
	public User register(User user) {
		if(user ==  null) {
			return null;
		}
		String encodedPW = encoder.encode(user.getPassword());
		user.setPassword(encodedPW);
		user.setEnabled(true);
		user.setRole("standard");
		userRepo.saveAndFlush(user);
		return user;
	}

}
