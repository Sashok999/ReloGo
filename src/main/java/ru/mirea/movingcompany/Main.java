package ru.mirea.movingcompany;

import ru.mirea.movingcompany.abdykalykov.enums.ClientStatus;
import ru.mirea.movingcompany.abdykalykov.models.ClientModel;
import ru.mirea.movingcompany.abdykalykov.repositories.InMemoryClientRepository;
import ru.mirea.movingcompany.abdykalykov.services.ClientService;

import ru.mirea.movingcompany.gall.enums.EmployeeRoles;
import ru.mirea.movingcompany.gall.models.EmolyeeModel;
import ru.mirea.movingcompany.gall.repositories.InMemoryEmplyeeRepository;
import ru.mirea.movingcompany.gall.services.EmployeeService;

import ru.mirea.movingcompany.belov.enums.MovingOrderStatus;
import ru.mirea.movingcompany.belov.models.MovingOrderModel;
import ru.mirea.movingcompany.belov.repositories.InMemoryMovingOrdrerRepository;
import ru.mirea.movingcompany.belov.services.MovingOrderService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.lineSeparator().equals("\r\n") ? System.in : System.in);

    private static final ClientService clientService =
            new ClientService(new InMemoryClientRepository());

    private static final EmployeeService employeeService =
            new EmployeeService(new InMemoryEmplyeeRepository());

    private static final MovingOrderService orderService =
            new MovingOrderService(new InMemoryMovingOrdrerRepository());

    public static void main(String[] args) {
        initSampleData();

        System.out.println("==================================================");
        System.out.println("   Добро пожаловать в систему компании ReloGo!   ");
        System.out.println("==================================================");

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Выберите пункт меню: ");

            switch (choice) {
                case 1 -> clientMenu();
                case 2 -> employeeMenu();
                case 3 -> orderMenu();
                case 0 -> {
                    System.out.println("Выход из системы. До свидания!");
                    running = false;
                }
                default -> System.out.println("Неверный пункт меню, попробуйте снова.");
            }
        }
    }

    private static void initSampleData() {
        clientService.createClient(new ClientModel(1, "Алексей Смирнов", "+79991112233", "alex@mail.ru", ClientStatus.ACTIVE));
        clientService.createClient(new ClientModel(2, "Мария Иванова", "+79994445566", "maria@gmail.com", ClientStatus.NEW));

        employeeService.createEmployee(new EmolyeeModel(1, "Иван Кузнецов", "+79001234567", EmployeeRoles.FOREMAN, 85000));
        employeeService.createEmployee(new EmolyeeModel(2, "Сергей Морозов", "+79007654321", EmployeeRoles.DRIVER, 65000));
        employeeService.createEmployee(new EmolyeeModel(3, "Дмитрий Волков", "+79005556677", EmployeeRoles.LOADER, 50000));

        orderService.createMovingOrder(new MovingOrderModel(1, 1, 1, 350, "ул. Ленина, д. 10", "пр. Мира, д. 25", MovingOrderStatus.IN_PROCESSING));
    }

    private static void printMainMenu() {
        System.out.println("\n--- ГЛАВНОЕ МЕНЮ ---");
        System.out.println("1. Управление клиентами (Client)");
        System.out.println("2. Управление сотрудниками (Employee)");
        System.out.println("3. Управление заказами на переезд (MovingOrder)");
        System.out.println("0. Завершить работу");
    }

    // =========================================================================
    // Меню клиентов
    // =========================================================================
    private static void clientMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- УПРАВЛЕНИЕ КЛИЕНТАМИ ---");
            System.out.println("1. Показать всех клиентов");
            System.out.println("2. Найти клиента по ID");
            System.out.println("3. Найти клиентов по статусу");
            System.out.println("4. Добавить нового клиента");
            System.out.println("5. Изменить статус клиента");
            System.out.println("6. Удалить клиента");
            System.out.println("0. Назад в главное меню");

            int choice = readInt("Выберите действие: ");
            switch (choice) {
                case 1 -> printClients(clientService.getAllClients());
                case 2 -> {
                    int id = readInt("Введите ID клиента: ");
                    Optional<ClientModel> client = clientService.getClientById(id);
                    client.ifPresentOrElse(
                            Main::printClientDetails,
                            () -> System.out.println("Клиент с ID " + id + " не найден.")
                    );
                }
                case 3 -> {
                    ClientStatus status = chooseClientStatus();
                    printClients(clientService.getClientsByStatus(status));
                }
                case 4 -> addClient();
                case 5 -> updateClientStatus();
                case 6 -> deleteClient();
                case 0 -> back = true;
                default -> System.out.println("Неверный выбор.");
            }
        }
    }

    private static void printClients(List<ClientModel> clients) {
        if (clients.isEmpty()) {
            System.out.println("Список клиентов пуст.");
            return;
        }
        System.out.println("\nID | ФИО | Телефон | Email | Статус");
        System.out.println("--------------------------------------------------");
        for (ClientModel c : clients) {
            System.out.printf("%d | %s | %s | %s | %s%n",
                    c.getId(), c.getName(), c.getPhone(), c.getEmail(), c.getStatus());
        }
    }

    private static void printClientDetails(ClientModel c) {
        System.out.println("Информация о клиенте:");
        System.out.println("  ID:      " + c.getId());
        System.out.println("  ФИО:     " + c.getName());
        System.out.println("  Телефон: " + c.getPhone());
        System.out.println("  Email:   " + c.getEmail());
        System.out.println("  Статус:  " + c.getStatus());
    }

    private static void addClient() {
        try {
            int id = readInt("Введите ID клиента: ");
            String name = readString("Введите ФИО клиента: ");
            String phone = readString("Введите телефон: ");
            String email = readString("Введите email: ");
            ClientStatus status = chooseClientStatus();

            ClientModel client = new ClientModel(id, name, phone, email, status);
            clientService.createClient(client);
            System.out.println("Клиент успешно добавлен!");
        } catch (Exception e) {
            System.out.println("Ошибка при создании клиента: " + e.getMessage());
        }
    }

    private static void updateClientStatus() {
        int id = readInt("Введите ID клиента: ");
        ClientStatus newStatus = chooseClientStatus();
        ClientModel updated = clientService.updateStatus(id, newStatus);
        if (updated != null) {
            System.out.println("Статус клиента успешно обновлен на: " + updated.getStatus());
        } else {
            System.out.println("Клиент с ID " + id + " не найден.");
        }
    }

    private static void deleteClient() {
        try {
            int id = readInt("Введите ID клиента для удаления: ");
            ClientModel deleted = clientService.deleteClientById(id);
            System.out.println("Клиент " + deleted.getName() + " (ID: " + id + ") успешно удален.");
        } catch (Exception e) {
            System.out.println("Ошибка при удалении клиента: " + e.getMessage());
        }
    }

    private static ClientStatus chooseClientStatus() {
        System.out.println("Выберите статус клиента:");
        ClientStatus[] statuses = ClientStatus.values();
        for (int i = 0; i < statuses.length; i++) {
            System.out.printf("%d. %s (%s)%n", i + 1, statuses[i].name(), statuses[i].getStatusName());
        }
        while (true) {
            int idx = readInt("Номер: ") - 1;
            if (idx >= 0 && idx < statuses.length) {
                return statuses[idx];
            }
            System.out.println("Некорректный номер статуса.");
        }
    }

    // =========================================================================
    // Меню сотрудников
    // =========================================================================
    private static void employeeMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- УПРАВЛЕНИЕ СОТРУДНИКАМИ ---");
            System.out.println("1. Показать всех сотрудников");
            System.out.println("2. Найти сотрудника по ID");
            System.out.println("3. Найти сотрудников по роли");
            System.out.println("4. Добавить нового сотрудника");
            System.out.println("5. Изменить роль сотрудника");
            System.out.println("6. Удалить сотрудника");
            System.out.println("0. Назад в главное меню");

            int choice = readInt("Выберите действие: ");
            switch (choice) {
                case 1 -> printEmployees(employeeService.getAllEmployees());
                case 2 -> {
                    int id = readInt("Введите ID сотрудника: ");
                    Optional<EmolyeeModel> emp = employeeService.getEmployeeById(id);
                    emp.ifPresentOrElse(
                            Main::printEmployeeDetails,
                            () -> System.out.println("Сотрудник с ID " + id + " не найден.")
                    );
                }
                case 3 -> {
                    EmployeeRoles role = chooseEmployeeRole();
                    printEmployees(employeeService.getEmployeesByRole(role));
                }
                case 4 -> addEmployee();
                case 5 -> updateEmployeeRole();
                case 6 -> deleteEmployee();
                case 0 -> back = true;
                default -> System.out.println("Неверный выбор.");
            }
        }
    }

    private static void printEmployees(List<EmolyeeModel> employees) {
        if (employees.isEmpty()) {
            System.out.println("Список сотрудников пуст.");
            return;
        }
        System.out.println("\nID | ФИО | Телефон | Должность | Зарплата (руб.)");
        System.out.println("---------------------------------------------------------");
        for (EmolyeeModel e : employees) {
            System.out.printf("%d | %s | %s | %s | %d%n",
                    e.getId(), e.getFullName(), e.getPhone(), e.getRole().getRoleName(), e.getSalary());
        }
    }

    private static void printEmployeeDetails(EmolyeeModel e) {
        System.out.println("Информация о сотруднике:");
        System.out.println("  ID:        " + e.getId());
        System.out.println("  ФИО:       " + e.getFullName());
        System.out.println("  Телефон:   " + e.getPhone());
        System.out.println("  Должность: " + e.getRole() + " (" + e.getRole().getRoleName() + ")");
        System.out.println("  Зарплата:  " + e.getSalary() + " руб.");
    }

    private static void addEmployee() {
        try {
            int id = readInt("Введите ID сотрудника: ");
            String name = readString("Введите ФИО сотрудника: ");
            String phone = readString("Введите телефон: ");
            EmployeeRoles role = chooseEmployeeRole();
            int salary = readInt("Введите зарплату (руб): ");

            EmolyeeModel emp = new EmolyeeModel(id, name, phone, role, salary);
            employeeService.createEmployee(emp);
            System.out.println("Сотрудник успешно добавлен!");
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении сотрудника: " + e.getMessage());
        }
    }

    private static void updateEmployeeRole() {
        int id = readInt("Введите ID сотрудника: ");
        EmployeeRoles newRole = chooseEmployeeRole();
        EmolyeeModel updated = employeeService.updateRole(id, newRole);
        if (updated != null) {
            System.out.println("Должность сотрудника обновлена на: " + updated.getRole().getRoleName());
        } else {
            System.out.println("Сотрудник с ID " + id + " не найден.");
        }
    }

    private static void deleteEmployee() {
        try {
            int id = readInt("Введите ID сотрудника для удаления: ");
            EmolyeeModel deleted = employeeService.deleteEmployeeById(id);
            System.out.println("Сотрудник " + deleted.getFullName() + " (ID: " + id + ") успешно удален.");
        } catch (Exception e) {
            System.out.println("Ошибка при удалении сотрудника: " + e.getMessage());
        }
    }

    private static EmployeeRoles chooseEmployeeRole() {
        System.out.println("Выберите роль сотрудника:");
        EmployeeRoles[] roles = EmployeeRoles.values();
        for (int i = 0; i < roles.length; i++) {
            System.out.printf("%d. %s (%s)%n", i + 1, roles[i].name(), roles[i].getRoleName());
        }
        while (true) {
            int idx = readInt("Номер: ") - 1;
            if (idx >= 0 && idx < roles.length) {
                return roles[idx];
            }
            System.out.println("Некорректный номер роли.");
        }
    }

    // =========================================================================
    // Меню заказов на переезд
    // =========================================================================
    private static void orderMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- УПРАВЛЕНИЕ ЗАКАЗАМИ НА ПЕРЕЕЗД ---");
            System.out.println("1. Показать все заказы");
            System.out.println("2. Найти заказ по ID");
            System.out.println("3. Найти заказы по статусу");
            System.out.println("4. Найти заказы по ID клиента");
            System.out.println("5. Найти заказы по ID бригадира");
            System.out.println("6. Создать новый заказ");
            System.out.println("7. Изменить статус заказа");
            System.out.println("8. Удалить заказ");
            System.out.println("0. Назад в главное меню");

            int choice = readInt("Выберите действие: ");
            switch (choice) {
                case 1 -> printOrders(orderService.getAllMovingOrder());
                case 2 -> {
                    int id = readInt("Введите ID заказа: ");
                    Optional<MovingOrderModel> order = orderService.getMovingOrderById(id);
                    order.ifPresentOrElse(
                            Main::printOrderDetails,
                            () -> System.out.println("Заказ с ID " + id + " не найден.")
                    );
                }
                case 3 -> {
                    MovingOrderStatus status = chooseOrderStatus();
                    printOrders(orderService.getMovingOrderByStatus(status));
                }
                case 4 -> {
                    int clientId = readInt("Введите ID клиента: ");
                    printOrders(orderService.getMovingOrderByIdClient(clientId));
                }
                case 5 -> {
                    int foremanId = readInt("Введите ID бригадира: ");
                    printOrders(orderService.getMovingOrderByIdForeman(foremanId));
                }
                case 6 -> addOrder();
                case 7 -> updateOrderStatus();
                case 8 -> deleteOrder();
                case 0 -> back = true;
                default -> System.out.println("Неверный выбор.");
            }
        }
    }

    private static void printOrders(List<MovingOrderModel> orders) {
        if (orders.isEmpty()) {
            System.out.println("Список заказов пуст.");
            return;
        }
        System.out.println("\nID | ID Клиента | ID Бригадира | Масса (кг) | Адрес отправки -> Адрес назначения | Статус");
        System.out.println("-----------------------------------------------------------------------------------------");
        for (MovingOrderModel o : orders) {
            System.out.printf("%d | %d | %d | %d кг | %s -> %s | %s%n",
                    o.getId(), o.getIdClient(), o.getIdForeman(), o.getMass(),
                    o.getStartAddress(), o.getEndAddress(), o.getStatus());
        }
    }

    private static void printOrderDetails(MovingOrderModel o) {
        System.out.println("Информация о заказе:");
        System.out.println("  ID заказа:        " + o.getId());
        System.out.println("  ID клиента:       " + o.getIdClient());
        System.out.println("  ID бригадира:     " + o.getIdForeman());
        System.out.println("  Масса груза:      " + o.getMass() + " кг");
        System.out.println("  Адрес отправки:   " + o.getStartAddress());
        System.out.println("  Адрес назначения: " + o.getEndAddress());
        System.out.println("  Статус:           " + o.getStatus());
    }

    private static void addOrder() {
        try {
            int id = readInt("Введите ID нового заказа: ");
            int clientId = readInt("Введите ID клиента: ");
            int foremanId = readInt("Введите ID бригадира (сотрудника): ");
            int mass = readInt("Введите примерную массу груза (кг): ");
            String startAddress = readString("Введите начальный адрес: ");
            String endAddress = readString("Введите конечный адрес: ");
            MovingOrderStatus status = chooseOrderStatus();

            MovingOrderModel order = new MovingOrderModel(id, clientId, foremanId, mass, startAddress, endAddress, status);
            orderService.createMovingOrder(order);
            System.out.println("Заказ успешно создан!");
        } catch (Exception e) {
            System.out.println("Ошибка при создании заказа: " + e.getMessage());
        }
    }

    private static void updateOrderStatus() {
        int id = readInt("Введите ID заказа: ");
        MovingOrderStatus newStatus = chooseOrderStatus();
        MovingOrderModel updated = orderService.updateStatus(id, newStatus);
        if (updated != null) {
            System.out.println("Статус заказа успешно обновлен на: " + updated.getStatus());
        } else {
            System.out.println("Заказ с ID " + id + " не найден.");
        }
    }

    private static void deleteOrder() {
        try {
            int id = readInt("Введите ID заказа для удаления: ");
            MovingOrderModel deleted = orderService.deleteMovingOrderById(id);
            System.out.println("Заказ с ID " + id + " (" + deleted.getStartAddress() + " -> " + deleted.getEndAddress() + ") удален.");
        } catch (Exception e) {
            System.out.println("Ошибка при удалении заказа: " + e.getMessage());
        }
    }

    private static MovingOrderStatus chooseOrderStatus() {
        System.out.println("Выберите статус заказа:");
        MovingOrderStatus[] statuses = MovingOrderStatus.values();
        for (int i = 0; i < statuses.length; i++) {
            System.out.printf("%d. %s%n", i + 1, statuses[i].name());
        }
        while (true) {
            int idx = readInt("Номер: ") - 1;
            if (idx >= 0 && idx < statuses.length) {
                return statuses[idx];
            }
            System.out.println("Некорректный номер статуса.");
        }
    }

    // =========================================================================
    // Вспомогательные методы ввода
    // =========================================================================
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Пожалуйста, введите целое число.");
            }
        }
    }

    private static String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Поле не может быть пустым.");
        }
    }
}