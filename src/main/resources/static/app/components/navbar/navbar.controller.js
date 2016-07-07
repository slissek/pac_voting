(function()
{
    'use strict';

    angular.module('VotingApp').controller('NavigationController', NavigationController);

    NavigationController.$inject = [ 'Principal', 'ProfileService' ];

    function NavigationController(Principal, ProfileService)
    {
        var vm = this;

        vm.isAuthenticated = Principal.isAuthenticated;

        ProfileService.getProfileInfo().then(function(response) 
        {
            vm.inProduction = response.inProduction;
            vm.swaggerDisabled = response.swaggerDisabled;
        });
    }
})();