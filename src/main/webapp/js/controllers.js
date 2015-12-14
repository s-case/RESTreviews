var restreviewsControllers = angular.module('restreviewsControllers', []);

restreviewsControllers.controller('MainCtrl', ['$scope', '$http', '$location', 'Notification', '$q',
  function ($scope, $http, $location, Notification, $q) {

    $scope.productList = [];

    $http.get('api/product').then(function(response) {
      var data = response.data;
      var deferred = $q.defer();
      var urlCalls = [];
      angular.forEach(data.linklist, function(link) {
        var id = link.linkURI.split('product/')[1];
        if(link.linkType === 'Child') {
          urlCalls.push($http.get('api/product/' + id));
        }
      });
      $q.all(urlCalls).then(function(results) {
        deferred.resolve(console.log('resolved'));
        angular.forEach(results, function(result) {
          var id = result.data.productId;
          var route = '#/products/' + id;
          var listItem = {
            id: id,
            name: result.data.title,
            url: route,
            description: result.data.description,
            imageUrl: result.data.imageUrl
          };
          $scope.productList.push(listItem);
        });
      });
    });


    $scope.search = function(text) {
      $location.path('/search').search({text: text});
    };

  }
]);

restreviewsControllers.controller('SearchCtrl', ['$scope', '$http', '$location', '$q', 'Notification',
  function ($scope, $http, $location, $q, Notification) {

    $scope.products = [];

    $http.get('api/Algosearch?searchProductTitle=true&searchProductCategory=true&searchProductDescription=true&searchKeyword=' + $location.search().text).then(function(response) {
      var data = response.data;
      var deferred = $q.defer();
      var urlCalls = [];
      if(data.linklist && data.linklist.length > 1) {
        angular.forEach(data.linklist, function(link) {
          var id = link.linkURI.split('product/')[1];
          urlCalls.push($http.get('api/product/' + id));
        });
        $q.all(urlCalls).then(function(results) {
          deferred.resolve(console.log('resolved'));
          angular.forEach(results, function(result) {
            var id = result.data.productId;
            var route = '#/products/' + id;
              var listItem = {
                id: id,
                name: result.data.title,
                url: route
              };
              $scope.products.push(listItem);
          });
        });
        Notification("Search completed");
      } else if(data.linklist) {
        var id = data.linklist.linkURI.split('product/')[1];
        urlCalls.push($http.get('api/product/' + id));
        $q.all(urlCalls).then(function(results) {
          deferred.resolve(console.log('resolved'));
          angular.forEach(results, function(result) {
            var id = result.data.productId;
            var route = '#/products/' + id;
              var listItem = {
                id: id,
                name: result.data.title,
                url: route
              };
              $scope.products.push(listItem);
          });
        });
        Notification("Search completed");
      }
    });
  }
]);

restreviewsControllers.controller('SigninCtrl', ['$scope', '$http', '$location', '$cookies', 'Notification',
  function ($scope, $http, $location, $cookies, Notification) {

    $scope.signin = function(username, password) {
      var credentials = username.concat(':').concat(password);
      $http.get('api/account', {headers: {'Authorization': 'Basic ' + btoa(credentials)}}).then(function(response) {
        $location.path('/');
        $cookies.putObject('headers', {headers: {'Authorization': 'Basic ' + btoa(credentials)}});
        var id = null;
        console.log(response);
        angular.forEach(response.data.linklist, function(link) {
          console.log(link);
          if(link.linkType === "Child" && link.linkRel === username) {
            id = link.idType;
          }
        });
        $http.get('api/account/' + id, $cookies.getObject('headers')).then(function(response) {
          $cookies.put('user', username);
          var account = {
            id: response.data.accountId,
            email: response.data.email,
            username: response.data.username,
            creditCard: response.data.creditCard,
            expYear: response.data.expYear,
            expMonth: response.data.expMonth
          };
          $cookies.putObject('account', account);
          Notification("Login succesful. Welcome " + username);
        });
      }, function(response) {
        $location.path('/signup');
        Notification.error("Wrong credentials");
      });
    };

    $scope.signout = function() {
      console.log('signout');
      $cookies.remove('user');
      $cookies.remove('account');
      $cookies.remove('headers');
      $location.path('#/');
    };

  }
]);

restreviewsControllers.controller('ProductCtrl', ['$scope', '$http', '$location', '$routeParams', '$cookies', 'Notification', '$rootScope',
  function ($scope, $http, $location, $routeParams, $cookies, Notification, $rootScope) {

    var productid = $routeParams.productId;
    $scope.productid = productid;
    var deleteLink = null;
    var updateLink = null;
    var reviewsLink = null;
    $scope.newProduct = true;

    $scope.reviews = [];

    if(productid) {
      $http.get('api/product/' + productid, $cookies.getObject('headers')).then(function(response) {
        $scope.product = response.data;
          angular.forEach($scope.product.linklist, function(link) {
            if(link.linkVerb === 'DELETE') {
              deleteLink = link.linkURI.split('reviews/')[1];
            }
            if(link.linkVerb === 'PUT') {
              updateLink = link.linkURI.split('reviews/')[1];
            }
            if(link.linkVerb === 'GET' && link.linkType === 'Child') {
              reviewsLink = link.linkURI.split('reviews/')[1];
            }
          });
          $http.get(reviewsLink, $cookies.getObject('headers')).then(function(response) {
            var data = response.data;
            angular.forEach(data.linklist, function(link) {
              var id = link.linkURI.split('review/')[1];
              var route = '#/products/' + productid + '/reviews/' + id;
              var listItem = {
                id: id,
                name: link.linkRel,
                url: route
              };
              if(link.linkType === 'Child') {
                $scope.reviews.push(listItem);
                console.log(listItem);
              }
            });
          });
      });
    }

    if($location.path().indexOf('edit') > -1) {
      $scope.newProduct = false;
    }

    $scope.submit = function() {
      $http.post('api/product', $scope.product, $cookies.getObject('headers')).then(function(response) {
        $location.path('/');
        Notification("Product added succesfully");
      });
    };

    $scope.update = function() {
      $http.put(updateLink, $scope.product, $cookies.getObject('headers')).then(function(response) {
        $location.path('/products/' + productid);
        Notification("Product updated succesfully");
      });
    };

    $scope.delete = function() {
      $http.delete(deleteLink, $cookies.getObject('headers')).then(function(response) {
        $location.path('/');
        Notification("Product deleted succesfully");
      });
    };

    $scope.disableBuy = false;

    $scope.buy = function() {
      $scope.disableBuy = true;
      var now = new Date();
      var purchase = {
        sku: $scope.product.productId,
        date: now.toISOString(),
        quantity: 1
      };
      $http.post('api/account/' + $rootScope.getUser().id + '/purchase', purchase, $cookies.getObject('headers')).then(function(response) {
        $http.get('api/AlgoscasedemoResource?description=purchase&to=' + $rootScope.getUser().email +
          '&exp_year=' + $rootScope.getUser().expYear +
          '&domain=sandbox97122.mailgun.org&apikey=key-28malnpd9dzfw1nny5eapyid35w-t5j9&card_number=' +  $rootScope.getUser().creditCard +
          '&exp_month=' +  $rootScope.getUser().expMonth +
          '&from=restreviews@example.com&currency=EUR&subject=Order completed&amount=' + Math.round($scope.product.price) +
          '&text='+$scope.product.title + ' was purchased! Thank you.',
          $cookies.getObject('headers')).then(function(response) {
            $scope.disableBuy = false;
            $location.path('/user/' + $rootScope.getUser().id);
            Notification("Order succesful. You'll receive an email shortly");
        });
      });
    };

  }
]);

restreviewsControllers.controller('ReviewCtrl', ['$scope', '$http', '$location', '$routeParams', '$cookies', 'Notification',
  function ($scope, $http, $location, $routeParams, $cookies, Notification) {

    var productid = $routeParams.productId;
    var reviewid = $routeParams.reviewId;
    $scope.productid = productid;
    $scope.reviewid = reviewid;
    var deleteLink = null;
    var updateLink = null;
    $scope.newReview = true;

    if(reviewid) {
      $http.get('api/product/' + productid + '/review/' + reviewid, $cookies.getObject('headers')).then(function(response) {
        $scope.review = response.data;
        $scope.review.rating = parseInt($scope.review.rating);
          angular.forEach($scope.review.linklist, function(link) {
            if(link.linkVerb === 'DELETE') {
              deleteLink = link.linkURI.split('reviews/')[1];
            }
            if(link.linkVerb === 'PUT') {
              updateLink = link.linkURI.split('reviews/')[1];
            }
          });
          $http.get('api/AlgosentimentAnalysis?text='+$scope.review.review, $cookies.getObject('headers')).then(function(response) {
            $scope.review.sentiment = response.data.sentiment;
          });
      });
    }

    if($location.path().indexOf('edit') > -1) {
      $scope.newReview = false;
    }

    $scope.submit = function() {
      console.log(productid);
      $http.post('api/product/' + productid + '/review', $scope.review, $cookies.getObject('headers')).then(function(response) {
        $location.path('/products/' + productid);
        Notification("Review created succesfully");
      });
    };

    $scope.update = function() {
      console.log(productid);
      $http.put(updateLink, $scope.review, $cookies.getObject('headers')).then(function(response) {
        $location.path('/products/' + productid + '/reviews/' + reviewid);
        Notification("Review updated succesfully");
      });
    };

    $scope.delete = function() {
      console.log(productid);
      $http.delete(deleteLink, $cookies.getObject('headers')).then(function(response) {
        console.log('success');
        $location.path('/products/' + productid);
        Notification("Review deleted succesfully");
      });
    };

  }
]);


restreviewsControllers.controller('UserCtrl', ['$scope', '$http', '$location', '$routeParams', '$cookies', 'Notification',
  function ($scope, $http, $location, $routeParams, $cookies, Notification) {

    var userid = $routeParams.userId;

    if(userid) {
      $http.get('api/account/' + userid, $cookies.getObject('headers')).then(function(response) {
        $scope.username = response.data.username;
        $scope.email = response.data.email;
        $scope.password = response.data.password;
        $scope.creditCard = response.data.creditCard;
        $scope.expMonth = response.data.expMonth;
        $scope.expYear = response.data.expYear;
      });
    }

    $scope.save = function() {
      var data = {
        username: $scope.username,
        email: $scope.email,
        password: $scope.password,
        creditCard: $scope.creditCard,
        expMonth: $scope.expMonth,
        expYear: $scope.expYear
      };
      $http.put('api/account/' + userid, data, $cookies.getObject('headers')).then(function(response) {
        var account = {
          id: response.data.accountId,
          email: response.data.email,
          username: response.data.username,
          creditCard: response.data.creditCard,
          expYear: response.data.expYear,
          expMonth: response.data.expMonth
        };
        var credentials = data.username.concat(':').concat(data.password);
        $cookies.putObject('headers', {headers: {'Authorization': 'Basic ' + btoa(credentials)}});
        $cookies.put('user', data.username);
        $cookies.putObject('account', account);
        $location.path('/');
        Notification("User updated succesfully");
      });
    };

    $scope.signup = function() {
      var data = {
        username: $scope.username,
        email: $scope.email,
        password: $scope.password
      };
      $http.post('api/account', data).then(function(response) {
        $location.path('/');
        Notification("Account created succesfully");
      });
    };

  }
]);
