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
import com.techelevator.projects.model.jdbc.JDBCEmployeeDAO;

public class JBCDEmployeeDAOIntegrationTest {

	private static SingleConnectionDataSource dataSource;

	private JDBCEmployeeDAO dao;

	private static final String TEST_Employee_first_name = "Michael";

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
		String sqlInsertEmployee = "INSERT INTO employee (employee_id, department_id, ?, last_name, birth_date, gender, hire_date)";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertEmployee,TEST_Employee_first_name);
		dao = new JDBCEmployeeDAO(dataSource);

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
	public void get_all_employees() {
		Employee testEmp = new Employee();
		List<Employee> getAllEmpList = dao.getAllEmployees();
		assertEquals(13, getAllEmpList.size());
		
	}
	

}
