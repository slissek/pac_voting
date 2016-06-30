(function()
{
    'use strict';

    angular.module('VotingApp').controller('VoteDialogController', VoteDialogController);

    VoteDialogController.$inject = ['$scope', '$state', '$uibModalInstance'];

    function VoteDialogController($scope, $state, $uibModalInstance)
    {
        var vm = this;

        vm.votes = [];
        vm.voteOption = {
            options : [],
            newOption : {text:''}
        };
        vm.addVoteOption = addVoteOption;
        vm.deleteVoteOption = deleteVoteOption;
        vm.saveVote = saveVote;
        vm.cancel = cancel;

        function getAllVotes() {
            $http.get('api/votes');
        }

        function addVoteOption() {
            vm.voteOption.options.push(vm.voteOption.newOption);
            vm.voteOption.newOption = {text:''}
        }

        function deleteVoteOption(option)
        {
            vm.voteOption.options.splice(vm.voteOption.options.indexOf(option), 1);
            vm.voteOption.newOption = {text:''}
        }

        function saveVote()
        {
            var data = {
                topic : vm.vote.topic,
                options : vm.voteOption.options
            };

            $http.post('api/votes', data).success(function (response) {
                $uibModalInstance.close();
                return response;
            });
        }

        function cancel()
        {
            vm.voteOption = {
                options : [],
                newOption : {text:''}
            };
            $uibModalInstance.dismiss('cancel');
        }
    };
})();