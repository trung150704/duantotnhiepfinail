package com.mt.controller;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mt.entity.Product;
import com.mt.entity.Size;
import com.mt.service.ProductService;
import com.mt.service.SizeService;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;
	@Autowired
	private SizeService sizeService;

	@RequestMapping("/product/list")
	public String list(Model model, @RequestParam(value = "cid", required = false) Optional<String> cid,
			@RequestParam(value = "p", defaultValue = "0") int page,
			@RequestParam(value = "sort", defaultValue = "name") String sort,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@RequestParam(value = "priceFilter", required = false) Optional<String> priceFilter,
			@RequestParam(value = "priceDirection", required = false) Optional<String> priceDirection,
			@RequestParam(value = "productLine", required = false) Optional<String> productLine) {

		// Lấy danh sách dòng sản phẩm từ database
		List<String> productLines = productService.findDistinctProductLines();
		model.addAttribute("productLines", productLines);

		// Tạo Sort dựa trên các yêu cầu
		Sort sortOrder = priceDirection.map(dir -> Sort.by(Sort.Direction.fromString(dir), "price"))
				.orElseGet(() -> Sort.by(Sort.Direction.fromString(direction), sort));

		Pageable pageable = PageRequest.of(page, 21, sortOrder);
		Page<Product> productPage;

		// Lọc sản phẩm theo giá tiền và dòng sản phẩm
		if (productLine.isPresent()) {
			productPage = productService.findByDescribe(productLine.get(), pageable);
		} else if (priceFilter.isPresent()) {
			if (priceFilter.get().equals("greater")) {
				productPage = productService.findByPriceRange(500.0, Double.MAX_VALUE, pageable);
			} else if (priceFilter.get().equals("less")) {
				productPage = productService.findByPriceRange(0.0, 500.0, pageable);
			} else {
				productPage = productService.findAll(pageable);
			}
		} else if (cid.isPresent()) {
			productPage = productService.findByCategoryId(cid.get(), pageable);
		} else {
			productPage = productService.findAll(pageable);
		}

		// Thêm dữ liệu vào mô hình
		model.addAttribute("items", productPage.getContent());
		model.addAttribute("page", productPage);
		model.addAttribute("sort", sort);
		model.addAttribute("pageTitle", "Danh Sách Sản Phẩm");
		model.addAttribute("direction", direction);
		model.addAttribute("priceFilter", priceFilter.orElse(""));
		model.addAttribute("priceDirection", priceDirection.orElse(""));
		model.addAttribute("productLine", productLine.orElse(""));
		return "product/list";
	}

	@RequestMapping("/product/ListNam")
	public String ListNam(Model model, @RequestParam(value = "p", defaultValue = "0") int page,
			@RequestParam(value = "sort", defaultValue = "name") String sort,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {
		// Lấy danh sách dòng sản phẩm từ database
		List<String> productLines = productService.findDistinctProductLines();
		model.addAttribute("productLines", productLines);

		// Tạo Sort và Pageable
		Sort sortOrder = Sort.by(Sort.Direction.fromString(direction), sort);
		Pageable pageable = PageRequest.of(page, 15, sortOrder);

		// Gọi tất cả sản phẩm theo CategoryId "DM01"
		Page<Product> productPage = productService.findByCategoryId("DM01", pageable);

		// Thêm dữ liệu vào mô hình
		model.addAttribute("items", productPage.getContent());
		model.addAttribute("page", productPage);
		model.addAttribute("pageTitle", "Danh Sách Sản Phẩm Cho Nam");
		model.addAttribute("sort", sort);
		model.addAttribute("direction", direction);

		return "product/ListNam";
	}

	@RequestMapping("/product/ListNu")
	public String ListNu(Model model, @RequestParam(value = "p", defaultValue = "0") int page,
			@RequestParam(value = "sort", defaultValue = "name") String sort,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {
		// Lấy danh sách dòng sản phẩm từ database
		List<String> productLines = productService.findDistinctProductLines();
		model.addAttribute("productLines", productLines);
		// Tạo Sort và Pageable
		Sort sortOrder = Sort.by(Sort.Direction.fromString(direction), sort);
		Pageable pageable = PageRequest.of(page, 15, sortOrder);

		// Gọi tất cả sản phẩm theo CategoryId "DM01"
		Page<Product> productPage = productService.findByCategoryId("DM02", pageable);

		// Thêm dữ liệu vào mô hình
		model.addAttribute("items", productPage.getContent());
		model.addAttribute("page", productPage);
		model.addAttribute("pageTitle", "Danh Sách Sản Phẩm Cho Nữ");
		model.addAttribute("sort", sort);
		model.addAttribute("direction", direction);

		return "product/ListNu";
	}

	@RequestMapping("/product/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id,
			@RequestParam(value = "productLine", required = false) Optional<String> productLine) {

		Product item = productService.findById(id);
		List<Size> sizes = sizeService.findAll();
		DecimalFormat decimalFormat = new DecimalFormat("#,###.000");
		String formattedPrice = decimalFormat.format(item.getPrice()) + " VNĐ";

		List<Product> products = productService.findAll();

		model.addAttribute("items", products);
		model.addAttribute("item", item);
		model.addAttribute("sizes", sizes);
		model.addAttribute("formattedPrice", formattedPrice);
		model.addAttribute("pageTitle", item.getName());
		model.addAttribute("productLine", productLine);
		return "product/detail";
	}

	@RequestMapping("/product/Search")
	public String Search(Model model, 
	        @RequestParam(value = "query", required = false) String query, // Tham số tìm kiếm
	        @RequestParam(value = "p", defaultValue = "0") int page,
	        @RequestParam(value = "sort", defaultValue = "name") String sort,
	        @RequestParam(value = "direction", defaultValue = "asc") String direction) {

	    // Tạo Sort dựa trên các yêu cầu
	    List<Product> items = productService.findByDescribe(query);
	    Sort sortOrder = Sort.by(Sort.Direction.fromString(direction), sort);
	    Pageable pageable = PageRequest.of(page, 38, sortOrder);

	    Page<Product> productPage;

	    // Kiểm tra xem có truy vấn tìm kiếm hay không
	    if (query != null && !query.trim().isEmpty()) {
	        productPage = productService.searchByQuery(query, pageable); // Gọi phương thức tìm kiếm
	    } else {
	        productPage = productService.findAll(pageable); // Nếu không có truy vấn, hiển thị tất cả sản phẩm
	    }

	    // Tạo tiêu đề trang
	    String pageTitle = "Tìm kiếm sản phẩm";
	    if (query != null && !query.trim().isEmpty()) {
	        pageTitle += ": " + query; // Gắn thêm từ khóa tìm kiếm vào tiêu đề
	    }

	    // Thêm dữ liệu vào mô hình
	    model.addAttribute("items", productPage.getContent());
	    model.addAttribute("page", productPage);
	    model.addAttribute("sort", sort);
	    model.addAttribute("direction", direction);
	    model.addAttribute("totalProducts", productPage.getTotalElements());
	    model.addAttribute("productLine", query);
	    model.addAttribute("pageTitle", pageTitle); // Cập nhật tiêu đề vào model

	    return "product/Search";
	}


	@GetMapping("/products")
	public String listProducts(Model model) {
		List<Product> items = productService.findAll(); // Fetch all products from service
		model.addAttribute("items", items); // Add the items to the model
		return "product/list"; // Return the name of the Thymeleaf template to render
	}
	@RequestMapping("/product/sale")
    public String listSale(Model model) {
        model.addAttribute("items", productService.findAllSaleProducts());
        model.addAttribute("pageTitle", "Giảm Giá");
        return "product/sale";
    }
}
