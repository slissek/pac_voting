(function() {
    'use strict';

    angular.module('VotingApp').config(stateConfig);
    
    stateConfig.$inject = ['$stateProvider', '$urlRouterProvider'];
    
    function stateConfig($stateProvider, $urlRouterProvider)
    {
        $urlRouterProvider.otherwise('/home');

        $stateProvider
        .state('app',
        {
            abstract: true,
            views: {
                'navbar@': {
                    templateUrl: 'app/components/navbar/navbar.html',
                    controller: 'NavigationController',
                    controllerAs: 'vm'
                }
            }
        })

        .state('entity', 
        {
            abstract: true,
            parent: 'app'
        })

        .state('admin', 
        {
            abstract: true,
            parent: 'app'
        })

        // HOME STATES AND NESTED VIEWS
        .state('home',
        {
            parent : 'app',
            url : '/home',
            views: {
                'content@' : {
                    templateUrl : 'app/home/home.html',
                    controller : 'HomeController',
                    controllerAs: 'vm'
                },

                'votes@home' : {
                    templateUrl : 'app/votes/votes.html',
                    controller : 'VotesController',
                    controllerAs: 'vm'
                }
            }
        })

        .state('home.new',
        {
            parent : 'home',
            url : '/new',
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/votes/vote-dialog.html',
                    controller: 'VoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null, userId: null, topic: null, options: []
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('home', null, { reload: true });
                }, function() {
                    $state.go('home');
                });
            }],
        })

        .state('home.edit',
        {
            parent : 'home',
            url : '/{id}/edit',
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/votes/vote-dialog.html',
                    controller: 'VoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Votes', function(Votes) {
                            return Votes.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('home', null, { reload: true });
                }, function() {
                    $state.go('home');
                });
            }],
        })

        .state('home.delete', {
            parent: 'home',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/votes/vote-delete-dialog.html',
                    controller: 'VoteDeleteController',
                    controllerAs: 'vm',
                    size: 'sm',
                    resolve: {
                        entity: ['Votes', function(Votes) {
                            return Votes.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('home', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })

        // ADMINISTRATION
        .state('user-management',
        {
            parent : 'admin',
            url : '/user-management',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@' :
                {
                    templateUrl : 'app/admin/user-management/user-management.html',
                    controller : 'UserManagementController',
                    controllerAs: 'vm'
                }
            }
        })

        .state('user-management.new',
        {
            parent : 'user-management',
            url : '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
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
                                id: null, userName: null, firstName: null, lastName: null, authorities: null
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

        .state('user-management.edit', 
        {
            parent: 'user-management',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/admin/user-management/user-management-dialog.html',
                    controller: 'UserManagementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'sm',
                    resolve: {
                        entity: ['User', function(User) {
                            return User.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-management', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })

        .state('user-management-detail',
        {
            parent : 'admin',
            url : '/user-management-detail',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@' :
                {
                    templateUrl : 'app/admin/user-management/user-management-detail.html',
                    controller : 'UserManagementDetailController',
                    controllerAs: 'vm'
                }
            }
        })

        .state('user-management.delete', {
            parent: 'user-management',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/admin/user-management/user-magagement-delete-dialog.html',
                    controller: 'UserManagementDeleteController',
                    controllerAs: 'vm',
                    size: 'sm',
                    resolve: {
                        entity: ['User', function(User) {
                            return User.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-management', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })

        .state('metrics', {
            parent: 'admin',
            url: '/metrics',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@' :
                {
                    templateUrl: 'app/admin/metrics/metrics.html',
                    controller: 'MetricsMonitoringController',
                    controllerAs: 'vm'
                }
            }
        })

        .state('configuration', {
            parent: 'admin',
            url: '/configuration',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@' :
                {
                    templateUrl: 'app/admin/configuration/configuration.html',
                    controller: 'ConfigurationController',
                    controllerAs: 'vm'
                }
            }
        })

        .state('health', {
            parent: 'admin',
            url: '/health',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@': {
                    templateUrl: 'app/admin/health/health.html',
                    controller: 'HealthCheckController',
                    controllerAs: 'vm'
                }
            }
        })

        .state('docs', {
            parent: 'admin',
            url: '/docs',
            data: {
                authorities: ['ROLE_ADMIN'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/admin/docs/docs.html'
                }
            }
        })
        
        // ABOUT PAGE AND MULTIPLE NAMED VIEWS
        .state('about',
        {
            parent : 'app',
            url : '/about',
            views :
            {
                'content@' :
                {
                    templateUrl : 'app/about/about.html'
                }
            }
        })

        .state('signin', 
        {
            parent : 'app',
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
            parent : 'app',
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

        .state('error', 
        {
            parent: 'app',
            url: '/error',
            data: {
                authorities: [],
            },
            views: {
                'content@': {
                    templateUrl: 'app/components/error/error.html'
                }
            }
        })

        .state('accessdenied', 
        {
            parent: 'app',
            url: '/accessdenied',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/components/error/accessdenied.html'
                }
            }
        })
    }
})();