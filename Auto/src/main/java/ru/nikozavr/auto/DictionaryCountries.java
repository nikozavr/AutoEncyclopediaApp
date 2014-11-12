package ru.nikozavr.auto;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import java.util.HashMap;

public class DictionaryCountries{
    private HashMap<String,String> Countries;

    public DictionaryCountries(Resources res){
        Countries = new HashMap<String, String>();
        String[] code = res.getStringArray(R.array.country_code);
        String[] name = res.getStringArray(R.array.country_name);
        for (int i = 0; i < code.length; i++){
            Countries.put(code[i],name[i]);
        }
    }

    public String Translate(String code){
        String res = Countries.get(code);
        if(res == null)
            return code;
        else
            return res;
    }
}
