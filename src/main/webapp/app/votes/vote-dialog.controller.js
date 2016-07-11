(function()
{
    'use strict';

    angular.module('VotingApp').controller('VoteDialogController', VoteDialogController);

    VoteDialogController.$inject = ['$uibModalInstance', 'Principal', 'Votes', 'VoteOptions', 'entity'];

    function VoteDialogController($uibModalInstance, Principal, Votes, VoteOptions, entity)
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
        });

        function addVoteOption() {
            if(vm.vote.voteOptions === undefined) {
                vm.vote.voteOptions = [];
            }
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

            if (option.id !== null) 
            {
                VoteOptions.delete({id: option.id});
            }
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
            vm.vote.userId = vm.currentAccount.id;
            if (vm.vote.id !== null) {
                Votes.update(vm.vote, onSaveSuccess, onSaveError);
            } else {
                Votes.save(vm.vote, onSaveSuccess, onSaveError);
            }
        }
    }
})();