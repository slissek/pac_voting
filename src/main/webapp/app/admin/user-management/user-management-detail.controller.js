(function()
{
    'use strict';

    angular.module('VotingApp').controller('UserManagementDetailController', UserManagementDetailController);

    UserManagementDetailController.$inject = [ '$stateParams', 'User', 'UserVotings', 'VotesCreator' ];

    function UserManagementDetailController($stateParams, User, UserVotings, VotesCreator)
    {
        var vm = this;

        vm.load = load;
        vm.loadVotes = loadVotes;
        vm.loadVotesCreator = loadVotesCreator;
        vm.user = {};
        vm.uservotes = [];
        vm.votescreator = [];

        vm.load($stateParams.id);
        vm.loadVotes($stateParams.id);
        vm.loadVotesCreator($stateParams.id);

        function load(id)
        {
            User.get(
            {
                id : id
            }, function(result)
            {
                vm.user = result;
            });
        }

        function loadVotes(id)
        {
            UserVotings.get(
            {
                id : id
            }, function(result)
            {
                vm.uservotes = result;
            });
        }

        function loadVotesCreator(id)
        {
            VotesCreator.get(
            {
                id : id
            }, function(result)
            {
                vm.votescreator = result;
            });
        }
    }
    ;
})();