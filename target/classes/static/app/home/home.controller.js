(function()
{
    'use strict';

    angular.module('VotingApp').controller('HomeController', HomeController);
    
    HomeController.$inject = ['$scope'];

    function HomeController($scope)
    {
//        Principal, LoginService, 
        $scope.votes = [
        {
            "id" : "1",
            "topic" : "What is you favorite operating system?",
            "userVoted" : false,
            "options" : [
            {
                "id" : "1",
                "text" : "Windows",
                "percent" : "4,3",
                "userChoice" : false
            },
            {
                "id" : "2",
                "text" : "Linux",
                "percent" : "18,7",
                "userChoice" : false 
            },
            {
                "id" : "3",
                "text" : "macOS",
                "percent" : "34,9",
                "userChoice" : false
            },
            {
                "id" : "4",
                "text" : "other",
                "percent" : "42,1",
                "userChoice" : false
            }]
        },
        {
            "id" : "2",
            "topic" : "What is you favorite pet?",
            "userVoted" : true,
            "options" : [
            {
                "id" : "1",
                "text" : "Cat",
                "percent" : "4,3",
                "userChoice" : false
            },
            {
                "id" : "2",
                "text" : "Dog",
                "percent" : "18,7",
                "userChoice" : false
            },
            {
                "id" : "3",
                "text" : "Rabbit",
                "percent" : "34,9",
                "userChoice" : false
            },
            {
                "id" : "4",
                "text" : "Fish",
                "percent" : "42,1",
                "userChoice" : true
            }]
        }];
        
//        vm.account = null;
//        vm.isAuthenticated = null;
//        vm.login = LoginService.open;

//        $scope.$on('authenticationSuccess', function()
//        {
//            getAccount();
//        });
//
//        getAccount();
//
//        function getAccount()
//        {
//            Principal.identity().then(function(account)
//            {
//                vm.account = account;
//                vm.isAuthenticated = Principal.isAuthenticated;
//            });
//        }
    };
})();