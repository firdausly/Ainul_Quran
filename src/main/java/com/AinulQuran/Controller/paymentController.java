package com.AinulQuran.Controller;


import com.AinulQuran.dto.createBill;
import com.AinulQuran.model.*;
import com.AinulQuran.repository.UserPaymentRepo;
import com.AinulQuran.repository.UserRepository;
import com.AinulQuran.repository.propertiesrepo;
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
import java.util.*;

@Controller
public class paymentController {

    @Autowired
    private UserService service;

    @Autowired
    private UserPaymentRepo paymentRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private propertiesrepo properties;

    @GetMapping("/payment/pricing")
    public String pricing(Model model) {

        String price=properties.findByName("toyyibbillamount").getValue();

        price=price.replace("0"," ");

        model.addAttribute("price",price);
        return "payment/pricing";
    }

    @GetMapping("/payment/createBill")
    public String createBill(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentusername=auth.getName();

        if(paymentRepo.findByUsername(currentusername)!=null){
            userPayment payment=paymentRepo.findByUsername(currentusername);
            String billCode=payment.getBillcode();
            String status=payment.getStatus();

            if(status.equals("success")){
                model.addAttribute("alreadysuccess","");
                model.addAttribute("invoice",payment);
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


        String createBillApiUrl=properties.findByName("toyyibcreatebillapiurl").getValue();
        String userSecretKey=properties.findByName("toyyibusersecretkey").getValue();
        String categoryCode=properties.findByName("toyyibcategorycode").getValue();
        String billCallbackUrl=properties.findByName("toyyibcallbackurl").getValue();
        String billReturnUrl=properties.findByName("toyyibreturnurl").getValue();
        int billAmount=Integer.parseInt(properties.findByName("toyyibbillamount").getValue());
        String billContentEmail=properties.findByName("toyyibbillcontentemail").getValue();

        createBill.setUserSecretKey(userSecretKey);
        createBill.setCategoryCode(categoryCode);
        createBill.setBillCallbackUrl(billCallbackUrl);
        createBill.setBillReturnUrl(billReturnUrl);
        createBill.setBillAmount(billAmount);
        createBill.setBillContentEmail(billContentEmail);

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
        newPayment.setBillcode(billCode);
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


        billTransactions transactions=status(billcode);
        System.out.println(transactions);
        if(transactions!=null &&transactions.billpaymentStatus.equals("1")){


            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String currentusername=auth.getName();
            userPayment payment=paymentRepo.findByUsername(currentusername);
            payment.setStatus("success");
            payment.setInvoiceno(transactions.billpaymentInvoiceNo);
            String paymountamount=transactions.billpaymentAmount;
            payment.setAmountreceived(paymountamount);

            paymentRepo.save(payment);



            User currentuser=service.findByUsername(currentusername);


            //set the password first before deleting
            currentuser.setPassword(currentuser.getPassword());

            //delete first to delete all the existing roles too
            userRepo.deleteByUsername(currentuser.getUsername());


            currentuser.getRoles().clear();
            currentuser.getRoles().add(new Role("ROLE_USER"));
            currentuser.getRoles().add(new Role("ROLE_PAID"));

            //then save a new instance.. if using edit the existing data in the roles table
            //will not be deleted.
            if(userRepo.findByUsername(currentuser.getUsername()) ==null){
                service.normalSave(currentuser);
            }


            model.addAttribute("invoice",payment);
            model.addAttribute("pleaserelog","relog");
            model.addAttribute("alreadysuccess","success");
            return "payment/confirmpay";
        }

        return "redirect:/payment/createBill";


    }


    public billTransactions status(String billCode) throws JsonProcessingException {

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

        if (Objects.requireNonNull(responsegetBillTransaction.getBody()).contains("No data found")){
            return null;
        }

        billTransactions[] billTransactions=objectMapper.readValue(responsegetBillTransaction.getBody(),
                com.AinulQuran.model.billTransactions[].class);





        return billTransactions[0];

    }






}
