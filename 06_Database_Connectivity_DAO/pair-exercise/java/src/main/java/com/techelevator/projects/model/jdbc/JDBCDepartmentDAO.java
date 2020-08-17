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
		return new ArrayList<>();
	}

	@Override
	public void saveDepartment(Department updatedDepartment) {
		
	}

	@Override
	public Department createDepartment(Department newDepartment) {
		return null;
	}

	@Override
	public Department getDepartmentById(Long id) {
		return null;
	}

}
