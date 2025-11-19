package com.example.ejb.remote.singletonbean;

import jakarta.ejb.Local;

import java.util.List;

@Local
public interface CountryState {

    public List<String> getStates(String country);
    
    public void setStates(String country, List<String> states);

}
