(function()
{
    'use strict';

    angular.module('VotingApp').controller('UserManagementDialogController', UserManagementDialogController);
    
    UserManagementDialogController.$inject = ['$uibModalInstance', 'User', 'entity']; 

    function UserManagementDialogController($uibModalInstance, User, entity)
    {
        var vm = this;
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.clear = clear;
        vm.save = save;
        vm.user = entity;

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            $uibModalInstance.close(result);
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function save() {
            if(vm.user.id !== null) {
                User.update(vm.user, onSaveSuccess, onSaveError);
            } else {
                User.save(vm.user, onSaveSuccess, onSaveError);
            }
        }
    };
})();