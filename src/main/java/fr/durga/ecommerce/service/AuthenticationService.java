package fr.durga.ecommerce.service;




import fr.durga.ecommerce.common.MessageStrings;
import fr.durga.ecommerce.exceptions.AuthFailException;
import fr.durga.ecommerce.helper.Helper;
import fr.durga.ecommerce.model.AuthenticationToken;
import fr.durga.ecommerce.model.User;
import fr.durga.ecommerce.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    TokenRepository repository;

    public void saveConfirmationToken(AuthenticationToken authenticationToken) {
        repository.save(authenticationToken);
    }

    public AuthenticationToken getToken(User user) {
        return repository.findTokenByUser(user);
    }

    public User getUser(String token) {
        AuthenticationToken authenticationToken = repository.findTokenByToken(token);
        if (Helper.notNull(authenticationToken)) {
            if (Helper.notNull(authenticationToken.getUser())) {
                return authenticationToken.getUser();
            }
        }
        return null;
    }

    public void authenticate(String token) throws AuthFailException {
        if (!Helper.notNull(token)) {
            throw new AuthFailException(MessageStrings.AUTH_TOEKN_NOT_PRESENT);
        }
        if (!Helper.notNull(getUser(token))) {
            throw new AuthFailException(MessageStrings.AUTH_TOEKN_NOT_VALID);
        }
    }
}

