(function()
{
    'use strict';

    angular.module('VotingApp').factory('Votes', Votes);

    Votes.$inject = ['$resource'];

    function Votes($resource)
    {
        var service = $resource('api/votes/:id', {}, {
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