package com.ausy_technologies.demospring.Service;

import com.ausy_technologies.demospring.Model.DAO.Role;
import com.ausy_technologies.demospring.Model.DAO.User;
import com.ausy_technologies.demospring.Model.DTO.UserDto;
import com.ausy_technologies.demospring.Repository.RoleRepository;
import com.ausy_technologies.demospring.Repository.UserRepository;
import com.ausy_technologies.demospring.RestErrorHandling.CustomException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.logging.Level;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;


    public Role saveRole(Role role) {


        return this.roleRepository.save(role);
    }


    public User saveUser(User user) {


        return this.userRepository.save(user);
    }

    public User saveUser2(User user ,int idRole)
    {

       Role role= this.roleRepository.findById(idRole).get();

       List<Role> roleList =new ArrayList<>();
       roleList.add(role);

       if(role!=null) {

           user.setRoleList(roleList);
           return this.userRepository.save(user);
       }
       else
       {
           throw new RuntimeException("Role not found");
       }


    }


    public User saveUser3(  User user ,List<Role> roleList)
    {
        user.setRoleList(roleList);
        return this.userRepository.save(user);

    }


    public User findUserById(int id) {

        if(!this.userRepository.findById(id).isPresent()){
            CustomException customException = new CustomException(HttpStatus.NOT_FOUND, "User not found!");
            CustomException.DisplayException(customException, Level.SEVERE);
            throw customException;
        }
        return this.userRepository.findById(id).get();
    }

    public Role findRoleById(int id)
    {
        if(!this.roleRepository.findById(id).isPresent()){
            CustomException customException = new CustomException(HttpStatus.NOT_FOUND, "Role not found!");
            CustomException.DisplayException(customException, Level.SEVERE);
            throw customException;
        }
        return this.roleRepository.findById(id).get();

    }

    public List<Role> findAllRoles()
    {
        List<Role> roleList = this.roleRepository.findAll();
        if(roleList.size()==0){
            CustomException customException = new CustomException(HttpStatus.NO_CONTENT, "Content not found!");
            CustomException.DisplayException(customException, Level.WARNING);
            throw customException;
        }
        return roleList;
    }


    public List<User> findAllUsers()
    {
        List<User> userList = this.userRepository.findAll();
        if(userList.size()==0){
            CustomException customException = new CustomException(HttpStatus.NO_CONTENT, "Content not found!");
            CustomException.DisplayException(customException, Level.WARNING);
            throw customException;
        }
        return userList;
    }


    public void deleteUserById(int id){

        if(!this.userRepository.findById(id).isPresent()){
            CustomException customException = new CustomException(HttpStatus.NOT_FOUND, "User not found!");
            CustomException.DisplayException(customException, Level.SEVERE);
            throw customException;
        }
        this.userRepository.deleteById(id);
    }


    public void deleteRoleById(int id){    //in acest fel dispar si inregistrarile din user_role

        if(!this.roleRepository.findById(id).isPresent()){
            CustomException customException = new CustomException(HttpStatus.NOT_FOUND, "Role not found!");
            CustomException.DisplayException(customException, Level.SEVERE);
            throw customException;
        }
        Role role = this.roleRepository.findById(id).get();
        List<User> users = this.userRepository.findAll();
        for(User u: users){
            List<Role> roles = u.getRoleList();
            roles.remove(role);
            u.setRoleList(roles);
        }
        this.roleRepository.deleteById(id);
    }


    public Role updateRole(Role role, int id){

        Optional<Role> opt = this.roleRepository.findById(id);

        if(!opt.isPresent()){
            CustomException customException = new CustomException(HttpStatus.NOT_FOUND, "Role not found!");
            CustomException.DisplayException(customException, Level.SEVERE);
            throw customException;
        }

        Role existingRole = opt.get();
        role.setId(id);
        copyNonNullProperties(role, existingRole);
        return this.roleRepository.save(existingRole);

    }

    public User updateUser(User user, int id){

        Optional<User> opt = this.userRepository.findById(id);

        if(!opt.isPresent()){
            CustomException customException = new CustomException(HttpStatus.NOT_FOUND, "User not found!");
            CustomException.DisplayException(customException, Level.SEVERE);
            throw customException;
        }
        User existingUser = opt.get();
        user.setId(id);
        copyNonNullProperties(user, existingUser);
        return this.userRepository.save(existingUser);

    }

    //functie ce ajuta la copierea proprietatilor nenule
    //astfel, la update, putem scire in body doar campul care trebuie sa fie actualizat
    private String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set emptyNames = new HashSet();
        for(java.beans.PropertyDescriptor pd : pds) {

            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return (String[]) emptyNames.toArray(result);
    }

    public void copyNonNullProperties(Object source, Object destination){
        BeanUtils.copyProperties(source, destination,
                getNullPropertyNames(source));
    }



    public UserDto findUserDtoById(int id) {

        Optional<User> opt = this.userRepository.findById(id);

        if(!opt.isPresent()){
            CustomException customException = new CustomException(HttpStatus.NOT_FOUND, "UserDTO not found!");
            CustomException.DisplayException(customException, Level.SEVERE);
            throw customException;
        }

        User user = opt.get();
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(user, UserDto.class);

        List<String> roleList = new ArrayList<>();
        for(Role role: user.getRoleList()){
            roleList.add(role.getName());
        }
        userDto.setRoleList(roleList);
        return userDto;
    }

    public List<UserDto> findAllUsersDto(){

        List<User> userList = this.userRepository.findAll();

        if(userList.size()==0){
            CustomException customException = new CustomException(HttpStatus.NO_CONTENT, "Content not found!");
            CustomException.DisplayException(customException, Level.WARNING);
            throw customException;
        }

        List<UserDto> userDtoList = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        for(User user: userList){
            List<String> roleList = new ArrayList<>();
            UserDto userDto = modelMapper.map(user, UserDto.class);
            for(Role role: user.getRoleList()){
                roleList.add(role.getName());
            }

            userDto.setRoleList(roleList);
            userDtoList.add(userDto);

        }
        return userDtoList;
    }


}
