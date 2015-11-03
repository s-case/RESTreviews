var restreviewsApp = angular.module('restreviewsApp', [
  'ngRoute',
  'restreviewsControllers'
]);

restreviewsApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/', {
        templateUrl: 'partials/main.html',
        controller: 'MainCtrl'
      }).
      when('/signup', {
        templateUrl: 'partials/user.html',
        controller: 'UserCtrl'
      }).
      when('/users/:id', {
        templateUrl: 'partials/user.html',
        controller: 'UserCtrl'
      }).
      when('/products/new', {
        templateUrl: 'partials/productEdit.html',
        controller: 'ProductCtrl'
      }).
      when('/products/:productId', {
        templateUrl: 'partials/productDetail.html',
        controller: 'ProductCtrl'
      }).
      when('/products/:productId/edit', {
        templateUrl: 'partials/productEdit.html',
        controller: 'ProductCtrl'
      }).
      when('/products/:productId/reviews/new', {
        templateUrl: 'partials/reviewEdit.html',
        controller: 'ReviewCtrl'
      }).
      when('/products/:productId/reviews/:reviewId', {
        templateUrl: 'partials/reviewDetail.html',
        controller: 'ReviewCtrl'
      }).
      when('/products/:productId/reviews/:reviewId/edit', {
        templateUrl: 'partials/reviewEdit.html',
        controller: 'ReviewCtrl'
      }).
      otherwise({
        redirectTo: '/'
      });
  }]);
