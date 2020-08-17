package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.DepartmentDAO;

public class JDBCDepartmentDAO implements DepartmentDAO {
	
	private JdbcTemplate jdbcTemplate;

	public JDBCDepartmentDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Department> getAllDepartments() {
		ArrayList<Department> departments = new ArrayList<Department>();
		String sqlGetDepartment = "SELECT * FROM department";
		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlGetDepartment);
		while (sqlRowSet.next()) {
			Department deptName = addRowToDepartment(sqlRowSet);
//		   String idName = sqlRowSet.getString("id");
			departments.add(deptName);
			
			
		}
		return departments;
	}

	private Department addRowToDepartment(SqlRowSet sqlRowSet) {
		// TODO Auto-generated method stub
		Department deptName = new Department();
		deptName.setId(sqlRowSet.getLong("department_id"));
		deptName.setName(sqlRowSet.getString("name"));
		return deptName;
	}

	@Override
	public List<Department> searchDepartmentsByName(String nameSearch) {
		ArrayList<Department> department= new ArrayList<Department>();
		String search = "SELECT * FROM department WHERE name ILIKE (?)";
		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(search, nameSearch+"%");
		while (sqlRowSet.next()) {
			Department deptName = addRowToDepartment(sqlRowSet);
			department.add(deptName);
		}
		return department;
	}

	@Override
	public void saveDepartment(Department updatedDepartment) {
		String sqlInsertDept = "UPDATE department SET name = ? WHERE department_id = ?";
		jdbcTemplate.update(sqlInsertDept, updatedDepartment.getName(), updatedDepartment.getId());
	}

	@Override
	public Department createDepartment(Department newDepartment) {
		String sqlInsertDept = "INSERT INTO department(name) VALUES(?)";
		newDepartment.setId(getNewDepartmentById());
		jdbcTemplate.update(sqlInsertDept, newDepartment.getName());
		return newDepartment;
	}

	private long getNewDepartmentById() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_department_id')");
		if(nextIdResult.next()) {
		return nextIdResult.getLong(1);
		} else {
		throw new RuntimeException("Something went wrong while getting an id for the new dept");
		}}
	
	
	
	@Override
	public Department getDepartmentById(Long id) {
		Department idDepartment = new Department();
		String sqlDeptString = "SELECT department_id, name FROM department WHERE department_id = ?";
		SqlRowSet sqlDeptRow = jdbcTemplate.queryForRowSet(sqlDeptString, id);
		
		if (sqlDeptRow.next()) {
			idDepartment.setId(sqlDeptRow.getLong("id"));
			idDepartment.setName(sqlDeptRow.getString("Name"));

		}
		
		return idDepartment;
	}

}
