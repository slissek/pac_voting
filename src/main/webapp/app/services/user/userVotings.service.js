(function()
{
    'use strict';

    angular.module('VotingApp').factory('UserVotings', UserVotings);

    UserVotings.$inject = ['$resource'];

    function UserVotings($resource)
    {
        var service = $resource('api/users/:id/votes', {}, {
            'get': { method: 'GET', isArray: true}
        });

        return service;
    }
})();