(function()
{
    'use strict';

    angular.module('VotingApp').controller('VoteDialogController', VoteDialogController);

    VoteDialogController.$inject = ['$uibModalInstance', 'Principal', 'Votes', 'entity'];

    function VoteDialogController($uibModalInstance, Principal, Votes, entity)
    {
        var vm = this;

        vm.addVoteOption = addVoteOption;
        vm.clear = clear;
        vm.currentAccount = null;
        vm.deleteVoteOption = deleteVoteOption;
        vm.save = save;
        vm.vote = entity;
        vm.voteOption = {
                newOption : {id: null, text:''}
        };

        Principal.identity().then(function(account) {
            vm.currentAccount = account;
            vm.vote.userId = vm.currentAccount.id;
        });

        function addVoteOption() {
            vm.vote.voteOptions.push(vm.voteOption.newOption);
            vm.voteOption.newOption = {id: null, text:''}
        }

        function clear()
        {
            vm.voteOption = {
                newOption : {text:''}
            };
            $uibModalInstance.dismiss('cancel');
        }

        function deleteVoteOption(option)
        {
            if (vm.vote.voteOptions.length > 0) {
                vm.vote.voteOptions.splice(vm.vote.voteOptions.indexOf(option), 1);
            }
            vm.voteOption.newOption = {id: null, text:''}
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            $uibModalInstance.close(result);
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function save()
        {
            if (vm.vote.id !== null) {
                Votes.update(vm.vote, onSaveSuccess, onSaveError);
            } else {
                Votes.save(vm.vote, onSaveSuccess, onSaveError);
            }
        }
    }
})();