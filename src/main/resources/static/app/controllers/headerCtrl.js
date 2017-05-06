angular.module("backgammonApp")
    .controller('HeaderCtrl',
    ['$rootScope', '$location', '$http',
    function ($rootScope, $location, $http) {
    	
    	$rootScope.credentials = {};
		$rootScope.isAuthenticated = false; 
		
	  var authenticate = function(credentials, callback) {

	    var headers = credentials ? {authorization : "Basic "
	        + btoa(credentials.username + ":" + credentials.password)
	    } : {};

	    $http.get('authenticateUser', {headers : headers}).success(function(data) {
	      if (data.name) {	   
	    	$rootScope.credentials.username = data.name;
	        $rootScope.isAuthenticated = true;	        
	    	$location.path("/lobby");
	      } else {
	        $rootScope.isAuthenticated = false;
	      }
	      callback && callback();
	    }).error(function() {
	      $rootScope.isAuthenticated = false;
	      callback && callback();
	    });

	  }

	  authenticate();
	  
	  $rootScope.login = function() {
	      authenticate($rootScope.credentials, function() {
	        if ($rootScope.isAuthenticated) {	        	
	          $location.path("/lobby");
	          self.error = false;
	        } else {
	          $location.path("/");
	          self.error = true;
	        }
	      });
	  };
	  
	  $rootScope.logout = function() {
		  $http.post('http://localhost:8080/logout', {}).success(function() {
		    $rootScope.isAuthenticated = false;
		    $location.path("/");
		    $rootScope.logoutSuccess = true;
		  }).error(function(data) {
		    $rootScope.authenticated = false;			    
		  });
		};
    	
    }]);

//(function(){
//	
//	function HeaderCtrl($rootScope, $http, $location){
//		
//		
//	}
//	backgammonApp.controller("HeaderCtrl", HeaderCtrl);
//})();