(function()
{
    'use strict';

    angular.module('VotingApp').controller('VotesController', VotesController);
    
    VotesController.$inject = ['Principal', 'Votes', 'UserVotes'];

    function VotesController(Principal, Votes, UserVotes)
    {
        var vm = this;
        vm.currentAccount = null;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.isAdmin = Principal.hasAuthority("ROLE_ADMIN")
        vm.loadAll = loadAll;
        vm.save = save;
        vm.userVote = {};
        vm.votes = [];

        vm.loadAll();

        Principal.identity().then(function(account) {
            vm.currentAccount = account;
            if (vm.currentAccount !== null) {
                vm.userVote.userId = vm.currentAccount.id;
            }
        });

        function loadAll() {
            Votes.query(
                function (result, headers) {
//                    vm.totalItems = headers('X-Total-Count');
                    vm.votes = result;
                }
            );
        }

        function onSaveSuccess (result) {
            vm.loadAll();
        }

        function onSaveError () {
        }

        function save(vote) {
            vm.userVote.voteId = vote.id;
            for (var i=0; i<vote.options.length; i++) {
                var voteOptions = vote.options[i];
                if (voteOptions.userChoice) {
                    vm.userVote.voteOptionsId = voteOptions.id;
                    break;
                }
            }
            
            UserVotes.save(vm.userVote, onSaveSuccess, onSaveError);
        }
    }
})();