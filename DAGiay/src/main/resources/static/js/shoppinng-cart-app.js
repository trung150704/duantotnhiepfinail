const app = angular.module("shopping-cart-app", []);

app.controller("shopping-cart-ctrl", function($scope, $http) {
	// Shopping cart object
	$scope.cart = {
		items: [],

		favorites: [],

		add(id) {
			let selectedSize = document.getElementById("size").value;
			let selectedQuantity = parseInt(document.getElementById("quantity").value);

			if (isNaN(selectedQuantity) || selectedQuantity < 1) {
				alert("Vui lòng chọn số lượng hợp lệ");
				document.getElementById("quantity").value = "";
				return;
			}

			var item = this.items.find(item => item.id === id && item.size === selectedSize); // Tìm item theo id và size
			if (item) {
				item.qty += selectedQuantity;
				this.saveToLocalStorage();
			} else {
				var cart = this;
				$http.get(`/rest/products/${id}`).then(resp => {
					resp.data.qty = selectedQuantity;
					//resp.data.qty = 1;
					resp.data.size = selectedSize;
					cart.items.push(resp.data);
					cart.saveToLocalStorage();
				});
			}
		},

		// Save cart to local storage
		saveToLocalStorage() {
			var json = JSON.stringify(angular.copy(this.items));
			localStorage.setItem("cart", json);
		},

		// Get total item count
		get count() {
			return this.items
				.map(item => item.qty)
				.reduce((total, qty) => total + qty, 0);
		},

		// Get total amount
		get amount() {
			return this.items
				.map(item => item.qty * item.price)
				.reduce((total, amount) => total + amount, 0);
		},

		// Load cart from local storage
		loadFromLocalStorage() { // Corrected method name
			let json = localStorage.getItem("cart");
			this.items = json ? JSON.parse(json) : [];
		},

		// Remove item from cart
		remove(id) {
			let index = this.items.findIndex(item => item.id === id);
			if (index > -1) {
				this.items.splice(index, 1);
				this.saveToLocalStorage();
			}
		},

		// Clear all items from cart
		clear() {
			this.items = [];
			this.saveToLocalStorage();
		},

		addToFavorites(id) {
			// Mặc định lấy số lượng là 1
			let selectedQuantity = 1;

			// Mặc định lấy giá trị đầu tiên của size
			let sizeElement = document.getElementById("size");
			let selectedSize = sizeElement.options[0]?.value || ""; // Lấy giá trị đầu tiên hoặc chuỗi rỗng nếu không có giá trị

			// Kiểm tra xem size có hợp lệ không
			if (!selectedSize) {
				alert("Không có size hợp lệ để thêm vào yêu thích.");
				return;
			}

			// Kiểm tra xem sản phẩm đã có trong danh sách yêu thích chưa
			var favorite = this.favorites.find(item => item.id === id && item.size === selectedSize);

			if (favorite) {
				// Nếu sản phẩm đã có trong yêu thích, hiển thị thông báo
				alert("Sản phẩm này đã có trong danh sách yêu thích.");
			} else {
				// Nếu sản phẩm chưa có trong danh sách yêu thích, tải thông tin sản phẩm từ API
				var cart = this;
				$http.get(`/rest/products/${id}`).then(resp => {
					resp.data.qty = selectedQuantity; // Gán số lượng mặc định là 1
					resp.data.size = selectedSize; // Gán size mặc định là giá trị đầu tiên
					cart.favorites.push(resp.data);
					cart.saveFavoritesToLocalStorage();
				});
			}
		},

		removeFavorite(id) {
			let index = this.favorites.findIndex(item => item.id === id);
			if (index > -1) {
				this.favorites.splice(index, 1);
				this.saveFavoritesToLocalStorage();
			}
		},

		saveFavoritesToLocalStorage() {
			var json = JSON.stringify(angular.copy(this.favorites));
			localStorage.setItem("favorites", json);
		},

		loadFavoritesFromLocalStorage() {
			let json = localStorage.getItem("favorites");
			this.favorites = json ? JSON.parse(json) : [];
		},

		saveToLocalStorage() {
			var json = JSON.stringify(angular.copy(this.items));
			localStorage.setItem("cart", json);
		},

		get totalFavoritesQty() {
			return this.favorites
				.map(favorite => favorite.qty)
				.reduce((total, qty) => total + qty, 0);
		},

		addToCartFromFavorites(item) {
			// Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
			let existingItem = this.items.find(cartItem => cartItem.id === item.id && cartItem.size === item.size);

			if (existingItem) {
				// Nếu sản phẩm đã có trong giỏ hàng, thông báo
				alert("Sản phẩm này đã có trong giỏ hàng.");
			} else {
				// Nếu chưa có, thêm sản phẩm vào giỏ hàng
				let cartItem = angular.copy(item);
				this.items.push(cartItem);
				this.saveToLocalStorage();
			}
		},
		addToFavoritesFromCart(item) {
			// Kiểm tra xem sản phẩm đã có trong yêu thích chưa
			let existingFavorite = this.favorites.find(favoriteItem => favoriteItem.id === item.id && favoriteItem.size === item.size);

			if (existingFavorite) {
				// Nếu sản phẩm đã có trong yêu thích, thông báo
				alert("Sản phẩm này đã có trong yêu thích.");
			} else {
				// Nếu chưa có, thêm sản phẩm vào yêu thích
				let favoriteItem = angular.copy(item);
				this.favorites.push(favoriteItem);
				this.saveFavoritesToLocalStorage();
			}
		},
	};
	$scope.cart.loadFromLocalStorage();
	$scope.cart.loadFavoritesFromLocalStorage();
	/*$http.get('/api/user').then(function(response) {
		$scope.user = response.data; // Lấy thông tin người dùng
	}).catch(function(error) {
		console.error("Lỗi khi lấy thông tin người dùng:", error);
		alert("Bạn cần đăng nhập trước khi sử dụng tính năng này!");
	});
*/
	$scope.loadUser = function() {
		$http.get('/api/user').then(resp => {
			if (resp.data && resp.data.username) {
				$scope.order.account = { username: resp.data.username };
			} else {
				alert("Vui lòng đăng nhập trước khi đặt hàng.");
			}
		});
	};

	$scope.loadUser();
	$scope.order = {
		hoten: "",
		sdt: "",
		email: "",
		address: "",
		city: "",
		district: "",
		ward: "",
		payment: "",
		account: { username: "" },
		status: true, 
		totalamount: 0, 

		get orderDetails() {
			return $scope.cart.items.map(item => {
				return {
					product: { id: item.id },
					price: item.price,
					quantity: item.qty
				};
			});
		},

		purchase() {
			if ($scope.cart.items.length === 0) {
				alert("Giỏ hàng hiện tại không có sản phẩm!");
				return;
			}
			// Lấy dữ liệu từ form HTML
			this.hoten = document.getElementById("name").value;
			this.sdt = document.getElementById("phone").value;
			this.email = document.getElementById("email").value;
			this.address = document.getElementById("address").value;
			this.city = document.getElementById("cityNameHidden").value;
			this.district = document.getElementById("districtNameHidden").value;
			this.ward = document.getElementById("wardNameHidden").value;
			this.payment = { id: document.querySelector('input[name="paymentMethod"]:checked')?.value };
			this.totalamount = document.getElementById("totalAmountInput").value;
			
			const nameRegex = /^[A-Za-zÀ-ỹ\s]+$/;
			if (!this.hoten || !nameRegex.test(this.hoten)) {
				alert("Vui lòng nhập họ tên hợp lệ (chỉ chứa chữ cái, có thể có dấu và không có ký tự đặc biệt).");
				document.getElementById("name").focus();
				return;
			}
			
			const phoneRegex = /^\d{10,11}$/;
			if (!this.sdt || !phoneRegex.test(this.sdt)) {
				alert("Vui lòng nhập số điện thoại hợp lệ (chỉ có số và độ dài từ 10-11 chữ số).");
				document.getElementById("phone").focus();
				return;
			}

			const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
			if (!this.email || !emailRegex.test(this.email)) {
				alert("Vui lòng nhập email hợp lệ.");
				document.getElementById("email").focus();
				return;
			}

			const addressRegex = /^[A-Za-z0-9À-ỹ\s]+$/;
			if (!this.address || !addressRegex.test(this.address)) {
				alert("Vui lòng nhập địa chỉ hợp lệ (không chứa ký tự đặc biệt).");
				document.getElementById("address").focus();
				return;
			}

			if (!this.city || !this.district || !this.ward) {
				alert("Vui lòng chọn đầy đủ thành phố, quận và phường.");
				return;
			}
			
			if (!this.hoten || !this.sdt || !this.address || !this.city || !this.district || !this.ward || !this.payment) {
				alert("Vui lòng điền đầy đủ thông tin đặt hàng!");
				return;
			}

			this.address = `${this.address}, ${this.ward}, ${this.district}, ${this.city}`;
			
			let order = angular.copy(this);
			$http.post("/rest/orders", order).then(resp => {
				const orderId = resp.data;
				alert("Đặt hàng thành công!");
				$scope.cart.clear(); 
				location.href = `/order/success?orderId=${orderId}`;
			}).catch(error => {
				alert("Đặt hàng thất bại!");
				console.error("Lỗi đặt hàng:", error);
			});
		}
	};

});





