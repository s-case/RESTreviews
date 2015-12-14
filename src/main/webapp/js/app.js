var restreviewsApp = angular.module('restreviewsApp', [
  'ngRoute',
  'ngCookies',
  'restreviewsControllers',
  'ui-notification'
]);

restreviewsApp.run([ '$rootScope', '$cookies',
  function($rootScope, $cookies) {
    $rootScope.signedIn = function() {
      if($cookies.get('user')) {
        return true;
      } else {
        return false;
      }
    };

    $rootScope.getUser = function() {
      if($cookies.getObject('account')) {
        return $cookies.getObject('account');
      } else {
        return null;
      }
    };
  }
])

restreviewsApp.config(['$routeProvider', '$locationProvider',
  function($routeProvider, $locationProvider) {

    $routeProvider.
      when('/', {
        templateUrl: 'partials/main.html',
        controller: 'MainCtrl'
      }).
      when('/search', {
        templateUrl: 'partials/search.html',
        controller: 'SearchCtrl'
      }).
      when('/signup', {
        templateUrl: 'partials/user.html',
        controller: 'UserCtrl'
      }).
      when('/users/:userId', {
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
  }]).config(function(NotificationProvider) {
        NotificationProvider.setOptions({
            delay: 2000,
            startTop: 20,
            startRight: 10,
            verticalSpacing: 20,
            horizontalSpacing: 20,
            positionX: 'center',
            positionY: 'top'
        });
    });
