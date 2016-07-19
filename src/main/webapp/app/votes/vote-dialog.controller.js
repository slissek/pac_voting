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
                newOption : {identifier: null, text:''}
        };

        Principal.identity().then(function(account) {
            vm.currentAccount = account;
        });

        function addVoteOption() {
            if(vm.vote.voteOptions === undefined) {
                vm.vote.voteOptions = [];
            }
            vm.vote.voteOptions.push(vm.voteOption.newOption);
            vm.voteOption.newOption = {identifier: null, text:''}
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
            vm.voteOption.newOption = {identifier: null, text:''}

            if (option.identifier !== null) 
            {
                VoteOptions.delete({id: option.identifier});
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
            vm.vote.userId = vm.currentAccount.identifier;
            if (vm.vote.identifier !== null) {
                Votes.update(vm.vote, onSaveSuccess, onSaveError);
            } else {
                Votes.save(vm.vote, onSaveSuccess, onSaveError);
            }
        }
    }
})();