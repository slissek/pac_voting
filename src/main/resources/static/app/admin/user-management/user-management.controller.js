(function()
{
    'use strict';

    angular.module('VotingApp').controller('UserManagementController', UserManagementController);

    UserManagementController.$inject = [ 'Principal', 'User' ];

    function UserManagementController(Principal, User)
    {
        var vm = this;
        vm.authorities = [ 'ROLE_USER', 'ROLE_ADMIN' ];
        vm.currentAccount = null;
        vm.loadAll = loadAll;
        vm.totalItems = null;
        vm.users = [];

        vm.loadAll();

        Principal.identity().then(function(account) {
            vm.currentAccount = account;
        });

        function loadAll()
        {
            User.query(
               function (result, headers) {
                   vm.totalItems = headers('X-Total-Count');
                   vm.users = result;
               });
        }
    }
})();