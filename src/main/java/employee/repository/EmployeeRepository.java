package employee.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import employee.model.Employee;

@Repository
public class EmployeeRepository {

    private final JdbcTemplate jdbcTemplate;

    public EmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Employee> rowMapper = (rs, rowNum) -> new Employee(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("department"),
            rs.getDouble("salary"));

    public Employee save(Employee e) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO employee (name, department, salary) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, e.getName());
            ps.setString(2, e.getDepartment());
            ps.setDouble(3, e.getSalary());
            return ps;
        }, keyHolder);
        e.setId(keyHolder.getKey().intValue());
        return e;
    }

    public List<Employee> findAll() {
        return jdbcTemplate.query("SELECT * FROM employee", rowMapper);
    }

    public Optional<Employee> findById(int id) {
        List<Employee> list = jdbcTemplate.query(
                "SELECT * FROM employee WHERE id = ?", rowMapper, id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public boolean update(Employee e) {
        int rows = jdbcTemplate.update(
                "UPDATE employee SET name = ?, department = ?, salary = ? WHERE id = ?",
                e.getName(), e.getDepartment(), e.getSalary(), e.getId());
        return rows > 0;
    }

    public boolean deleteById(int id) {
        int rows = jdbcTemplate.update("DELETE FROM employee WHERE id = ?", id);
        return rows > 0;
    }
}