package com.skilldistillery.todoapp.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.skilldistillery.todoapp.entities.User;

class UserTest {

	private EntityManagerFactory emf;
	private EntityManager em;
	User user;

	@BeforeEach
	public void setUp() throws Exception {
		emf = Persistence.createEntityManagerFactory("todoApp");
		em = emf.createEntityManager();
		user = em.find(User.class, 1);
	}

	@AfterEach
	public void tearDown() throws Exception {
		em.close();
		emf.close();
	}

	@Test
	public void test_category_table() {
		assertEquals(1, user.getId());
		assertEquals("Go round Mum's", user.getTodos().get(0).getTask());
		assertEquals("shaun@winchester.co.uk", user.getEmail());
	}

}
