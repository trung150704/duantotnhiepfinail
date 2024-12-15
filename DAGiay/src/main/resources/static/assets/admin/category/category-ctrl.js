app.controller("category-ctrl", function($scope, $http){
    $scope.categories = [];
    $scope.form = {};

    $scope.initialize = function(){
        // Load categories
        $http.get("/rest/categories").then(resp => {
            $scope.categories = resp.data;
        });
    }

    $scope.initialize();
    
    $scope.reset = function(){
        $scope.form = {};
    }

    $scope.edit = function(category){
        $scope.form = angular.copy(category);
        $(".nav-tabs a:eq(0)").tab('show')
    }

   $scope.create = function(){
    var category = angular.copy($scope.form);
    $http.post('/rest/categories', category).then(resp => {
        $scope.categories.push(resp.data);
        $scope.reset();
        alert("Thêm mới danh mục thành công");
    }).catch(error => {
        if (error.status === 409) {
            // Kiểm tra mã lỗi từ server (409 Conflict)
            alert("Mã hoặc tên danh mục đã tồn tại. Vui lòng kiểm tra lại!");
        } else {
            alert("Lỗi thêm mới danh mục");
        }
        console.log("Error", error);
    });
}


    $scope.update = function(){
        var category = angular.copy($scope.form);
        $http.put(`/rest/categories/${category.id}`, category).then(resp => {
            var index = $scope.categories.findIndex(c => c.id == category.id);
            $scope.categories[index] = category;
            alert("Cập nhật danh mục thành công");
        }).catch(error => {
            alert("Lỗi cập nhật danh mục");
            console.log("Error", error);
        });
    }

    $scope.delete = function(category){
        $http.delete(`/rest/categories/${category.id}`).then(resp => {
            var index = $scope.categories.findIndex(c => c.id == category.id);
            $scope.categories.splice(index, 1);
            $scope.reset();
            alert("Xóa danh mục thành công");
        }).catch(error => {
            alert("Lỗi xóa danh mục");
            console.log("Error", error);
        });
    }
    
    $scope.pager = {
        page: 0,
        size: 10,
        get items(){
            var start = this.page * this.size;
            return $scope.categories.slice(start, start + this.size);
        },
        get count(){
            return Math.ceil(1.0 * $scope.categories.length / this.size);
        },
        first(){
            this.page = 0;
        },
        prev(){
            this.page--;
            if(this.page < 0){
                this.last();
            }
        },
        next(){
            this.page++;
            if(this.page >= this.count){
                this.first();
            }
        },
        last(){
            this.page = this.count - 1;
        }
    }
});
