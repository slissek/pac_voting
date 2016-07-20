(function()
{
    'use strict';

    angular.module('VotingApp').factory('VotesCreator', VotesCreator);

    VotesCreator.$inject = ['$resource'];

    function VotesCreator($resource)
    {
        var service = $resource('api/votes/users/:id', {}, {
            'get': { method: 'GET', isArray: true }
        });

        return service;
    }
})();