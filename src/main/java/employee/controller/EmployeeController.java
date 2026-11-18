package employee.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import employee.model.Employee;
import employee.repository.EmployeeRepository;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository repository;

    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    // (b) Add a new employee
    @PostMapping
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        Employee saved = repository.save(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // (c) Get all employees
    @GetMapping
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    // (c) Get one employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable int id) {
        Optional<Employee> emp = repository.findById(id);
        if (emp.isPresent()) {
            return ResponseEntity.ok(emp.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Employee not found with id " + id);
    }

    // (d) Update an employee
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable int id, @RequestBody Employee employee) {
        employee.setId(id);
        if (repository.update(employee)) {
            return ResponseEntity.ok(employee);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Employee not found with id " + id);
    }

    // (e) Delete an employee
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable int id) {
        if (repository.deleteById(id)) {
            return ResponseEntity.ok("Employee deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Employee not found with id " + id);
    }
}