package com.koha.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username", columnDefinition = "NVARCHAR(50) NOT NULL", unique = true)
    private String username;

    @Column(name = "password", columnDefinition = "NVARCHAR(255) NOT NULL")
    private String password;

    @Column(name = "fullname", columnDefinition = "NVARCHAR(100) NOT NULL")
    private String fullName;

    @Column(name = "email", columnDefinition = "NVARCHAR(150) NULL")
    private String email;

    @Column(name = "phone", columnDefinition = "NVARCHAR(20) NULL")
    private String phone;

    @Column(name = "roleid")
    private int roleid = 2; // 1: Admin, 2: User

    @Column(name = "status")
    private int status = 0; // 0: Chưa kích hoạt, 1: Đã kích hoạt

    @Column(name = "code", columnDefinition = "NVARCHAR(10) NULL")
    private String code; // Mã OTP kích hoạt hoặc quên mật khẩu

    @Column(name = "images", columnDefinition = "NVARCHAR(500) NULL")
    private String images; // Ảnh đại diện người dùng

    public User() {
    }

    public User(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public User(int id, String username, String password, String fullName, String email, String phone, int roleid) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.roleid = roleid;
    }

    public User(int id, String username, String password, String fullName, String email, String phone, int roleid, int status, String code) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.roleid = roleid;
        this.status = status;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", fullName=" + fullName + ", roleid=" + roleid
                + ", status=" + status + ", code=" + code + "]";
    }
}
