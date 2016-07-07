(function()
{
    'use strict';

    angular.module('VotingApp').factory('UserVotes', UserVotes);

    UserVotes.$inject = ['$resource'];

    function UserVotes($resource)
    {
        var service = $resource('api/user-votings/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' },
            'delete':{ method:'DELETE'}
        });

        return service;
    }
})();