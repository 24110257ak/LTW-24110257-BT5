package com.koha.service.impl;

import java.io.File;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.koha.dao.IUserDao;
import com.koha.dao.impl.UserDao;
import com.koha.entity.User;
import com.koha.repository.UserRepository;
import com.koha.service.IUserService;
import com.koha.util.Constant;
import com.koha.util.EmailUtil;

@Service
@Primary
@Transactional
public class UserServiceImpl implements IUserService {

    @Autowired(required = false)
    private UserRepository userRepository;

    private IUserDao userDao = new UserDao();

    public UserServiceImpl() {
    }

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public User login(String username, String password) {
        User user = findByUsername(username);
        if (user != null && user.getPassword() != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    @Override
    public boolean register(String username, String password, String fullname, String email, String phone) {
        if (checkExistUsername(username) || (email != null && checkExistEmail(email))) {
            return false;
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFullName(fullname);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRoleid(2);
        user.setStatus(1);
        insert(user);
        return true;
    }

    @Override
    public boolean registerWithOtp(String username, String password, String fullname, String email, String phone) {
        if (checkExistUsername(username) || (email != null && checkExistEmail(email))) {
            return false;
        }

        String otp = String.format("%06d", new Random().nextInt(999999));

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFullName(fullname);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRoleid(2);
        user.setStatus(0);
        user.setCode(otp);

        insert(user);

        if (email != null && !email.trim().isEmpty()) {
            EmailUtil.sendOtpEmail(email.trim(), otp, "kích hoạt tài khoản");
        }

        return true;
    }

    @Override
    public boolean verifyOtp(String username, String otp) {
        if (username == null || otp == null) {
            return false;
        }
        User user = findByUsername(username.trim());
        if (user != null && user.getCode() != null && user.getCode().trim().equals(otp.trim())) {
            user.setStatus(1);
            user.setCode(null);
            update(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean resendOtp(String username) {
        if (username == null) {
            return false;
        }
        User user = findByUsername(username.trim());
        if (user != null && user.getEmail() != null) {
            String newOtp = String.format("%06d", new Random().nextInt(999999));
            user.setCode(newOtp);
            update(user);
            EmailUtil.sendOtpEmail(user.getEmail(), newOtp, "kích hoạt tài khoản (gửi lại)");
            return true;
        }
        return false;
    }

    @Override
    public boolean sendForgotPasswordOtp(String account) {
        if (account == null || account.trim().isEmpty()) {
            return false;
        }
        account = account.trim();
        User user = findByUsername(account);
        if (user == null) {
            user = findByEmail(account);
        }

        if (user != null && user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
            String otp = String.format("%06d", new Random().nextInt(999999));
            user.setCode(otp);
            update(user);
            EmailUtil.sendOtpEmail(user.getEmail(), otp, "đặt lại mật khẩu");
            return true;
        }
        return false;
    }

    @Override
    public boolean resetPassword(String account, String otp, String newPassword) {
        if (account == null || otp == null || newPassword == null) {
            return false;
        }
        account = account.trim();
        User user = findByUsername(account);
        if (user == null) {
            user = findByEmail(account);
        }

        if (user != null && user.getCode() != null && user.getCode().trim().equals(otp.trim())) {
            user.setPassword(newPassword);
            user.setCode(null);
            update(user);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkExistUsername(String username) {
        if (userRepository != null) {
            return userRepository.existsByUsername(username);
        }
        return userDao.checkExistUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkExistEmail(String email) {
        if (userRepository != null) {
            return userRepository.existsByEmail(email);
        }
        return userDao.checkExistEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        if (userRepository != null) {
            return userRepository.findByUsername(username).orElse(null);
        }
        return userDao.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        if (userRepository != null) {
            return userRepository.findByEmail(email).orElse(null);
        }
        return userDao.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(int id) {
        if (userRepository != null) {
            return userRepository.findById(id).orElse(null);
        }
        return userDao.findById(id);
    }

    @Override
    public void insert(User user) {
        if (userRepository != null) {
            userRepository.save(user);
        } else {
            userDao.insert(user);
        }
    }

    @Override
    public void update(User user) {
        if (userRepository != null) {
            userRepository.save(user);
        } else {
            userDao.update(user);
        }
    }

    @Override
    public void delete(int id) throws Exception {
        User user = findById(id);
        if (user != null) {
            String oldImg = user.getImages();
            if (oldImg != null && !oldImg.startsWith("http")) {
                File file = new File(Constant.DIR + "/" + oldImg);
                if (file.exists() && file.isFile()) {
                    file.delete();
                }
            }
            if (userRepository != null) {
                userRepository.deleteById(id);
            } else {
                userDao.delete(id);
            }
        }
    }

    @Override
    public boolean updateProfile(int id, String fullname, String phone, String images) {
        User user = findById(id);
        if (user != null) {
            if (fullname != null && !fullname.trim().isEmpty()) {
                user.setFullName(fullname.trim());
            }
            if (phone != null) {
                user.setPhone(phone.trim());
            }
            if (images != null && !images.trim().isEmpty()) {
                user.setImages(images.trim());
            }
            update(user);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        if (userRepository != null) {
            return userRepository.findAllByOrderByIdDesc();
        }
        return userDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }
        if (userRepository != null) {
            return userRepository.searchByKeyword(keyword.trim());
        }
        String kw = keyword.trim().toLowerCase();
        return userDao.findAll().stream()
                .filter(u -> (u.getUsername() != null && u.getUsername().toLowerCase().contains(kw))
                        || (u.getFullName() != null && u.getFullName().toLowerCase().contains(kw))
                        || (u.getEmail() != null && u.getEmail().toLowerCase().contains(kw))
                        || (u.getPhone() != null && u.getPhone().toLowerCase().contains(kw)))
                .toList();
    }
}
