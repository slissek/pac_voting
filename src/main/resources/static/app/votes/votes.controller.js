(function()
{
    'use strict';

    angular.module('VotingApp').controller('VotesController', VotesController);
    
    VotesController.$inject = ['$scope', 'Votes'];

    function VotesController($scope, Votes)
    {
        $scope.votes = [
        {
            "id" : "1",
            "topic" : "What is you favorite operating system?",
            "userVoted" : false,
            "options" : [
            {
                "id" : "1",
                "text" : "Windows",
                "percent" : "4,3",
                "userChoice" : false
            },
            {
                "id" : "2",
                "text" : "Linux",
                "percent" : "18,7",
                "userChoice" : false 
            },
            {
                "id" : "3",
                "text" : "macOS",
                "percent" : "34,9",
                "userChoice" : false
            },
            {
                "id" : "4",
                "text" : "other",
                "percent" : "42,1",
                "userChoice" : false
            }]
        },
        {
            "id" : "2",
            "topic" : "What is you favorite pet?",
            "userVoted" : true,
            "options" : [
            {
                "id" : "1",
                "text" : "Cat",
                "percent" : "4,3",
                "userChoice" : false
            },
            {
                "id" : "2",
                "text" : "Dog",
                "percent" : "18,7",
                "userChoice" : false
            },
            {
                "id" : "3",
                "text" : "Rabbit",
                "percent" : "34,9",
                "userChoice" : false
            },
            {
                "id" : "4",
                "text" : "Fish",
                "percent" : "42,1",
                "userChoice" : true
            }]
        }];
    }
})();