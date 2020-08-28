package com.ausy_technologies.demospring.Controller;

import com.ausy_technologies.demospring.Model.DAO.Role;
import com.ausy_technologies.demospring.Model.DAO.User;
import com.ausy_technologies.demospring.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;



    @PostMapping("/addRole")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {

        Role roleAdded = this.userService.saveRole(role);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Responded", "addRole");
        return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(roleAdded);
    }




    @PostMapping("/addUser")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        User userAdded = this.userService.saveUser(user);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Responded", "addUser");
        return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(userAdded);
    }

    @PostMapping("/addUser2/{idRole}")
    public ResponseEntity<User> saveUser2(@RequestBody User user, @PathVariable int idRole)
    {
        User userAdded = this.userService.saveUser2(user, idRole);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Responded", "addUser2");
        return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(userAdded);

    }

    @PostMapping("/addUser3/{roleList}")
    public ResponseEntity<User> saveUser3(@RequestBody User user , @PathVariable List<Role> roleList)
    {
        User userAdded = this.userService.saveUser3(user, roleList);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Responded", "addUser3");
        return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(userAdded);
    }

    @GetMapping("/findRoleBy/{id}")
    public ResponseEntity<Role> findRoleById(@PathVariable int id)
    {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Responded", "findRole");
        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(this.userService.findRoleById(id));
    }

    @GetMapping("/findAllRoles")
    public ResponseEntity<List<Role>> findAllRoles()
    {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Responded", "findAllRoles");
        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(this.userService.findAllRoles());
    }

    @GetMapping("/findUserBy/{id}")
    public ResponseEntity<User> findUserById(@PathVariable int id){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Responded", "findUser");
        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(this.userService.findUserById(id));

    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<User>> findAllUsers()
    {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Responded", "findAllUsers");
        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(this.userService.findAllUsers());
    }


    @DeleteMapping("/deleteUserById/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id)
    {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Responded", "deleteUser");
        this.userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).headers(httpHeaders).body("Delete successful");

    }

    @DeleteMapping("/deleteRoleById/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable int id){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Responded", "deleteRole");
        this.userService.deleteRoleById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).headers(httpHeaders).body("Delete successful");
    }



    @PutMapping("updateRole/{id}")
    public ResponseEntity<Role> updateRole(@RequestBody Role role, @PathVariable int id){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Responded", "updateRole");
        return ResponseEntity.status(HttpStatus.ACCEPTED).headers(httpHeaders).body(this.userService.updateRole(role, id));
    }

    @PutMapping("updateUser/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable int id){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Responded", "updateUser");
        return ResponseEntity.status(HttpStatus.ACCEPTED).headers(httpHeaders).body(this.userService.updateUser(user, id));
    }

}
