package fr.durga.ecommerce.repository;

import fr.durga.ecommerce.model.Order;
import fr.durga.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository  extends JpaRepository<Order, Integer> {
    List<Order> findAllByUserOrderByCreatedDateDesc(User user);

    int[] arr = new int[]{0};

    
}