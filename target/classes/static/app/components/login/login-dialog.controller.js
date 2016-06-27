(function()
{
    'use strict';

    angular.module('VotingApp').controller('LoginController', LoginController);
    
    LoginController.$inject = ['$rootScope', '$timeout', 'Auth', '$uibModalInstance'];

    function LoginController($rootScope, $timeout, Auth, $uibModalInstance)
    {
        var vm = this;
        vm.authenticationError = false;
        vm.credentials = {};
        vm.username = null;
        vm.password = null;
        vm.rememberMe = true;
        vm.login = login;
        vm.cancel = cancel;

        function login(event)
        {
            event.preventDefault();
            Auth.login({
                username: vm.username,
                password: vm.password,
                rememberMe: vm.rememberMe
            }).then(function () {
                vm.authenticationError = false;
                $uibModalInstance.close();
                $rootScope.$broadcast('authenticationSuccess');
            }).catch(function () {
                vm.authenticationError = true;
            });
        }

        function cancel()
        {
            vm.credentials = {
                username: null,
                password: null,
                rememberMe: true
            };
            vm.authenticationError = false;
            $uibModalInstance.dismiss('cancel');
        }
    }
})();