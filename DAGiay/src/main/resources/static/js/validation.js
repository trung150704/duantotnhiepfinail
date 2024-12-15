document.addEventListener("DOMContentLoaded", function() {
    const token = "7d0ed6b7-96b2-11ef-b17f-4603c5b85b86"; 
    const shopId = "5424124"; 
    const shippingFeeEl = document.getElementById("shippingFee");

    // Lấy tỉnh thành từ GHN API
    fetch('https://online-gateway.ghn.vn/shiip/public-api/master-data/province', {
        headers: {
            'Content-Type': 'application/json',
            'Token': token
        }
    })
    .then(response => response.json())
    .then(data => {
        let citySelect = document.getElementById("city");
        data.data.forEach(province => {
            let option = document.createElement("option");
            option.value = province.ProvinceID; // Lưu ID
            option.text = province.ProvinceName;
            citySelect.add(option);
        });
    });

    // Lấy quận/huyện khi chọn tỉnh
    document.getElementById("city").addEventListener("change", function() {
        let provinceId = this.value;
        fetch(`https://online-gateway.ghn.vn/shiip/public-api/master-data/district?province_id=${provinceId}`, {
            headers: {
                'Content-Type': 'application/json',
                'Token': token
            }
        })
        .then(response => response.json())
        .then(data => {
            let districtSelect = document.getElementById("district").querySelector("select");
            districtSelect.innerHTML = '<option value="0">Chọn Quận/Huyện</option>';
            data.data.forEach(district => {
                let option = document.createElement("option");
                option.value = district.DistrictID; // Lưu ID
                option.text = district.DistrictName; // Hiển thị tên
                districtSelect.add(option);
            });
        });
    });

    // Lấy phường/xã khi chọn quận/huyện
    document.getElementById("district").querySelector("select").addEventListener("change", function() {
        let districtId = this.value;
        fetch(`https://online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=${districtId}`, {
            headers: {
                'Content-Type': 'application/json',
                'Token': token
            }
        })
        .then(response => response.json())
        .then(data => {
            let wardSelect = document.getElementById("ward");
            wardSelect.innerHTML = '<option value="0">Chọn Phường/Xã</option>';
            data.data.forEach(ward => {
                let option = document.createElement("option");
                option.value = ward.WardCode; // Lưu mã
                option.text = ward.WardName; // Hiển thị tên
                wardSelect.add(option);
            });
        });
    });
	
    // Tính phí vận chuyển khi chọn phường/xã
    document.getElementById("ward").addEventListener("change", function() {
        //let citySelect = document.getElementById("city");
        let districtSelect = document.getElementById("district").querySelector("select");

        let districtId = parseInt(districtSelect.value, 10);
        let wardCode = this.value;

        fetch('https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Token': token,
                'ShopId': shopId
            },
            body: JSON.stringify({
					"from_district_id": districtId,
				    "to_district_id": districtId,
				    "to_ward_code": wardCode,
				    "weight": 1000,  // tổng khối lượng đơn hàng
				    "length": 30,
				    "width": 20,
				    "height": 10,
				    "service_type_id": 2, 
				    "items": [
				        {
				            "name": "Tên sản phẩm",
				            "quantity": 1,
				            "weight": 1000,  // khối lượng của từng sản phẩm
				            "length": 30,
				            "width": 20,
				            "height": 10
				        }
				    ]
            })
        })
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
				let shippingFee = data.data.total;
				shippingFeeEl.textContent = `${new Intl.NumberFormat('vi-VN').format(shippingFee)} VNĐ`;				
				updateTotalAmount();
            } else {
                shippingFeeEl.textContent = "Không thể tính phí";
                console.error("Lỗi tính phí:", data.message);
            }
        })
        .catch(error => {
            console.error("Lỗi kết nối API:", error);
        });
    });
	function updateTotalAmount() {
	    const cartAmountEl = document.getElementById("cartAmount");
	    const shippingFeeEl = document.getElementById("shippingFee"); 
	    const totalAmountEl = document.getElementById("totalAmount"); 

	    const cartAmount = parseFloat(cartAmountEl.textContent.replace(/\D/g, '')) || 0;
	    const shippingFee = parseFloat(shippingFeeEl.textContent.replace(/\D/g, '')) || 0;

	    const totalAmount = cartAmount + shippingFee;
	    totalAmountEl.textContent = `${totalAmount.toLocaleString('vi-VN')} VNĐ`;
		
		document.getElementById("totalAmountInput").value = totalAmount;
	}


});

document.getElementById("city").addEventListener("change", function() {
	const cityName = this.options[this.selectedIndex].text;
	document.getElementById("cityNameHidden").value = cityName;
});

document.getElementById("districtSelect").addEventListener("change", function() {
	const districtName = this.options[this.selectedIndex].text;
	document.getElementById("districtNameHidden").value = districtName;
});

document.getElementById("ward").addEventListener("change", function() {
	const wardName = this.options[this.selectedIndex].text;
	document.getElementById("wardNameHidden").value = wardName;
});

document.querySelector("form").addEventListener("submit", function(event) {
    const paymentMethod = document.querySelector("input[name='paymentMethod']:checked").value;

    if (paymentMethod === "2") {
        event.preventDefault();
        window.location.href = "/order/qrcode";
    }
});


function validateForm() {
	let name = document.getElementById("name").value.trim();
	let phone = document.getElementById("phone").value.trim();
	let email = document.getElementById("email").value.trim();
	let address = document.getElementById("address").value.trim();
	let citySelect = document.getElementById("city");
	let cityId = citySelect.value;
	let cityName = citySelect.options[citySelect.selectedIndex].text;

	let districtSelect = document.getElementById("district").querySelector("select");
	let districtId = districtSelect.value;
	let districtName = districtSelect.options[districtSelect.selectedIndex].text;

	let wardSelect = document.getElementById("ward");
	let wardId = wardSelect.value;
	let wardName = wardSelect.options[wardSelect.selectedIndex].text;
	console.log("City Name:", cityName);
	console.log("District Name:", districtName);
	console.log("Ward Name:", wardName);
	if (name === "" || phone === "" || email === "" || address === "" || cityId === "0" || districtId === "0" || wardId === "0") {
		return false;
	}

	let nameRegex = /^[A-Za-zÀ-ỹ\s]+$/;
	if (!nameRegex.test(name)) {
		return false;
	}

	let phoneRegex = /^[0-9]{10}$/;
	if (!phoneRegex.test(phone)) {
		return false;
	}
	let emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
	if (!emailRegex.test(email)) {
		return false;
	}

	
	let formattedString = `${address}, ${wardName}, ${districtName}, ${cityName}`;

	let dataToSend = {
		name: name,
		phone: phone,
		email: email,
		address: formattedString
	};
	return true;
}
