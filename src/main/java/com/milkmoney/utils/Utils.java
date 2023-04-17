package com.milkmoney.utils;

import com.milkmoney.Repositories.UserRepository;
import com.milkmoney.controllers.PoliticianController;
import com.milkmoney.models.Politician;
import com.milkmoney.models.User;
import jdk.jshell.execution.Util;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class Utils {

    public boolean containsName(String name){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        System.out.println("made it");
        return currentUser.getPoliticians().stream().anyMatch(pol -> pol.getName().equals(name));
    }
    public void test(){
        System.out.println("test,,,");
    }
}
