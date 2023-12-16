package ru.lesson.stream;

import ru.lesson.stream.dto.Employee;
import ru.lesson.stream.dto.PositionType;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LessonStreamApi {

    /**
     * Задача №1.
     * Получить список сотрудников, у которых рейтинг {@link Employee#getRating()} больше 50.
     * Важно: Необходимо учесть, что List<Employee> employees может содержать дублирующие записи.
     */
    public List<Employee> task1(List<Employee> employees) {
        var result = employees.parallelStream()
                                            .distinct()
                                            .filter(employee -> employee.getRating() > 50)
                                            .collect(Collectors.toList());
        return result;
    }

    /**
     * Задача №2.
     * Получить список сотрудников (List<String> - формат строки {@link Employee#getName()}"="{@link Employee#getRating()})
     * У которых рейтинг {@link Employee#getRating()} меньше 50.
     */
    public List<String> task2(List<Employee> employees) {
        var result = employees.parallelStream()
                                        .distinct()
                                        .filter(employee -> employee.getRating() < 50)
                                        .map(employee -> employee.getName() + "=" + employee.getRating())
                                        .collect(Collectors.toList());
        return result;
    }

    /**
     * Задача №3.
     * Получить средний рейтнг всех сотрудников.
     */
    public double task3(List<Employee> employees) {
        var middleRate = employees.stream().mapToDouble(Employee::getRating).average().getAsDouble();
        return middleRate;
    }

    /**
     * Задача №4.
     * Получить общий список сотрудников из разных отделов и
     * отсортировать его по убыванию рейтинга {@link Employee#getRating()}.
     * Важно: необходимо учесть, что один сотрудник может числиться в разных отделах.
     * Необходимо устранить дублирование.
     */
    public List<Employee> task4(List<List<Employee>> employeeDepartments) {
        var employeeByrating = employeeDepartments.stream()
                                                                .flatMap(List::stream)
                                                                .distinct()
                                                                .sorted((e1, e2) -> (e2.getRating() - e1.getRating()))
                                                                .collect(Collectors.toList());
        return employeeByrating;
    }

    /**
     * Задача №5.
     * Предположим, что требуется выводить список сотрудников на веб-страницу.
     * Необходимо реализовать построчный вывод (пагинацию).
     * <p>
     * Пример:
     * Если number = 1, size = 3, то результирующий список будет
     * Employee{id=1, name='Name1', rating=11}, Employee{id=2, name='Name2', rating=12}, Employee{id=3, name='Name3', rating=13}
     * <p>
     * Если number = 2, size = 3, то результирующий список
     * Employee{id=4, name='Name4', rating=14}, Employee{id=5, name='Name5', rating=15}, Employee{id=6, name='Name6', rating=16}
     */
    public List<Employee> task5(List<Employee> employees, int number, int size) {
        if (number <= 0) {
            throw new IllegalArgumentException(Integer.toString(number));
        }
        var employeeFiltered = employees.stream()
                                                        .filter(employee -> employee.getId() > (number - 1) * size)
                                                        .limit(size).collect(Collectors.toList());
        return employeeFiltered;
    }

    /**
     * Задача №6.
     * Получить имена сотрудников в String: начинается строка с '[',
     * затем следуют имена сотрудников через ', ' и заканчивается строка ']'.
     * <p>
     * Пример результата: [Ivan, Olga, John]
     */
    public String task6(List<Employee> employees) {
        var names =employees.stream().map(employee -> employee.getName()).collect(Collectors.joining(", ","[","]"));
        return names;
    }

    /**
     * Задача №7.
     * Проверить наличие дублирующихся имен сотрудников {@link Employee#getName()} в списке.
     * <p>
     * Если дубли существуют - вернуть true, если дублей нет - вернуть false
     */
    public boolean task7(List<Employee> employees) {
        return employees.stream().allMatch(new HashSet<>()::add);
    }

    /**
     * Задача №8.
     * Получить средний рейтинг {@link Employee#getRating()} по каждой
     * должности сотрудника {@link Employee#getPositionType()}
     */
    public Map<PositionType, Double> task8(List<Employee> employees) {
        var result = employees.stream()
                                                    .collect(Collectors.groupingBy(Employee::getPositionType
                                                            ,Collectors.averagingDouble(Employee::getRating)));
        return result;
    }

    /**
     * Задача 9.
     * Получить словарь, который содержит две записи:
     * Ключи: true - эффективные и false - неэффективные сторудники.
     * Значение: количество сотрудников для каждой из двух групп.
     * Сотрудник является эффективным, если его рейтинг больше 50.
     */
    public Map<Boolean, Long> task9(List<Employee> employees) {
        var result = employees.stream()
                                                .collect(Collectors.partitioningBy(employee -> employee.getRating() > 50
                                                        , Collectors.counting()));
        return result;
    }

    /**
     * Задача 10.
     * Получить словарь, который содержит две записи:
     * Ключи: true - эффективные и false - неэффективные сотрудники.
     * Значение: список имен сотрудников через ', '.
     * Сотрудник является эффективным, если его рейтинг больше 50.
     */
    public Map<Boolean, String> task10(List<Employee> employees) {
        return employees.stream()
                        .collect(Collectors.partitioningBy(employee -> employee.getRating() > 50
                                , Collectors.mapping(Employee::getName, Collectors.joining(", "))));
    }

}
