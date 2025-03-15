package gatech.cs.buzzcar.entity.dto;


import lombok.Data;

@Data
public class CustomerDto {

    private String cid;


    private String street;


    private String city;


    private String state;


    private String postalCode;


    private String phone;


    private String email;

    private String customerType;

    // biz

    private String cidTaxNumber;


    private String businessName;


    private String contactTitle;


    private String firstName;


    private String lastName;

    //individual
    private String cidDriverLicense;



}
