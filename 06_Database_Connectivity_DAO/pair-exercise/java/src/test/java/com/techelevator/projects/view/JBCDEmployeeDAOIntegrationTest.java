package com.techelevator.projects.view;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
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
import com.techelevator.projects.model.jdbc.JDBCDepartmentDAO;
import com.techelevator.projects.model.jdbc.JDBCEmployeeDAO;
import com.techelevator.projects.model.jdbc.JDBCProjectDAO;

public class JBCDEmployeeDAOIntegrationTest {

	private static SingleConnectionDataSource dataSource;

	private JDBCEmployeeDAO dao;
	private JDBCProjectDAO daoProj;
	private JDBCDepartmentDAO daoDept;



//	private static final String TEST_Employee_first_name = "Kobe";
	private long deptId;
	private long projId;
	private long empId;

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
		
		System.out.println("All tests are done");
		dataSource.destroy();
	}

	@Before
	public void setup() {
		System.out.println("Starting test");
		String sqlInsertEmployee = "INSERT INTO employee (employee_id, department_id, first_name, last_name, birth_date, gender, hire_date) VALUES (20, 1, ?, ?, ?, ?, ?)";
		Date birthDate = Date.valueOf("1990-01-01");
		Date hireDate = Date.valueOf("2020-01-01");
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//		jdbcTemplate.update(sqlInsertEmployee, TEST_Employee_first_name, "Bryant", birthDate, "M", hireDate);
		projId = jdbcTemplate.queryForObject("INSERT INTO project (name, from_date) VALUES ('RAW','2019-08-18') RETURNING project_id",Long.class);
		deptId = jdbcTemplate.queryForObject("INSERT INTO department (name) VALUES ('MonNight') RETURNING department_id",Long.class);
		empId = jdbcTemplate.queryForObject("INSERT INTO employee (last_name, first_name, birth_date, gender, hire_date) VALUES ('Styles','AJ','1986-06-11','M','2020-08-15') RETURNING employee_id" ,Long.class);
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
		assertEquals(12, getAllEmpList.size());
		
	}
	
	@Test
	public void search_employees_by_name_test() {
		Employee testEmp = new Employee();
		testEmp.setFirstName("Kobe");
		testEmp.setLastName("Bryant");
		List<Employee> searchEmpList = dao.searchEmployeesByName("Kobe", "Bryant");
		searchEmpList.add(testEmp);
		assertEquals("Kobe", searchEmpList.get(0).getFirstName());
		assertEquals("Bryant", searchEmpList.get(0).getLastName());
	
	}
	
	@Test
	public void get_employee_by_department_id() {	
		Employee testEmp = new Employee();
		List<Employee> deptIdList = dao.getAllEmployees();
		assertEquals(12, deptIdList.size());
	}

	@Test
	public void get_employees_without_project_id() {
		Employee testEmp = new Employee();
		testEmp.setFirstName("Kobe");
		testEmp.setLastName("Bryant");
		List<Employee> empWithoutId = dao.getEmployeesWithoutProjects();
		empWithoutId.add(testEmp);
		System.out.println(empWithoutId.size());
		assertEquals(4, empWithoutId.size());
		assertEquals("Kobe", empWithoutId.get(3).getFirstName());
		assertEquals("Bryant", empWithoutId.get(3).getLastName());
	}
	
	@Test
	public void get_employee_by_project_id() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		jdbcTemplate.update("INSERT INTO employee (employee_id, department_id, first_name, last_name, birth_date, gender, hire_date) VALUES (33, 1, 'Hulk', 'Hogan', '1986-02-02', 'M', '1986-02-02')");
		jdbcTemplate.update("INSERT INTO project_employee (project_id, employee_id)VALUES(1, 33)");
		int size = dao.getEmployeesByProjectId(1L).size();
		System.out.println(size);
		List<Employee> emp = dao.getEmployeesByProjectId(1L);
		assertEquals(2, emp.size());
		assertEquals("Hulk", emp.get(0).getFirstName());
		
		
		
//		List<Employee> empByProjId = dao.getEmployeesByProjectId(projId);
//		System.out.println(empByProjId.size());
//		assertEquals(3, empByProjId.size());

		
		int beforeTest = (dao.getEmployeesByProjectId(1L)).size();
		
		jdbcTemplate.update("INSERT INTO employee VALUES (-2, 1, 'Kobe', 'Bryant', '1990-01-01', 'M', '2020-01-01')");
			jdbcTemplate.update("INSERT INTO project_employee VALUES( 1,-2)");
			
			
			int afterTest = (dao.getEmployeesByProjectId(1L)).size();

			assertEquals(beforeTest + 1, afterTest);
			
//		Employee testEmp = new Employee();
//		List<Employee> empByProjId = dao.getEmployeesByProjectId((long) daoProj.getAllActiveProjects().size());
//		System.out.println(empByProjId.size());
//		assertEquals(9, empByProjId.size());


	}
	
	@Test
	public void change_employee_department() {
	JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
	jdbcTemplate.update("INSERT INTO employee VALUES (-2, 1, 'Kobe', 'Bryant', '1990-01-01', 'M', '2020-01-01')");

	int before = dao.getEmployeesByDepartmentId(1L).size();
	int after = dao.getEmployeesByDepartmentId((long) 2).size();
	dao.changeEmployeeDepartment(1L, (long) 2);
	
	assertEquals(before + 1, after);
	
	}
}
