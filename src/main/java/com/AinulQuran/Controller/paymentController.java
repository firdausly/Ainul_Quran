package com.AinulQuran.Controller;


import com.AinulQuran.dto.createBill;
import com.AinulQuran.model.*;
import com.AinulQuran.repository.UserPaymentRepo;
import com.AinulQuran.repository.UserRepository;
import com.AinulQuran.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Controller
public class paymentController {

    @Autowired
    private UserService service;

    @Autowired
    private UserPaymentRepo paymentRepo;

    @Autowired
    private UserRepository userRepo;



    @GetMapping("/payment/createBill")
    public String createBill(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentusername=auth.getName();

        if(paymentRepo.findByUsername(currentusername)!=null){
            userPayment payment=paymentRepo.findByUsername(currentusername);
            String billCode=payment.getBillCode();
            String status=payment.getStatus();

            if(status.equals("success")){
                model.addAttribute("alreadysuccess","");
                return "payment/confirmpay";
            }

            model.addAttribute("billCode",billCode);
            model.addAttribute("alreadyhasBillCode","BillCode already exist");
            return "payment/confirmpay";
        }

        User currentuser=service.findByUsername(currentusername);

        createBill bill=new createBill();
        bill.setBillName("Ainul Quran Paid Plan");
        bill.setBillDescription("Unlock full access of Ainul Quran");
        bill.setBillTo(currentuser.getFirstName()+" "+currentuser.getLastName());
        bill.setBillEmail(currentuser.getEmail());


        model.addAttribute("createBill",bill);

        return "payment/createBill";
    }


    @PostMapping("/payment/sendBill")
    public String sendBill(@ModelAttribute("createBill") @Valid createBill createBill, BindingResult result, Model model) throws JsonProcessingException {


        RestTemplate restTemplate = new RestTemplate();

        String createBillApiUrl="https://dev.toyyibpay.com/index.php/api/createBill";

        createBill.setUserSecretKey("vyn1qq8b-j4kb-r06k-26vo-usvprmpjdk34");
        createBill.setCategoryCode("ukqoiqhf");
        createBill.setBillCallbackUrl("http://localhost:8080/payment/callback");
        createBill.setBillReturnUrl("http://localhost:8080/payment/callback");
        createBill.setBillAmount(500);
        createBill.setBillContentEmail("Thank you for subscribing to Ainul Quran!");

        if (result.hasErrors()) {
            return "payment/createBill";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, Object> map= new LinkedMultiValueMap<>();
        map.add("userSecretKey", createBill.getUserSecretKey());
        map.add("categoryCode", createBill.getCategoryCode());
        map.add("billName", createBill.getBillName());
        map.add("billDescription", createBill.getBillDescription());
        map.add("billPriceSetting", createBill.getBillPriceSetting());
        map.add("billPayorInfo", createBill.getBillPayorInfo());
        map.add("billAmount", createBill.getBillAmount());
        map.add("billReturnUrl", createBill.getBillReturnUrl());
        map.add("billCallbackUrl", createBill.getBillCallbackUrl());
        map.add("billExternalReferenceNo", createBill.getBillExternalReferenceNo());
        map.add("billTo", createBill.getBillTo());
        map.add("billEmail", createBill.getBillEmail());
        map.add("billPhone", createBill.getBillPhone());
        map.add("billSplitPayment", createBill.getBillSplitPayment());
        map.add("billSplitPaymentArgs", createBill.getBillSplitPaymentArgs());
        map.add("billDisplayMerchant", createBill.getBillDisplayMerchant());
        map.add("billContentEmail", createBill.getBillContentEmail());
        map.add("billChargeToCustomer", createBill.getBillChargetoCustomer());

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                createBillApiUrl, request , String.class);


        ObjectMapper objectMapper=new ObjectMapper();

        BillCode[] billCodes=objectMapper.readValue(response.getBody(), BillCode[].class);

        String billCode=billCodes[0].BillCode;

        //set userpayment data
        userPayment newPayment=new userPayment();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentusername=auth.getName();

        newPayment.setUsername(currentusername);
        newPayment.setStatus("pending");
        newPayment.setBillCode(billCode);
        paymentRepo.save(newPayment);



            model.addAttribute("billDetails",createBill);
            model.addAttribute("billCode",billCode);
        return "payment/confirmpay";

    }

    @Transactional
    @GetMapping("/payment/callback")
    public String callbackPost(@RequestParam String status_id,
                               @RequestParam String billcode,
                               @RequestParam String order_id,
                               @RequestParam String transaction_id,Model model) throws JsonProcessingException {

        System.out.println(status_id+" "+billcode+" "+transaction_id);

        if(checkStatus(billcode)){


            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String currentusername=auth.getName();
            userPayment payment=paymentRepo.findByUsername(currentusername);
            payment.setStatus("success");
            paymentRepo.save(payment);



            User currentuser=service.findByUsername(currentusername);
            //set new role
//            Collection<Role> role=new ArrayList<>();
//            role.add(new Role("ROLE_PAID_USER"));
//            role.add(new Role("ROLE_USER"));

            //set the password first before deleting
            currentuser.setPassword(currentuser.getPassword());

            //delete first to delete all the existing roles too
            userRepo.deleteByUsername(currentuser.getUsername());


            currentuser.getRoles().clear();
            currentuser.getRoles().add(new Role("ROLE_USER"));
            currentuser.getRoles().add(new Role("ROLE_PAID"));

            //then save a new instance.. if using edit the existing data in the roles table
            //will not be deleted.
//
            if(userRepo.findByUsername(currentuser.getUsername()) ==null){
                service.normalSave(currentuser);
            }

            model.addAttribute("alreadysuccess","success");
            return "payment/confirmpay";
        }

        return "redirect:/payment/createBill";


    }


    public boolean checkStatus(String billCode) throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();

        String billTransactionsurl="https://dev.toyyibpay.com/index.php/api/getBillTransactions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        ObjectMapper objectMapper=new ObjectMapper();

        MultiValueMap<String, Object> billTransactionMap= new LinkedMultiValueMap<>();
        billTransactionMap.add("billCode",billCode);
        HttpEntity<MultiValueMap<String, Object>> requestBillTransaction =
                new HttpEntity<>(billTransactionMap, headers);

        ResponseEntity<String> responsegetBillTransaction = restTemplate.postForEntity(
                billTransactionsurl, requestBillTransaction , String.class);

        if (responsegetBillTransaction.getBody().contains("No data found")){
            return false;
        }

        billTransactions[] billTransactions=objectMapper.readValue(responsegetBillTransaction.getBody(),
                com.AinulQuran.model.billTransactions[].class);



        if(billTransactions[0].billpaymentStatus.equals("1")){

            return true;
        }

        return false;

    }






}
