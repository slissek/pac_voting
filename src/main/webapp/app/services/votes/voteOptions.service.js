(function()
{
    'use strict';

    angular.module('VotingApp').factory('VoteOptions', VoteOptions);

    VoteOptions.$inject = ['$resource'];

    function VoteOptions($resource)
    {
        var service = $resource('api/voteOptions/:id', {}, {
            'delete':{ method:'DELETE'}
        });

        return service;
    }
})();