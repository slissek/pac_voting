(function()
{
    'use strict';

    angular.module('VotingApp').factory('Auth', Auth);

    Auth.$inject = ['$q', 'Principal', 'AuthServerProvider'];

    function Auth($q, Principal, AuthServerProvider)
    {
        var service = {
            login: login,
            logout: logout
        }

        return service;

        function login (credentials, callback) {
            var cb = callback || angular.noop;
            var deferred = $q.defer();

            AuthServerProvider.login(credentials)
                .then(loginThen)
                .catch(function (err) {
                    this.logout();
                    deferred.reject(err);
                    return cb(err);
                }.bind(this));

            function loginThen (data) {
                Principal.identity(true).then(function(account) {
                    // After the login the language will be changed to
                    // the language selected by the user during his registration
                    if (account!== null) {
                        $translate.use(account.langKey).then(function () {
                            $translate.refresh();
                        });
                    }
                    deferred.resolve(data);
                });
                return cb();
            }

            return deferred.promise;
        }

        function logout () {
            AuthServerProvider.logout();
            Principal.authenticate(null);
        }
    }
})();