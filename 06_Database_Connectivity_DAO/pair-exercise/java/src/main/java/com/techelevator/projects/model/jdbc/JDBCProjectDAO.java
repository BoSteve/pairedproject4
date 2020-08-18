package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.ProjectDAO;

public class JDBCProjectDAO implements ProjectDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCProjectDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Project> getAllActiveProjects() {
		ArrayList<Project> projList = new ArrayList<Project>();
		String sqlProjString = "SELECT * FROM project WHERE (from_date IS NOT NULL AND from_date < now() AND (to_date IS NULL OR to_date > now()))";
		SqlRowSet sqlProjSet = jdbcTemplate.queryForRowSet(sqlProjString);
		while (sqlProjSet.next()) {
			Project proj = addRowToProjects(sqlProjSet);
			projList.add(proj);
		}
		
		return projList;
	}

	private Project addRowToProjects(SqlRowSet sqlProjSet) {
		Project addRowProject = new Project();
		addRowProject.setId(sqlProjSet.getLong("project_id"));
		addRowProject.setName(sqlProjSet.getString("name"));
		
		
		
		// +++++++++++++ Check this if an error occurs. +++++++++++++++++++++++++++++
//		addRowProject.setStartDate(sqlProjSet.getDate("from_date").toLocalDate());
//		addRowProject.setEndDate(sqlProjSet.getDate("to_date").toLocalDate());

		

	
		return addRowProject;
	}

	@Override
	public void removeEmployeeFromProject(Long employeeId, Long projectId) {
		String removeEmp = "DELETE FROM project_employee WHERE employee_id = ? AND project_id = ?";
		jdbcTemplate.update(removeEmp, employeeId, projectId);
		
	}

	@Override
	public void addEmployeeToProject(Long projectId, Long employeeId) {
		String addEmp = "INSERT INTO project_employee (project_id, employee_id) VALUES (?, ?)";
		jdbcTemplate.update(addEmp, projectId, employeeId);
		
	}

}
