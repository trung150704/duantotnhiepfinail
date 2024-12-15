app.controller("order-ctrl", function($scope, $http) {
	$scope.orders = [];
	$scope.accounts = [];
	$scope.payments = [];
	$scope.form = {};

	$scope.initialize = function() {
		$http.get("/rest/orders").then(resp => {
			$scope.orders = resp.data;
			$scope.pager.page = 0;
		}).catch(error => {
			console.error("Lỗi khi tải order:", error);
		});

		$http.get("/rest/account1").then(resp => {
			$scope.accounts = resp.data;
		}).catch(error => {
			console.error("Lỗi khi tải accounts:", error);
		});

		$http.get("/rest/payments").then(resp => {
			$scope.payments = resp.data;
		}).catch(error => {
			console.error("Lỗi khi tải payments:", error);
		});
	};

	$scope.edit = function(order) {
		$scope.form = angular.copy(order);
		$scope.isEditable = false;
		$(".nav-tabs a:eq(0)").tab('show')
	};

	$scope.isEditable = false;

	function isValidName(name) {
		// Kiểm tra ký tự chữ (bao gồm có dấu) và khoảng trắng
		return /^[\p{L}\s]+$/u.test(name);
	}

	function isValidAddress(address) {
		// Kiểm tra ký tự chữ (bao gồm có dấu) và khoảng trắng
		return /^[\p{L}\d\s]+$/u.test(address);
	}

	function isValidEmail(email) {
		return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
	}

	function isValidPhone(phone) {
		return /^[0-9]{10,11}$/.test(phone);
	}
	function isValidPrice(totalamount) {
		return !isNaN(totalamount) && totalamount > 0;
	}

	$scope.reset = function() {
		$scope.form = {
			createdAt: new Date().toISOString().slice(0, 10),
			status: true,
		};
		$scope.isEditable = true;
	};

	$scope.create = function() {
		if (!$scope.form.hoten || !isValidName($scope.form.hoten)) {
			alert("Tên không được chứa số, kí tự đặc biết hoặc để trống.");
			return;
		}

		if (!$scope.form.address || !isValidAddress($scope.form.address)) {
			alert("Địa chỉ không chứa kí tự đặc biệt hoặc để trống.");
			return;
		}

		if (!$scope.form.sdt || !isValidPhone($scope.form.sdt)) {
			alert("Số điện thoại phải là số và có độ dài từ 10-11 ký tự.");
			return;
		}

		if (!$scope.form.email || !isValidEmail($scope.form.email)) {
			alert("Email không đúng định dạng.");
			return;
		}

		if (!$scope.form.totalamount || !isValidPrice($scope.form.totalamount)) {
			alert("Giá phải là số và lớn hơn 0.");
			return;
		}
		const order = angular.copy($scope.form);
		$http.post("/rest/orders/create", order).then(resp => {
			$scope.orders.push(resp.data);
			alert("Lưu thành công!");
			$scope.reset();
		}).catch(error => {
			console.error("Error creating order:", error);
			alert("Lưu thất bại order!");
		});
	};

	$scope.update = function() {
		if (!$scope.form.hoten || !$scope.form.sdt || !$scope.form.email || !$scope.form.address) {
			alert("Vui lòng điền đầy đủ thông tin!");
			return; // Dừng nếu có trường bị thiếu
		}
		const order = angular.copy($scope.form);
		$http.put(`/rest/orders/${order.id}`, order).then(resp => {
			const index = $scope.orders.findIndex(o => o.id === order.id);
			$scope.orders[index] = resp.data;
			alert("Cập nhật thành công!");
		}).catch(error => {
			console.error("Error updating order:", error);
			alert("Cập nhật thất bại order!");
		});
	};

	$scope.delete = function(order) {
		if (confirm("Bạn có chắc chắn muốn xóa?")) {
			$http.delete(`/rest/orders/${order.id}`).then(resp => {
				const index = $scope.orders.findIndex(o => o.id === order.id);
				$scope.orders.splice(index, 1);
				alert("Xóa thành công!");
				$scope.reset();
			}).catch(error => {
				console.error("Không thể xóa order:", error);
				alert("Xóa thất bại order!");
			});
		}
	};

	$scope.pager = {
		page: 0,
		size: 10,
		get items() {
			var start = this.page * this.size;
			return $scope.orders.slice(start, start + this.size); 
		},
		get count() {
			return Math.ceil($scope.orders.length / this.size); 
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
	};

	$scope.initialize();
});

