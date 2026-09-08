package com.koha.service;

import java.util.List;
import com.koha.entity.User;

public interface IUserService {

    User login(String username, String password);

    boolean register(String username, String password, String fullname, String email, String phone);

    boolean registerWithOtp(String username, String password, String fullname, String email, String phone);

    boolean verifyOtp(String username, String otp);

    boolean resendOtp(String username);

    boolean sendForgotPasswordOtp(String account);

    boolean resetPassword(String account, String otp, String newPassword);

    boolean checkExistUsername(String username);

    boolean checkExistEmail(String email);

    User findByUsername(String username);

    User findByEmail(String email);

    User findById(int id);

    void insert(User user);

    void update(User user);

    void delete(int id) throws Exception;

    boolean updateProfile(int id, String fullname, String phone, String images);

    List<User> findAll();

    List<User> search(String keyword);
}
