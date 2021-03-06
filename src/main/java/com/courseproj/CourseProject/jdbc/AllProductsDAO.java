package com.courseproj.CourseProject.jdbc;

import com.courseproj.CourseProject.Entity.Chipset;
import com.courseproj.CourseProject.Entity.Img;
import com.courseproj.CourseProject.Entity.Product;
import com.courseproj.CourseProject.Entity.Type;

import java.util.List;

public interface AllProductsDAO {
    List<Product> getAllProducts();
    Product index(int id);
    void addProduct(String name, int price, String path, String idType, String Description, int Img_id, Chipset chipset);
    void deleteProduct(int idProduct);
    void updateProduct(String name, int price, int idType, int idProduct, String Description);
    List<Type> getAllCategories();
    List<Product> getByCategory(int idCategory, String chipset);
    Type getCategoryByIndex(int idCategory);
    void updateCategory(int idCategory, String name);
    void addCategory(String name);
    void deleteCategory(int idCategory);
    Img getLastImgId();
    Type getCategoryByName(String name);
}
