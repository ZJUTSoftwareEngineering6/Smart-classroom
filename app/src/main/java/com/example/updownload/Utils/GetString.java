package com.example.updownload.Utils;

public class GetString {
    String highTemp,lowTemp,Weather;

    public String HighTemp(String string) {
        //String str = string.substring(string.indexOf('高')+3,string.indexOf('高')+5);
        String str = string.substring(string.indexOf('高')+3,string.indexOf('℃')+1);
        return str;
    }

    public String LowTemp(String string) {
//        String str = string.substring(string.indexOf('低')+3,string.indexOf('低')+5);
//        String str = string.substring(string.indexOf('低')+3,string.indexOf('℃',1)+1);
//        return str;

        int index = string.indexOf("℃");
        index = string.indexOf("℃", index+1);
        String str = string.substring(string.indexOf('低')+3,index+1);
        return str;
    }

    public String Week(String string) {
        String str = string.substring(string.indexOf('日')+1,string.indexOf('日')+4);
        return str;
    }

    public String Weather(String string) {
        String str = string.substring(string.lastIndexOf('"',string.lastIndexOf('"')-1)+1,string.lastIndexOf('"'));
        return str;
    }

    public String GetTemp(String string) {
        String str = string.substring(0,string.indexOf('℃'));
        return str;
    }

}
