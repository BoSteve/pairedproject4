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

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.jdbc.JDBCDepartmentDAO;

public class JBCDDepartmentDAOIntegrationTest {

	private static SingleConnectionDataSource dataSource;
	private static final String Department_Test_String = "Superstars";

	private JDBCDepartmentDAO dao;

	/*
	 * things to test 1. Insert a department
	 * 
	 * 
	 * 
	 * 
	 */

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
		String sqlInsertDepartment = "INSERT INTO department (department_id, name) VALUES (50, ?)";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertDepartment, Department_Test_String);
		dao = new JDBCDepartmentDAO(dataSource);

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
	public void test_to_get_all_departments() {
		Department testDepartment = new Department();
//		testDepartment.setId((long) 100);
//		testDepartment.setName("SQL test Department");

//	dao.saveDepartment(testDepartment);

		List<Department> savedDepartment = dao.getAllDepartments();
		savedDepartment.add(testDepartment);
		boolean actualResult = savedDepartment.contains(testDepartment);

		assertEquals(true, actualResult);

	}

	@Test
	public void search_for_department_by_name() {
		Department testDepartment = new Department();
		testDepartment.setName("Cruiserweight");
		dao.createDepartment(testDepartment);
		List<Department> deptList = dao.searchDepartmentsByName(testDepartment.getName());
		assertEquals(1, deptList.size());
	}

	@Test
	public void save_department_test() {
		Department testSaveDept = new Department();

		testSaveDept.setName("Heavyweight");

		List<Department> saveDeptList = dao.getAllDepartments();
		saveDeptList.add(testSaveDept);
		boolean actualResult = saveDeptList.contains(testSaveDept);

		assertEquals(true, actualResult);

	}

	@Test
	public void create_new_department() {
		Department testCreateDept = new Department();

		testCreateDept.setName("Featherweight");

		Department createDept = dao.createDepartment(testCreateDept);
		List<Department> createDeptList = dao.getAllDepartments();
		createDeptList.add(testCreateDept);
		boolean actualResult = createDeptList.contains(testCreateDept);

		assertEquals(true, actualResult);
	}

	@Test
	public void get_department_by_id_test() {
		Department testIdDept = new Department();

		testIdDept.setId((long) 100);

		Department idDept = dao.getDepartmentById(testIdDept.getId());
		List<Department> testDeptIdList = dao.getAllDepartments();
		testDeptIdList.add(testIdDept);
		boolean actualResult = testDeptIdList.contains(testIdDept);

		assertEquals(true, actualResult);

	}

}
