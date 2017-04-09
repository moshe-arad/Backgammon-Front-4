
var backgammonApp = angular.module("backgammonApp", [ "ngRoute" , "ngCookies"]);

backgammonApp.config(function ($routeProvider, $httpProvider) {
	
	$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
	
	$httpProvider.interceptors.push('unauthorized401');
	
	$routeProvider	
	.when("/", {controller: "HomeCtrl", templateUrl: "/app/partials/home.html"})	
	.when("/error", {controller: "HomeCtrl", templateUrl: "/app/partials/error.html"})
	.when("/lobby", {controller: "LobbyCtrl", templateUrl: "/app/partials/lobby.html"});
});

backgammonApp.factory("unauthorized401", function ($q, $location) {
    return {
        'responseError': function(rejection) {
            if(rejection.status === 401){
                $location.path('/error');                    
            }
            return $q.reject(rejection);
         }
     };
});

