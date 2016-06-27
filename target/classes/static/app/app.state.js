(function() {
    'use strict';

    angular.module('VotingApp').config(stateConfig);
    
    stateConfig.$inject = ['$stateProvider', '$urlRouterProvider'];
    
    function stateConfig($stateProvider, $urlRouterProvider)
    {
        $urlRouterProvider.otherwise('/home');

        $stateProvider
        // HOME STATES AND NESTED VIEWS
        .state('home',
        {
            url : '/home',
            templateUrl : 'app/home/home.html',
            controller : 'HomeController'
        })

        .state('home.new',
        {
            parent : 'home',
            url : '/new',
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/voting/user-management/user-management-dialog.html',
                    controller: 'UserManagementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'sm',
                    resolve: {
                        entity: function () {
                            return {
                                id: null, username: null, firstName: null, lastName: null, authorities: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-management', null, { reload: true });
                }, function() {
                    $state.go('user-management');
                });
            }]
        })

        // ADMINISTRATION
        .state('user-management',
        {
            url : '/user-management',
            templateUrl : 'app/admin/user-management/user-management.html',
            controller : 'UserManagementController'
        })

        .state('user-management.new',
        {
            parent : 'user-management',
            url : '/new',
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/admin/user-management/user-management-dialog.html',
                    controller: 'UserManagementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'sm',
                    resolve: {
                        entity: function () {
                            return {
                                id: null, username: null, firstName: null, lastName: null, authorities: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-management', null, { reload: true });
                }, function() {
                    $state.go('user-management');
                });
            }]
        })

        .state('user-management-detail',
        {
            url : '/user-management-detail',
            templateUrl : 'app/admin/user-management/user-management-detail.html',
            controller : 'UserManagementDetailController'
        })
        
        // ABOUT PAGE AND MULTIPLE NAMED VIEWS
        .state('about',
        {
            url : '/about',
            views :
            {
                // the main template will be placed here (relatively named)
                '' :
                {
                    templateUrl : 'app/about/partial-about.html'
                },

                // the child views will be defined here (absolutely named)
                'columnOne@about' :
                {
                    template : 'Look I am a column!'
                },

                // for column two, we'll define a separate controller
                'columnTwo@about' :
                {
                    templateUrl : 'app/about/table-data.html',
                    controller : 'scotchController'
                }
            }
        })

        .state('signin', 
        {
            url : '/signin',
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/components/login/login-dialog.html',
                    controller: 'LoginController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'sm',
                    resolve: {
                        entity: function () {
                            return {
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('home', null, { reload: true });
                }, function() {
                    $state.go('home');
                });
            }]
        })

        .state('signout', 
        {
            url : '/signout',
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/components/logout/logout-dialog.html',
                    controller: 'LogoutController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'sm',
                    resolve: {
                        entity: function () {
                            return {
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('home', null, { reload: true });
                }, function() {
                    $state.go('home');
                });
            }]
        })
    }
})();