package fr.durga.ecommerce.service;

import fr.durga.ecommerce.common.MessageStrings;
import fr.durga.ecommerce.dto.ResponseDto;
import fr.durga.ecommerce.dto.user.SignInDto;
import fr.durga.ecommerce.dto.user.SignInResponseDto;
import fr.durga.ecommerce.dto.user.SignupDto;
import fr.durga.ecommerce.dto.user.UserCreateDto;
import fr.durga.ecommerce.enums.ResponseStatus;
import fr.durga.ecommerce.enums.Role;
import fr.durga.ecommerce.exceptions.AuthFailException;
import fr.durga.ecommerce.exceptions.CustomException;
import fr.durga.ecommerce.helper.Helper;
import fr.durga.ecommerce.model.AuthenticationToken;
import fr.durga.ecommerce.model.User;
import fr.durga.ecommerce.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import jakarta.xml.bind.DatatypeConverter;

import static fr.durga.ecommerce.common.MessageStrings.USER_CREATED;


@Service
    public class UserService {

        @Autowired
        UserRepository userRepository;

        @Autowired
        AuthenticationService authenticationService;

        Logger logger = LoggerFactory.getLogger(UserService.class);


        public ResponseDto signUp(SignupDto signupDto)  throws CustomException {
            // Check to see if the current email address has already been registered.
            if (Helper.notNull(userRepository.findByEmail(signupDto.getEmail()))) {
                // If the email address has been registered then throw an exception.
                throw new CustomException("User already exists");
            }
            // first encrypt the password
            String encryptedPassword = signupDto.getPassword();
            try {
                encryptedPassword = hashPassword(signupDto.getPassword());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                logger.error("hashing password failed {}", e.getMessage());
            }


            User user = new User(signupDto.getFirstName(), signupDto.getLastName(), signupDto.getEmail(),  encryptedPassword,Role.user );

            User createdUser;
            try {
                // save the User
                createdUser = userRepository.save(user);
                // generate token for user
                final AuthenticationToken authenticationToken = new AuthenticationToken(createdUser);
                // save token in database
                authenticationService.saveConfirmationToken(authenticationToken);
                // success in creating
                return new ResponseDto(ResponseStatus.success.toString(), USER_CREATED);
            } catch (Exception e) {
                // handle signup error
                throw new CustomException(e.getMessage());
            }
        }

        public SignInResponseDto signIn(SignInDto signInDto) throws CustomException {
            // first find User by email
            User user = userRepository.findByEmail(signInDto.getEmail());
            if(!Helper.notNull(user)){
                throw  new AuthFailException("user not present");
            }
            try {
                // check if password is right
                if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))){
                    // passowrd doesnot match
                    throw  new AuthFailException(MessageStrings.WRONG_PASSWORD);
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                logger.error("hashing password failed {}", e.getMessage());
                throw new CustomException(e.getMessage());
            }

            AuthenticationToken token = authenticationService.getToken(user);

            if(!Helper.notNull(token)) {
                // token not present
                throw new CustomException("token not present");
            }

            return new SignInResponseDto ("success", token.getToken());
        }


        String hashPassword(String password) throws NoSuchAlgorithmException {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
            return myHash;
        }

        public ResponseDto createUser(String token, UserCreateDto userCreateDto) throws CustomException, AuthFailException {
            User creatingUser = authenticationService.getUser(token);
            if (!canCrudUser(creatingUser.getRole())) {
                // user can't create new user
                throw  new AuthFailException(MessageStrings.USER_NOT_PERMITTED);
            }
            String encryptedPassword = userCreateDto.getPassword();
            try {
                encryptedPassword = hashPassword(userCreateDto.getPassword());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                logger.error("hashing password failed {}", e.getMessage());
            }

            User user = new User(userCreateDto.getFirstName(), userCreateDto.getLastName(), userCreateDto.getEmail(),encryptedPassword, userCreateDto.getRole()  );
            User createdUser;
            try {
                createdUser = userRepository.save(user);
                final AuthenticationToken authenticationToken = new AuthenticationToken(createdUser);
                authenticationService.saveConfirmationToken(authenticationToken);
                return new ResponseDto(ResponseStatus.success.toString(), USER_CREATED);
            } catch (Exception e) {
                // handle user creation fail error
                throw new CustomException(e.getMessage());
            }

        }

        boolean canCrudUser(Role role) {
            if (role == Role.admin || role == Role.manager) {
                return true;
            }
            return false;
        }

        boolean canCrudUser(User userUpdating, Integer userIdBeingUpdated) {
            Role role = userUpdating.getRole();
            // admin and manager can crud any user
            if (role == Role.admin || role == Role.manager) {
                return true;
            }
            // user can update his own record, but not his role
            if (role == Role.user && userUpdating.getId() == userIdBeingUpdated) {
                return true;
            }
            return false;
        }
    }


