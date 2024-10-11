package com.Ecommerce_backend.service.impl;

import com.Ecommerce_backend.Filter.ProductFilter;
import com.Ecommerce_backend.Filter.ProductSpec;
import com.Ecommerce_backend.dto.GettingProductRequest;
import com.Ecommerce_backend.dto.ProductDTO;
import com.Ecommerce_backend.dto.Response;
import com.Ecommerce_backend.dto.UserDTO;
import com.Ecommerce_backend.entity.Category;
import com.Ecommerce_backend.entity.Product;
import com.Ecommerce_backend.entity.User;
import com.Ecommerce_backend.exception.OurException;
import com.Ecommerce_backend.repo.CategoryRepository;
import com.Ecommerce_backend.repo.ProductRepository;
import com.Ecommerce_backend.repo.UserRepository;
import com.Ecommerce_backend.service.CloudinaryService;
import com.Ecommerce_backend.service.interfac.IProductService;
import com.Ecommerce_backend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.math.BigDecimal;
import java.util.*;

@Service
public class ProductService implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Response addProduct(String userId, MultipartFile photo, String title, BigDecimal productPrice, String description, List<String> categoryNames) {
        Response response = new Response();

        try {
            String imageUrl = cloudinaryService.saveImageToCloud(photo);
            Product product = new Product();
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new OurException("User not found"));
            product.setImage(imageUrl);
            product.setDescription(description);
            product.setProductPrice(productPrice);
            product.setTitle(title);
//            product.setCategories(categories);

            Set<Category> categories = new HashSet<>();
            for(String categoryName: categoryNames) {
                Category category = categoryRepository.findByName(categoryName)
                        .orElseGet(() -> {
                           Category newCategory = new Category();
                           newCategory.setName(categoryName);
                           return categoryRepository.save(newCategory);
                        });
                categories.add(category);
            }

            product.setCategories(categories);
            product.setUser(user);
            Product savedProduct = productRepository.save(product);
            ProductDTO productDTO = Utils.mapProductEntityToProductDTO(savedProduct);
            response.setMessage("successful");
            response.setStatusCode(200);
            response.setProduct(productDTO);
        }

        catch (OurException e) {
            handleException(response, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            handleException(response, HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred during Adding product: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllProducts(GettingProductRequest gettingProductRequest) {

        Response response = new Response();

        // Set default values for page size and number if not provided
        int pageSize = (gettingProductRequest.getPageSize() > 0) ? gettingProductRequest.getPageSize() : 10;
        int pageNumber = (gettingProductRequest.getPageNumber() >= 0) ? gettingProductRequest.getPageNumber() : 0;
        try {
            // Prepare filter and specification
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Product> pageProducts;

            if(gettingProductRequest.getPriceFrom() != null|| gettingProductRequest.getPriceTo() != null|| gettingProductRequest.getCategories() != null){
                ProductFilter filter = createProductFilter(gettingProductRequest);
                Specification<Product> spec = createProductSpecification(filter);
                pageProducts = productRepository.findAll(spec, pageable);
            } else {
                pageProducts = productRepository.findAll(pageable);
            }

            List<Product> products = pageProducts.getContent();

            if (products.isEmpty()) {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No products found matching the criteria.");
                response.setProductList(Collections.emptyList());
                return response;
            }
                // Map to DTO and set response
                List<ProductDTO> productDTOList = Utils.mapProductListEntityToProductListDTO(products);
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage(gettingProductRequest.getPriceTo()+"");
                response.setProductList(productDTOList);
        } catch (OurException e) {
            handleException(response, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            handleException(response, HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred during getting products: " + e.getMessage());
        }
        return response;
    }

    private ProductFilter createProductFilter(GettingProductRequest gettingProductRequest) {
        ProductFilter filter = new ProductFilter();
        filter.setCategories(gettingProductRequest.getCategories());
        filter.setPriceFrom(gettingProductRequest.getPriceFrom());
        filter.setPriceTo(gettingProductRequest.getPriceTo());
        return filter;
    }

    private Specification<Product> createProductSpecification(ProductFilter filter) {
        Specification<Product> spec = Specification.where(null);


        if (filter.getPriceFrom() == null && filter.getPriceTo() == null) {
            spec = spec.and(ProductSpec.filterByCategories(filter.getCategories()));
        } else if(filter.getCategories() == null) {
            spec = spec.and(ProductSpec.filterByPriceRange(filter.getPriceFrom(), filter.getPriceTo()));
        }
        else {
            spec = spec.and(ProductSpec.filterByPriceRangeAndCategory(filter.getPriceFrom(), filter.getPriceTo(), filter.getCategories()));
        }

        return spec;
    }


    @Override
    public Response getProductById(String productId) {
        Response response = new Response();
        try {
            Product product = productRepository.findById(Long.valueOf(productId)).orElseThrow(() -> new OurException("User not found"));
            ProductDTO productDTO = Utils.mapProductEntityToProductDTO(product);
            response.setMessage("successful");
            response.setProduct(productDTO);
            response.setStatusCode(200);
        }  catch (OurException e) {
            handleException(response, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            handleException(response, HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred during getting product: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getMyProducts(String username) {
        Response response = new Response();
        try {
            User user = userRepository.findByUsername(username).orElseThrow(() -> new OurException("User not found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTOPlusUserProducts(user);
            response.setMessage("successful");
            response.setUser(userDTO);
            response.setStatusCode(200);
        }   catch (OurException e) {
            handleException(response, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            handleException(response, HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred during getting users product info: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteProduct(String productId) {
        Response response = new Response();
        try {
            productRepository.findById(Long.valueOf(productId)).orElseThrow(() -> new OurException("Product Not found"));
            productRepository.deleteById(Long.valueOf(productId));
            response.setStatusCode(200);
            response.setMessage("successful");
        }    catch (OurException e) {
            handleException(response, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            handleException(response, HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred during Deleting product: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response editProduct(String productId, MultipartFile photo, String title, BigDecimal productPrice, String description,   List<String> categoryNames) {
        Response response = new Response();

        try {
            String imageUrl = null;
            if(photo != null && !photo.isEmpty()) {
                imageUrl = cloudinaryService.saveImageToCloud(photo);
            }

            Product productData = productRepository.findById(Long.valueOf(productId)).orElseThrow(() -> new OurException("Product Not found"));
            if(title != null) productData.setTitle(title);
            if(productPrice != null) productData.setProductPrice(productPrice);
            if(description != null) productData.setDescription(description);
            if(imageUrl != null) productData.setImage(imageUrl);
            if(categoryNames != null ) {
                Set<Category> categories = new HashSet<>();
                for (String categoryName : categoryNames) {
                    // Check if the category already exists
                    Category category = categoryRepository.findByName(categoryName)
                            .orElseGet(() -> {
                                // If not found, create a new one
                                Category newCategory = new Category();
                                newCategory.setName(categoryName);
                                return categoryRepository.save(newCategory);
                            });
                    categories.add(category);
                }

                // Set the categories to the product
                productData.setCategories(categories);
            }
            Product updatedProduct = productRepository.save(productData);
            ProductDTO productDTO = Utils.mapProductEntityToProductDTO(updatedProduct);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setProduct(productDTO);
        } catch (OurException e) {
            handleException(response, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            handleException(response, HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred during updating product: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response searchProducts(String title, String description) {
        Response response = new Response();
        try {
            validateSearchParameters(title, description);

            Specification<Product> spec = buildProductSpecification(title, description);

            Pageable pageable = PageRequest.of(0, 4); // Consider making page and size dynamic
            Page<Product> pageProducts = productRepository.findAll(spec, pageable);

            List<ProductDTO> productsDTO = Utils.mapProductListEntityToProductListDTO(pageProducts.getContent());

            response.setMessage("Search results for title: " + title + " and description: " + description);
            response.setProductList(productsDTO);
            response.setStatusCode(200);
        }         catch (OurException e) {
            handleException(response, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            handleException(response, HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred during product reterival : " + e.getMessage());
        }
        return response;
    }

    private void validateSearchParameters(String title, String description) throws OurException {
        if (!StringUtils.hasText(title) && !StringUtils.hasText(description)) {
            throw new OurException("At least one of title or description must be provided");
        }
    }

    private Specification<Product> buildProductSpecification(String title, String description) {
        ProductFilter filter = new ProductFilter();
        if (StringUtils.hasText(title) && StringUtils.hasText(description)) {
            filter.setTitle(title);
            filter.setDescription(description);
            return ProductSpec.searchByTitleDescription(filter);
        } else if (StringUtils.hasText(title)) {
            filter.setTitle(title);
            return ProductSpec.searchByTitle(filter);
        } else {
            filter.setDescription(description);
            return ProductSpec.searchByDescription(filter);
        }
    }

    private void handleException(Response response, HttpStatus status, String message) {
        response.setStatusCode(status.value());
        response.setMessage(message);
    }




}
