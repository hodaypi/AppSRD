package com.srd.srdapplication;

public class User {

    private String email;
    private String password;
    private String url;

    public User(String email) {}

    public User(String fullName, String email) {
        this.password = password;
        this.email = email;


    }
    public User(String password, String email,String url) {
        this.email = email;
        this.password = password;
        this.url = url;
    }
/*    public HashMap<String, Object> getAsMap(){
        HashMap<String, Object> userAsMap = new HashMap<>();
        userAsMap.put("username",username);
        userAsMap.put("password",password);
        userAsMap.put("age",age);
        userAsMap.put("name",name);
        //Add or remove more key value pair
        return userAsMap;
    }*/


    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
    public void setUrl(String sex) { this.url = url; }
    public String getUrl() { return url; }


}
