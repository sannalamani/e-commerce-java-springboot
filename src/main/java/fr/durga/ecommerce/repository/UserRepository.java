package fr.durga.ecommerce.repository;

import fr.durga.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {


    User findByEmail(String email);
}
