package com.sylvie.clibank.business;

import com.sylvie.clibank.repository.UserRepository;

public class UserService {
    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }


}
