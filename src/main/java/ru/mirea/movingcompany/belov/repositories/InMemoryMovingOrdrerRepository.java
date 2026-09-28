package ru.mirea.movingcompany.belov.repositories;

import ru.mirea.movingcompany.belov.enums.MovingOrderStatus;
import ru.mirea.movingcompany.belov.models.MovingOrderModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryMovingOrdrerRepository implements MovingOrderRepository {

    private List<MovingOrderModel> orders = new ArrayList<>();
    public InMemoryMovingOrdrerRepository() {}

    @Override
    public List<MovingOrderModel> getAllMovingOrders() {
        return List.copyOf(orders);
    }

    @Override
    public Optional<MovingOrderModel> getMovingOrderById(int id)
    {
        return orders.stream().filter(o -> o.getId() == id).findFirst();
    }

    @Override
    public List<MovingOrderModel> getMovingOrderByStatus(MovingOrderStatus status) {
        return orders.stream().filter(o -> o.getStatus().equals(status)).toList();
    }

    @Override
    public List<MovingOrderModel> getMovingOrderByMass(int mass) {
        return orders.stream().filter(o -> o.getMass() == mass).toList();
    }

    @Override
    public List<MovingOrderModel> getMovingOrderByIdForeman (int idForeman) {
        return orders.stream().filter(o -> o.getIdForeman() == idForeman).toList();
    }

    @Override
    public List<MovingOrderModel> getMovingOrderByIdClient (int idClient) {
        return orders.stream().filter(o -> o.getIdClient() == idClient).toList();
    }

    @Override
    public boolean createMovingOrder(MovingOrderModel movingOrder) {
        if (!orders.stream().filter(o -> o.getId() == movingOrder.getId()).toList().isEmpty()) {
            throw new IllegalArgumentException("Заказ с данным ID уже существует!");
        }
        return orders.add(movingOrder);
    }

    @Override
    public MovingOrderModel updateMovingOrder(MovingOrderModel movingOrder) {
        MovingOrderModel movingOrderModel = orders.stream().filter(o -> o.getId() == movingOrder.getId()).findFirst().orElse(null);

        if(movingOrderModel == null) {
            throw new IllegalArgumentException("Заказ с данным ID не существует!");
        }

        int i = orders.indexOf(movingOrderModel);
        return orders.set(i, movingOrder);
    }

    @Override
    public MovingOrderModel deleteMovingOrderById(int id) {
        MovingOrderModel movingOrderModel = orders.stream().filter(o -> o.getId() == id).findFirst().orElse(null);

        if(movingOrderModel == null) {
            throw new IllegalArgumentException("Заказ с данным ID не существует!");
        }

        int i = orders.indexOf(movingOrderModel);
        return orders.remove(i);
    }



}
