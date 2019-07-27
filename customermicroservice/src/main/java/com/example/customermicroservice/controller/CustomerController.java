package com.example.customermicroservice.controller;


import com.example.customermicroservice.dto.CustomerCredentialDto;
import com.example.customermicroservice.dto.CustomerDetailsDto;
import com.example.customermicroservice.dto.IdMessageDto;
import com.example.customermicroservice.entity.CustomerCredentials;
import com.example.customermicroservice.entity.CustomerDetail;
import com.example.customermicroservice.service.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utility.Email;
import utility.PasswordEncrupt;
import utility.SendingMail;

@RestController
@CrossOrigin
@RequestMapping("/Customer")
public class
CustomerController {

    @Autowired
    CustomerService customerService;

    //TODO:Null Pointer Exception
    @RequestMapping(value = "/checkLogin", method = RequestMethod.GET)
    public ResponseEntity<?> checkLogin(@RequestParam(value = "email", required = true) String email,
                              @RequestParam(value = "password", required = true) String password) {

        CustomerCredentials customerCredentials = customerService.checkLogin(email, password);
        IdMessageDto idMessageDto = new IdMessageDto();

        if(customerCredentials!=null) {
            idMessageDto.setCustomerId(customerCredentials.getCustomerId());
            idMessageDto.setMessage("Login Success");
            return new ResponseEntity<IdMessageDto>(idMessageDto,HttpStatus.OK);
        }
        idMessageDto.setMessage("Incorrect UserName/Password");

        return new ResponseEntity<IdMessageDto>(idMessageDto,HttpStatus.OK);
    }

    @RequestMapping(value = "/getCustomerDetail", method = RequestMethod.GET)
    public ResponseEntity<?> getCustomerDetails(@RequestParam(value = "cid") Long customerId){

        String message="Customer Detail Does not Exist";
        CustomerDetailsDto customerDetailsDto = new CustomerDetailsDto();
        CustomerDetail customerDetail= customerService.getCustomerDetails(customerId);
        if(customerDetail!=null){
            BeanUtils.copyProperties(customerDetail,customerDetailsDto);
            return new ResponseEntity<CustomerDetailsDto>(customerDetailsDto,HttpStatus.OK);
        }
        return new ResponseEntity<String>(message,HttpStatus.OK);

    }
    @RequestMapping(value = "/addCustomerDetail", method = RequestMethod.POST)
    public ResponseEntity<?> addCustomerDetails(@RequestBody CustomerDetailsDto customerDetailsDto) {
        CustomerDetail customerDetail = new CustomerDetail();
        BeanUtils.copyProperties(customerDetailsDto, customerDetail);

        return new  ResponseEntity<String>(customerService.addCustomerDetails(customerDetail),HttpStatus.OK);
    }



    @RequestMapping(method = RequestMethod.POST, value = "/signUp")
    public ResponseEntity<?> signUp(@RequestBody CustomerCredentialDto customerCredentialDto) {
        PasswordEncrupt passwordEncrupt=new PasswordEncrupt();
        customerCredentialDto.setPassword(passwordEncrupt.hashPassword(customerCredentialDto.getPassword()));
        CustomerCredentials customerCredentials = new CustomerCredentials();
        BeanUtils.copyProperties(customerCredentialDto, customerCredentials);

        return new ResponseEntity<String>(customerService.addCustomer(customerCredentials),HttpStatus.OK);
    }

    @RequestMapping(method=RequestMethod.GET,value="/findCustomerByEmail")
    public ResponseEntity<?> findEmployeeById(@RequestParam(value = "empId", required = false) String email)
    {
        return new ResponseEntity<Boolean>(customerService.checkRegisteredCustomer(email),HttpStatus.OK);
    }

    //TODO:Mail failed to send but correct response generated
    // TODO: Email authentication whether really an email or not
    @RequestMapping(method = RequestMethod.GET,value = "/forgotPassword")
    public ResponseEntity<?> authenticateEmail(@RequestParam(value = "email") String email){


        CustomerCredentials customerCredentials = customerService.authenticateEmail(email);
        IdMessageDto idMessageDto = new IdMessageDto();
        //SendingMail sendingMail = new SendingMail("sidana1997@gmail.com", "Test message", "This is testing");
        Email email1=new Email();
        if(customerCredentials!=null) {
            idMessageDto.setCustomerId(customerCredentials.getCustomerId());
            //sendingMail.run();
            System.out.println(email1.home());
            idMessageDto.setMessage("Email Sent");
            return new ResponseEntity<IdMessageDto>(idMessageDto,HttpStatus.OK);
        }
        idMessageDto.setMessage("Invalid Email");

        return new ResponseEntity<IdMessageDto>(idMessageDto,HttpStatus.OK);
    }




}
