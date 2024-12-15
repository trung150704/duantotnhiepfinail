var app = angular.module("admin-app", ["ngRoute"]);

// Chỉ hiện phần main trang index
app.run(["$rootScope", "$location", function ($rootScope, $location) {
  $rootScope.$on("$routeChangeSuccess", function () {
    // Kiểm tra xem đường dẫn có phải là trang index hay không
    $rootScope.isIndexPage = $location.path() === "" || $location.path() === "/";
  });
}]);

app.config(function($routeProvider) {
  $routeProvider
    .when("/product", {
      templateUrl: "/assets/admin/product/index.html",
      controller: "product-ctrl"
    })
    .when("/category", {
      templateUrl: "/assets/admin/category/index.html",
      controller: "category-ctrl"
    })
    .when("/order", {
      templateUrl: "/assets/admin/order/index.html",
      controller: "order-ctrl"
    })
    .when("/account", {
      templateUrl: "/assets/admin/account/index.html",
      controller: "account-ctrl"
    })
    .otherwise({
      template: "", // Rỗng cho trường hợp không khớp route
    });
});

