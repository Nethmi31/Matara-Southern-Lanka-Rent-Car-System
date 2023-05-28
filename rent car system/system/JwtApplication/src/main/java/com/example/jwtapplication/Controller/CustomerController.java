package com.example.jwtapplication.Controller;

import com.example.jwtapplication.Entity.Customer;
import com.example.jwtapplication.Entity.Damage;
import com.example.jwtapplication.Service.CustomerServiceImpl;
import com.example.jwtapplication.Service.DamageServiceImpl;
import com.example.jwtapplication.Util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("/customer")
public class CustomerController {


    @Autowired
    private CustomerServiceImpl customerService;


    @PostMapping("/save")
    public ResponseEntity<String> saveCustomer(Customer customer, @RequestParam("image") MultipartFile multipartFile) {
        try {
            if (!multipartFile.isEmpty()) {
                String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                customer.setNicimage(filename);
                Customer saveCustomer=customerService.saveCustomer(customer);
                String uploadPath = "images/customer" + saveCustomer.getId();

                FileUploadUtil.saveFile(uploadPath, filename, multipartFile);
            } else {
                if (customer.getImageFileNic().isEmpty()) {
                    customer.setImageFileNic(null);
                }
                customerService.saveCustomer(customer);
            }

            return ResponseEntity.ok("customer saved successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while saving customer: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }



    @GetMapping("/view")
    @ResponseBody
    public ResponseEntity<List<Customer>> listCustomer() {
        Iterable<Customer> customerList = customerService.getAllCustomer();
        return ResponseEntity.ok((List<Customer>) customerList);
    }


    @GetMapping("/get/{id}")
    @ResponseBody
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        try {
           customerService.deleteCustomerById(id);
            return ResponseEntity.ok("Student deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while deleting student: " + e.getMessage());
        }
    }




}
