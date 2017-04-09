(function(){
	
	function HeaderCtrl($rootScope, $http, $location){
		
		$rootScope.credentials = {};
		$rootScope.loginError = "";
		$rootScope.isAuthenticated = false;
		$rootScope.logoutSuccess = false; 
		
		var authenticate = function(credentials, callback) {

		    var headers = credentials ? {authorization : "Basic "
		        + btoa(credentials.username + ":" + credentials.password)
		    } : {};

		    $http.get('authenticateUser', {headers : headers}).success(function(data) {
		      if (data.name) {
		    	  $rootScope.isAuthenticated = true;
		    	  $rootScope.credentials.username = data.name;
//		    	  $location.path("/lobby");
		      } else {
		    	  $rootScope.isAuthenticated = false;
		      }
		      callback && callback();
		    }).error(function() {
		    	$rootScope.isAuthenticated = false;
		      callback && callback();
		    });

		  }
		
//		authenticate();
		
		$rootScope.login = function() {
		      authenticate($rootScope.credentials, function() {
		    	  $rootScope.logoutSuccess = false;
		    	if ($rootScope.isAuthenticated) {		      
		          $rootScope.error = false;		          
		          angular.element("#loginUserName").val("");
		    	  angular.element("#loginPassword").val("");
		    	  
		    	  //handle access token
		    	  $http.post("/access_token", {headers:{"Accept":"application/json", "Content-Type":"application/json"}})
		    	  	.then(function successCallback(response){
		    	  		$location.path("/lobby");
		    	  	}, function errorCallback(response){
		    	  		$location.path("/");
		    	  	});
		        } else {
		          $location.path("/");
		          
		          $rootScope.error = true;
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
	}
	
	backgammonApp.controller("HeaderCtrl", HeaderCtrl);
})();