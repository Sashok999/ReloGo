package ru.mirea.movingcompany.gall.repositories;

import ru.mirea.movingcompany.gall.enums.EmployeeRoles;
import ru.mirea.movingcompany.gall.models.EmolyeeModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryEmplyeeRepository implements EmployeeRepository {

    private List<EmolyeeModel> employees = new ArrayList<>();
    public InMemoryEmplyeeRepository() {}

    @Override
    public List<EmolyeeModel> getAllEmployees() {
        return List.copyOf(employees);
    }

    @Override
    public Optional<EmolyeeModel> getEmployeeById(int id) {
        return employees.stream().filter(e -> e.getId() == id).findFirst();
    }

    @Override
    public List<EmolyeeModel> getEmployeesByRole(EmployeeRoles role) {
        return employees.stream().filter(e -> e.getRole() == role).toList();
    }

    @Override
    public Optional<EmolyeeModel> getEmployeeByPhone(String phone) {
        return employees.stream().filter(e -> e.getPhone() != null && e.getPhone().equals(phone)).findFirst();
    }

    @Override
    public List<EmolyeeModel> getEmployeesByName(String name) {
        return employees.stream().filter(e -> e.getFullName() != null && e.getFullName().equalsIgnoreCase(name)).toList();
    }

    @Override
    public boolean createEmployee(EmolyeeModel employee) {
        if (!employees.stream().filter(e -> e.getId() == employee.getId()).toList().isEmpty()) {
            throw new IllegalArgumentException("Сотрудник с данным ID уже существует!");
        }
        return employees.add(employee);
    }

    @Override
    public EmolyeeModel updateEmployee(EmolyeeModel employee) {
        EmolyeeModel employeeModel = employees.stream().filter(e -> e.getId() == employee.getId()).findFirst().orElse(null);

        if (employeeModel == null) {
            throw new IllegalArgumentException("Сотрудник с данным ID не существует!");
        }

        int i = employees.indexOf(employeeModel);
        return employees.set(i, employee);
    }

    @Override
    public EmolyeeModel deleteEmployeeById(int id) {
        EmolyeeModel employeeModel = employees.stream().filter(e -> e.getId() == id).findFirst().orElse(null);

        if (employeeModel == null) {
            throw new IllegalArgumentException("Сотрудник с данным ID не существует!");
        }

        int i = employees.indexOf(employeeModel);
        return employees.remove(i);
    }
}
