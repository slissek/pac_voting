(function()
{
    'use strict';

    angular.module('VotingApp').controller('NavigationController', NavigationController);

    NavigationController.$inject = [ 'Principal' ];

    function NavigationController(Principal)
    {
        var vm = this;

        vm.isAuthenticated = Principal.isAuthenticated;
    }
})();