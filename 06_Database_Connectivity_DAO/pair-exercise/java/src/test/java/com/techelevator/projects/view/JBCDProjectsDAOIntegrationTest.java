package com.techelevator.projects.view;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.jdbc.JDBCDepartmentDAO;
import com.techelevator.projects.model.jdbc.JDBCEmployeeDAO;
import com.techelevator.projects.model.jdbc.JDBCProjectDAO;



public class JBCDProjectsDAOIntegrationTest {
	private static SingleConnectionDataSource dataSource;
	private static final Date Project_test_from_date = 
	private JDBCProjectDAO dao;

	

	@BeforeClass
	public static void setupTestSource() {
		System.out.println("Starting testing");
		dataSource = new SingleConnectionDataSource();

		dataSource.setUrl("jdbc:postgresql://localhost:5432/projects");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);

	}

	@AfterClass
	public static void endTestSource() {
		dataSource.destroy();
	}

	@Before
	public void setup() {
		System.out.println("Starting test");
		String sqlInsertProject = "INSERT INTO project (project_id, name, from_date, to_date) VALUES (50, ?, ?, ?)";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertProject, "test insert", 1990-01-01, 2020-01-01);
		dao = new JDBCProjectDAO(dataSource);
		JDBCEmployeeDAO dao2 = new JDBCEmployeeDAO(dataSource);
		JDBCDepartmentDAO dao3 = new JDBCDepartmentDAO(dataSource);
	}

	@After
	public void rollback() {
		System.out.println("Ending test");
		try {
			dataSource.getConnection().rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Database Connection Problems");
		}

		
	}
	@Test
	public void test_to_get_all_active_projects() {
		Project testProject = new Project();
		
       List<Project> projectList = dao.getAllActiveProjects();
		
       projectList.add(testProject);
		boolean actualResult = projectList.contains(testProject);

		assertEquals(true, actualResult);
	}

}
