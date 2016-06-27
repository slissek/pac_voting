(function()
{
    'use strict';

    angular.module('VotingApp').controller('LogoutController', LogoutController);
    
    LogoutController.$inject = ['$scope', '$uibModalInstance'];

    function LogoutController($scope, $uibModalInstance)
    {
        var vm = this;
        vm.username = null;
        vm.password = null;
        vm.rememberMe = true;
        vm.logout = logout;
        vm.cancel = cancel;

        function logout()
        {
            //TODO logout
            $uibModalInstance.close();
        };

        function cancel()
        {
            $uibModalInstance.dismiss('cancel');
        };
    };

})();