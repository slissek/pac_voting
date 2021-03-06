(function()
{
    'use strict';

    angular.module('VotingApp').controller('VotesController', VotesController);

    VotesController.$inject = [ '$scope', 'Principal', 'Votes', 'UserVotes' ];

    function VotesController($scope, Principal, Votes, UserVotes)
    {
        var vm = this;
        vm.account = null;
        vm.isAuthenticated = null;
        vm.loadAll = loadAll;
        vm.save = save;
        vm.userVote = {};
        vm.votes = [];

        $scope.$on('authenticationSuccess', function()
        {
            init();
        });

        init();

        function init()
        {
            getAccount();
            loadAll();
        }

        function getAccount()
        {
            Principal.identity().then(function(account)
            {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }

        function loadAll()
        {
            Votes.query(function(result, headers)
            {
                // vm.totalItems = headers('X-Total-Count');
                vm.votes = result;
            });
        }

        function onSaveSuccess(result)
        {
            vm.loadAll();
        }

        function onSaveError()
        {
        }

        function save(vote)
        {
            vm.userVote.userId = vm.account.identifier;
            vm.userVote.voteId = vote.identifier;
            for (var i = 0; i < vote.voteOptions.length; i++)
            {
                var voteOptions = vote.voteOptions[i];
                if (voteOptions.userChoice)
                {
                    vm.userVote.voteOptionsId = voteOptions.identifier;
                    break;
                }
            }

            UserVotes.save(vm.userVote, onSaveSuccess, onSaveError);
        }
    }
})();