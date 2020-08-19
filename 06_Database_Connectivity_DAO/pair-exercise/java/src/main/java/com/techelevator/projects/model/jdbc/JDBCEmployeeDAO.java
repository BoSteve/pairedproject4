package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.EmployeeDAO;

public class JDBCEmployeeDAO implements EmployeeDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCEmployeeDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Employee> getAllEmployees() {
		ArrayList<Employee> employees = new ArrayList<>();
		String sqlGetEmp = "SELECT * FROM employee";
		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlGetEmp);
		while (sqlRowSet.next()) {
			Employee emp = addRowToEmployee(sqlRowSet);
			employees.add(emp);
		}
		
		return employees;
	}

	private Employee addRowToEmployee(SqlRowSet sqlRowSet) {
		Employee worker = new Employee();
		worker.setId(sqlRowSet.getLong("employee_id"));
		worker.setFirstName(sqlRowSet.getString("first_name"));
		worker.setLastName(sqlRowSet.getString("last_name"));
		worker.setBirthDay(sqlRowSet.getDate("birth_date").toLocalDate());
		worker.setHireDate(sqlRowSet.getDate("hire_date").toLocalDate());
		worker.setGender(sqlRowSet.getString("gender").charAt(0));
		worker.setDepartmentId(sqlRowSet.getLong("department_id"));
		return worker;
		
	}
	
	@Override
	public List<Employee> searchEmployeesByName(String firstNameSearch, String lastNameSearch) {
		ArrayList<Employee> empSearch = new ArrayList<>();
		String search = "SELECT * FROM employee WHERE first_name ILIKE (?) OR last_name ILIKE (?)";
		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(search, "%" + firstNameSearch +"%" , "%"+ lastNameSearch +"%");
		while (sqlRowSet.next()) {
			Employee empClass = addRowToEmployee(sqlRowSet);
			empSearch.add(empClass);
		}
		
		return empSearch;
	}

	@Override
	public List<Employee> getEmployeesByDepartmentId(long id) {
		ArrayList<Employee> empList = new ArrayList<>();
		String idSearch = "SELECT * FROM employee WHERE department_id = ?";
		SqlRowSet sqlEmpRow = jdbcTemplate.queryForRowSet(idSearch, id);
		
		while (sqlEmpRow.next()) {
			Employee emp = addRowToEmployee(sqlEmpRow);
			empList.add(emp);
		}
		
		return empList;
	}

	@Override
	public List<Employee> getEmployeesWithoutProjects() {
		ArrayList<Employee> empList = new ArrayList<>();
		String noProj = "SELECT * FROM employee E LEFT JOIN project_employee PE ON E.employee_id = PE.employee_id WHERE PE.employee_id IS NULL";
		SqlRowSet sqlEmpRow = jdbcTemplate.queryForRowSet(noProj);
		while (sqlEmpRow.next()) {
			empList.add(addRowToEmployee(sqlEmpRow));
		}
		return empList;
	}

	@Override
	public List<Employee> getEmployeesByProjectId(Long projectId) {
		ArrayList<Employee> empList = new ArrayList<>();
		String projSearch = "SELECT * FROM employee E JOIN project_employee PE ON E.employee_id = PE.employee_id WHERE PE.project_id = ?";
		SqlRowSet sqlEmpRow = jdbcTemplate.queryForRowSet(projSearch, projectId);
		while (sqlEmpRow.next()) {
			empList.add(addRowToEmployee(sqlEmpRow));
		}
		return empList;
		
	}

	@Override
	public void changeEmployeeDepartment(Long employeeId, Long departmentId) {
		String empUpdate = "UPDATE employee SET department_id = ? WHERE employee_id = ?";
		jdbcTemplate.update(empUpdate, departmentId, employeeId);
	}

}
