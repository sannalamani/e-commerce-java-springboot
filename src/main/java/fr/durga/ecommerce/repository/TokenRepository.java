package fr.durga.ecommerce.repository;

import fr.durga.ecommerce.model.AuthenticationToken;
import fr.durga.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<AuthenticationToken,Integer> {


    AuthenticationToken findTokenByUser(User user);

    AuthenticationToken findTokenByToken(String token);
}
