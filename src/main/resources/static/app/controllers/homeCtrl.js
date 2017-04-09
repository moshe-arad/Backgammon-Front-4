
(function(){
	
	function HomeCtrl ($rootScope, $scope, $interval, $timeout, $location, $http) {
	
		$scope.user = {};
		$scope.register_error = false;
				
		$scope.register = function() {
			console.log(isPassedValidation());
			if(isPassedValidation() == "valid"){
				$http.post("http://localhost:8080/users/", $scope.user)
				.success(function(data, status) {
					if(status == 201){
						$rootScope.isAuthenticated = true;
						$rootScope.credentials = {username:data.userName};
						console.log("Navigating to lobby");
						$location.path("/lobby");	
					} 
					else if(status == 500){
						console.log("Registeration failure");
						$scope.register_error = "Failed to do registration."
					}
				})
				.error(function (data, status) {
	              	register_error = "Failed to do registration.";                     
				});
			}
			else{
				$scope.register_error = isPassedValidation();
			}
		}
		
		var isPassedValidation = function(){
			
			var firstName = isValidName($scope.user.firstName);
			var lastName = isValidName($scope.user.lastName);
			var email = checkValidEmail();
	
			var password = isValidPassword($scope.user.basicUser.password) == "valid" ? true:false;
			var passwordMatch = $scope.user.confirm == $scope.user.basicUser.password ? true:false; 
			
			if(!Boolean(firstName)) return "First name didn't passed validation";
			if(!Boolean(lastName)) return "Last name didn't passed validation";
			if(!Boolean(email)) return "Email didn't passed validation";
//			if(!Boolean(userName)) return "User name didn't passed validation";
			if(!Boolean(password)) return "password didn't passed validation";
			if(!Boolean(passwordMatch)) return "password Match didn't passed validation";
			
			return "valid";
		}
		
		var isValidName = function(name){
			
			var re = /^[A-Z|a-z| \\-]+$/;
			
			if(!re.test(name)) return false;
			else return true;
		}
		
		/******** shared with view ************/
			
		$scope.onEmailInsert = checkValidEmail;
				
		$scope.onUserNameInsert = checkUserNameAvailable;
		
		$scope.onCheckValidPassword = checkValidPassword;
		
		$scope.onCheckPasswordsMatch = checkPasswordsMatch;
		
		/***** email *****/
		
		function checkValidEmail(){
			
			if(isValidEmail($scope.user.email)) 
			{	
				angular.element("#invalidEmail").addClass("hidden");
				checkEmailAvailable($scope.user);					
				return true;
			}
			else{
				angular.element("#invalidEmail").html("Invalid email address.");
				angular.element("#invalidEmail").removeClass("hidden");
				return false;
			}
		}
		
		function checkEmailAvailable(user){
			$timeout(function(){
				checkAvailable("http://localhost:8080/users/email/" + $scope.user.email + "/")
			} , 1000, false);
		}
		
		var isValidEmail = function (email){
			var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
			
			if(!re.test(email)) return false;
			else return true;
		}
		
		/***** user name *****/
		
		function checkUserNameAvailable(user){		
			if(typeof user.basicUser.userName != "undefined" && user.basicUser.userName.length >=3)
			{
				console.log(user.basicUser.userName);
				
				$timeout(function(){
					checkAvailable("http://localhost:8080/users/user_name/" + $scope.user.basicUser.userName + "/")
					}, 1000, false);
			}
				
		}
		
		function checkAvailable(url){
			
			var config = {
					method:"GET",
					url:url
			};
			
			$http(config).then(function success(response){
				successCheckAvailable(response.data);			
			}, function error(response){
				errorFunc(response.status);
			});
		}

		function errorFunc(status){
			console.log(status);
		}
		
		function successCheckAvailable(jsonObj){
			console.log("Json object result is: " + jsonObj);
			
			if(jsonObj == "") {
				angular.element("#invalidUserName").addClass("hidden");
			}
			else {
				if(jsonObj == "Email is not available.") {
					angular.element("#invalidEmail").html(jsonObj);
					angular.element("#invalidEmail").removeClass("hidden");					
				}
				else if(jsonObj == "User name is not available."){
					angular.element("#invalidUserName").html("User name is not available.")
					angular.element("#invalidUserName").removeClass("hidden");
				}
			}
		}
		
		/************** password ************/
		
		function checkValidPassword(user){
			
			$interval(function(){
				if(typeof user.basicUser.password != "undefined"){
					var result = isValidPassword(user.basicUser.password);
					if(result != "valid") {
						angular.element("#invalidPassword").html(result);
						angular.element("#invalidPassword").removeClass("hidden")
					}
					else{
						angular.element("#invalidPassword").addClass("hidden")
					}
				}
			}, 3000, 0, false, user);
			
		}

		function isValidPassword(password){
			
			if(password.length < 8) return "Password length must be at least 8 characters.";
			if(!twoNumbersValidation(password)) return "Password must contain at least 2 numbers.";
			if(!oneUpperCaseValidation(password)) return "Password must contain at least 1 upper case letter.";
			if(!oneUniqueCharacter(password)) return "Password must contain at least 1 unique character.";
			if(!threeLowerCaseValidation(password)) return "Password must contain at least 3 lower case letters.";
			
			return "valid";
		}

		function twoNumbersValidation(password){
			var pattern = /^[^(0-9)]*[0-9]{1}[^(0-9)]*[0-9]{1}.*/;
			return pattern.test(password);
		}


		function oneUpperCaseValidation(password){
			var pattern = /[A-Z]+/;
			return pattern.test(password);
		}

		function oneUniqueCharacter(password){
			var pattern = /[^(A-Z)|^(a-z)|^(0-9)]+/;
			return pattern.test(password);
		}

		function threeLowerCaseValidation(password){
			var pattern = /^[^(a-z)]*[a-z]{1}[^(a-z)]*[a-z]{1}[^(a-z)]*[a-z]{1}.*/;
			return pattern.test(password);
		}
		
		function clearPasswordMessage(){
			
			if($scope.password_error != "") {
				$scope.password_error = "";
			}
		}
		
		/************* password match ***********/
		function checkPasswordsMatch(user){
			
			console.log(user.confirm);
			$interval(function(){
				if(angular.isDefined(user.confirm) && angular.isDefined(user.basicUser.password)){					
					if(user.confirm != user.basicUser.password){
						angular.element("#invalidConfirmPassword").html("Passwords does not match.");
						angular.element("#invalidConfirmPassword").removeClass("hidden");						
					}
					else{
						angular.element("#invalidConfirmPassword").addClass("hidden");
					}
				}
			}, 500, 0, false, user);
			
		}
	}
	
	backgammonApp.controller("HomeCtrl", HomeCtrl);
})();