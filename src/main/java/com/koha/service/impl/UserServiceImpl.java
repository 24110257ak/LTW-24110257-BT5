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
        User user = userDao.findByUsername(username);
        User user = findByUsername(username);
        if (user != null && user.getPassword() != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    @Override
    public boolean register(String username, String password, String fullname, String email, String phone) {
        if (userDao.checkExistUsername(username) || (email != null && userDao.checkExistEmail(email))) {
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
        user.setStatus(1); // Mặc định kích hoạt nếu không dùng OTP
        userDao.insert(user);
        insert(user);
        return true;
    }

    @Override
    public boolean registerWithOtp(String username, String password, String fullname, String email, String phone) {
        if (userDao.checkExistUsername(username) || (email != null && userDao.checkExistEmail(email))) {
        if (checkExistUsername(username) || (email != null && checkExistEmail(email))) {
            return false;
        }

        // 1. Sinh mã OTP ngẫu nhiên 6 chữ số
        String otp = String.format("%06d", new Random().nextInt(999999));

        // 2. Tạo đối tượng User với trạng thái chưa kích hoạt (status = 0) và lưu mã OTP vào cột code
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFullName(fullname);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRoleid(2);
        user.setStatus(0); // 0: Chưa kích hoạt
        user.setCode(otp); // Lưu mã OTP

        userDao.insert(user);
        insert(user);

        // 3. Gửi email chứa mã OTP kích hoạt tài khoản
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
        User user = userDao.findByUsername(username.trim());
        User user = findByUsername(username.trim());
        if (user != null && user.getCode() != null && user.getCode().trim().equals(otp.trim())) {
            user.setStatus(1); // Kích hoạt tài khoản thành công
            user.setCode(null); // Xóa OTP đã sử dụng
            userDao.update(user);
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
        User user = userDao.findByUsername(username.trim());
        User user = findByUsername(username.trim());
        if (user != null && user.getEmail() != null) {
            String newOtp = String.format("%06d", new Random().nextInt(999999));
            user.setCode(newOtp);
            userDao.update(user);
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
        User user = userDao.findByUsername(account);
        User user = findByUsername(account);
        if (user == null) {
            user = userDao.findByEmail(account);
            user = findByEmail(account);
        }

        if (user != null && user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
            String otp = String.format("%06d", new Random().nextInt(999999));
            user.setCode(otp);
            userDao.update(user);
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
        User user = userDao.findByUsername(account);
        User user = findByUsername(account);
        if (user == null) {
            user = userDao.findByEmail(account);
            user = findByEmail(account);
        }

        if (user != null && user.getCode() != null && user.getCode().trim().equals(otp.trim())) {
            user.setPassword(newPassword);
            user.setCode(null); // Xóa mã OTP
            userDao.update(user);
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
        userDao.insert(user);
        if (userRepository != null) {
            userRepository.save(user);
        } else {
            userDao.insert(user);
        }
    }

    @Override
    public void update(User user) {
        userDao.update(user);
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
            // Xóa ảnh đại diện vật lý nếu có và không phải link web
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
        User user = userDao.findById(id);
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
            userDao.update(user);
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
        // Fallback filter
        String kw = keyword.trim().toLowerCase();
        return userDao.findAll().stream()
                .filter(u -> (u.getUsername() != null && u.getUsername().toLowerCase().contains(kw))
                        || (u.getFullName() != null && u.getFullName().toLowerCase().contains(kw))
                        || (u.getEmail() != null && u.getEmail().toLowerCase().contains(kw))
                        || (u.getPhone() != null && u.getPhone().toLowerCase().contains(kw)))
                .toList();
    }
}
