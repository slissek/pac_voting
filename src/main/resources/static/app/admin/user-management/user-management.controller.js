(function()
{
    'use strict';

    angular.module('VotingApp').controller('UserManagementController', UserManagementController);

    UserManagementController.$inject = ['$scope'];

    function UserManagementController($scope)
    {
        $scope.users = [
        {
            "id" : "1",
            "username" : "admin",
            "firstname" : "",
            "lastname" : "",
            "authorities" : [
             {
                 "name" : "ADMINISTRATOR"
             }]
        },
        {
            "id" : "2",
            "username" : "user",
            "firstname" : "test",
            "lastname" : "user",
            "authorities" : [
             {
                 "name" : "USER"
             }]
        }]
    };
})();