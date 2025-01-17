package sit.int221.oasip.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.oasip.configs.JwtTokenUtil;
import sit.int221.oasip.dto.userdto.*;
import sit.int221.oasip.entities.Roles;
import sit.int221.oasip.entities.User;
import sit.int221.oasip.repositories.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapperService listMapper;
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtUtil;

    @Value("${app.upload.dir:${user.home}}")
    public String uploadDir;

    //Method List User All
    public List<UserDtoList> getUsersAll() {
        List<User> userList = userRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        return listMapper.mapList(userList, UserDtoList.class, modelMapper);
    }

    //Method Show Detail User by ID
    public UserDtoDetail getUsertById(Integer id){
        User user = userRepository.findById(id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User ID: "+id+" does not exist"));
        return modelMapper.map(user, UserDtoDetail.class);
    }

    //Method Create User
    public User save(UserDtoCreate newUser){
        if(newUser.getRole() == null){
            newUser.setRole(String.valueOf(Roles.student));
        }
        newUser.setPassword(passwordService.securePassword(newUser.getPassword()));

        User user = modelMapper.map(newUser, User.class);
        User saveUser = userRepository.saveAndFlush(user);
        saveUser.setPassword("*******");
        return saveUser;
    }

    //Method Edit User
    public UserDtoList edit(UserDtoEdit editUser, Integer userId){
        User user = userRepository.findById(userId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User id "+userId+" does not exist"));

       if (editUser.getName().equals(user.getName() ) && editUser.getEmail().equals(user.getEmail())
                && editUser.getRole().equals(String.valueOf(user.getRole())) ){
            return modelMapper.map(user, UserDtoList.class);
        }

        if(editUser.getName() != null && userRepository.existsByNameAndUserIdNot(editUser.getName(), userId)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is already registered");
        }
        if(editUser.getEmail() != null && userRepository.existsByEmailAndUserIdNot(editUser.getEmail(), userId)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email address is already registered");
        }
        user.setName(editUser.getName());
        user.setEmail(editUser.getEmail());
        user.setRole(editUser.getRole());
        return modelMapper.map(userRepository.saveAndFlush(user), UserDtoList.class);
    }

    public ResponseEntity<?> refresh(HttpServletRequest request){
        String authToken = request.getHeader("Authorization");
        final String token = authToken.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (jwtUtil.canTokenBeRefreshed(token)) {
            String accessToken = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(new JwtRefresh(accessToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
