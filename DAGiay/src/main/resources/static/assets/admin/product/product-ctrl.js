app.controller("product-ctrl", function($scope, $http) {
	$scope.items = [];
	$scope.cates = [];
	$scope.sizes = [];
	$scope.sizeProducts = [];
	$scope.originalForm = {};
	$scope.form = {
		selectedSizes: {},
		counts: {},
		productId: null
	};
	$scope.selectProduct = function(productId) {
		// Gán productId vào form
		$scope.form.productId = productId;
		console.log("Selected Product ID: " + $scope.form.productId);
	};
	$scope.onSizeSelect = function(sizeId) {
		const productId = $scope.form.productId;//lấy id sản phẩm

		if (!productId) {
			console.error("Product ID is not set. Please select a product first.");
			return;
		}

		if (!$scope.form.selectedSizes) {
			$scope.form.selectedSizes = {};
		}

		if (!$scope.form.counts) {
			$scope.form.counts = {};
		}

		// Nếu counts chưa có giá trị cho sizeId, thiết lập mặc định là 0
		if (!$scope.form.counts[sizeId]) {
			$scope.form.counts[sizeId] = 0;  // Khởi tạo count mặc định là 0
		}

		// Kiểm tra nếu checkbox được chọn hay bỏ chọn
		if ($scope.form.selectedSizes[sizeId]) {
			// Nếu checkbox được chọn, lấy giá trị số lượng
			$http.get(`/rest/products/size/${sizeId}/product/${productId}/quantity`)
				.then(function(response) {
					$scope.form.counts[sizeId] = response.data; // Lưu giá trị count vào form.counts
					console.log(`Cập nhật Size: ${sizeId}: `, response.data);
					//$scope.addSizeProduct({ id: sizeId }, $scope.form.counts[sizeId]);  // Thêm size sau khi lấy được số lượng
				})
				.catch(function(error) {
					console.error("Error fetching quantity for size:", error);
				});
		} else {
			delete $scope.form.counts[sizeId];
			delete $scope.form.selectedSizes[sizeId];
		}
	};


	// Thêm size và số lượng
	$scope.addSizeProduct = function(size, count) {
		// Kiểm tra nếu $scope.form.sizeProducts đã được khởi tạo
		if (!$scope.form.sizeProducts) {
			$scope.form.sizeProducts = [];  // Khởi tạo là mảng trống nếu chưa có
		}

		console.log("Adding size:", size);
		console.log("Adding count:", count);

		// Kiểm tra nếu $scope.items và $scope.form.sizeProducts đã được khởi tạo
		if (!$scope.items || !$scope.items.length) {
			console.error("Items list is empty or undefined.");
			alert("Danh sách sản phẩm không hợp lệ.");
			return;
		}

		if (!$scope.form || !$scope.form.sizeProducts) {
			console.error("Size products list is undefined.");
			alert("Danh sách kích cỡ sản phẩm không hợp lệ.");
			return;
		}

		// Kiểm tra size đã tồn tại trong form.sizeProducts
		const existing = $scope.form.sizeProducts.find(sp => sp.size.id === size.id);
		if (existing) {
			existing.count = count; // Cập nhật count nếu size đã tồn tại
		} else {
			$scope.form.sizeProducts.push({ size: size, count: count }); // Thêm mới
		}

		// Cập nhật danh sách size cho sản phẩm hiện tại
		const product = $scope.items.find(item => item.id === $scope.form.productId);
		if (!product) {
			alert("Sản phẩm không tồn tại.");
			return;
		}

		console.log("Cập nhật số lượng sản phẩm thành công.");
	};
	// Khởi tạo dữ liệu ban đầu
	$scope.initialize = function() {
		// Load danh sách sản phẩm
		$http.get("/rest/products").then(resp => {
			$scope.items = resp.data;
			$scope.items.forEach(item => {
				item.create_at = new Date(item.create_at);
			});
		});

		// Load danh mục
		$http.get("/rest/categories").then(resp => {
			$scope.cates = resp.data;
		});
		$http.get("/rest/sizes").then(resp => {
			$scope.sizes = resp.data;
		});
	}

	$scope.initialize();

	// Reset form
	$scope.reset = function() {
		$scope.form = {
			images: 'Black.jpg',
			sizeProducts: [],
			category: [],
			selectedSizes: {},
			counts: {},
			productId: null
		};
	};
	$scope.edit = function(item) {
		if (item && item.id) {
			console.log("Editing Product ID: " + item.id);
			$scope.form = angular.copy(item);
			$scope.form.productId = item.id;

			$scope.originalForm = angular.copy($scope.form);
			// Đảm bảo sizeProducts được khởi tạo là mảng trống nếu không có
			if (!$scope.form.sizeProducts) {
				$scope.form.sizeProducts = [];
			}

			$(".nav-tabs a:eq(0)").tab('show');
			console.log("Editing Product ID: " + $scope.form.productId);
		} else {
			console.error("Product ID is not available.");
		}
	};
	// Kiểm tra nếu form đã thay đổi
	$scope.hasFormChanged = function() {
		return !angular.equals($scope.form, $scope.originalForm); // So sánh form hiện tại và bản gốc
	};
	// Tạo sản phẩm mới
	$scope.create = function() {
		// Kiểm tra thông tin bắt buộc
		if (!$scope.form.name || !$scope.form.price || !$scope.form.category || !$scope.form.describe) {
			alert("Vui lòng nhập đầy đủ thông tin cần thiết.");
			return;
		}

		// Kiểm tra giá sản phẩm phải là số dương
		if (isNaN($scope.form.price) || $scope.form.price <= 0) {
			alert("Giá sản phẩm phải là số dương.");
			return;
		}

		// Tạo bản sao của form và thiết lập ngày tạo mới
		var item = angular.copy($scope.form);
		item.create_at = new Date(); // Cập nhật thời gian tạo

		// Gửi yêu cầu POST để thêm sản phẩm mới
		$http.post('/rest/products', item).then(resp => {
			resp.data.create_at = new Date(resp.data.create_at);
			$scope.items.push(resp.data);
			$scope.reset();
			alert("Thêm mới sản phẩm thành công");
		}).catch(error => {
			alert("Lỗi thêm mới sản phẩm: " + (error.data.message || "Lỗi không xác định"));
			console.log("Error", error);
		});
	};

	// Cập nhật sản phẩm
	$scope.update = function() {
		if (angular.equals($scope.form, $scope.originalForm)) {
			alert("Không có thay đổi gì trên form.");
			return;
		}
		var item = angular.copy($scope.form);
		$http.put(`/rest/products/${item.id}`, item).then(resp => {
			var index = $scope.items.findIndex(p => p.id === item.id);
			$scope.items[index] = angular.copy(resp.data);
			alert("Cập nhật thành công!");
			$scope.originalForm = angular.copy($scope.form);
			// Cập nhật số lượng cho từng size đã chọn
			for (const sizeId in $scope.form.selectedSizes) {
				if ($scope.form.selectedSizes[sizeId]) {
					const newCount = $scope.form.counts[sizeId];
					if (newCount > 0) {
						// Cập nhật số lượng cho size trong database
						$scope.updateSizeProductCount(sizeId, newCount);
					}
				}
			}

		}).catch(error => {
			alert("Lỗi cập nhật sản phẩm");
			console.log("Error", error);
		});
	}
	$scope.validateCount = function(count) {
		if (!count || isNaN(count)) {
			alert("Số lượng phải là một số hợp lệ.");
			return false;
		}
		count = parseInt(count, 10);  // Chuyển giá trị thành số nguyên
		console.log("Validating count: ", count);  // Kiểm tra giá trị

		if (count <= 0) {
			alert("Số lượng phải là số dương.");
			return false;
		}
		return true;
	};
	$scope.updateSizeProductCount = function(sizeId, newCount) {
		console.log("Received sizeId:", sizeId);
		console.log("Received newCount:", newCount);

		if (!sizeId || !newCount || isNaN(newCount) || newCount <= 0) {
			console.error("Invalid count received: ", newCount);
			alert("Số lượng phải là một số hợp lệ và lớn hơn 0.");
			return;
		}

		// Tiến hành cập nhật nếu count hợp lệ
		$http.put(`/rest/products/size/${sizeId}/product/${$scope.form.productId}/quantity`, newCount)
			.then(function(response) {
				if (response.status === 200) {
					console.log("Cập nhật số lượng thành công.");
				} else {
					alert("Cập nhật thất bại.");
				}
			})
			.catch(function(error) {
				console.error("Error updating count:", error);
				alert("Lỗi khi cập nhật.");
			});
	};

	// Xóa sản phẩm
	$scope.delete = function(item) {
		$http.delete(`/rest/products/${item.id}`).then(resp => {
			var index = $scope.items.findIndex(p => p.id === item.id);
			$scope.items.splice(index, 1);
			$scope.reset();
			alert("Xóa sản phẩm thành công");
		}).catch(error => {
			alert("Lỗi xóa sản phẩm"); -
				console.log("Error", error);
		});
	}

	// Xử lý khi thay đổi ảnh
	$scope.imageChanged = function(files) {
		var data = new FormData();
		data.append('file', files[0]);
		$http.post('/rest/upload/images', data, {
			transformRequest: angular.identity,
			headers: { 'Content-Type': undefined }
		}).then(resp => {
			$scope.form.images = resp.data.name;  // Đảm bảo trường 'images' được cập nhật đúng
			console.log('Ảnh đã được cập nhật:', $scope.form.images);
		}).catch(error => {
			alert("Lỗi upload hình ảnh");
			console.log("Error", error);
		});
	}
	$scope.addSizeProduct = function(size, count) {
		// Kiểm tra nếu sizeProducts chưa được khởi tạo
		if (!$scope.form.sizeProducts) {
			$scope.form.sizeProducts = [];  // Khởi tạo là mảng trống nếu chưa có
		}

		// Kiểm tra nếu size đã tồn tại trong form.sizeProducts
		const existing = $scope.form.sizeProducts.find(sp => sp.size.id === size.id);
		if (existing) {
			existing.count = count; // Cập nhật count nếu size đã tồn tại
		} else {
			$scope.form.sizeProducts.push({ size: size, count: count }); // Thêm mới
		}

		// Cập nhật danh sách size cho sản phẩm hiện tại
		const product = $scope.items.find(item => item.id === $scope.form.productId);
		if (!product) {
			alert("Sản phẩm không tồn tại.");
			return;
		}

		console.log("Cập nhật số lượng sản phẩm thành công.");
		//console.log("$scope.form.sizeProducts:", $scope.form.sizeProducts);
	};


	// Xóa size sản phẩm
	$scope.removeSizeProduct = function(size) {
		const index = $scope.form.sizeProducts.findIndex(sp => sp.size.id === size.id);
		if (index >= 0) {
			$scope.form.sizeProducts.splice(index, 1);
		}
	};

	// Phân trang
	$scope.pager = {
		page: 0,
		size: 10,
		get items() {
			var start = this.page * this.size;
			return $scope.items.slice(start, start + this.size);
		},
		get count() {
			return Math.ceil(1.0 * $scope.items.length / this.size);
		},
		first() {
			this.page = 0;
		},
		prev() {
			this.page = Math.max(this.page - 1, 0);
		},
		next() {
			this.page = Math.min(this.page + 1, this.count - 1);
		},
		last() {
			this.page = this.count - 1;
		}
	}
});
