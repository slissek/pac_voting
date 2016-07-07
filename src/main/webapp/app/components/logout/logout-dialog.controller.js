(function()
{
    'use strict';

    angular.module('VotingApp').controller('LogoutController', LogoutController);
    
    LogoutController.$inject = ['$scope', '$uibModalInstance', '$state', 'Auth'];

    function LogoutController($scope, $uibModalInstance, $state, Auth)
    {
        var vm = this;
        vm.logout = logout;
        vm.cancel = cancel;

        function logout()
        {
            Auth.logout();
            $uibModalInstance.close();
            $state.go('home');
        };

        function cancel()
        {
            $uibModalInstance.dismiss('cancel');
        };
    };

})();