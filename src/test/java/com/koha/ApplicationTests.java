package com.koha;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.koha.controller.AdminUserController;
import com.koha.controller.CategoryController;
import com.koha.entity.Category;
import com.koha.entity.User;
import com.koha.repository.CategoryRepository;
import com.koha.repository.UserRepository;
import com.koha.service.ICategoryService;
import com.koha.service.IUserService;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryController categoryController;

    @Autowired
    private AdminUserController adminUserController;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IUserService userService;

    @Test
    void contextLoads() {
        assertNotNull(categoryController, "CategoryController bean should be loaded");
        assertNotNull(adminUserController, "AdminUserController bean should be loaded");
        assertNotNull(categoryRepository, "CategoryRepository bean should be loaded");
        assertNotNull(userRepository, "UserRepository bean should be loaded");
        assertNotNull(categoryService, "CategoryService bean should be loaded");
        assertNotNull(userService, "UserService bean should be loaded");
    }

    @Test
    void testCategoryCRUDAndSearch() {
        // Test Find All
        List<Category> allCategories = categoryService.findAll();
        assertNotNull(allCategories);

        // Test Search
        List<Category> searchResults = categoryRepository.searchByName("Thoại");
        assertNotNull(searchResults);
    }

    @Test
    void testUserCRUDAndSearch() {
        // Test Find All
        List<User> allUsers = userService.findAll();
        assertNotNull(allUsers);

        // Test Search
        List<User> searchResults = userRepository.searchByKeyword("admin");
        assertNotNull(searchResults);
    }

    @Test
    void testAdminCategories() throws Exception {
        mockMvc.perform(get("/admin/categories"))
                .andExpect(status().isOk());
    }

    @Test
    void testAdminUsers() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk());
    }

    @Test
    void testHome() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk());
    }

    @Test
    void testProductList() throws Exception {
        mockMvc.perform(get("/product"))
                .andExpect(status().isOk());
    }

    @Test
    void testProductsEndpoint() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk());
    }

    @Test
    void testProductDetail() throws Exception {
        mockMvc.perform(get("/product/detail").param("id", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testAdminProducts() throws Exception {
        mockMvc.perform(get("/admin/products"))
                .andExpect(status().isOk());
    }

    @Test
    void testAdminProductAdd() throws Exception {
        mockMvc.perform(get("/admin/product/add"))
                .andExpect(status().isOk());
    }
}


