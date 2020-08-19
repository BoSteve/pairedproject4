package com.techelevator.projects.view;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.jdbc.JDBCEmployeeDAO;
import com.techelevator.projects.model.jdbc.JDBCProjectDAO;

public class JDBCProjectDAOtest {
	private static SingleConnectionDataSource dataSource;
	private JDBCProjectDAO dao;
	private JDBCEmployeeDAO empDao;
	private long empId;
	private long deptId;
	private long projId;
	
	/*
	 * 1. Get a list of all active projects - check
	 * 2. Add row to projects - WWE - check
	 * 3. Remove employee from a project
	 * 4. Add employee to a project - check
	 */
	
	
	@BeforeClass
	public static void setupDataSource() {
		System.out.println("Starting test suite");
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/projects");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}
	
	@AfterClass
		public static void closeDataSource() throws SQLException { 
		dataSource.destroy(); 
		System.out.println("All tests are done");
	}
	@Before
	public void setup() {
		System.out.println("Starting test");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String sqlInsertProj = "INSERT INTO project (name, from_date, to_date) VALUES ('WWE Smackdown', '2019-09-09', '2030-09-09')";
		jdbcTemplate.update(sqlInsertProj);
		//Lines above add a new proj 'WWE Smackdown'
		String sqlInsertDept = "INSERT INTO department (name) VALUES ('Strong Style')";
		jdbcTemplate.update(sqlInsertDept);
		//Lines above add a new dept 'Strong Style'
		String sqlInsertEmp = "INSERT INTO employee (first_name, last_name, birth_date, gender, hire_date) VALUES ('Shinsuke', 'Nakamura', '1986-06-11', 'M', '2020-08-15')";
		jdbcTemplate.update(sqlInsertEmp);
		//Lines above add a new emp 'Shinsuke Nakamura'
		deptId = jdbcTemplate.queryForObject("INSERT INTO department (name) VALUES ('TESTDept') RETURNING department_id",Long.class);
		projId = jdbcTemplate.queryForObject("INSERT INTO project (name, from_date) VALUES ('RAW','2019-08-18') RETURNING project_id",Long.class);
		empId = jdbcTemplate.queryForObject("INSERT INTO employee (last_name, first_name, birth_date, gender, hire_date) VALUES ('Styles','AJ','1986-06-11','M','2020-08-15') RETURNING employee_id" ,Long.class);
		//Lines above create longs for method args
		dao = new JDBCProjectDAO(dataSource);
		empDao = new JDBCEmployeeDAO(dataSource);
	}
	
	
	@After
	public void rollback() {
		System.out.println("Ending test");
		try {
			dataSource.getConnection().rollback();
		} catch (SQLException e) {
			System.out.println("Database connection problems");
		}
	}

	@Test
	public void list_all_active_projects() {
		List<Project> activeProj = dao.getAllActiveProjects();
		assertEquals(4, activeProj.size());
		assertEquals("WWE Smackdown", activeProj.get(2).getName());
	}

	@Test
	public void add_emp_to_proj() {
		dao.addEmployeeToProject(projId, empId);
		List<Employee> emp = empDao.getEmployeesByProjectId(projId);
		System.out.println(emp);
		assertEquals(1, emp.size());
		
	}
	
}
