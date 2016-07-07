(function()
{
    'use strict';

    angular.module('VotingApp').factory('Auth', Auth);

    Auth.$inject = ['$rootScope', '$state', '$q', 'Principal', 'AuthSessionProvider'];

    function Auth($rootScope, $state, $q, Principal, AuthSessionProvider)
    {
        var service = {
            authorize: authorize,
            login: login,
            logout: logout
        }

        return service;

        function authorize (force) {
            var authReturn = Principal.identity(force).then(authThen);

            return authReturn;

            function authThen () {
                var isAuthenticated = Principal.isAuthenticated();

                // an authenticated user can't access to signin page
                if (isAuthenticated && $rootScope.toState.parent === 'account' && $rootScope.toState.name === 'signin')
                {
                    $state.go('home');
                }

                if ($rootScope.toState.data.authorities && $rootScope.toState.data.authorities.length > 0 && 
                        !Principal.hasAnyAuthority($rootScope.toState.data.authorities)) {
                    if (isAuthenticated) {
                        // user is signed in but not authorized for desired state
                        $state.go('accessdenied');
                    } else {
                        $state.go('signin');
                    }
                }
            }
        }

        function login (credentials, callback) {
            var cb = callback || angular.noop;
            var deferred = $q.defer();

            AuthSessionProvider.login(credentials)
                .then(loginThen)
                .catch(function (err) {
                    this.logout();
                    deferred.reject(err);
                    return cb(err);
                }.bind(this));

            function loginThen (data) {
                Principal.identity(true).then(function(account) {
                    deferred.resolve(data);
                });
                return cb();
            }

            return deferred.promise;
        }

        function logout () {
            AuthSessionProvider.logout();
            Principal.authenticate(null);
        }
    }
})();