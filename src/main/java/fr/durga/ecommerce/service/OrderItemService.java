package fr.durga.ecommerce.service;

import fr.durga.ecommerce.model.OrderItem;
import fr.durga.ecommerce.repository.OrderItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemsRepository;

    public void addOrderedProducts(OrderItem orderItem) {
        orderItemsRepository.save(orderItem);
    }


}
