package com.courseproj.CourseProject.jdbc;

import com.courseproj.CourseProject.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AllProductsDAOImpl implements AllProductsDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AllProductsDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SQL_GET_BY_ID = "select product.idProduct,  product.name, product.price, type.idType as idType, type.name as type, product.productDescription, c.name as chipset, " +
            " img.path_to_file " +
            " FROM product " +
            " JOIN type ON type.idType = product.idType " +
            " JOIN img on img.idImg = product.Img_idImg " +
            " JOIN chipset c on c.idChipset = product.idChipset " +
            " where idProduct = ? " +
            " ORDER BY product.idProduct;";
    private static final String SQL_GET_ALL = "select product.idProduct,  product.name, product.price, type.idType as idType, type.name as type, product.productDescription, c.name as chipset," +
            " img.path_to_file " +
            " FROM product " +
            " JOIN type ON type.idType = product.idType " +
            " JOIN img on img.idImg = product.Img_idImg " +
            " JOIN chipset c on c.idChipset = product.idChipset " +
            " ORDER BY product.idProduct;";

    @Override
    public List<Product> getAllProducts() {
        System.out.println(SQL_GET_ALL);
        return jdbcTemplate.query(SQL_GET_ALL, new ProductMapper());
    }

    @Override
    public Product index(int id) {


        Product product = jdbcTemplate.query(SQL_GET_BY_ID, new Object[]{id}, new ProductMapper())
                .stream().findAny().orElse(null);

        return product;
    }

    @Override
    public void addProduct(String name, int price, String path, String idType, String Description, int Img_id, Chipset chipset) {
        jdbcTemplate.update("insert into img(description, path_to_file) values(?, ?)", "product_img", path);
        jdbcTemplate.update("insert into product(name, price, Img_idImg, idType, productDescription, idChipset ) values(?, ?, ?, ?, ?, ?)", name, price, Img_id, getCategoryByName(idType).getId(), Description, chipset.getId());
    }

    @Override
    public void deleteProduct(int idProduct) {
        jdbcTemplate.update("delete from reciept_has_product where idProduct = ?", idProduct);
        jdbcTemplate.update("delete from product where idProduct = ?", idProduct);
    }

    @Override
    public void updateProduct(String name, int price, int idType, int idProduct, String Description) {
        Product product = index(idProduct);
        if (price != product.getPrice()) {
            jdbcTemplate.update("update product set price = " + price + " where idProduct = ?", idProduct);
        }
        if (name != product.getName()) {
            jdbcTemplate.update("update product set name = '" + name + "' where idProduct = ?", idProduct);
        }
        if (idType != product.getIdType()) {
            jdbcTemplate.update("update product set idType = " + idType + " where idProduct = ?", idProduct);
        }
        if (Description != product.getDescription()) {
            jdbcTemplate.update("update product set productDescription = '" + Description + "' where idProduct = ?", idProduct);
        }
    }

    @Override
    public void addCategory(String name) {
        jdbcTemplate.update("insert into type(name) values('" + name + "')");
    }

    @Override
    public void deleteCategory(int idCategory) {
        jdbcTemplate.update("delete from type where type.idType = ?", idCategory);
    }

    @Override
    public Img getLastImgId() {
        return jdbcTemplate.query("select max(idImg) as idImg from img", new Object[]{}, new ImgMapper())
                .stream().findAny().orElse(null);
    }

    @Override
    public void updateCategory(int idCategory, String name) {
        jdbcTemplate.update("update type set name = '" + name + "' where idType = " + idCategory);
    }

    @Override
    public Type getCategoryByIndex(int idCategory) {
        return jdbcTemplate.query("select idType, name from type where idType = " + idCategory, new TypeMapper()).stream().findAny().orElse(null);
    }

    @Override
    public List<Product> getByCategory(int idCategory, String chipset) {
        return jdbcTemplate.query("select product.idProduct,  product.name, product.price, type.idType as idType, type.name as type, product.productDescription, img.path_to_file, c.name as chipset" +
                " from product " +
                " JOIN img on img.idImg = product.Img_idImg " +
                " join type on product.idType = type.idType" +
                " join chipset c on c.idChipset = product.idChipset" +
                " where type.idType = " + idCategory +
                (chipset == null ? "": " and c.name = '" + chipset + "'"), new ProductMapper());
    }

    @Override
    public List<Type> getAllCategories() {
        return jdbcTemplate.query("select idType, name from type", new TypeMapper());
    }

    public List<Chipset> getAllChipsets() {
        return jdbcTemplate.query("select idChipset, name from chipset", new ChipsetMapper());
    }

    @Override
    public Type getCategoryByName(String name) {
        return jdbcTemplate.query("select idType, name from type where name = '" + name + "'", new TypeMapper()).stream().findAny().orElse(null);
    }

    public Chipset getChipByName(String chipset) {
        return jdbcTemplate.query("select c.idChipset, c.name from chipset c where c.name = ?", new Object[]{chipset}, new ChipsetMapper()).stream().findFirst().orElse(null);
    }

    public Chipset getChipById(int chipset) {
        return jdbcTemplate.query("select c.idChipset, c.name from chipset c where c.idChipset = ?", new Object[]{chipset}, new ChipsetMapper()).stream().findFirst().orElse(null);
    }
}
