(function() {
    'use strict';

    angular.module('VotingApp').controller('VoteDeleteController', VoteDeleteController);

    VoteDeleteController.$inject = ['$uibModalInstance', 'entity', 'Votes'];

    function VoteDeleteController ($uibModalInstance, entity, Votes) {
        var vm = this;

        vm.vote = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Votes.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
