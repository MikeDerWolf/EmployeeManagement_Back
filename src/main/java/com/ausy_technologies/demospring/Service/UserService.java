package com.ausy_technologies.demospring.Service;

import com.ausy_technologies.demospring.Model.DAO.Role;
import com.ausy_technologies.demospring.Model.DAO.User;
import com.ausy_technologies.demospring.Repository.RoleRepository;
import com.ausy_technologies.demospring.Repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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


    public User findUserById(int id){

        return this.userRepository.findById(id);
    }

    public Role findRoleById(int id)
    {
        return this.roleRepository.findById(id).get();

    }

    public List<Role> findAllRoles()
    {
        return this.roleRepository.findAll();
    }


    public List<User> findAllUsers()
    {
        return this.userRepository.findAll();
    }


    public void deleteUserById(int id){

        this.userRepository.deleteById(id);
    }


    public void deleteRoleById(int id){    //in acest fel dispar si inregistrarile din useri_roluri

        Role role = this.roleRepository.findById(id).get();
        List<User> users = this.userRepository.findAll();
        for(User u: users){
            List<Role> roles = u.getRoleList();
            roles.remove(role);
            u.setRoleList(roles);
        }
        this.roleRepository.deleteById(id);
    }


    public Role updateRole(Role role){

        Role existingRole = this.roleRepository.findById(role.getId()).get();
        copyNonNullProperties(role, existingRole);
        return this.roleRepository.save(existingRole);
    }

    public User updateUser(User user){

        User existingUser = this.userRepository.findById(user.getId());
        copyNonNullProperties(user, existingUser);
        return this.userRepository.save(existingUser);
    }


    private String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set emptyNames = new HashSet();
        for(java.beans.PropertyDescriptor pd : pds) {
            //check if value of this property is null then add it to the collection
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




}
