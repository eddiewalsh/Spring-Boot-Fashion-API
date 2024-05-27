package zelora.api.helper;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import zelora.api.controller.*;
import zelora.api.model.Orders;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class LinkFactory {

    //Customer Links
    public static Link getAllCustomersLink() {
        return linkTo(methodOn(CustomerController.class)
                .getAllCustomers(PageRequest.of(0, 10))).withRel("all-customers");
    }

    public static Link getAllCustomersSelfLink() {
        return linkTo(methodOn(CustomerController.class)
                .getAllCustomers(PageRequest.of(0, 10))).withSelfRel();
    }

    public static Link getCustomerByEmailLink(String email) {
        return linkTo(methodOn(CustomerController.class)
                .getCustomerByEmail(email)).withRel("single-customer");
    }

    public static Link getCustomerByEmailSelfLink(String email) {
        return linkTo(methodOn(CustomerController.class)
                .getCustomerByEmail(email)).withSelfRel();
    }

    public static Link getCustomerByIDSelfLink(Integer id) {
        return linkTo(methodOn(CustomerController.class)
                .getCustomerByID(id)).withSelfRel();
    }

    public static Link getCustomerByIDLink(Integer id) {
        return linkTo(methodOn(CustomerController.class)
                .getCustomerByID(id)).withRel("single-customer");
    }

    // Orders Link
    public static Link getAllOrdersSelfLink(Pageable pageable){
        return linkTo(methodOn(OrdersController.class)
                .getAllOrders(pageable)).withSelfRel();
    }

    public static Link getAllOrdersLink(Pageable pageable){
        return linkTo(methodOn(OrdersController.class)
                .getAllOrders(pageable)).withRel("all-orders");
    }

    public static Link getOrderByIDSelfLink(Integer id){
        return linkTo(methodOn(OrdersController.class)
                .getOrderByID(id)).withSelfRel();
    }

    public static Link getOrderByIDLink(Integer id){
        return linkTo(methodOn(OrdersController.class)
                .getOrderByID(id)).withRel("single-order");
    }

    public static Link getOrdersByCustomerIDLink(Integer id){
        return linkTo(methodOn(OrdersController.class)
                .getOrdersByCustomerID(id, PageRequest.of(0, 10))).withRel("customer-orders");
    }

    public static Link getOrdersByCustomerIDSelfLink(Integer id){
        return linkTo(methodOn(OrdersController.class)
                .getOrdersByCustomerID(id, PageRequest.of(0, 10))).withSelfRel();
    }

    public static Link getOrderItemByOrderIDLink(Integer id){
        return linkTo(methodOn(OrdersController.class)
                .getOrderItemsByOrderID(id, PageRequest.of(0, 10)))
                .withRel("order-items");
    }


    //Order Items Links
    public static Link getOrderItemByOrderIDSelfLink(Integer id){
        return linkTo(methodOn(OrdersController.class)
                .getOrderItemsByOrderID(id, PageRequest.of(0, 10)))
                .withSelfRel();
    }

    public static Link getOrderItemByIDLink(Integer id){
        return linkTo(methodOn(OrderItemController.class)
                .getOrderItemByID(id)).withRel("single-order-item");
    }

    public static Link getOrderItemByIDSelfLink(Integer id){
        return linkTo(methodOn(OrderItemController.class)
                .getOrderItemByID(id)).withSelfRel();
    }

    public static Link getOrderItemsByProductIDSelfLink(Integer productID){
        return linkTo(methodOn(OrderItemController.class)
                .getOrderItemByProductID(productID, PageRequest.of(0, 10)))
                .withSelfRel();
    }

    public static Link getOrderItemsByProductIDLink(Integer productID){
        return linkTo(methodOn(OrderItemController.class)
                .getOrderItemByProductID(productID, PageRequest.of(0, 10)))
                .withRel("product-order-items");
    }

    //Product Links
    public static Link getProductByIDLink(Integer id) {
        return linkTo(methodOn(ProductController.class)
                .getProductByID(id)).withRel("single-product");
    }

    public static Link getProductByIDSelfLink(Integer id) {
        return linkTo(methodOn(ProductController.class)
                .getProductByID(id)).withSelfRel();
    }

    public static Link getAllProductsLink(){
        return linkTo(methodOn(ProductController.class)
                .getAllProducts(PageRequest.of(0, 10)))
                .withRel("all-products");
    }

    public static Link getAllProductsSelfLink(){
        return linkTo(methodOn(ProductController.class)
                .getAllProducts(PageRequest.of(0, 10)))
                .withSelfRel();
    }

    public static Link getProductsBySupplierIDLink(Integer id){
        return linkTo(methodOn(ProductController.class)
                .getProductsBySupplierID(id, PageRequest.of(0, 10)))
                .withRel("product-suppliers");
    }

    public static Link getProductsBySupplierIDSelfLink(Integer id){
        return linkTo(methodOn(ProductController.class)
                .getProductsBySupplierID(id, PageRequest.of(0, 10)))
                .withSelfRel();
    }

    public static Link getProductsByCategoryIDLink(Integer id){
        return linkTo(methodOn(ProductController.class)
                .getProductsByCategoryID(id, PageRequest.of(0, 10)))
                .withRel("product-category");
    }

    public static Link getProductsByCategoryIDSelfLink(Integer id){
        return linkTo(methodOn(ProductController.class)
                .getProductsByCategoryID(id, PageRequest.of(0, 10)))
                .withSelfRel();
    }

    public static Link getProductThumbnailImage(Integer id){
        return linkTo(methodOn(ProductController.class)
                            .getProductThumbImage(id))
                            .withRel("thumbnail-image");
    }

    public static Link getProductLargeImage(Integer id){
        return linkTo(methodOn(ProductController.class)
                .getProductLargeImage(id))
                .withRel("large-image");
    }


    // Category Links
    public static Link getCategoryByIDLink(Integer id){
        return linkTo(methodOn(CategoryController.class)
                .getCategoryByID(id)).withRel("single-category");
    }

    public static Link getCategoryByIDSelfLink(Integer id){
        return linkTo(methodOn(CategoryController.class)
                .getCategoryByID(id)).withSelfRel();
    }

    public static Link getInventoryByIDLink(Integer id){
        return linkTo(methodOn(InventoryController.class)
                .getInventoryByID(id)).withRel("single-inventory");
    }

    public static Link getInventoryByIDSelfLink(Integer id){
        return linkTo(methodOn(InventoryController.class)
                .getInventoryByID(id)).withSelfRel();
    }

    public static Link getInventoryByProductIDLink(Integer id){
        return linkTo(methodOn(InventoryController.class)
                .getInventoryByProductID(id, PageRequest.of(0, 10)))
                .withRel("inventory-product");
    }

    public static Link getInventoryByProductIDSelfLink(Integer id){
        return linkTo(methodOn(InventoryController.class)
                .getInventoryByProductID(id, PageRequest.of(0, 10)))
                .withSelfRel();
    }

    public static Link getInventoryBySupplierIDSelfLink(Integer id){
        return linkTo(methodOn(InventoryController.class)
                .getInventoryBySupplierID(id, PageRequest.of(0, 10)))
                .withSelfRel();
    }

    public static Link getInventoryBySupplierIDLink(Integer id){
        return linkTo(methodOn(InventoryController.class)
                .getInventoryBySupplierID(id, PageRequest.of(0, 10)))
                .withRel("supplier-inventory");
    }



    //Supplier Links
    public static Link getSupplierByIDLink(Integer id){
        return linkTo(methodOn(SupplierController.class)
                .getSupplierByID(id)).withRel("single-supplier");
    }

    public static Link getSupplierByIDSelfLink(Integer id) {
        return linkTo(methodOn(SupplierController.class)
                .getSupplierByID(id)).withSelfRel();
    }

    // Review Links
    public static Link getAllReviewsSelfLink(Pageable pageable) {
        return linkTo(methodOn(ReviewsController.class)
                .getAllReviews(pageable)).withSelfRel();
    }

    public static Link getAllReviewsLink(Pageable pageable) {
        return linkTo(methodOn(ReviewsController.class)
                .getAllReviews(pageable)).withRel("all-reviews");
    }
    public static Link getReviewByIDSelfLink(Integer id){
        return linkTo(methodOn(ReviewsController.class)
                .getReviewByID(id)).withSelfRel();
    }

    public static Link getReviewByIDLink(Integer id){
        return linkTo(methodOn(ReviewsController.class)
                .getReviewByID(id)).withRel("single-review");
    }

    public static Link getReviewsByCustomerIDSelfLink(Integer id, Pageable pageable){
        return linkTo(methodOn(ReviewsController.class)
                .getReviewsByCustomerID(id, pageable)).withSelfRel();
    }

    public static Link getReviewsByCustomerIDLink(Integer id){
        return linkTo(methodOn(ReviewsController.class)
                .getReviewsByCustomerID(id, PageRequest.of(0, 10)))
                .withRel("customer-reviews");
    }

    public static Link getReviewsByProductIDLink(Integer id) {
        return linkTo(methodOn(ReviewsController.class)
                .getReviewsByProductID(id, PageRequest.of(0,10)))
                .withRel("product-reviews");
    }

    public static Link getReviewsByProductIDSelfLink(Integer id) {
        return linkTo(methodOn(ReviewsController.class)
                .getReviewsByProductID(id, PageRequest.of(0, 10)))
                .withSelfRel();
    }

    public static Link getWishlistByIDLink(Integer id) {
        return linkTo(methodOn(WishlistController.class)
                .getWishlistByID(id)).withRel("single-wishlist");
    }

    public static Link getWishlistByIDSelfLink(Integer id){
        return linkTo(methodOn(WishlistController.class)
                .getWishlistByID(id)).withSelfRel();
    }

    public static Link getWishlistByCustomerIDLink(Integer id){
        return linkTo(methodOn(WishlistController.class)
                .getWishlistsByCustomerID(id, PageRequest.of(0, 10)))
                .withRel("customer-wishlist");
    }

    public static Link getWishlistByCustomerIDSelfLink(Integer id){
        return linkTo(methodOn(WishlistController.class)
                .getWishlistsByCustomerID(id, PageRequest.of(0, 10)))
                .withSelfRel();
    }

    public static Link getWishlistByProductIDSelfLink(Integer id){
        return linkTo(methodOn(WishlistController.class)
                .getWishlistByProductID(id, PageRequest.of(0, 10)))
                .withSelfRel();
    }

    public static Link getWishlistByProductIDLink(Integer id){
        return linkTo(methodOn(WishlistController.class)
                .getWishlistByProductID(id, PageRequest.of(0, 10)))
                .withRel("product-wishlist");
    }

    public static Link getDiscountsByCustomerIDSelfLink(Integer id){
        return linkTo(methodOn(DiscountController.class)
                .getDiscountsByCustomerID(id, PageRequest.of(0, 10)))
                .withSelfRel();
    }

    public static Link getDiscountsByCustomerIDLink(Integer id){
        return linkTo(methodOn(DiscountController.class)
                .getDiscountsByCustomerID(id, PageRequest.of(0, 10)))
                .withRel("customer-discounts");
    }

    public static Link getDiscountByIDSelfLink(Integer id){
        return linkTo(methodOn(DiscountController.class)
                .getDiscountsByID(id))
                .withSelfRel();
    }

    public static Link getDiscountByIDLink(Integer id){
        return linkTo(methodOn(DiscountController.class)
                .getDiscountsByID(id))
                .withRel("single-discount");
    }
}
