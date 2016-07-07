(function() {
    'use strict';

    angular.module('VotingApp').directive('passwordStrength', passwordStrength);

    function passwordStrength () 
    {
        var directive = 
        {
            require: 'ngModel',
            restrict: 'E',
            scope: {
                password: '=ngModel'
            },

            link: function(scope, elem, attrs, ctrl) 
            {
                var types = ['danger', 'warning', 'info', 'success'];
                var messages = ['weak', 'insufficient', 'ok', 'strong'];
                scope.$watch('password', function(newVal) 
                {
                    var strength = isSatisfied(newVal && newVal.length >= 4) +
                    isSatisfied(newVal && /[A-z]/.test(newVal)) +
                    isSatisfied(newVal && /(?=.*\W)/.test(newVal)) +
                    isSatisfied(newVal && /\d/.test(newVal));

                    function isSatisfied(criteria) 
                    {
                        return criteria ? 1 : 0;
                    }
                    scope.type = types[strength];
                    scope.message = messages[strength];
                }, true);
            },
            template: '<uib-progressbar animate="false" value="dynamic" type="{{type}}"><i>{{message}}</i></uib-progressbar>'
        };

        return directive;
    }
})();
