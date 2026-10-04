package ru.mirea.movingcompany.abdykalykov.repositories;

import ru.mirea.movingcompany.abdykalykov.enums.ClientStatus;
import ru.mirea.movingcompany.abdykalykov.models.ClientModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryClientRepository implements ClientRepository {

    private List<ClientModel> clients = new ArrayList<>();
    public InMemoryClientRepository() {}

    @Override
    public List<ClientModel> getAllClients() {
        return List.copyOf(clients);
    }

    @Override
    public Optional<ClientModel> getClientById(int id) {
        return clients.stream().filter(c -> c.getId() == id).findFirst();
    }

    @Override
    public List<ClientModel> getClientsByStatus(ClientStatus status) {
        return clients.stream().filter(c -> c.getStatus() == status).toList();
    }

    @Override
    public Optional<ClientModel> getClientByPhone(String phone) {
        return clients.stream().filter(c -> c.getPhone() != null && c.getPhone().equals(phone)).findFirst();
    }

    @Override
    public List<ClientModel> getClientsByName(String name) {
        return clients.stream().filter(c -> c.getName() != null && c.getName().equalsIgnoreCase(name)).toList();
    }

    @Override
    public boolean createClient(ClientModel client) {
        if (!clients.stream().filter(c -> c.getId() == client.getId()).toList().isEmpty()) {
            throw new IllegalArgumentException("Клиент с данным ID уже существует!");
        }
        return clients.add(client);
    }

    @Override
    public ClientModel updateClient(ClientModel client) {
        ClientModel clientModel = clients.stream().filter(c -> c.getId() == client.getId()).findFirst().orElse(null);

        if (clientModel == null) {
            throw new IllegalArgumentException("Клиент с данным ID не существует!");
        }

        int i = clients.indexOf(clientModel);
        return clients.set(i, client);
    }

    @Override
    public ClientModel deleteClientById(int id) {
        ClientModel clientModel = clients.stream().filter(c -> c.getId() == id).findFirst().orElse(null);

        if (clientModel == null) {
            throw new IllegalArgumentException("Клиент с данным ID не существует!");
        }

        int i = clients.indexOf(clientModel);
        return clients.remove(i);
    }
}
