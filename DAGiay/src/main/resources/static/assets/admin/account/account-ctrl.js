app.controller("account-ctrl", function($scope, $http) {
	$scope.accounts = [];
	$scope.form = {};

	// Khởi tạo và tải dữ liệu từ API
	$scope.initialize = function() {
		$http.get("/rest/account1").then(resp => {
			$scope.accounts = resp.data; // Lưu dữ liệu trả về vào biến accounts
			$scope.pager.page = 0; // Đặt trang bắt đầu là 0
		}).catch(error => {
			console.error("Error loading accounts:", error);
		});
	};

	// Sửa dữ liệu tài khoản
	$scope.edit = function(account) {
		$scope.form = angular.copy(account);
		$scope.isEditable = false;
		$(".nav-tabs a:eq(0)").tab('show');
	};

	$scope.isEditable = false;

	$scope.reset = function() {
		$scope.form = {
			created_at: new Date().toISOString().slice(0, 10),
			role: false,
		};
		$scope.isEditable = true;
	};

	$scope.delete = function(account) {
		if (confirm("Bạn có chắc chắn muốn xóa?")) {
			$http.delete(`/rest/account1/${account.username}`).then(resp => {
				if (resp.status === 204) {
					const index = $scope.accounts.findIndex(a => a.username === account.username);
					if (index !== -1) {
						$scope.accounts.splice(index, 1);
						alert("Xóa thành công!");
						$scope.reset();
					}
				} else {
					alert("Xóa thất bại!");
				}
			}).catch(error => {
				console.error("Không thể xóa account:", error);
				alert("Xóa thất bại account!");
			});
		}
	};

	$scope.create = function() {
		const account = angular.copy($scope.form);
		$http.post("/rest/account1/create", account).then(resp => {
			$scope.accounts.push(resp.data);
			alert("Lưu thành công!");
			$scope.reset();
		}).catch(error => {
			console.error("Error creating account:", error);
			alert("Lưu thất bại account!");
		});
	};
	$scope.update = function() {
		const account = angular.copy($scope.form);
		$http.put(`/rest/account1/${account.id}`, account).then(resp => {
			const index = $scope.accounts.findIndex(o => o.id === account.id);
			$scope.orders[index] = resp.data;
			alert("Cập nhật thành công!");
		}).catch(error => {
			console.error("Error updating account:", error);
			alert("Cập nhật thất bại account!");
		});
	};
	// Phân trang
	$scope.pager = {
		page: 0,
		size: 10,
		get items() {
			var start = this.page * this.size;
			return $scope.accounts.slice(start, start + this.size); // Lấy trang cần hiển thị
		},
		get count() {
			return Math.ceil($scope.accounts.length / this.size); // Tính tổng số trang
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

	// Khởi tạo khi controller load
	$scope.initialize();
});
