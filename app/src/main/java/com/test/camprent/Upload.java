package com.test.camprent;

public class Upload {
    private String mImageUrl;
    private String Name;
    private String From;
    private String To;


    //private String Location2;
    //private String Location3;
    // private String Location4;
    //private String Location5;
    private String Price;
    private String Phone;
    private String Location1;
    private String Key1;
    private String mail1;

    public Upload(){ //empty constructor needed

    }

    public Upload( String ImageUrl,String name,String from,String to, String price, String phone , String location1,String key,String mail) {
        Key1=key;
        mail1=mail;

        mImageUrl = ImageUrl;
        Name=name;
        Location1 = location1;
        Price = price;
        Phone = phone;
        From = from;
        To = to;

    }
    public String getKey() {
        return Key1;
    }
    public void  setKey(String key){
        Key1=key;
    }
    public String getmail() {
        return mail1;
    }
    public void  setmail(String mail){
        mail1=mail;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }




    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }



    public String getImageUrl(){
        return mImageUrl;
    }
    public void setImageUrl(String imageUrl){
        mImageUrl=imageUrl;
    }
    public String getLocation1() {
        return Location1;
    }

    public void setLocation1(String location) {
        Location1 = location;
    }
}